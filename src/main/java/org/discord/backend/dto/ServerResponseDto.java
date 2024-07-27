package org.discord.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponseDto {
    private String id;
    private String name;
    private String imageUrl;
    private String inviteCode;
    private UserResponseDto user;
    private List<MemberResponseDto> members;
    private List<ChannelResponseDto> channels;
}
