package com.yush.journalApp.controller;

import com.yush.journalApp.entity.User;
import com.yush.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;
    @PostMapping("/create-user")
    public void addUser(@RequestBody User user){
        userService.saveEntry(user);
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }
}
