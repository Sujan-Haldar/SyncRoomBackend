package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.util.OTPType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;

    public void saveOtp(String email, String otp , OTPType otpType) {
        redisTemplate.opsForValue().set(email+otpType.toString(), otp, 5, TimeUnit.MINUTES); // Save OTP with a TTL of 5 minutes
    }

    public String getOtp(String email,OTPType otpType) {
        return redisTemplate.opsForValue().get(email+otpType.toString());
    }
    public void validateOtp(String email,OTPType otpType,String otp) throws DiscordException {
        if(otp == null || otp.isEmpty()) throw new DiscordException("E-10010", HttpStatus.BAD_REQUEST);
        String storedOtp = this.getOtp(email,otpType);
        if(storedOtp == null) throw new DiscordException("E-10011", HttpStatus.BAD_REQUEST);
        if(!storedOtp.equals(otp)) throw new DiscordException("E-10011", HttpStatus.BAD_REQUEST);
    }

    public void sendOtpToEmail(String email,OTPType otpType) throws DiscordException {
        String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
        try{
           this.saveOtp(email,otp,otpType);
           emailService.sendSimpleEmail(email,"DISCORD OTP","This is your otp "+otp);
        }catch (Exception e){
            e.printStackTrace();
            throw new DiscordException("E-10012",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
