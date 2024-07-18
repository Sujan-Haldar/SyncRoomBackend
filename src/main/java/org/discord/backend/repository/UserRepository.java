package org.discord.backend.repository;

import org.discord.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    @Query(value = "{'email': ?0}")
    Optional<User> findUsersByEmail(String email);
    @Query(value = "{'userId': ?0}")
    Optional<User> findUsersByUserId(String userId);
}
