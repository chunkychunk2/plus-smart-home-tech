package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto orderDto);

    Double getTotalCost(OrderDto orderDto);

    PaymentDto successPay(UUID payId);

    Double getProductCost(OrderDto orderDto);

    PaymentDto failedPay(UUID payId);
}
