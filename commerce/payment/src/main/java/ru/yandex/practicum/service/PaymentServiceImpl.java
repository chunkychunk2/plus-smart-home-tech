package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.payment.PaymentStatus;
import ru.yandex.practicum.entity.Payment;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Value("${tax}")
    private Double TAX_K;

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {

        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .productCost(getProductCost(orderDto))
                .deliveryCost(orderDto.getDeliveryPrice())
                .totalCost(getTotalCost(orderDto))
                .status(PaymentStatus.PENDING)
                .build();

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDto(saved);
    }

    @Override
    public Double getTotalCost(OrderDto orderDto) {
        Double productCost = orderDto.getProductPrice() == null ? getProductCost(orderDto) : orderDto.getProductPrice();
        Double deliveryPrice = orderDto.getDeliveryPrice();
        return productCost + deliveryPrice + productCost * TAX_K;
    }

    @Override
    public PaymentDto successPay(UUID payId) {
        Payment payment = paymentRepository.findById(payId)
                .orElseThrow(() -> new NoOrderFoundException(404, "Payment not found"));
        payment.setStatus(PaymentStatus.SUCCESS);
        Payment saved = paymentRepository.save(payment);
        orderClient.payOrder(payment.getOrderId());
        return paymentMapper.toDto(payment);
    }

    @Override
    public Double getProductCost(OrderDto orderDto) {
        return orderDto.getProducts().entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    Integer quantity = entry.getValue();
                    Double price = shoppingStoreClient.getProductById(productId).getPrice();
                    return price * quantity;
                })
                .mapToDouble(Double::valueOf)
                .sum();
    }

    @Override
    public PaymentDto failedPay(UUID payId) {
        Payment payment = paymentRepository.findById(payId)
                .orElseThrow(() -> new NoOrderFoundException(404, "Payment not found"));
        payment.setStatus(PaymentStatus.FAILED);
        Payment saved = paymentRepository.save(payment);
        orderClient.failOrderPayment(payment.getOrderId());
        return paymentMapper.toDto(payment);
    }
}
