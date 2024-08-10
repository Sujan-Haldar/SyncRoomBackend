package org.discord.backend.exception;

import org.discord.backend.dto.DiscordErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = DiscordException.class)
    public ResponseEntity<DiscordErrorResponse> discordExceptionHandler(DiscordException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new DiscordErrorResponse(e.getMessage()));
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public DiscordErrorResponse exceptionHandler(Exception e) {
        return new DiscordErrorResponse("E-10001");
    }
}
