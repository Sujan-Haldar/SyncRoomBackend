package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.discord.backend.util.ChannelType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResponseDto {
    private String id;
    private String name;
    private ChannelType type;
}
