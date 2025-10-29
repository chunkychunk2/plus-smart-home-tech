package ru.yandex.practicum.entity;

import java.util.Optional;

public class NullSensorAdapter implements SensorData {
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