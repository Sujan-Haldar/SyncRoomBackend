package org.discord.backend.dto;

import lombok.Data;

@Data
public class DiscordErrorResponse {
    private Boolean status;
    private String status_code;
    public DiscordErrorResponse(String status_code){
        this.status_code = status_code;
        this.status = false;
    }
}
