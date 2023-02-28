package com.example.socialnetworkgui;

import com.example.socialnetworkgui.model.Friendship;
import com.example.socialnetworkgui.model.Message;
import com.example.socialnetworkgui.model.User;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.database.FriendshipDBRepository;
import com.example.socialnetworkgui.repository.database.MessageDBRepository;
import com.example.socialnetworkgui.repository.database.UserDBRepository;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        Repository<Long, User> userRepository = new UserDBRepository();
        Repository<Long, Friendship> friendshipRepository = new FriendshipDBRepository();
        Repository<Long, Message> messageRepository = new MessageDBRepository();
        Service service = new Service(userRepository, friendshipRepository, messageRepository);
        UserInterface userInterface = new UserInterface(service);
        userInterface.startApplication();

        //md5 - pentru hashing parole
    }
}