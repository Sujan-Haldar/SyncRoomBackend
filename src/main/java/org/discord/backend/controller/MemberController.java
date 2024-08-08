package org.discord.backend.controller;

import lombok.RequiredArgsConstructor;
import org.discord.backend.dto.DiscordSuccessResponse;
import org.discord.backend.dto.MemberRequestDto;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/{userId}/{serverId}")
    public ResponseEntity<DiscordSuccessResponse> getMemberByUserAndServerId(@PathVariable String userId,@PathVariable String serverId) throws DiscordException {
        return memberService.getMemberByUserAndServerId(userId,serverId)
                .map(member->ResponseEntity.ok(new DiscordSuccessResponse("",member)))
                .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
    }

    @PatchMapping()
    public ResponseEntity<DiscordSuccessResponse> updateMember(@RequestBody MemberRequestDto body) throws DiscordException {
        return memberService.updateMember(body).
                map(member->ResponseEntity.status(HttpStatus.OK).body(new DiscordSuccessResponse("",member)))
                .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
    }
    @DeleteMapping()
    public ResponseEntity<DiscordSuccessResponse> deleteMember(@RequestBody MemberRequestDto body) throws DiscordException {
         memberService.deleteMember(body);
         return ResponseEntity.ok(new DiscordSuccessResponse(""));
    }
}
