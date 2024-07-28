package org.discord.backend.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.discord.backend.entity.User;
import org.discord.backend.service.UserService;
import org.discord.backend.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2LoginSucessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = oauthToken.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String client = oauthToken.getAuthorizedClientRegistrationId();
        List<User> users = new ArrayList<>();
        if(client.equals("github")){
            users = userService.getUsersByUserId(String.valueOf((int) attributes.get("id")));
            if(users.isEmpty()){
                User user = User.builder()
                        .name((String) attributes.get("name"))
                        .imageUrl((String) attributes.get("avatar_url"))
                        .userId(String.valueOf((int) attributes.get("id")))
                        .build();
                user = userService.createUser(user);
                users.add(user);
            }

        }else if(client.equals("google")){
            users = userService.getUsersByEmail((String) attributes.get("email"));
            if(users.isEmpty()){
                User user = User.builder()
                        .name((String) attributes.get("name"))
                        .imageUrl((String) attributes.get("picture"))
                        .email((String) attributes.get("email"))
                        .build();
                user = userService.createUser(user);
                users.add(user);
            }

        }
        String authToken = jwtUtils.generateToken(users.getFirst().getId());
        Cookie cookie = new Cookie("DISCORDTOKEN", authToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
        String redirectUrl = "http://localhost:3000/oauth2/redirect";
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}

