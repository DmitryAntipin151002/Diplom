package com.example.UserService.repository;

import model.UserActivity;
import model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {
    UserActivity findByUser(Users user);
}
