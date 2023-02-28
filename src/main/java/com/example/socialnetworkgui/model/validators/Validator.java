package com.example.socialnetworkgui.model.validators;


import com.example.socialnetworkgui.model.exceptions.*;

public interface Validator<T> {
    void validate(T entity) throws ValidationException, UserExceptionEmptyID, UserExceptionEmptyFirstName, UserExceptionEmptyLastName, FriendshipExceptionSameUser, FriendshipExceptionNonexistentUser, FriendshipExceptionAlreadyFriends;
}