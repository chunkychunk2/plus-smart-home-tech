package ru.yandex.practicum.exception;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleFeignException(final FeignException e) {
        log.error("FeignException: status={}, message={}", e.status(), e.getMessage(), e);
        return ResponseEntity.status(e.status())
                .body(Map.of("type", e.getClass().getName(),
                        "message", e.getMessage(),
                        "cause", e.getCause().toString()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(final ConstraintViolationException e) {
        log.warn("ConstraintViolationException: {}", e.getMessage());
        boolean isUsernameViolation = e.getConstraintViolations().stream()
                .anyMatch(v -> v.getPropertyPath().toString().contains("username"));

        if (isUsernameViolation) {
            log.warn("Constraint violation related to 'username' detected. Returning 401 Unauthorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("type", e.getClass().getName(),
                            "message", e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("type", e.getClass().getName(),
                        "message", e.getMessage(),
                        "cause", e.getCause().toString()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleAllUnhandledExceptions(final Exception e) {
        log.error("An unexpected error occurred: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("type", e.getClass().getName(),
                        "message", e.getMessage(),
                        "cause", e.getCause().toString()));
    }
}
