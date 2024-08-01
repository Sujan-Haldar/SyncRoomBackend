package org.discord.backend.repository;

import org.discord.backend.entity.Channel;
import org.discord.backend.entity.Server;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends MongoRepository<Channel,String> {
    void deleteChannelsByServer(Server server);
}
