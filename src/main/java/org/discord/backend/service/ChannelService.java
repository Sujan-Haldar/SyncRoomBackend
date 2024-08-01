package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import org.discord.backend.dto.ChannelCreateRequestDto;
import org.discord.backend.dto.ChannelResponseDto;
import org.discord.backend.dto.ChannelUpdateRequestDto;
import org.discord.backend.entity.Channel;
import org.discord.backend.entity.Server;
import org.discord.backend.entity.User;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.repository.ChannelRepository;
import org.discord.backend.repository.ServerRepository;
import org.discord.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private  final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final  ConvertToDto convertToDto;
    public Optional<ChannelResponseDto> createChannel(ChannelCreateRequestDto data) throws DiscordException {
        System.out.println(data);
        if(data.getName() ==null) throw  new DiscordException("", HttpStatus.BAD_REQUEST);
        if(data.getName().equalsIgnoreCase("general")) throw  new DiscordException("", HttpStatus.BAD_REQUEST);
        if(data.getType() ==null) throw  new DiscordException("", HttpStatus.BAD_REQUEST);
        User user = userRepository.findById(data.getUserId()).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        Server server = serverRepository.findById(data.getServerId()).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        Channel channel = Channel.builder()
                .name(data.getName())
                .type(data.getType())
                .user(user)
                .server(server)
                .build();
        channel = channelRepository.save(channel);
        user.getChannels().add(channel);
        server.getChannels().add(channel);
        userRepository.save(user);
        serverRepository.save(server);
        return Optional.of(convertToDto.channelToChannelResponseDto(channel));
    }

    public  Optional<ChannelResponseDto> updateChannel(ChannelUpdateRequestDto data) throws DiscordException {
        if(data.getId() == null) throw  new DiscordException("",HttpStatus.BAD_REQUEST);
        Channel channel = channelRepository.findById(data.getId()).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        if(data.getName() != null){
           channel.setName(data.getName());
        }
        if(data.getType() != null){
           channel.setType(data.getType());
        }
        return Optional.of(convertToDto.channelToChannelResponseDto(channelRepository.save(channel)));
    }
    public  void deleteChannel(String channelId) throws DiscordException {
        if(channelId == null) throw  new DiscordException("",HttpStatus.BAD_REQUEST);
        Channel channel = channelRepository.findById(channelId).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        userRepository.deleteChannelFromUser(channel.getUser().getId(),channelId);
        serverRepository.deleteChannelFromServer(channel.getServer().getId(),channelId);
        channelRepository.delete(channel);
    }

}
