package org.discord.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.backend.dto.*;
import org.discord.backend.entity.User;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.repository.UserRepository;
import org.discord.backend.service.AuthService;
import org.discord.backend.service.ConvertToDto;
import org.discord.backend.service.OTPService;
import org.discord.backend.service.UserService;
import org.discord.backend.util.JWTUtils;
import org.discord.backend.util.OTPType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final ConvertToDto convertToDto;
    private final AuthService authService;
    private final OTPService otpService;
    @PostMapping("/sign-up")
    public ResponseEntity<DiscordSuccessResponse> signUp(@RequestBody UserRegisterDto body, HttpServletResponse response) throws DiscordException, InterruptedException {
        UserResponseDto user = authService.signUpUser(body);
        response.addCookie(authService.generateCookie(user.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new DiscordSuccessResponse("S-10005",user));

    }
    @PostMapping("/sign-in")
    public ResponseEntity<DiscordSuccessResponse> signIn(@RequestBody AuthRequest body,HttpServletResponse response) throws DiscordException {
        UserResponseDto user = authService.signInUser(body);
        response.addCookie(authService.generateCookie(user.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new DiscordSuccessResponse("S-10006",user));
    }
    @GetMapping("otp")
    public ResponseEntity<DiscordSuccessResponse> sendOtp(@RequestParam String email, @RequestParam OTPType type) throws DiscordException {
        otpService.sendOtpToEmail(email,type);
        return  ResponseEntity.ok(new DiscordSuccessResponse("S-10007"));
    }
    @PatchMapping("forget-password")
    public ResponseEntity<DiscordSuccessResponse> forgetPassword(@RequestBody ForgetPasswordRequestDto body) throws DiscordException {
        authService.forgetPassword(body);
        return ResponseEntity.status(HttpStatus.OK).body(new DiscordSuccessResponse("S-10008"));
    }
    @GetMapping("/is-login")
    public ResponseEntity<DiscordSuccessResponse> checkAuthentication(HttpServletRequest request) throws DiscordException {
        String token = null;
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("DISCORDTOKEN")){
                    token = cookie.getValue();
                }
            }
        }
        log.info("DISCORDTOKEN :"+token);
        if(token!=null && jwtUtils.validateJwtToken(token)){
            HashMap<String,Object> map = new HashMap<>();
            User user = userRepository.findById(jwtUtils.getUserMongoId(token)).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
            map.put("is_login",true);
            map.put("id", user.getId());
            map.put("user", convertToDto.userToUserResponseDto(user));
            return  ResponseEntity.ok().body(new DiscordSuccessResponse("",map));
        }
        else{
            throw new DiscordException("E-10013",HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/access-token")
    public ResponseEntity<DiscordSuccessResponse> generateAccessToken(HttpServletRequest request) throws DiscordException {
        String token = null;
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("DISCORDTOKEN")){
                    token = cookie.getValue();
                }
            }
        }

        if(token!=null && jwtUtils.validateJwtToken(token)){
            String mongoId = jwtUtils.getUserMongoId(token);
            HashMap<String,Object> map = new HashMap<>();
            map.put("status",true);
            map.put("access_token",jwtUtils.generateToken(mongoId,true));
            return  ResponseEntity.ok().body(new DiscordSuccessResponse("",map));
        }else{
            throw new DiscordException("E-10013",HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/sign-out")
    public ResponseEntity<DiscordSuccessResponse> signOut(HttpServletResponse response){
        Cookie cookie = new Cookie("DISCORDTOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
        return ResponseEntity.ok(new DiscordSuccessResponse("S-10009"));
    }
}
