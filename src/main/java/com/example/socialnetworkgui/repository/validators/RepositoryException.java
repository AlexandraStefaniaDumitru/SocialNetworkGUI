package com.example.socialnetworkgui.repository.validators;

public class RepositoryException extends RuntimeException{
    private final String errorMessage;

    public RepositoryException(String message) {
        this.errorMessage = message;
    }

    public String getMessage() {
        return errorMessage;
    }
}