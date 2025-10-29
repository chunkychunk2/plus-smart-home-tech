package ru.yandex.practicum.entity;

import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Optional;

public class ClimateSensorAdapter implements SensorData {
    private final ClimateSensorAvro data;

    public ClimateSensorAdapter(ClimateSensorAvro data) {
        this.data = data;
    }

    @Override
    public Optional<Integer> getMotion() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getLuminosity() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getSwitchState() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getTemperature() {
        return Optional.of(data.getTemperatureC());
    }

    @Override
    public Optional<Integer> getHumidity() {
        return Optional.of(data.getHumidity());
    }

    @Override
    public Optional<Integer> getCo2Level() {
        return Optional.of(data.getCo2Level());
    }
}