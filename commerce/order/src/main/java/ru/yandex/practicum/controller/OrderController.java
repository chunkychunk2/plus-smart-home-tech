package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderClient {

    private final OrderService orderService;

    @Override
    @GetMapping
    public Page<OrderDto> getOrders(String username, Pageable pageable) {
        log.info("Getting orders, username={}", username);
        return orderService.getOrders(username, pageable);
    }

    @Override
    @PutMapping
    public OrderDto createOrder(CreateNewOrderRequest request) {
        log.info("Creating order request={}", request);
        return orderService.createOrder(request);
    }

    @Override
    @PostMapping("/return")
    public OrderDto returnOrder(ProductReturnRequest request) {
        log.info("Returning order request={}", request);
        return orderService.returnOrder(request);
    }

    @Override
    @PostMapping("/payment")
    public OrderDto payOrder(UUID orderId) {
        log.info("Paying order id={}", orderId);
        return orderService.payOrder(orderId);
    }

    @Override
    @PostMapping("/payment/failed")
    public OrderDto failOrderPayment(UUID orderId) {
        log.info("Failed to pay order id={}", orderId);
        return orderService.failOrderPayment(orderId);
    }

    @Override
    @PostMapping("/delivery")
    public OrderDto deliverOrder(UUID orderId) {
        log.info("Delivery order id={}", orderId);
        return orderService.deliverOrder(orderId);
    }

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto failedDeliverOrder(UUID orderId) {
        log.info("Failed to deliver order id={}", orderId);
        return orderService.failedDeliverOrder(orderId);
    }

    @Override
    @PostMapping("/completed")
    public OrderDto completeOrder(UUID orderId) {
        log.info("Complete order id={}", orderId);
        return orderService.completeOrder(orderId);
    }

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(UUID orderId) {
        log.info("Calculating total price order id={}", orderId);
        return orderService.calculateTotal(orderId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(UUID orderId) {
        log.info("Calculating delivery price order id={}", orderId);
        return orderService.calculateDelivery(orderId);
    }

    @Override
    @PostMapping("/assembly")
    public OrderDto assembleOrder(UUID orderId) {
        log.info("Assemble order id={}", orderId);
        return orderService.assembleOrder(orderId);
    }

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto assembleOrderFailed(UUID orderId) {
        log.info("Failed to assemble order id={}", orderId);
        return orderService.assembleOrderFailed(orderId);
    }
}
