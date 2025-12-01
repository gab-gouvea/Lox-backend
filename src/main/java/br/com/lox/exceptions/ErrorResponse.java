package br.com.lox.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        HttpStatus status,
        int statusCode,
        String message,
        LocalDateTime data
) {
}
