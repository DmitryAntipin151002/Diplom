package com.example.UserService.controller;

import com.example.UserService.service.FriendService;
import dto.FriendDTO;
import model.Friend;
import model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @PostMapping
    public ResponseEntity<Friend> addFriend(@RequestBody FriendDTO friendDTO, @RequestAttribute Users user) {
        Friend friend = friendService.addFriend(user, friendDTO);
        return ResponseEntity.ok(friend);
    }

    @GetMapping
    public ResponseEntity<List<Friend>> getFriends(@RequestAttribute Users user) {
        List<Friend> friends = friendService.getFriends(user);
        return ResponseEntity.ok(friends);
    }
}