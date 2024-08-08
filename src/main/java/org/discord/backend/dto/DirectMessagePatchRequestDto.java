package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessagePatchRequestDto {
    private String serverId;
    private String conversationId;
    private String userId;
    private String messageId;
    private String method;
    private  String content;
}