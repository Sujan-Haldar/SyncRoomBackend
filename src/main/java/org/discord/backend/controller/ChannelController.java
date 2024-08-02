package org.discord.backend.controller;

import lombok.RequiredArgsConstructor;
import org.discord.backend.dto.ChannelCreateRequestDto;
import org.discord.backend.dto.ChannelUpdateRequestDto;
import org.discord.backend.dto.DiscordSuccessResponse;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/channel")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<DiscordSuccessResponse> createChannel(@RequestBody ChannelCreateRequestDto body) throws DiscordException {
        return channelService.createChannel(body)
                .map(channel->ResponseEntity.status(HttpStatus.CREATED)
                        .body(new DiscordSuccessResponse("",channel)))
                .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
    }
    @PatchMapping()
    public ResponseEntity<DiscordSuccessResponse> updateChannel(@RequestBody ChannelUpdateRequestDto body) throws DiscordException {
        return channelService.updateChannel(body)
                .map((channelResponseDto -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new DiscordSuccessResponse("",channelResponseDto))))
                .orElseThrow(()->new  DiscordException("",HttpStatus.INTERNAL_SERVER_ERROR));
    }
    @DeleteMapping("/{channelId}")
    public  ResponseEntity<DiscordSuccessResponse> deleteChannel(@PathVariable String channelId) throws DiscordException {
        channelService.deleteChannel(channelId);
        return ResponseEntity.ok(new DiscordSuccessResponse(""));
    }

}
