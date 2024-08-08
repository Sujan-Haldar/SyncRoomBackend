package org.discord.backend.cascade;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.discord.backend.entity.Message;
import org.discord.backend.repository.ChannelRepository;
import org.discord.backend.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageCascade {
    private final ChannelRepository channelRepository;
    private final MemberRepository memberRepository;
    public void onAfterSaveMessage(Message message){
        channelRepository.addMessageToChannel(message.getChannel().getId(),new ObjectId(message.getId()));
        memberRepository.addMessageToMember(message.getMember().getId(),new ObjectId(message.getId()));
    }
}
