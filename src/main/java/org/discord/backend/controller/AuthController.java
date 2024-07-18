package org.discord.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.discord.backend.dto.AuthRequest;
import org.discord.backend.dto.DiscordSuccessResponse;
import org.discord.backend.dto.UserDto;
import org.discord.backend.entity.User;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.service.UserService;
import org.discord.backend.util.JWTUtils;
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
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtils jwtUtils;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        userService.createUser(user);
        return  ResponseEntity.ok().body("REgistered");
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) throws DiscordException {
        try {
            Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            if(authentication.isAuthenticated())
                return  ResponseEntity.ok().body("Login");
        }catch (BadCredentialsException e){
            throw new DiscordException("Wrong username or password",HttpStatus.BAD_REQUEST);
        }
        return null;
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
        if(token!=null && jwtUtils.validateJwtToken(token)){
            HashMap<String,Object> map = new HashMap<>();
            map.put("is_login",true);
            map.put("id",jwtUtils.getUserMongoId(token));
            return  ResponseEntity.ok().body(new DiscordSuccessResponse("",map));

        }
        else{
            throw new DiscordException("",HttpStatus.UNAUTHORIZED);
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
            map.put("access_token",jwtUtils.generateToken(mongoId));
            return  ResponseEntity.ok().body(new DiscordSuccessResponse("",map));
        }else{
            throw new DiscordException("",HttpStatus.UNAUTHORIZED);
        }

    }


}
