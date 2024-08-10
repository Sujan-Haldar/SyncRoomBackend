package org.discord.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.backend.entity.User;
import org.discord.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }
    public List<User> getUsersByUserId(String userId){
        Optional<User> users =userRepository.findUsersByUserId(userId);
        return users.stream().toList();
    }
    public List<User> getUsersByEmail(String email){
        Optional<User> users =userRepository.findUsersByEmail(email);
        return users.stream().toList();
    }
}
