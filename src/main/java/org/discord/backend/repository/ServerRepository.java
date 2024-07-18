package org.discord.backend.repository;

import org.discord.backend.entity.Server;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServerRepository extends MongoRepository<Server,String> {
    @Query(value = "{'members.user.id': ?0}")
    Optional<Server> findFirstByMembersUserId(String userId);
}
