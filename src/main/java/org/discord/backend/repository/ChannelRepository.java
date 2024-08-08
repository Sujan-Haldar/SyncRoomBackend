package org.discord.backend.repository;

import org.bson.types.ObjectId;
import org.discord.backend.entity.Channel;
import org.discord.backend.entity.Server;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends MongoRepository<Channel,String> {
    void deleteChannelsByServer(Server server);
    Optional<Channel> findFirstByIdAndServer(String id,Server server);

    @Query("{'id': ?0}")
    @Update("{'$push': {'messages': ?1}}")
    void addMessageToChannel(String channelId, ObjectId messageId);
}
