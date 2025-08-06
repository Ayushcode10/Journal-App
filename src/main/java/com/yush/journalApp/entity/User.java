package com.yush.journalApp.entity;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class User{
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @Nonnull
    private String userName;
    @Nonnull
    private String password;
    private LocalDateTime date;
    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();
}