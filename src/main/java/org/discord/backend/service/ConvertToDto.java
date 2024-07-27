package org.discord.backend.service;

import org.discord.backend.dto.*;
import org.discord.backend.entity.Channel;
import org.discord.backend.entity.Member;
import org.discord.backend.entity.Server;
import org.discord.backend.entity.User;
import org.discord.backend.util.Role;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class ConvertToDto {
    public ServerResponseDtoForBasicInfo serverToServerResponseDtoBasicInfo(Server server){
        return ServerResponseDtoForBasicInfo.builder()
                .id(server.getId())
                .name(server.getName())
                .inviteCode(server.getInviteCode())
                .imageUrl(server.getImageUrl())
                .build();
    }
    public ServerResponseDto serverToServerResponseDto(Server server){
        return ServerResponseDto.builder()
                .id(server.getId())
                .name(server.getName())
                .inviteCode(server.getInviteCode())
                .imageUrl(server.getImageUrl())
                .user(userToUserResponseDto(server.getUser()))
                .channels(server.getChannels()
                        .stream()
                        .sorted(Comparator.comparing(Channel::getCreatedAt))
                        .map(this::channelToChannelResponseDto)
                        .toList())
                .members(server.getMembers()
                        .stream()
                        .sorted(Comparator.comparing(member ->{
                            String temp =  member.getRole().toString();
                            if(temp.equals(Role.MODERATOR.toString())) return 'C';
                            return temp.charAt(0);
                        }))
                        .map(this::memberToMemberResponseDto)
                        .toList())
                .build();
    }

    public ChannelResponseDto channelToChannelResponseDto(Channel channel){
        return ChannelResponseDto.builder()
                .id(channel.getId())
                .type(channel.getType())
                .name(channel.getName())
                .build();
    }
    public MemberResponseDto memberToMemberResponseDto(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .role(member.getRole())
                .user(this.userToUserResponseDto(member.getUser()))
                .build();
    }
    public UserResponseDto userToUserResponseDto(User user){
        return  UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .build();
    }
}
