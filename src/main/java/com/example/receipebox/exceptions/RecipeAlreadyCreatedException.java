package com.example.receipebox.exceptions;

public class RecipeAlreadyCreatedException extends RuntimeException{

    public RecipeAlreadyCreatedException(String message) {
        super(message);
    }
}
