package org.discord.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("server")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Server {
    @Id
    private String id;
    private String name;
    private String imageUrl;
    private  String inviteCode;
    @DocumentReference(lazy = true)
    @JsonIgnore
    private User user;
    @DocumentReference(lazy = true,collection = "member")
    private List<Member> members = new ArrayList<>();
    @DocumentReference(lazy = true,collection = "channel")
    private List<Channel> channels = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}

