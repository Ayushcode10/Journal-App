package com.yush.journalApp.entity;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//name of the collection where the instance of this class will be stored
//Map to users collection.
@Builder
@Document(collection = "users")
@Data //auto-generated getters and setters
public class User{
    @Id  //_id field
    private ObjectId id;
    @Indexed(unique = true)//creates a unique index on "userName"
    @Nonnull // should be non-null
    private String userName;
    @Nonnull //should not contain null val
    private String password;
    private LocalDateTime date;

    //this is a ref list to JournalEntry docs stored in journal_entries
    //this is an array of ObjectIds referencing to JournalEntry doc.
    //same as foreign key
    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();
    private List<String> roles;
}