package org.discord.backend.service;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.discord.backend.cascade.MessageCascade;
import org.discord.backend.dto.MessageCreateRequestDto;
import org.discord.backend.dto.MessagePatchRequestDto;
import org.discord.backend.dto.MessageResponseDto;
import org.discord.backend.entity.*;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.repository.ChannelRepository;
import org.discord.backend.repository.MemberRepository;
import org.discord.backend.repository.MessageRepository;
import org.discord.backend.repository.ServerRepository;
import org.discord.backend.socketIO.SocketIoService;
import org.discord.backend.util.Role;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final MessageCascade messageCascade;
    private final  ConvertToDto convertToDto;
    private final SocketIoService socketIoService;
    public Optional<MessageResponseDto> createMessage(MessageCreateRequestDto data) throws DiscordException {
        if(data.getServerId() == null) throw new DiscordException("", HttpStatus.BAD_REQUEST);
        if(data.getChannelId() == null) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        if(data.getUserId() == null) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        if(data.getContent() == null && data.getFileUrl()==null) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        Member member = memberRepository
                .findFirstByUserAndServer(User.builder().id(data.getUserId()).build(),Server.builder().id(data.getServerId()).build())
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        Channel channel = channelRepository
                .findFirstByIdAndServer(data.getChannelId(),Server.builder().id(data.getServerId()).build())
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        Message message = Message.builder()
                .content(data.getContent())
                .fileUrl(data.getFileUrl())
                .member(member)
                .channel(channel)
                .build();
        message = messageRepository.save(message);
        messageCascade.onAfterSaveMessage(message);
        socketIoService.sendNewMessageToChannel(data.getChannelId(), convertToDto.messageToMessageResponseDto(message));
        return Optional.of(convertToDto.messageToMessageResponseDto(message));
    }

    public Optional<List<MessageResponseDto>> getMessages(String userId,String serverId,String channelId,String cursor,int pageSize) throws DiscordException {
        if(channelId == null || channelId.isEmpty()) throw  new DiscordException("",HttpStatus.BAD_REQUEST);
        if(userId == null || userId.isEmpty()) throw  new DiscordException("",HttpStatus.BAD_REQUEST);
        if(serverId == null || serverId.isEmpty()) throw  new DiscordException("",HttpStatus.BAD_REQUEST);
        Member member = memberRepository
                .findFirstByUserAndServer(User.builder().id(userId).build(),Server.builder().id(serverId).build())
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        Channel channel = channelRepository
                .findFirstByIdAndServer(channelId,Server.builder().id(serverId).build())
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        List<Message> messages;
        PageRequest pageRequest = PageRequest.of(0,pageSize);
        if(cursor != null && !cursor.isEmpty()){
            messages = messageRepository
                    .findByChannelAndIdLessThanOrderByCreatedAtDesc(channel,cursor,pageRequest)
                    .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
        }else{
            messages = messageRepository
                    .findByChannelOrderByCreatedAtDesc(channel,pageRequest)
                    .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
        }
        return  Optional.of(messages.stream().map(convertToDto::messageToMessageResponseDto).toList());

    }

    public Optional<MessageResponseDto> updateOrDeleteMessage(MessagePatchRequestDto data) throws DiscordException {
        if(data.getMessageId() == null || data.getMessageId().isEmpty()) throw  new DiscordException("Missing Message Id",HttpStatus.BAD_REQUEST);
        if(data.getChannelId() == null || data.getChannelId().isEmpty()) throw  new DiscordException("Missing Channel Id",HttpStatus.BAD_REQUEST);
        if(data.getUserId() == null || data.getUserId().isEmpty()) throw  new DiscordException("Missing User Id",HttpStatus.BAD_REQUEST);
        if(data.getServerId() == null || data.getServerId().isEmpty()) throw  new DiscordException("Missing Server Id",HttpStatus.BAD_REQUEST);
        if(data.getMethod() == null || data.getMethod().isEmpty()) throw  new DiscordException("Missing Method Id",HttpStatus.BAD_REQUEST);
        if(!data.getMethod().equals("UPDATE") && !data.getMethod().equals("DELETE")) throw  new DiscordException("Method type",HttpStatus.BAD_REQUEST);
        if(data.getMethod().equals("UPDATE") && (data.getContent()==null || data.getContent().isEmpty())) throw  new DiscordException("Missing Content",HttpStatus.BAD_REQUEST);
        Member member = memberRepository
                .findFirstByUserAndServer(User.builder().id(data.getUserId()).build(),Server.builder().id(data.getServerId()).build())
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        Channel channel = channelRepository
                .findFirstByIdAndServer(data.getChannelId(),Server.builder().id(data.getServerId()).build())
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        Message message = messageRepository.findById(data.getMessageId()).orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        boolean isOwner = message.getMember().getId().equals(member.getId());
        boolean isAdmin = member.getRole().equals(Role.ADMIN);
        boolean isModerator = member.getRole().equals(Role.MODERATOR);
        boolean canDelete = isOwner || isModerator || isAdmin;

        if(data.getMethod().equals("DELETE") && canDelete){
            message.setDeleted(true);
            message.setContent("This message has been deleted.");
            message.setFileUrl(null);
        } else if(data.getMethod().equals("UPDATE")){
            if(!isOwner) throw new DiscordException("Unautorized For update",HttpStatus.BAD_REQUEST);
            message.setUpdated(true);
            message.setContent(data.getContent());
        }
        message = messageRepository.save(message);
        socketIoService.sendUpdateOrDeleteMessageToChannel(data.getChannelId(), convertToDto.messageToMessageResponseDto(message));
        return Optional.of(convertToDto.messageToMessageResponseDto(message));
    }
}
