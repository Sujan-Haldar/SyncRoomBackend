package org.discord.backend.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class DiscordException extends Exception{
    private HttpStatus httpStatus;
    public DiscordException(String message,HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}
