package com.h2rd.refactoring.constants;

public final class ErrorMessages {
    
    private ErrorMessages(){
    }
    
    public static final String USER_NOT_FOUND = "The requested user cannot be found";
    public static final String USER_ALREADY_EXISTS = "A user with this email address already exists";
    public static final String CANNOT_CHANGE_EMAIL_OF_USER = "You cannot change the email address of the user";
    public static final String USER_NAME_EMPTY = "The user's name cannot be empty";
    public static final String USER_HAS_NO_ROLES = "The user must have at least 1 role";
    
    public static final String REQUEST_BODY_MALFORMED = "The request body is malformed";
    
}
