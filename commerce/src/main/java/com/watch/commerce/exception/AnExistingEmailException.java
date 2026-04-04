package com.watch.commerce.exception;

public class AnExistingEmailException extends RuntimeException{


    public AnExistingEmailException(String message){
        super(message);
    }

}