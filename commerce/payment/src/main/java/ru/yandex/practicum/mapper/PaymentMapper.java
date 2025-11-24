package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toEntity(PaymentDto payment);

    PaymentDto toDto(Payment payment);
}
