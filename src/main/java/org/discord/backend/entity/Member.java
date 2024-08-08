package org.discord.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.discord.backend.util.Role;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("member")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Member {
    @Id
    private String id;
    @Builder.Default
    private Role role = Role.GUEST;
    @DocumentReference(lazy = true,collection = "user")
    @JsonIgnore
    private User user;
    @DocumentReference(lazy = true,collection = "server")
    @JsonIgnore
    private Server server;
    @DocumentReference(lazy = true,collection = "message")
    @Builder.Default
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();
    @DocumentReference(lazy = true,collection = "conversation")
    @Builder.Default
    @JsonIgnore
    private List<Conversation> conversationsInitiated = new ArrayList<>();
    @DocumentReference(lazy = true,collection = "conversation")
    @Builder.Default
    @JsonIgnore
    private List<Conversation> conversationsReceived = new ArrayList<>();
    @DocumentReference(lazy = true,collection = "direct_message")
    @Builder.Default
    @JsonIgnore
    private List<DirectMessage> directMessages = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    @Override
    public boolean equals(Object obj){
        if(obj == this) return  true;
        if(!(obj instanceof Member)) return false;
        Member member = (Member) obj;
        return member.getId().equals(this.getId());
    }

}

