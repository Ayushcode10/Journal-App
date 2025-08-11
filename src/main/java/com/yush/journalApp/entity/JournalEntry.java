package com.yush.journalApp.entity;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


//tells spring data Mongodb to store the instances of this class
//in "journal_entries" collection.
//this merely sets the name of the collection if not present
//spring infers collection name as the class name
@Document(collection = "journal_entries")
//lombok auto-generates the getters and setters(standard methods)
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id //marks this field as mongodb _id
    private ObjectId id;
    @Nonnull //title must not be null
    private String title;
    private String content;
    private LocalDateTime date;
}
