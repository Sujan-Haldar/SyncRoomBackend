package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.discord.backend.util.Role;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private String id;
    private Role role;
    private UserResponseDto user;

}
