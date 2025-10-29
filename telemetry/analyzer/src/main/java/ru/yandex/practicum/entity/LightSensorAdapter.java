package ru.yandex.practicum.entity;

import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Optional;


public class LightSensorAdapter implements SensorData {
    private final LightSensorAvro data;

    public LightSensorAdapter(LightSensorAvro data) {
        this.data = data;
    }

    @Override
    public Optional<Integer> getMotion() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getLuminosity() {
        return Optional.of(data.getLuminosityl());
    }

    @Override
    public Optional<Integer> getSwitchState() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getTemperature() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getHumidity() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getCo2Level() {
        return Optional.empty();
    }
}