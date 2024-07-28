package org.discord.backend.repository;

import org.bson.types.ObjectId;
import org.discord.backend.entity.Member;
import org.discord.backend.entity.Server;
import org.discord.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
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

}
