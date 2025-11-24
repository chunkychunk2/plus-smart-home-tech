package ru.yandex.practicum.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.payment.PaymentStatus;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_cost")
    private Double productCost;

    @Column(name = "delivery_cost")
    private Double deliveryCost;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}