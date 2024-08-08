package org.discord.backend.entity;

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

@Document("message")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Message {
    @Id
    private String id;
    private String content;
    private String fileUrl;
    @Builder.Default
    private boolean deleted = false;
    @Builder.Default
    private boolean updated = false;

    @DocumentReference(lazy = true,collection = "member")
    private Member member;
    @DocumentReference(lazy = true,collection = "channel")
    private Channel channel;


    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
