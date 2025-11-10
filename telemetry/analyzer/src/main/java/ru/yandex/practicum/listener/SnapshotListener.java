package ru.yandex.practicum.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.entity.*;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotListener {

    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final SensorDataAdapterFactory sensorDataAdapterFactory;

    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    @Transactional(readOnly = true)
    @KafkaListener(topics = "${topic.snapshot}", containerFactory = "snapshotListenerFactory")
    public void listenSnapshots(SensorsSnapshotAvro sensorsSnapshotAvro) {
        log.info("Getting snapshot {}", sensorsSnapshotAvro);

        String hubId = sensorsSnapshotAvro.getHubId();
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
        Map<String, SensorStateAvro> sensorsState = sensorsSnapshotAvro.getSensorsState();

        scenarios.forEach(scenario -> {
            boolean allMatch = scenario.getConditions().entrySet().stream()
                    .allMatch(entry -> {
                        String sensorId = entry.getKey();
                        Condition condition = entry.getValue();
                        SensorStateAvro sensorStateAvro = sensorsState.get(sensorId);
                        return sensorsState != null && ifConditionMatch(condition, sensorStateAvro);
                    });
            if (allMatch) {
                Map<String, Action> actions = scenario.getActions();
                String scenarioName = scenario.getName();
                sendActionRequest(actions, hubId, scenarioName);
            }
        });
    }

    private void sendActionRequest(Map<String, Action> actions, String hubId, String scenarioName) {
        actions.forEach((sensorId, action) -> {
            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(hubId)
                    .setScenarioName(scenarioName)
                    .setAction(DeviceActionProto.newBuilder()
                            .setSensorId(sensorId)
                            .setType(ActionTypeProto.valueOf(action.getType().name()))
                            .setValue(action.getValue())
                            .build())
                    .build();
            log.info("Sending action request {}", request);
            hubRouterClient.handleDeviceAction(request);
        });
    }

    private boolean ifConditionMatch(Condition condition, SensorStateAvro sensorsState) {
        if (sensorsState == null) {
            return false;
        }

        SensorData sensorData = sensorDataAdapterFactory.createAdapter(sensorsState.getData());
        ConditionType type = condition.getType();
        Integer value = condition.getValue();
        ConditionOperation operation = condition.getOperation();
        Optional<Integer> actualValue = switch (type) {
            case MOTION -> sensorData.getMotion();
            case LUMINOSITY -> sensorData.getLuminosity();
            case SWITCH -> sensorData.getSwitchState();
            case TEMPERATURE -> sensorData.getTemperature();
            case HUMIDITY -> sensorData.getHumidity();
            case CO2LEVEL -> sensorData.getCo2Level();
        };

        return actualValue.map(actual -> compareValues(actual, operation, value))
                .orElse(false);
    }

    private boolean compareValues(Integer actual, ConditionOperation operation, Integer expected) {
        return switch (operation) {
            case EQUALS -> Objects.equals(actual, expected);
            case LOWER_THAN -> actual < expected;
            case GREATER_THAN -> actual > expected;
        };
    }
}