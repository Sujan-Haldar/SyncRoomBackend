package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInviteCodeUpdateRequestDto {
    private String serverId;
    private String userId;
    private String inviteCode;
}
