package com.example.receipebox.exceptions;

public class NotEnoughQuantityException extends RuntimeException{

    public NotEnoughQuantityException() {
        super("There is not enough ingredient quantity in store. This recipe cannot be cooked!");
    }
}
