package org.discord.backend.cascade;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.discord.backend.entity.DirectMessage;
import org.discord.backend.entity.Message;
import org.discord.backend.repository.ConversationRepository;
import org.discord.backend.repository.DirectMessageRepository;
import org.discord.backend.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectMessageCascade {
    private final MemberRepository memberRepository;
    private  final ConversationRepository conversationRepository;
    public void onAfterSaveMessage(DirectMessage message){
        conversationRepository.addDirectMessageToConversation(message.getConversation().getId(),new ObjectId(message.getId()));
        memberRepository.addDirectMessageToMember(message.getMember().getId(),new ObjectId(message.getId()));
    }
}
