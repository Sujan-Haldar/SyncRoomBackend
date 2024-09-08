package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.discord.backend.cascade.DirectMessageCascade;
import org.discord.backend.dto.*;
import org.discord.backend.entity.*;
import org.discord.backend.exception.DiscordException;
import org.discord.backend.repository.ConversationRepository;
import org.discord.backend.repository.DirectMessageRepository;
import org.discord.backend.repository.MemberRepository;
import org.discord.backend.util.Role;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DirectMessageService {
    private final MemberRepository memberRepository;
    private final ConversationRepository conversationRepository;
    private final DirectMessageRepository directMessageRepository;
    private final ConvertToDto convertToDto;
    private final DirectMessageCascade directMessageCascade;
    private final RedisService redisService;
    public Optional<MessageResponseDto> createMessage(DirectMessageCreateRequestDto data) throws DiscordException {
        if(data.getServerId() == null || data.getServerId().isEmpty()) throw new DiscordException("", HttpStatus.BAD_REQUEST);
        if(data.getConversationId() == null || data.getConversationId().isEmpty()) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        if(data.getUserId() == null || data.getUserId().isEmpty()) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        if(data.getContent() == null && data.getFileUrl()==null) throw new DiscordException("",HttpStatus.BAD_REQUEST);
        Member member = memberRepository
                .findFirstByUserAndServer(User.builder().id(data.getUserId()).build(), Server.builder().id(data.getServerId()).build())
                .orElseThrow(()->new DiscordException("Member Not Found",HttpStatus.NOT_FOUND));
        System.out.println(data.getConversationId());
        Conversation conversation = conversationRepository
                .findFirstByIdAndMemberOneOrMemberTwoId(data.getConversationId(), new ObjectId(member.getId()))
                .orElseThrow(()->new DiscordException("Conversation Not Found",HttpStatus.NOT_FOUND));
        System.out.println(conversation.getId());
        DirectMessage message = DirectMessage.builder()
                .content(data.getContent())
                .fileUrl(data.getFileUrl())
                .member(member)
                .conversation(conversation)
                .build();
        message = directMessageRepository.save(message);
        directMessageCascade.onAfterSaveMessage(message);
        redisService.sendNewMessageToConversation(data.getConversationId(), convertToDto.directMessageToMessageResponseDto(message));
        return Optional.of(convertToDto.directMessageToMessageResponseDto(message));
    }
    public Optional<List<MessageResponseDto>> getMessages(String userId, String serverId, String conversationId, String cursor, int pageSize) throws DiscordException {
        if(conversationId == null || conversationId.isEmpty()) throw  new DiscordException("",HttpStatus.BAD_REQUEST);
        if(userId == null || userId.isEmpty()) throw  new DiscordException("",HttpStatus.BAD_REQUEST);
        if(serverId == null || serverId.isEmpty()) throw  new DiscordException("",HttpStatus.BAD_REQUEST);
        Member member = memberRepository
                .findFirstByUserAndServer(User.builder().id(userId).build(),Server.builder().id(serverId).build())
                .orElseThrow(()->new DiscordException("Member Not Found",HttpStatus.NOT_FOUND));
        Conversation conversation = conversationRepository
                .findFirstByIdAndMemberOneOrMemberTwoId(conversationId, new ObjectId(member.getId()) )
                .orElseThrow(()->new DiscordException("Conversation Not Found",HttpStatus.NOT_FOUND));
        List<DirectMessage> messages;
        PageRequest pageRequest = PageRequest.of(0,pageSize);
        if(cursor != null && !cursor.isEmpty()){
            messages = directMessageRepository
                    .findByConversationAndIdLessThanOrderByCreatedAtDesc(conversation,cursor,pageRequest)
                    .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
        }else{
            messages = directMessageRepository
                    .findByConversationOrderByCreatedAtDesc(conversation,pageRequest)
                    .orElseThrow(()->new DiscordException("",HttpStatus.BAD_REQUEST));
        }
        return  Optional.of(messages.stream().map(convertToDto::directMessageToMessageResponseDto).toList());
    }
    public Optional<MessageResponseDto> updateOrDeleteMessage(DirectMessagePatchRequestDto data) throws DiscordException {
        if(data.getMessageId() == null || data.getMessageId().isEmpty()) throw  new DiscordException("Missing Message Id",HttpStatus.BAD_REQUEST);
        if(data.getConversationId() == null || data.getConversationId().isEmpty()) throw  new DiscordException("Missing Conversation Id",HttpStatus.BAD_REQUEST);
        if(data.getUserId() == null || data.getUserId().isEmpty()) throw  new DiscordException("Missing User Id",HttpStatus.BAD_REQUEST);
        if(data.getServerId() == null || data.getServerId().isEmpty()) throw  new DiscordException("Missing Server Id",HttpStatus.BAD_REQUEST);
        if(data.getMethod() == null || data.getMethod().isEmpty()) throw  new DiscordException("Missing Method Id",HttpStatus.BAD_REQUEST);
        if(!data.getMethod().equals("UPDATE") && !data.getMethod().equals("DELETE")) throw  new DiscordException("Method type",HttpStatus.BAD_REQUEST);
        if(data.getMethod().equals("UPDATE") && (data.getContent()==null || data.getContent().isEmpty())) throw  new DiscordException("Missing Content",HttpStatus.BAD_REQUEST);
        Member member = memberRepository
                .findFirstByUserAndServer(User.builder().id(data.getUserId()).build(),Server.builder().id(data.getServerId()).build())
                .orElseThrow(()->new DiscordException("",HttpStatus.NOT_FOUND));
        Conversation conversation = conversationRepository
                .findFirstByIdAndMemberOneOrMemberTwoId(data.getConversationId(),new ObjectId(member.getId()))
                .orElseThrow(()->new DiscordException("Conversation Not Found",HttpStatus.NOT_FOUND));
        DirectMessage message = directMessageRepository.findById(data.getMessageId()).orElseThrow(()->new DiscordException("Message Not found",HttpStatus.NOT_FOUND));
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
        message = directMessageRepository.save(message);
        redisService.sendUpdateOrDeleteMessageToConversation(data.getConversationId(), convertToDto.directMessageToMessageResponseDto(message));
        return Optional.of(convertToDto.directMessageToMessageResponseDto(message));
    }
}
