package com.example.socialnetworkgui.model.validators;

import com.example.socialnetworkgui.model.Friendship;
import com.example.socialnetworkgui.model.User;
import com.example.socialnetworkgui.model.exceptions.*;
import com.example.socialnetworkgui.repository.Repository;

public class FriendshipValidator implements Validator<Friendship> {
    private Repository<Long, User> users;
    private Repository<Long, Friendship> friendships;

    @Override
    public void validate(Friendship entity) throws FriendshipExceptionSameUser, FriendshipExceptionNonexistentUser, FriendshipExceptionAlreadyFriends {
        if (entity.getSender().equals(entity.getReceiver())) {
            throw new FriendshipExceptionSameUser("Same user IDs.\n");
        }
    }

}