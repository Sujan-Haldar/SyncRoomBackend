package org.discord.backend.controller;

import lombok.RequiredArgsConstructor;
import org.discord.backend.dto.*;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.service.ServerService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;
    private final MongoTemplate mongoTemplate;
    @GetMapping("/member/{profileId}")
    public ResponseEntity<DiscordSuccessResponse> getFirstServerByMemberProfileId(@PathVariable String profileId) throws DiscordException {
        return serverService.findFirstServerByMemberProfileId(profileId)
                .map((server)->ResponseEntity.ok(new DiscordSuccessResponse("S-10004",server)))
                .orElseThrow(()->new DiscordException("E-10003", HttpStatus.NOT_FOUND));
    }
    @PostMapping()
    public  ResponseEntity<DiscordSuccessResponse> createServer(@RequestBody ServerRequestDto data) throws DiscordException {
        return serverService.createServer(data).
                map((server -> ResponseEntity.status(HttpStatus.CREATED).body(new DiscordSuccessResponse("S-10003",server))))
                .orElseThrow(()->new DiscordException("E-10004",HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DiscordSuccessResponse> getAllServerByUserID(@PathVariable String userId) throws DiscordException {
        return serverService.getServersByUSerID(userId)
                .map(servers->ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new DiscordSuccessResponse("S-10004",servers)))
                .orElseThrow(()->new DiscordException("E-10003",HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/server-and-userid")
    public ResponseEntity<DiscordSuccessResponse> getServerByServerIdAndUserId(@RequestBody ServerRequestByServerAndUserIdDto body) throws DiscordException {
        return serverService.getServerByServerIdAndUserId(body.serverId,body.userId)
                .map(server->ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new DiscordSuccessResponse("",server)))
                .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
    }
    @PatchMapping()
    public ResponseEntity<DiscordSuccessResponse> updateServerPatchByAdmin(@RequestBody ServerPatchRequestDto body) throws DiscordException {
        return serverService.updateServerPatchByAdmin(body)
                .map(server->ResponseEntity
                        .ok(new DiscordSuccessResponse("",server)))
                .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
    }
    @PatchMapping("/invite")
    public ResponseEntity<DiscordSuccessResponse> addMemberToServerForInvite(@RequestBody ServerInviteRequestDto body) throws DiscordException {
        return serverService.addMemberToServerForInvite(body.getUserId(),body.getInviteCode())
                .map(server->ResponseEntity
                        .ok(new DiscordSuccessResponse("",server)))
                .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
    }


}
