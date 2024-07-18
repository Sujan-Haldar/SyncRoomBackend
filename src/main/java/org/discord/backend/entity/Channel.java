package org.discord.backend.entity;

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
    private User user;
    @DocumentReference(lazy = true,collection = "server")
    private Server server;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}