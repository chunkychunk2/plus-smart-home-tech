package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.delivery.DeliveryDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto addDelivery(DeliveryDto deliveryDto);

    void completeDelivery(UUID deliveryId);

    void pickDelivery(UUID deliveryId);

    void failDelivery(UUID deliveryId);

    Double calculateDelivery(DeliveryDto deliveryDto);
}
