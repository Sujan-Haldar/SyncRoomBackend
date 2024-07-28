package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerRequestDto {
    private String userID;
    private String name;
    private String imageUrl;
    private  String inviteCode;
}
