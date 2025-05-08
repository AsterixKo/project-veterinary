package com.example.app.exceptions;

public class UserExistsInDatabaseException extends Exception {
    public UserExistsInDatabaseException(String message) {
        super(message);
    }
}
