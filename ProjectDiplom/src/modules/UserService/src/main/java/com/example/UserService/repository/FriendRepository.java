package com.example.UserService.repository;

import model.Friend;
import model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUser(Users user);
    List<Friend> findByFriend(Users friend);
}