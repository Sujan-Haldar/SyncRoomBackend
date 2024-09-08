package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.backend.dto.RedisMessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    public void publish(RedisMessageDto data) {
        log.info("Message Publish");
        redisTemplate.convertAndSend(channelTopic.getTopic(), data);
    }

    public String getChannelNewMessageEventName(String channelId){
        return "chat:"+channelId+":messages";
    }
    public String getChannelUpdateOrDeleteMessageEventName(String channelId){
        return "chat:"+channelId+":messages:update";
    }
    public void sendNewMessageToChannel(String channelId, Object object){
        publish(RedisMessageDto.builder()
                .socketEventName(getChannelNewMessageEventName(channelId))
                .message(object)
                .build());
    }
    public void sendUpdateOrDeleteMessageToChannel(String channelId, Object object){
        publish(RedisMessageDto.builder()
                .socketEventName(getChannelUpdateOrDeleteMessageEventName(channelId))
                .message(object)
                .build());
    }
    public void sendNewMessageToConversation(String conversationId, Object object){
        publish(RedisMessageDto.builder()
                .socketEventName(getChannelNewMessageEventName(conversationId))
                .message(object)
                .build());
    }
    public void sendUpdateOrDeleteMessageToConversation(String channelId, Object object){
        publish(RedisMessageDto.builder()
                .socketEventName(getChannelUpdateOrDeleteMessageEventName(channelId))
                .message(object)
                .build());
    }


}
