package org.discord.backend.service;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.backend.dto.*;
import org.discord.backend.entity.User;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.repository.UserRepository;
import org.discord.backend.util.JWTUtils;
import org.discord.backend.util.OTPType;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private  final  OTPService otpService;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;
    private final  ConvertToDto convertToDto;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    public UserResponseDto signUpUser(UserRegisterDto data) throws DiscordException {
        if(data.getEmail() == null || data.getEmail().isEmpty()) throw new DiscordException("E-10005", HttpStatus.BAD_REQUEST);
        if(data.getName() == null || data.getName().isEmpty()) throw new DiscordException("E-10006", HttpStatus.BAD_REQUEST);
        if(data.getPassword() == null || data.getPassword().isEmpty()) throw new DiscordException("E-10007", HttpStatus.BAD_REQUEST);
        if(userRepository.findFirstByEmail(data.getEmail()).isPresent()) throw new DiscordException("E-10008",HttpStatus.BAD_REQUEST);
        otpService.validateOtp(data.getEmail(), OTPType.SIGNUP, data.getOtp());
        User user = User.builder()
                .name(data.getName())
                .email(data.getEmail())
                .password(passwordEncoder.encode(data.getPassword()))
                .build();
        return convertToDto.userToUserResponseDto(userRepository.save(user));
    }

    public  UserResponseDto signInUser(AuthRequest data) throws DiscordException {
        if(data.getEmail() == null || data.getEmail().isEmpty()) throw new DiscordException("E-10005",HttpStatus.BAD_REQUEST);
        if(data.getPassword() == null || data.getPassword().isEmpty()) throw new DiscordException("E-10007",HttpStatus.BAD_REQUEST);
        try {
            Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
            if(authentication.isAuthenticated()){
                User user = userRepository.findFirstByEmail(data.getEmail()).orElseThrow(()->new DiscordException("E-10002",HttpStatus.NOT_FOUND));
                return convertToDto.userToUserResponseDto(user);
            }
        }catch (BadCredentialsException e){
            throw new DiscordException("E-10009",HttpStatus.UNAUTHORIZED);
        }
        throw new DiscordException("E-10009",HttpStatus.UNAUTHORIZED);
    }

    public  void forgetPassword(ForgetPasswordRequestDto data) throws DiscordException {
        if(data.getEmail() == null || data.getEmail().isEmpty()) throw new DiscordException("E-10005",HttpStatus.BAD_REQUEST);
        if(data.getPassword() == null || data.getPassword().isEmpty()) throw new DiscordException("E-10007",HttpStatus.BAD_REQUEST);
        otpService.validateOtp(data.getEmail(), OTPType.FORGETPASSWORD, data.getOtp());
        User user = userRepository.findFirstByEmail(data.getEmail()).orElseThrow(()->new DiscordException("E-10002",HttpStatus.NOT_FOUND));
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        userRepository.save(user);
    }

    public Cookie generateCookie(String mongoId){
        String authToken = jwtUtils.generateToken(mongoId,false);
        Cookie cookie = new Cookie("DISCORDTOKEN", authToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }
}
