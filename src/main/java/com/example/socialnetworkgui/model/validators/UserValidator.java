package com.example.socialnetworkgui.model.validators;

import com.example.socialnetworkgui.model.User;
import com.example.socialnetworkgui.model.exceptions.*;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws UserExceptionEmptyID, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        if (entity.getId() == null) {
            throw new UserExceptionEmptyID("ID error!\n");
        }
        if (entity.getFirstName() == null || "".equals(entity.getFirstName())) {
            throw new UserExceptionEmptyFirstName("FirstName error\n");
        }
        if (entity.getLastName() == null || "".equals(entity.getLastName())) {
            throw new UserExceptionEmptyLastName("LastName error\n");
        }
    }
}