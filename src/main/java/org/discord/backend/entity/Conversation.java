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

@Document("conversation")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Conversation {
    @Id
    private String id;

    @DocumentReference(lazy = true,collection = "member")
    private Member memberOne;
    @DocumentReference(lazy = true,collection = "member")
    private Member memberTwo;

    @DocumentReference(lazy = true,collection = "direct_message")
    @Builder.Default
    @JsonIgnore
    private List<DirectMessage> directMessages = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
