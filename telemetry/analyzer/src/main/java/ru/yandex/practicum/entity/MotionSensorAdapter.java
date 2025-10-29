package ru.yandex.practicum.entity;

import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Optional;

public class MotionSensorAdapter implements SensorData {
    private final MotionSensorAvro data;

    public MotionSensorAdapter(MotionSensorAvro data) {
        this.data = data;
    }

    @Override
    public Optional<Integer> getMotion() {
        return Optional.of(data.getMotion() ? 1 : 0);
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