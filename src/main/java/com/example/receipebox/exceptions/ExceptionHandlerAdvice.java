package com.example.receipebox.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<String> user(UserAlreadyRegisteredException e){
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(RecipeAlreadyCreatedException.class)
    public ResponseEntity<String> recipe(RecipeAlreadyCreatedException e){
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<String> recipeNotFound(RecipeNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(ActionNotAllowedException.class)
    public ResponseEntity<String> action(ActionNotAllowedException e){
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(IngredientAlreadyCreatedException.class)
    public ResponseEntity<String> addIngredient(IngredientAlreadyCreatedException e){
        return new ResponseEntity<>(e.getMessage(), FORBIDDEN);
    }
}
