package org.discord.backend.dto;

import lombok.Data;

@Data
public class DiscordSuccessResponse {
    private boolean status;
    private String status_code;
    private Object data;
    public DiscordSuccessResponse(String status_code){
        this.status = true;
        this.status_code = status_code;
    }
    public DiscordSuccessResponse(String status_code,Object data){
        this.status = true;
        this.status_code = status_code;
        this.data = data;
    }
}
