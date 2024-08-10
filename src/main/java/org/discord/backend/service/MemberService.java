package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import org.discord.backend.cascade.MemberCascade;
import org.discord.backend.dto.MemberRequestDto;
import org.discord.backend.dto.MemberResponseDto;
import org.discord.backend.entity.Member;
import org.discord.backend.entity.Server;
import org.discord.backend.entity.User;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.repository.MemberRepository;
import org.discord.backend.repository.ServerRepository;
import org.discord.backend.repository.UserRepository;
import org.discord.backend.util.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ConvertToDto convertToDto;
    private final MemberCascade memberCascade;
    public Optional<MemberResponseDto> updateMember(MemberRequestDto data) throws DiscordException {
        if(data.getId() == null) throw new DiscordException("", HttpStatus.BAD_REQUEST);
        Member member = memberRepository.findById(data.getId())
                .orElseThrow(()-> new DiscordException("",HttpStatus.NOT_FOUND));
        member.setRole(data.getRole().equals(Role.MODERATOR.toString())?Role.MODERATOR:Role.GUEST);
        return Optional.of(convertToDto.memberToMemberResponseDto(memberRepository.save(member)));
    }
    public Optional<MemberResponseDto> getMemberByUserAndServerId(String userId,String serverId) throws DiscordException {
        return memberRepository
                .findFirstByUserAndServer(User.builder().id(userId).build(), Server.builder().id(serverId).build())
                .map(member -> Optional.of(convertToDto.memberToMemberResponseDto(member)))
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
    }
    public void deleteMember(MemberRequestDto data) throws DiscordException {
        if(data.getId() == null) throw new DiscordException("", HttpStatus.BAD_REQUEST);
        Member member = memberRepository.findById(data.getId())
                .orElseThrow(()-> new DiscordException("",HttpStatus.NOT_FOUND));
        memberRepository.delete(member);
        memberCascade.onAfterDeleteMember(member);
    }


}
