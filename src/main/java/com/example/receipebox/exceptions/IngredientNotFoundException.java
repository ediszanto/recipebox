package com.example.receipebox.exceptions;

public class IngredientNotFoundException extends RuntimeException{

    public IngredientNotFoundException() {
        super("Ingredient not found !");
    }
}
