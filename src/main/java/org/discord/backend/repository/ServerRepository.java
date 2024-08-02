package org.discord.backend.repository;

import org.bson.types.ObjectId;
import org.discord.backend.entity.Member;
import org.discord.backend.entity.Server;
import org.discord.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerRepository extends MongoRepository<Server,String> {
//    Optional<Server> findFirstByIdAndMembers(String id, Member member);
    Optional<Server> findFirstByMembersContaining(List<Member> members);
    List<Server> findAllByMembersContaining(List<Member> members);
    Optional<Server> findServerByIdAndMembersContains(String id,Member member);
    Optional<Server> findServerByIdAndUser(String id, User user);
    Optional<Server> findFirstByInviteCode(String inviteCode);
    @Query("{'id': ?0}")
    @Update("{'$push': {'members': ?1}}")
    void addMemberToServer(String serverId,ObjectId memberId);
    @Query("{'id': ?0}")
    @Update("{'$pull': {'members.id': ?1}}")
    void deleteMemberFromServer(String serverId,String memberId);
    @Query("{'id': ?0}")
    @Update("{'$push': {'channels': ?1}}")
    void addChannelToServer(String serverId,ObjectId channelId);
    @Query("{'id': ?0}")
    @Update("{'$pull': {'channels.id': ?1}}")
    void deleteChannelFromServer(String serverId,String channelId);
}
