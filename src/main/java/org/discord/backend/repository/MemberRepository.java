package org.discord.backend.repository;

import org.bson.types.ObjectId;
import org.discord.backend.entity.Member;
import org.discord.backend.entity.Server;
import org.discord.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member,String> {
    Optional<Member> findFirstByUserAndServer(User user, Server server);
    void deleteMemberByUserAndServer(User user,Server server);

    void deleteMembersByServer(Server server);

    @Query("{'id': ?0}")
    @Update("{'$push': {'conversationsInitiated': ?1}}")
    void addConversationsInitiatorToMember(String memberOneId, ObjectId conversationId);

    @Query("{'id': ?0}")
    @Update("{'$push': {'conversationsReceived': ?1}}")
    void addConversationsReceiverToMember(String memberTwoId, ObjectId conversationId);

    @Query("{'id': ?0}")
    @Update("{'$push': {'messages': ?1}}")
    void addMessageToMember(String memberId,ObjectId messageId);
    @Query("{'id': ?0}")
    @Update("{'$push': {'directMessages': ?1}}")
    void addDirectMessageToMember(String memberId,ObjectId directMessageId);
}
