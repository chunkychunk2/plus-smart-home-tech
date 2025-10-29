package ru.yandex.practicum.entity;

import java.util.Optional;

public interface SensorData {
    Optional<Integer> getMotion();
    Optional<Integer> getLuminosity();
    Optional<Integer> getSwitchState();
    Optional<Integer> getTemperature();
    Optional<Integer> getHumidity();
    Optional<Integer> getCo2Level();
}