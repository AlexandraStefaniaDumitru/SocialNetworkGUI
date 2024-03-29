package com.example.socialnetworkgui.repository.file;


import com.example.socialnetworkgui.model.User;
import com.example.socialnetworkgui.model.exceptions.*;
import com.example.socialnetworkgui.model.validators.Validator;

import java.util.List;

public class UserFileRepository extends AbstractFileRepository<Long, User> {

    public UserFileRepository(String fileName, Validator<User> validator) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        super(fileName, validator);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }
}