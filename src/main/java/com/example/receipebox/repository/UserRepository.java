package com.example.receipebox.repository;

import com.example.receipebox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.email= :username")
    Optional<User> findUserByUsername(String username);


}
