package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import org.discord.backend.dto.DiscordErrorResponse;
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
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;

    public Optional<MemberResponseDto> updateMember(MemberRequestDto data) throws DiscordException {
        if(data.getId() == null) throw new DiscordException("", HttpStatus.BAD_REQUEST);
        Member member = memberRepository.findById(data.getId())
                .orElseThrow(()-> new DiscordException("",HttpStatus.NOT_FOUND));
        member.setRole(data.getRole().equals(Role.MODERATOR.toString())?Role.MODERATOR:Role.GUEST);
        return Optional.of(convertToDto.memberToMemberResponseDto(memberRepository.save(member)));
    }
    public void deleteMember(MemberRequestDto data) throws DiscordException {
        if(data.getId() == null) throw new DiscordException("", HttpStatus.BAD_REQUEST);
        Member member = memberRepository.findById(data.getId())
                .orElseThrow(()-> new DiscordException("",HttpStatus.NOT_FOUND));
        Server server = serverRepository.findById(member.getServer().getId()).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        User user = userRepository.findById(member.getUser().getId()).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        server.getMembers().remove(member);
        user.getMembers().remove(member);
        serverRepository.save(server);
        userRepository.save(user);
        memberRepository.delete(member);
    }


}
