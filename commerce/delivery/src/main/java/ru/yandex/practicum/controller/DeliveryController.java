package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.client.DeliveryClient;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {

    private final DeliveryService deliveryService;

    @Override
    @PutMapping
    public DeliveryDto addDelivery(DeliveryDto deliveryDto) {
        log.info("Adding delivery: {}", deliveryDto);
        return deliveryService.addDelivery(deliveryDto);
    }

    @Override
    @PostMapping("/successful")
    public void completeDelivery(UUID orderId) {
        log.info("Complete delivery order id={}", orderId);
        deliveryService.completeDelivery(orderId);
    }

    @Override
    @PostMapping("/picked")
    public void pickDelivery(UUID orderId) {
        log.info("Picking delivery order id={}", orderId);
        deliveryService.pickDelivery(orderId);
    }

    @Override
    @PostMapping("/failed")
    public void failDelivery(UUID orderId) {
        log.info("Delivery failed: order id={}", orderId);
        deliveryService.pickDelivery(orderId);
    }

    @Override
    @PostMapping("/cost")
    public Double calculateDelivery(DeliveryDto deliveryDto) {
        log.info("Calculating delivery cost: delivery={}", deliveryDto);
        return deliveryService.calculateDelivery(deliveryDto);
    }
}
