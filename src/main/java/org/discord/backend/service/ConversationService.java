package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import org.discord.backend.cascade.ConversationCascade;
import org.discord.backend.dto.ConversationCreateRequestDto;
import org.discord.backend.dto.ConversationResponseDto;
import org.discord.backend.entity.Conversation;
import org.discord.backend.entity.Member;
import org.discord.backend.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationCascade conversationCascade;
    private final ConvertToDto convertToDto;
    public Optional<ConversationResponseDto> getOrCreateConversation(ConversationCreateRequestDto data){
        Member memberOne = Member.builder().id(data.getMemberOne()).build();
        Member memberTwo = Member.builder().id(data.getMemberTwo()).build();
        Optional<Conversation> conversationOptional = conversationRepository.findFirstByMemberOneAndMemberTwo(memberOne,memberTwo);
        if(conversationOptional.isPresent()) return  conversationOptional.map(convertToDto::coversationToConversationResponseDto);
        conversationOptional = conversationRepository.findFirstByMemberOneAndMemberTwo(memberTwo,memberOne);
        if(conversationOptional.isPresent()) return  conversationOptional.map(convertToDto::coversationToConversationResponseDto);;
        Conversation conversation = Conversation.builder()
                .memberOne(memberOne)
                .memberTwo(memberTwo)
                .build();
        conversation = conversationRepository.save(conversation);
        conversationCascade.onAfterSaveConverssation(conversation);
        return Optional.of(convertToDto.coversationToConversationResponseDto(conversation));
    }
}
