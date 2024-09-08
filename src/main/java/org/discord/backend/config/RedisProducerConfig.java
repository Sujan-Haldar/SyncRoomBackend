package org.discord.backend.config;

import org.discord.backend.dto.RedisMessageDto;
import org.discord.backend.entity.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisProducerConfig {

    @Bean
    RedisTemplate<String, Object> redisMessagingTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.setConnectionFactory(jedisConnectionFactory);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisMessageDto.class));

        return template;
    }

    @Bean
    StringRedisTemplate stringRedisTemplate(JedisConnectionFactory jedisConnectionFactory){
        return new StringRedisTemplate(jedisConnectionFactory);
    }
}