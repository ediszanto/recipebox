package com.example.receipebox.exceptions;

public class IngredientAlreadyCreatedException extends RuntimeException{
    public IngredientAlreadyCreatedException() {
        super("Ingredient already added to the store");
    }
}
