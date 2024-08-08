package org.discord.backend.controller;

import lombok.RequiredArgsConstructor;
import org.discord.backend.dto.*;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.service.DirectMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/direct-message")
@RequiredArgsConstructor
public class DirectMessageController {
    private final DirectMessageService directMessageService;
    private final int MESSAGE_BATCH_SIZE =10;
    @PostMapping()
    public ResponseEntity<DiscordSuccessResponse> createMessage(@RequestBody DirectMessageCreateRequestDto body) throws DiscordException {
        return directMessageService.createMessage(body)
                .map(message->ResponseEntity.ok().body(new DiscordSuccessResponse("",message)))
                .orElseThrow(()->new DiscordException("", HttpStatus.INTERNAL_SERVER_ERROR));
    }
    @GetMapping()
    public  ResponseEntity<DiscordSuccessResponse> getMessages(
            @RequestParam String userId,
            @RequestParam String serverId,
            @RequestParam String conversationId,
            @RequestParam(required = false) String cursor
    ) throws DiscordException {
        List<MessageResponseDto> messages = directMessageService.
                getMessages(userId,serverId,conversationId,cursor,MESSAGE_BATCH_SIZE)
                .orElseThrow(()->new DiscordException("",HttpStatus.INTERNAL_SERVER_ERROR));
        HashMap<String,Object> map = new HashMap<>();
        map.put("messages",messages);
        if(messages.size() == MESSAGE_BATCH_SIZE) map.put("nextCursor",messages.getLast().getId());
        return ResponseEntity.ok(new DiscordSuccessResponse("",map));
    }
    @PatchMapping()
    public ResponseEntity<DiscordSuccessResponse> updateOrDeleteMessage(@RequestBody DirectMessagePatchRequestDto body) throws DiscordException {
        return directMessageService
                .updateOrDeleteMessage(body)
                .map(message->ResponseEntity.ok(new DiscordSuccessResponse("",message)))
                .orElseThrow(()->new DiscordException("",HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
