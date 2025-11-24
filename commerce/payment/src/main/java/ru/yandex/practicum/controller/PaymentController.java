package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.client.PaymentClient;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentClient {

    private final PaymentService paymentService;

    @Override
    @PostMapping
    public PaymentDto createPayment(OrderDto orderDto) {
        log.info("Creating payment: order={}", orderDto);
        return paymentService.createPayment(orderDto);
    }

    @Override
    @PostMapping("/totalCost")
    public Double getTotalCost(OrderDto orderDto) {
        log.info("Getting total price order={}", orderDto);
        return paymentService.getTotalCost(orderDto);
    }

    @Override
    @PostMapping("/refund")
    public PaymentDto successPay(UUID payId) {
        log.info("Success payment id={}", payId);
        return paymentService.successPay(payId);
    }

    @Override
    @PostMapping("/productCost")
    public Double getProductCost(OrderDto orderDto) {
        log.info("Getting product price order={}", orderDto);
        return paymentService.getProductCost(orderDto);
    }

    @Override
    @PostMapping("/failed")
    public PaymentDto failedPay(UUID payId) {
        log.info("Fail payment id={}", payId);
        return paymentService.failedPay(payId);
    }
}
