package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.discord.backend.dto.*;
import org.discord.backend.entity.Channel;
import org.discord.backend.entity.Member;
import org.discord.backend.entity.Server;
import org.discord.backend.entity.User;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.repository.ChannelRepository;
import org.discord.backend.repository.MemberRepository;
import org.discord.backend.repository.ServerRepository;
import org.discord.backend.repository.UserRepository;
import org.discord.backend.util.ChannelType;
import org.discord.backend.util.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepository;
    private  final  ConvertToDto convertToDto;
    public Optional<ServerResponseDtoForBasicInfo> findFirstServerByMemberProfileId(String profileId) throws DiscordException {
        User user = userRepository.findById(profileId).orElseThrow(()->new DiscordException("E-10002", HttpStatus.NOT_FOUND));;
        return serverRepository.findFirstByMembersContaining(user.getMembers()).map(convertToDto::serverToServerResponseDtoBasicInfo);

    }
    public Optional<ServerResponseDtoForBasicInfo> createServer(ServerRequestDto serverRequestDto) throws DiscordException {
       User user= userRepository.findById(serverRequestDto.getUserID())
               .orElseThrow(()->new DiscordException("E-10002", HttpStatus.NOT_FOUND));

       Server server = Server.builder()
               .name(serverRequestDto.getName())
               .imageUrl(serverRequestDto.getImageUrl())
               .inviteCode(serverRequestDto.getInviteCode())
               .user(user)
               .build();
       Server savedServer = serverRepository.save(server);
       Member member = Member.builder()
               .user(user)
               .role(Role.ADMIN)
               .server(savedServer)
               .build();
       Member savedMember = memberRepository.save(member);
       Channel channel = Channel.builder()
               .name("general")
               .type(ChannelType.TEXT)
               .user(user)
               .server(server)
               .build();
       Channel savedChannel = channelRepository.save(channel);
       List<Member> members = new ArrayList<>();
       List<Channel> channels = new ArrayList<>();
       members.add(savedMember);
       channels.add(savedChannel);

       savedServer.setChannels(channels);
       savedServer.setMembers(members);
       user.getMembers().add(savedMember);
       user.getChannels().add(savedChannel);
       user.getServers().add(savedServer);
       userRepository.save(user);
       savedServer = serverRepository.save(savedServer);
       return Optional.of(convertToDto.serverToServerResponseDtoBasicInfo(savedServer));

    }

    public Optional<List<ServerResponseDtoForBasicInfo>> getServersByUSerID(String userId) throws DiscordException {
        User user = userRepository.findById(userId).orElseThrow(()->new DiscordException("E-10002", HttpStatus.NOT_FOUND));
        List<Server> servers = serverRepository.findAllByMembersContaining(user.getMembers());
        return Optional.of(servers.stream().map(convertToDto::serverToServerResponseDtoBasicInfo).toList());
    }
    public Optional<ServerResponseDto> getServerByServerIdAndUserId(String serverId,String userId ){
        Optional<Member> optionalMember = memberRepository.findFirstByUserAndServer(User.builder().id(userId).build(), Server.builder().id(serverId).build());
        AtomicReference<Optional<ServerResponseDto>> ar = new AtomicReference<>();
        optionalMember.ifPresent(member -> ar.set(serverRepository.findServerByIdAndMembersContains(serverId,member)
                .map(convertToDto::serverToServerResponseDto))
        );
        return ar.get();
    }
    public Optional<ServerResponseDtoForBasicInfo> updateServerPatchByAdmin(ServerPatchRequestDto data) throws DiscordException {
        Server server = serverRepository.findServerByIdAndUser(data.getServerId(),User.builder().id(data.getUserId()).build()).orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
        if(data.getInviteCode() != null)
            server.setInviteCode(data.getInviteCode());
        if(data.getName() != null)
            server.setName(data.getName());
        if(data.getImageUrl() != null)
            server.setImageUrl(data.getImageUrl());
        return Optional.of(convertToDto.serverToServerResponseDtoBasicInfo(serverRepository.save(server)));

    }
    public Optional<ServerResponseDtoForBasicInfo> addMemberToServerForInvite(String userId,String inviteCode) throws DiscordException {
        Server server = serverRepository.findFirstByInviteCode(inviteCode).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
       Optional<Member> optionalMember= memberRepository.findFirstByUserAndServer(User.builder().id(userId).build(), server) ;
       if(optionalMember.isPresent()) {
           return Optional.of(convertToDto.serverToServerResponseDtoBasicInfo(server));
       }
       User user = userRepository.findById(userId).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
       Member member = Member.builder()
               .user(user)
               .server(server)
               .role(Role.GUEST)
               .build();
       member = memberRepository.save(member);
        System.out.println(member);
       user.getMembers().add(member);
       userRepository.save(user);
       server.getMembers().add(member);
       return Optional.of(convertToDto.serverToServerResponseDtoBasicInfo(serverRepository.save(server)));
    }
    public  void leaveServer(ServerPatchRequestDto data) throws DiscordException {
        if(data.getServerId() == null) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        if(data.getUserId() == null) throw new DiscordException("",HttpStatus.BAD_REQUEST);

        Member member = memberRepository.
                findFirstByUserAndServer(User.builder().id(data.getUserId()).build(),
                        Server.builder().id(data.getServerId()).build())
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        if(member.getRole().toString().equals(Role.ADMIN.toString())) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        serverRepository.deleteMemberFromServer(data.getServerId(),member.getId());
        userRepository.deleteMemberFromUser(data.getUserId(),member.getId());
        memberRepository.delete(member);
    }
    public void deleteServer(ServerDeleteRequestDto data) throws DiscordException{
        if(data.getServerId() == null) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        if(data.getUserId() == null) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        Server server = serverRepository.findById(data.getServerId()).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        if(!server.getUser().getId().equals(data.getUserId())) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        String[] membersId = server.getMembers().stream().map(Member::getId).toArray(String[]::new);
        String[] channelsId = server.getChannels().stream().map(Channel::getId).toArray(String[]::new);
        System.out.println(Arrays.toString(membersId));
        System.out.println(Arrays.toString(channelsId));
        userRepository.deleteServerFromUser(data.getUserId(),data.getServerId());
        userRepository.deleteMembersFromUser(Arrays.stream(membersId).map(ObjectId::new).toArray(ObjectId[]::new));
        userRepository.deleteChannelsFromUser(Arrays.stream(channelsId).map(ObjectId::new).toArray(ObjectId[]::new));
        memberRepository.deleteMembersByServer(server);
        channelRepository.deleteChannelsByServer(server);
        serverRepository.delete(server);
    }

}
