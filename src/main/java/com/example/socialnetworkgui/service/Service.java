package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.model.*;
import com.example.socialnetworkgui.model.exceptions.*;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.validators.RepositoryException;
import com.example.socialnetworkgui.utils.events.ChangeEventType;
import com.example.socialnetworkgui.utils.events.FriendshipChangeEvent;
import com.example.socialnetworkgui.utils.events.MessageChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Service {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;

    private final Repository<Long, Message> messageRepository;

    private final List<Observer<FriendshipChangeEvent>> friendshipObserver = new ArrayList<>();
    private final List<Observer<MessageChangeEvent>> messageObserver = new ArrayList<>();

    public Service(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository, Repository<Long, Message> messageRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
    }

    public void addUser(Long ID, String firstName, String lastName) throws UserExceptionEmptyID, UserExceptionEmptyFirstName, UserExceptionEmptyLastName, FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, FriendshipExceptionSameUser, RepositoryException {
        User user = new User(firstName, lastName);
        user.setId(ID);
        user = userRepository.save(user);
        if (user != null) {
            throw new RepositoryException("User already exists.\n");
        }
    }

    public void removeUser(Long ID) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        Long user = userRepository.remove(ID);
        if (user != null) {
            removeFriend(ID);
        } else {
            throw new RepositoryException("User doesn't exist.\n");
        }
    }

    public void updateUser(Long ID, String newFirstName, String newLastName) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        User oldUser = userRepository.findOne(ID);
        if (oldUser != null) {
            User newUser = new User(newFirstName, newLastName);
            userRepository.update(userRepository.findOne(ID), newUser);
        } else {
            throw new RepositoryException("User doesn't exist.\n");
        }

    }

    private void removeFriend(Long ID) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        List<Long> IDs = new ArrayList<>();
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        for (Friendship f : friendships) {
            if (Objects.equals(f.getSender(), ID) || Objects.equals(f.getReceiver(), ID)) {
                IDs.add(f.getId());
            }
        }
        for (Long id : IDs) {
            removeFriendship(id);
        }
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public void addFriendship(Long ID, Long sender, Long receiver, LocalDate friendsFrom) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionSameUser, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        if (userRepository.findOne(sender) == null || userRepository.findOne(receiver) == null) {
            throw new FriendshipExceptionNonexistentUser("One user doesn't exist.\n");
        }
        if (sender.equals(receiver)){
            throw new FriendshipExceptionSameUser("Can't be friend with yourself! :)\n");
        }
        int friends = 0;
        for (Friendship f : friendshipRepository.findAll()) {
            if (((sender.equals(f.getSender()) && receiver.equals(f.getReceiver())) || (receiver.equals(f.getSender()) && sender.equals(f.getReceiver()))) && f.getFriendshipStatus() == FriendshipStatus.accepted) {
                throw new FriendshipExceptionAlreadyFriends("Users are already friends.\n");
            } else if (sender.equals(f.getSender()) && receiver.equals(f.getReceiver()) && f.getFriendshipStatus() == FriendshipStatus.pending) {
                throw new FriendshipExceptionAlreadyFriends("Friendship request already send.\n");
            } else if (sender.equals(f.getReceiver()) && receiver.equals(f.getSender()) && f.getFriendshipStatus() == FriendshipStatus.pending) {
                Friendship newFriendship = new Friendship(f.getSender(), f.getReceiver(), f.getFriendsFrom(), FriendshipStatus.accepted);
                friendshipRepository.update(f, newFriendship);
                friends = 1;
            } else if ((sender.equals(f.getReceiver()) && receiver.equals(f.getSender())) || (sender.equals(f.getSender()) && receiver.equals(f.getReceiver())) && f.getFriendshipStatus() == FriendshipStatus.rejected) {
                friendshipRepository.remove(f.getId());
            }
        }
        Friendship friendship = null;
        if (friends == 0) {
            friendship = new Friendship(sender, receiver, friendsFrom, FriendshipStatus.pending);
            friendship.setId(ID);
            friendship = friendshipRepository.save(friendship);
            if (friendship != null) {
                throw new RepositoryException("Friendship already exists.\n");
            }
        }
        this.notifyObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE, friendship), null);
    }

    public void removeFriendship(Long ID) {
        Friendship friendship = friendshipRepository.findOne(ID);
        if (friendship == null) {
            throw new RepositoryException("Friendship doesn't exist.\n");
        }
        friendshipRepository.remove(ID);
//        Friendship newFriendship = new Friendship(friendship.getSender(), friendship.getReceiver(), friendship.getFriendsFrom(), FriendshipStatus.rejected);
//        friendshipRepository.update(friendship, newFriendship);
        this.notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD, friendship), null);
    }

    public void updateFriendship(Long ID, LocalDate newFriendsFrom) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        Friendship oldFriendship = friendshipRepository.findOne(ID);
        if (oldFriendship != null) {
            Friendship newFriendship = new Friendship(oldFriendship.getSender(), oldFriendship.getReceiver(), newFriendsFrom, oldFriendship.getFriendshipStatus());
            friendshipRepository.update(friendshipRepository.findOne(ID), newFriendship);
        } else {
            throw new RepositoryException("Friendship doesn't exist.\n");
        }
    }

    public Iterable<Friendship> findAllFriendships() {
        return friendshipRepository.findAll();
    }

    public Iterable<User> findAllFriends(Long ID) {
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendshipRepository.findAll()) {
            if (friendship.getSender().equals(ID) && friendship.getFriendshipStatus() == FriendshipStatus.accepted) {
                for (User friend : userRepository.findAll()) {
                    if (friend.getId().equals(friendship.getReceiver())) {
                        friends.add(friend);
                    }
                }
            } else {
                if (friendship.getReceiver().equals(ID)) {
                    for (User friend : userRepository.findAll()) {
                        if (friend.getId().equals(friendship.getSender()) && friendship.getFriendshipStatus() == FriendshipStatus.accepted) {
                            friends.add(friend);
                        }
                    }
                }
            }
        }
        return friends;
    }

    public Iterable<Friendship> findAllFriendshipRequests(Long ID) {
        List<Friendship> requests = new ArrayList<>();
        for (Friendship friendship : friendshipRepository.findAll()) {
            if (friendship.getReceiver().equals(ID) && friendship.getFriendshipStatus() == FriendshipStatus.pending) {
                requests.add(friendship);
            }
        }
        return requests;
    }

    public Iterable<Friendship> findAllSentRequests(Long ID){
        List<Friendship> requests = new ArrayList<>();
        for( Friendship friendship: friendshipRepository.findAll()){
            if (friendship.getSender().equals(ID) && friendship.getFriendshipStatus() == FriendshipStatus.pending){
                requests.add(friendship);
            }
        }
        return requests;
    }
    public Friendship findFriendship(Long sender, Long receiver) {
        Friendship friendship = null;
        for (Friendship f : friendshipRepository.findAll()) {
            if (((sender.equals(f.getSender()) && receiver.equals(f.getReceiver())) || (receiver.equals(f.getSender()) && sender.equals(f.getReceiver())))) {
                return f;
            }
        }
        return friendship;
    }

    public User findUser(Long id) {
        User user = null;
        for (User u : userRepository.findAll()) {
            if (Objects.equals(u.getId(), id)) {
                return u;
            }
        }
        return user;
    }

    public Integer getCommunitiesNumber() {
        Network network = new Network(userRepository, friendshipRepository);
        return network.communitiesNumber();
    }

    public String getFriendship(Friendship friendship) {
        String f;
        f = friendship.getFriendshipStatus() + "Friendship " + friendship.getId() + ": " + userRepository.findOne(friendship.getSender()).getFirstName()
                + " " + userRepository.findOne(friendship.getSender()).getLastName() + " & "
                + userRepository.findOne(friendship.getReceiver()).getFirstName() + " "
                + userRepository.findOne(friendship.getReceiver()).getLastName() + " since " + friendship.getFriendsFrom();
        return f;
    }

    public void addMessage(Long ID, Long sender, Long receiver, String message, LocalDate dateOf, LocalTime timeOf) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        Message messageEntity = new Message(sender, receiver, message, dateOf, timeOf);
        messageEntity.setId(ID);
        messageEntity = messageRepository.save(messageEntity);
        if(messageEntity != null){
            throw new RepositoryException("Message already exists. \n");
        }
        this.notifyObservers(null, new MessageChangeEvent(ChangeEventType.UPDATE, messageEntity));
    }

    public Iterable<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    public Iterable<Message> findAllMessagesByUsers(Long ID1, Long ID2){
        List<Message> messages = new ArrayList<>();
        for( Message message: messageRepository.findAll()){
            if ((message.getSender().equals(ID1) && message.getReceiver().equals(ID2)) || (message.getSender().equals(ID2) && message.getReceiver().equals(ID1))){
                messages.add(message);
            }
        }
        messages.sort(Comparator.comparing(Message::getDate_of).thenComparing(Message::getTime_of));
        return messages;
    }
    public Iterable<Message> findAllMessagesFromUser(Long ID){
        List<Message> messages = new ArrayList<>();
        for( Message message: messageRepository.findAll()){
            if (message.getSender().equals(ID)){
                messages.add(message);
            }
        }
        messages.sort(Comparator.comparing(Message::getDate_of).thenComparing(Message::getTime_of));
        return messages;
    }

    public Iterable<Message> findAllMessagesToUser(Long ID){
        List<Message> messages = new ArrayList<>();
        for( Message message: messageRepository.findAll()){
            if (message.getReceiver().equals(ID)){
                messages.add(message);
            }
        }
        messages.sort(Comparator.comparing(Message::getDate_of).thenComparing(Message::getTime_of));
        return messages;
    }

    public void addObserver(Observer<FriendshipChangeEvent> e) {
        friendshipObserver.add(e);
    }

    public void removeObserver(Observer<FriendshipChangeEvent> e) {
        friendshipObserver.remove(e);
    }

    public void notifyObservers(FriendshipChangeEvent f, MessageChangeEvent m) {
        friendshipObserver.stream().forEach(x -> x.update(f));
        messageObserver.stream().forEach(x -> x.update(m));
    }


}