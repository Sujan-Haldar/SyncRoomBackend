package org.discord.backend.repository;

import org.bson.types.ObjectId;
import org.discord.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    @Query(value = "{'email': ?0}")
    Optional<User> findUsersByEmail(String email);
    @Query(value = "{'userId': ?0}")
    Optional<User> findUsersByUserId(String userId);
    @Query("{'id': ?0}")
    @Update("{'$push': {'members': ?1}}")
    void addMemberToUser(String userId,ObjectId memberId);
    @Query("{'id': ?0}")
    @Update("{'$pull': {'members.id': ?1}}")
    void deleteMemberFromUser(String userId,String memberId);

    @Query("{'id': ?0}")
    @Update("{'$push': {'channels': ?1}}")
    void addChannelTOUser(String userId,ObjectId memberId);
    @Query("{'id': ?0}")
    @Update("{'$pull': {'channels.id': ?1}}")
    void deleteChannelFromUser(String userId,String channelId);
    @Query("{'id': ?0}")
    @Update("{'$push': {'servers': ?1}}")
    void addServerToUser(String userId,ObjectId serverId);
    @Query("{'id': ?0}")
    @Update("{'$pull': {'servers.id': ?1}}")
    void deleteServerFromUser(String userId,String serverId);

    @Query(value = "{}", fields = "{'members': 1}")
    @Update("{'$pull': {'members': {'$in': ?0}}}")
    void deleteMembersFromUser(ObjectId[] membersId);
    @Query(value = "{}", fields = "{'channels': 1}")
    @Update("{'$pull': {'channels':  {'$in': ?0}}}")
    void deleteChannelsFromUser(ObjectId[] channelsId);


}
