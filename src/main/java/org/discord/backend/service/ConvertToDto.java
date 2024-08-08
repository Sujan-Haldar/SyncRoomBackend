package org.discord.backend.service;

import org.discord.backend.dto.*;
import org.discord.backend.entity.*;
import org.discord.backend.util.Role;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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
                .members(sortMember(server.getMembers()))
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
    public ConversationResponseDto coversationToConversationResponseDto(Conversation conversation){
        return ConversationResponseDto.builder()
                .id(conversation.getId())
                .memberOne(memberToMemberResponseDto(conversation.getMemberOne()))
                .memberTwo(memberToMemberResponseDto(conversation.getMemberTwo()))
                .build();
    }
    public  MessageResponseDto messageToMessageResponseDto (Message message){
        return MessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .fileUrl(message.getFileUrl())
                .member(this.memberToMemberResponseDto(message.getMember()))
                .createdAt(message.getCreatedAt().toString())
                .updated(message.isUpdated())
                .deleted(message.isDeleted())
                .build();
    }
    public  MessageResponseDto directMessageToMessageResponseDto (DirectMessage message){
        return MessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .fileUrl(message.getFileUrl())
                .member(this.memberToMemberResponseDto(message.getMember()))
                .createdAt(message.getCreatedAt().toString())
                .updated(message.isUpdated())
                .deleted(message.isDeleted())
                .build();
    }
    public List<MemberResponseDto> sortMember (List<Member> members){
        return members
                .stream()
                .sorted(Comparator.comparing(member ->{
                    String temp =  member.getRole().toString();
                    if(temp.equals(Role.MODERATOR.toString())) return 'C';
                    return temp.charAt(0);
                }))
                .map(this::memberToMemberResponseDto)
                .toList();
    }
}
