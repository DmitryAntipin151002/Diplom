package com.example.UserService.service;

import com.example.UserService.repository.FriendRepository;
import com.example.UserService.repository.UsersRepository;
import dto.FriendDTO;
import model.Friend;
import model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Friend addFriend(Users user, FriendDTO friendDTO) {
        Users friend = usersRepository.findById(friendDTO.getFriendId()).orElseThrow(() -> new RuntimeException("Friend not found"));
        Friend friendRelation = new Friend();
        friendRelation.setUser(user);
        friendRelation.setFriend(friend);
        return friendRepository.save(friendRelation);
    }

    public List<Friend> getFriends(Users user) {
        return friendRepository.findByUser(user);
    }
}