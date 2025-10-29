package ru.yandex.practicum.entity;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Component
public class SensorDataAdapterFactory {

    public SensorData createAdapter(Object sensorData) {
        if (sensorData instanceof MotionSensorAvro) {
            return new MotionSensorAdapter((MotionSensorAvro) sensorData);
        } else if (sensorData instanceof LightSensorAvro) {
            return new LightSensorAdapter((LightSensorAvro) sensorData);
        } else if (sensorData instanceof SwitchSensorAvro) {
            return new SwitchSensorAdapter((SwitchSensorAvro) sensorData);
        } else if (sensorData instanceof TemperatureSensorAvro) {
            return new TemperatureSensorAdapter((TemperatureSensorAvro) sensorData);
        } else if (sensorData instanceof ClimateSensorAvro) {
            return new ClimateSensorAdapter((ClimateSensorAvro) sensorData);
        }
        return new NullSensorAdapter();
    }
}