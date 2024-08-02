package org.discord.backend.cascade;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.discord.backend.entity.Member;
import org.discord.backend.repository.ServerRepository;
import org.discord.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberCascade {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    public void onAfterSaveMember(Member member){
        serverRepository.addMemberToServer(member.getServer().getId(), new ObjectId(member.getId()));
        userRepository.addMemberToUser(member.getUser().getId(),new ObjectId(member.getId()));
    }
    public void onAfterDeleteMember(Member member){
        serverRepository.deleteMemberFromServer(member.getServer().getId(),member.getId());
        userRepository.deleteMemberFromUser(member.getUser().getId(),member.getId());
    }
}
