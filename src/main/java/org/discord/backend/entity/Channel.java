package org.discord.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.discord.backend.util.ChannelType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("channel")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Channel {
    @Id
    private String id;
    private String name;
    @Builder.Default
    private ChannelType type = ChannelType.TEXT;
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

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}