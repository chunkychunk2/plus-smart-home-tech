package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.delivery.DeliveryState;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.entity.Delivery;
import ru.yandex.practicum.entity.DeliveryRepository;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    @Value("${delivery.base-price}")
    private Double DELIVERY_BASE;
    @Value("${delivery.fragile-k}")
    private Double FRAGILE_K;
    @Value("${delivery.weight-k}")
    private Double WEIGHT_K;
    @Value("${delivery.volume-k}")
    private Double VOLUME_K;
    @Value("${delivery.address-k}")
    private Double ADDRESS_K;
    @Value("${delivery.warehouse-address.address1}")
    private Double ADDRESS_1_K;
    @Value("${delivery.warehouse-address.address2}")
    private Double ADDRESS_2_K;


    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto addDelivery(DeliveryDto deliveryDto) {
        Delivery entity = deliveryMapper.toEntity(deliveryDto);
        entity.setDeliveryState(DeliveryState.CREATED);
        Delivery saved = deliveryRepository.save(entity);
        return deliveryMapper.toDto(saved);
    }

    @Override
    public void completeDelivery(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(404, "Delivery id=" + orderId + "not found"));
        delivery.setDeliveryState(DeliveryState.DELIVERED);

        orderClient.deliverOrder(orderId);

        deliveryRepository.save(delivery);
    }

    @Override
    public void pickDelivery(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(404, "Delivery id=" + orderId + "not found"));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);

        orderClient.deliverOrder(orderId);
        warehouseClient.shipProductsToDelivery(ShippedToDeliveryRequest.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(orderId)
                .build());

        deliveryRepository.save(delivery);
    }

    @Override
    public void failDelivery(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(404, "Delivery id=" + orderId + "not found"));
        delivery.setDeliveryState(DeliveryState.FAILED);

        orderClient.failedDeliverOrder(orderId);

        deliveryRepository.save(delivery);
    }

    @Override
    public Double calculateDelivery(DeliveryDto deliveryDto) {
        Double deliveryPrice = DELIVERY_BASE;

        String warehouseAddress = deliveryDto.getFromAddress().getCity();
        Double warehouseK;
        if (warehouseAddress.equals("ADDRESS_1")) {
            warehouseK = ADDRESS_1_K;
        } else if (warehouseAddress.equals("ADDRESS_2")) {
            warehouseK = ADDRESS_2_K;
        } else {
            throw new RuntimeException("Wrong warehouse address");
        }
        deliveryPrice = deliveryPrice + deliveryPrice * warehouseK;

        Double fragileK = deliveryDto.isFragile() ? FRAGILE_K : 0.;
        deliveryPrice = deliveryPrice + deliveryPrice * fragileK;

        deliveryPrice = deliveryPrice + deliveryDto.getDeliveryWeight() * WEIGHT_K;
        deliveryPrice = deliveryPrice + deliveryDto.getDeliveryVolume() * VOLUME_K;

        Double addressK;
        if (deliveryDto.getFromAddress().getStreet().equals(deliveryDto.getToAddress().getStreet())) {
            addressK = 0.;
        } else {
            addressK = ADDRESS_K;
        }
        deliveryPrice = deliveryPrice + deliveryPrice * addressK;

        return deliveryPrice;
    }
}
