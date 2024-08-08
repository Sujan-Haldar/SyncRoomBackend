package org.discord.backend.controller;

import lombok.RequiredArgsConstructor;
import org.discord.backend.dto.ConversationCreateRequestDto;
import org.discord.backend.dto.DiscordSuccessResponse;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.service.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversation")
public class ConversationController {
    private final ConversationService conversationService;
    @PostMapping()
    public ResponseEntity<DiscordSuccessResponse> getOrCreateConversation(@RequestBody ConversationCreateRequestDto body) throws DiscordException {
        return conversationService.getOrCreateConversation(body)
                .map(conversation->ResponseEntity.ok(new DiscordSuccessResponse("",conversation)))
                .orElseThrow(()->new DiscordException("", HttpStatus.BAD_REQUEST));
    }

}
