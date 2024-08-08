package org.discord.backend.cascade;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.discord.backend.entity.Conversation;
import org.discord.backend.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversationCascade {
    private final MemberRepository memberRepository;
    public void onAfterSaveConverssation(Conversation conversation){
        memberRepository.addConversationsInitiatorToMember(conversation.getMemberOne().getId(),new ObjectId(conversation.getId()));
        memberRepository.addConversationsReceiverToMember(conversation.getMemberTwo().getId(),new ObjectId(conversation.getId()));
    }
}
