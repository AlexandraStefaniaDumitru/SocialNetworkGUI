package com.example.socialnetworkgui.model;

import java.time.LocalDate;

public class Friendship extends Entity<Long> {
    private final Long sender;
    private final Long receiver;
    private final LocalDate friendsFrom;

    private final FriendshipStatus friendshipStatus;

    public Friendship(Long sender, Long receiver, LocalDate friendsFrom, FriendshipStatus friendshipStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.friendsFrom = friendsFrom;
        this.friendshipStatus = friendshipStatus;
    }

    public Long getSender() {
        return sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public LocalDate getFriendsFrom() {
        return friendsFrom;
    }

    public FriendshipStatus getFriendshipStatus() {
        return friendshipStatus;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", friendsFrom=" + friendsFrom +
                ", friendshipStatus=" + friendshipStatus +
                '}';
    }
}