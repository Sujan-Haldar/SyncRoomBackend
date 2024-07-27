package org.discord.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    private String id;
    private  String userId;
    private  String name;
    private String email;
    private String password;
    private  String imageUrl;
    @DocumentReference(collection = "server",lazy = true)
    private List<Server> servers = new ArrayList<>();
    @DocumentReference(collection = "member",lazy = true)
    private List<Member> members = new ArrayList<>();
    @DocumentReference(collection = "channel",lazy = true)
    private List<Channel> channels = new ArrayList<>();


    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
