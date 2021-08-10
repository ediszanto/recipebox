package com.example.receipebox.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException{

    public UserAlreadyRegisteredException() {
        super("This email is already registered!");
    }
}
