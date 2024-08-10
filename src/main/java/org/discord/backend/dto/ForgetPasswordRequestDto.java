package org.discord.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordRequestDto {
    private String email;
    private String password;
    private String otp;
}