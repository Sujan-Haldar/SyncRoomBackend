package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    private String id;
    private String content;
    private String fileUrl;
    private MemberResponseDto member;
    private boolean updated;
    private boolean deleted;
    private String createdAt;

}
