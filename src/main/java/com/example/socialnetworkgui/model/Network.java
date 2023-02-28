package com.example.socialnetworkgui.model;


import com.example.socialnetworkgui.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
    private final Repository<Long, User> users;
    private final Repository<Long, Friendship> friendships;
    private final List<List<User>> communities;
    private final Map<User, Boolean> inCommunity;
    private final Map<User, List<User>> friends;

    public Network(Repository<Long, User> users, Repository<Long, Friendship> friendships) {
        this.users = users;
        this.friendships = friendships;
        communities = new ArrayList<>();
        inCommunity = new HashMap<>();
        friends = new HashMap<>();
        addAllFriendships();
    }

    private void addAllFriendships() {
        for (Friendship friendship : friendships.findAll()) {
            var user1 = users.findOne(friendship.getSender());
            var user2 = users.findOne(friendship.getReceiver());
            addUser(user1, user2);
            addUser(user2, user1);
            inCommunity.putIfAbsent(user1, false);
            inCommunity.putIfAbsent(user2, false);
        }
    }

    private void addUser(User user1, User user2) {
        friends.computeIfAbsent(user1, k -> new ArrayList<>());
        friends.get(user1).add(user2);
    }

    public Integer communitiesNumber() {
        for (User user : friends.keySet()) {
            ArrayList<User> communityFriends = new ArrayList<>();
            if (!inCommunity.get(user)) {
                getCommunity(user, communityFriends);
                communities.add(communityFriends);
            }
        }
        return communities.size();
    }

    private void getCommunity(User user, ArrayList<User> communityFriends) {
        inCommunity.put(user, true);
        communityFriends.add(user);
        for (User newUser : friends.get(user)) {
            if (!inCommunity.get(newUser)) {
                getCommunity(newUser, communityFriends);
            }
        }
    }
}