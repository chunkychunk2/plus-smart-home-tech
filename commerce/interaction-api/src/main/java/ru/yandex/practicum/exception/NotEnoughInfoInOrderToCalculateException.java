package ru.yandex.practicum.exception;

import feign.FeignException;

public class NotEnoughInfoInOrderToCalculateException extends FeignException {
    public NotEnoughInfoInOrderToCalculateException(int status, String message) {
        super(status, message);
    }
}
