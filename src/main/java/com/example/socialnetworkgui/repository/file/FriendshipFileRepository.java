package com.example.socialnetworkgui.repository.file;

import com.example.socialnetworkgui.model.Friendship;
import com.example.socialnetworkgui.model.FriendshipStatus;
import com.example.socialnetworkgui.model.exceptions.*;
import com.example.socialnetworkgui.model.validators.Validator;

import java.time.LocalDate;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Long, Friendship> {

    public FriendshipFileRepository(String fileName, Validator<Friendship> validator) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Long sender = Long.parseLong(attributes.get(1));
        Long receiver = Long.parseLong(attributes.get(2));
        LocalDate friendsFrom = LocalDate.parse(attributes.get(3));
        FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(attributes.get(4));
        Friendship friendship = new Friendship(sender, receiver, friendsFrom, friendshipStatus);
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId() + ";" + entity.getSender() + ";" + entity.getReceiver() + ";" + entity.getFriendsFrom() + ";" + entity.getFriendshipStatus();
    }
}