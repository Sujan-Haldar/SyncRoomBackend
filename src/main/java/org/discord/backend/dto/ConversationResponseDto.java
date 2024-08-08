package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponseDto {
    private String id;
    private MemberResponseDto memberOne;
    private MemberResponseDto memberTwo;
}
