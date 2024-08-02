package org.discord.backend.cascade;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.discord.backend.entity.Channel;
import org.discord.backend.repository.ServerRepository;
import org.discord.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelCascade {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    public void onAfterSaveChannel(Channel channel){
        userRepository.addChannelTOUser(channel.getUser().getId(),new ObjectId(channel.getId()));
        serverRepository.addChannelToServer(channel.getServer().getId(),new ObjectId(channel.getId()));
    }
    public void onAfterDeleteChannel(Channel channel){
        userRepository.deleteChannelFromUser(channel.getUser().getId(),channel.getId());
        serverRepository.deleteChannelFromServer(channel.getServer().getId(),channel.getId());
    }


}
