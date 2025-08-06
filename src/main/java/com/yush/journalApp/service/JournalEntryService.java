package com.yush.journalApp.service;

import com.yush.journalApp.entity.JournalEntry;
import com.yush.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo journalEntryRepo;

    public void saveEntry(JournalEntry journalEntry){
        try{
            journalEntry.setDate(LocalDateTime.now());
            journalEntryRepo.save(journalEntry);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    //it returns an optional, like if it doesn't find it by the id
    //then it'll return null
    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    public void DeleteById(ObjectId id){
        journalEntryRepo.deleteById(id);
    }
}
