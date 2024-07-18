package org.discord.backend.service;

import org.discord.backend.entity.Server;
import org.discord.backend.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServerService {
    @Autowired
    private ServerRepository serverRepository;

    public Optional<Server> findFirstServerByMemberProfileId(String profileId) {
        return serverRepository.findFirstByMembersUserId(profileId);
    }
}
