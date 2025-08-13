package com.yush.journalApp.controller;

import com.yush.journalApp.entity.JournalEntry;
import com.yush.journalApp.entity.User;
import com.yush.journalApp.service.JournalEntryService;
import com.yush.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

//defines that this class will handle HTTP requests
@RestController  //(@Controller + @ResponseBody)
@RequestMapping("/journal") //sets the base path -> /journal
//class which handles all http requests

public class JournalEntryController {

    //📌 @Autowired tells Spring:
    //“Please inject an object of JournalEntryService here for me.”
    @Autowired  //dependency Injection
    private JournalEntryService journalEntryService;

    @Autowired  //dependency injection
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName); //calls user-service to fetch the user by username
        List<JournalEntry> all = user.getJournalEntries(); //creates a list of journalEntries of a user
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping()  //localhost:8080/journal --> POST
    //putting
    //creates new journal entry
    //@RequestBody tells spring that
    //"take this JSON body from request and convert it into a JournalEntry object"
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry MyEntry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(MyEntry, userName);
            return new ResponseEntity<>(MyEntry,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(MyEntry,HttpStatus.BAD_REQUEST);
        }
    }

    //handles delete request
    @DeleteMapping("id/{myId}") //delete
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId){

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            boolean removed = journalEntryService.DeleteById(myId, userName);
            if(removed){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


//    handles update request
    @PutMapping("id/{MyId}") //update
    public ResponseEntity<?> UpdateEntryById(@PathVariable ObjectId MyId, @RequestBody JournalEntry newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(MyId)).toList();

        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(MyId);
            if (journalEntry.isPresent()){
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")?newEntry.getTitle():oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals("")?newEntry.getContent():oldEntry.getContent());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
