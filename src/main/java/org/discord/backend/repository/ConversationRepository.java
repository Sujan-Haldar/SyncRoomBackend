package org.discord.backend.repository;

import org.bson.types.ObjectId;
import org.discord.backend.entity.Conversation;
import org.discord.backend.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation,String> {
    Optional<Conversation> findFirstByMemberOneAndMemberTwo(Member memberOne,Member memberTwo);
    Optional<Conversation> findFirstByIdAndMemberOne_IdOrMemberTwo_Id(String id, String memberOne_id, String memberTwo_id);

    @Query("{'id': ?0}")
    @Update("{'$push': {'directMessages': ?1}}")
    void addDirectMessageToConversation(String conversationId, ObjectId directMessageId);
}
