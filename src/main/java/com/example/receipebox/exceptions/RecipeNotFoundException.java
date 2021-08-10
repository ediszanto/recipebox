package com.example.receipebox.exceptions;

public class RecipeNotFoundException extends RuntimeException{

    public RecipeNotFoundException() {
        super("Receipe not found!!");
    }
}
