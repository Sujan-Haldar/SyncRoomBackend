package org.discord.backend.controller;

import lombok.RequiredArgsConstructor;
import org.discord.backend.service.EmailService;
import org.discord.backend.service.OTPService;
import org.discord.backend.util.OTPType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/temp")
@RequiredArgsConstructor
public class TempController {
    private final OTPService otpService;
    @GetMapping()
    public ResponseEntity<String> sendEmail(){
        otpService.saveOtp("hsujan122@gmail.com","1234567", OTPType.SIGNIN);
        System.out.println(otpService.getOtp("jskdk",OTPType.SIGNIN));
        return ResponseEntity.ok(otpService.getOtp("hsujan122@gmail.com",OTPType.SIGNIN));
    }
}
