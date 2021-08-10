package com.example.receipebox.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User Not Found!");
    }
}
