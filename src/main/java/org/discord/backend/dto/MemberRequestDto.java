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
public class MemberRequestDto {
    private String id;
    private String role;
}
