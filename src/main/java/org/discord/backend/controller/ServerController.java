package org.discord.backend.controller;

import org.discord.backend.dto.DiscordSuccessResponse;
import org.discord.backend.entity.Server;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/servers")
public class ServerController {
    @Autowired
    private ServerService serverService;

    @GetMapping("/member/{profileId}")
    public ResponseEntity<DiscordSuccessResponse> getFirstServerByMemberProfileId(@PathVariable String profileId) throws DiscordException {
        return serverService.findFirstServerByMemberProfileId(profileId)
                .map((server)->ResponseEntity.ok(new DiscordSuccessResponse("",server)))
                .orElseThrow(()->new DiscordException("", HttpStatus.NOT_FOUND));
    }
}
