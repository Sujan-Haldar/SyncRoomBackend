package org.discord.backend.repository;

import org.bson.types.ObjectId;
import org.discord.backend.entity.Channel;
import org.discord.backend.entity.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository  extends MongoRepository<Message,String> {

    Optional<List<Message>> findByChannelAndIdLessThanOrderByCreatedAtDesc(Channel channel, String id, PageRequest pageAble);
    Optional<List<Message>> findByChannelOrderByCreatedAtDesc(Channel channel,PageRequest pageAble);
}
