package org.discord.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private  String host;
    @Value("${redis.port}")
    private  int port;
    @Bean
    JedisConnectionFactory getConnectionFactory(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host,port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    ChannelTopic getTopic() {
        return new ChannelTopic("testing-chat");
    }
}