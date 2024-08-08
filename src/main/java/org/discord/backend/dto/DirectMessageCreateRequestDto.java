package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageCreateRequestDto {
    private String serverId;
    private String conversationId;
    private String userId;
    private String content;
    private String fileUrl;
}