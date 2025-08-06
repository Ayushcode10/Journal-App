package com.yush.journalApp.controller;

import com.yush.journalApp.entity.JournalEntry;
import com.yush.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal") //sets the base path -> /journal

//class which handles all http requests

public class JournalEntryController {

    //📌 @Autowired tells Spring:
    //“Please inject an object of JournalEntryService here for me.”
    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping() //localhost:8080/journal --> GET
    public ResponseEntity<?> getAll() {
        List<JournalEntry> all = journalEntryService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myId}")
    //getting
    //@PathVariable tells spring to extract {myId} from the URL and give
    //it to your method
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping  //localhost:8080/journal --> POST
    //putting
    //creates new journal entry
    //@RequestBody tells spring that
    //"take this JSON body from request and convert it into a JournalEntry object"
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry MyEntry){
        try{
            journalEntryService.saveEntry(MyEntry);
            return new ResponseEntity<>(MyEntry,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(MyEntry,HttpStatus.BAD_REQUEST);
        }
    }

    //handles delete request
    @DeleteMapping("id/{myId}") //delete
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId){
        try{
            journalEntryService.DeleteById(myId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //handles update request
    @PutMapping("id/{id}") //update
    public ResponseEntity<?> UpdateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry){
        JournalEntry oldEntry = journalEntryService.findById(id).orElse(null);
        if(oldEntry != null){
            oldEntry.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")?newEntry.getTitle():oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals("")?newEntry.getContent():oldEntry.getContent());
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
