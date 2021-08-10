package com.example.receipebox.controller;


import com.example.receipebox.entity.User;
import com.example.receipebox.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> add(@RequestBody User user){
        User savedUser = userService.add(user);
        log.info("User: " + savedUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
