package com.example.app.exceptions;

public class UserExistsInDatabase extends Exception {
    public UserExistsInDatabase(String message) {
        super(message);
    }
}
