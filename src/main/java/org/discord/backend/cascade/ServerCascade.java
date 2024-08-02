package org.discord.backend.cascade;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.discord.backend.entity.Channel;
import org.discord.backend.entity.Member;
import org.discord.backend.entity.Server;
import org.discord.backend.repository.ChannelRepository;
import org.discord.backend.repository.MemberRepository;
import org.discord.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ServerCascade {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepository;
    public void onAfterSaveServer(Server server){
        userRepository.addServerToUser(server.getUser().getId(),new ObjectId(server.getId()));
    }
    public void onAfterDeleteServer(Server server){
        String[] membersId = server.getMembers().stream().map(Member::getId).toArray(String[]::new);
        String[] channelsId = server.getChannels().stream().map(Channel::getId).toArray(String[]::new);
        userRepository.deleteServerFromUser(server.getUser().getId(),server.getId());
        userRepository.deleteMembersFromUser(Arrays.stream(membersId).map(ObjectId::new).toArray(ObjectId[]::new));
        userRepository.deleteChannelsFromUser(Arrays.stream(channelsId).map(ObjectId::new).toArray(ObjectId[]::new));
        memberRepository.deleteMembersByServer(server);
        channelRepository.deleteChannelsByServer(server);
    }
}
