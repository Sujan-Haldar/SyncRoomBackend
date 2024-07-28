package org.discord.backend.repository;

import org.discord.backend.entity.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends MongoRepository<Channel,String> {
}
