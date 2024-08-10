package org.discord.backend.repository;

import org.discord.backend.entity.Channel;
import org.discord.backend.entity.Conversation;
import org.discord.backend.entity.DirectMessage;
import org.discord.backend.entity.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectMessageRepository extends MongoRepository<DirectMessage,String> {
    Optional<List<DirectMessage>> findByConversationAndIdLessThanOrderByCreatedAtDesc(Conversation conversation,String id,PageRequest pageAble);
    Optional<List<DirectMessage>> findByConversationOrderByCreatedAtDesc(Conversation conversation,PageRequest pageAble);
}
