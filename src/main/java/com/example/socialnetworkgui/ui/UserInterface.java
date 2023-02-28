package com.example.socialnetworkgui.ui;


import com.example.socialnetworkgui.model.Friendship;
import com.example.socialnetworkgui.model.User;
import com.example.socialnetworkgui.model.exceptions.*;
import com.example.socialnetworkgui.repository.validators.RepositoryException;
import com.example.socialnetworkgui.service.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserInterface {
    private final Service service;

    public UserInterface(Service service) {
        this.service = service;
    }

    public void startApplication() {
        Scanner scanner = new Scanner(System.in);
        int running = 1;
        while (running == 1) {
            printMenu();
            System.out.println("My command: ");
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    addUser(scanner);
                    break;
                case 2 :
                    removeUser(scanner);
                    break;
                case 3 :
                    updateUser(scanner);
                    break;
                case 4 :
                    addFriend(scanner);
                    break;
                case 5 :
                    removeFriend(scanner);
                    break;
                case 6 :
                    updateFriendship(scanner);
                    break;
                case 7 :
                    showAllUsers();
                    break;
                case 8 :
                    showAllFriendships();
                    break;
                case 9 :
                    communitiesNumber();
                    break;
                case 10 :
                    showAllFriends(scanner);
                    break;
                case 0 :
                    running = 0;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n\nSOCIAL NETWORK");
        System.out.println("1. add user");
        System.out.println("2. remove user");
        System.out.println("3. update user");
        System.out.println("4. add friendship");
        System.out.println("5. remove friendship");
        System.out.println("6. update friendship");
        System.out.println("7. get all users");
        System.out.println("8. get all friendships");
        System.out.println("9. communities number");
        System.out.println("10. show all friends");
    }

    private void addUser(Scanner scanner) {
        scanner.nextLine();
        System.out.println("User ID:");
        Long ID = scanner.nextLong();
        scanner.nextLine();
        System.out.println("User first name:");
        String firstName = scanner.nextLine();
        System.out.println("User last name:");
        String lastName = scanner.nextLine();
        try {
            service.addUser(ID, firstName, lastName);
        } catch (UserExceptionEmptyID | UserExceptionEmptyFirstName | UserExceptionEmptyLastName |
                 RepositoryException error) {
            System.out.println(error.getMessage());
        } catch (FriendshipExceptionAlreadyFriends | FriendshipExceptionNonexistentUser |
                 FriendshipExceptionSameUser e) {
            throw new RuntimeException(e);
        }
    }

    private void removeUser(Scanner scanner) {
        scanner.nextLine();
        System.out.println("User ID: ");
        Long ID = scanner.nextLong();
        try {
            service.removeUser(ID);
        } catch (RepositoryException error) {
            System.out.println(error.getMessage());
        } catch (FriendshipExceptionAlreadyFriends | FriendshipExceptionNonexistentUser | UserExceptionEmptyID |
                 FriendshipExceptionSameUser | UserExceptionEmptyFirstName | UserExceptionEmptyLastName e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUser(Scanner scanner) {
        scanner.nextLine();
        System.out.println("User ID: ");
        Long ID = scanner.nextLong();
        scanner.nextLine();
        System.out.println("New user first name: ");
        String newFirstName = scanner.nextLine();
        System.out.println("New user last name: ");
        String newLastName = scanner.nextLine();
        try {
            service.updateUser(ID, newFirstName, newLastName);
        } catch (RepositoryException | FriendshipExceptionAlreadyFriends | FriendshipExceptionNonexistentUser |
                 UserExceptionEmptyID | FriendshipExceptionSameUser | UserExceptionEmptyFirstName |
                 UserExceptionEmptyLastName error) {
            System.out.println(error.getMessage());
        }
    }

    private void showAllUsers() {
        for (User user : service.findAllUsers()) {
            System.out.println(user);
        }
    }

    private void showAllFriends(Scanner scanner) {
        System.out.println("User ID:");
        Long ID = scanner.nextLong();
        scanner.nextLine();
        for (Friendship friendship : service.findAllFriendships()) {
            if (friendship.getSender().equals(ID)) {
                for (User friend : service.findAllUsers()) {
                    if (friend.getId().equals(friendship.getReceiver())) {
                        System.out.println(friend + " - " + friendship.getFriendshipStatus());
                    }
                }
            } else {
                if (friendship.getReceiver().equals(ID)) {
                    for (User friend : service.findAllUsers()) {
                        if (friend.getId().equals(friendship.getSender())) {
                            System.out.println(friend + " - " + friendship.getFriendshipStatus());
                        }
                    }
                }
            }
        }

    }


    private void addFriend(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Friendship ID: ");
        Long ID = scanner.nextLong();
        System.out.println("1st user ID: ");
        Long sender = scanner.nextLong();
        System.out.println("2nd user ID: ");
        Long receiver = scanner.nextLong();
        LocalDate friendsFrom = LocalDate.now();
        try {
            service.addFriendship(ID, sender, receiver, friendsFrom);
        } catch (FriendshipExceptionSameUser | FriendshipExceptionNonexistentUser | FriendshipExceptionAlreadyFriends |
                 RepositoryException error) {
            System.out.println(error.getMessage());
        } catch (UserExceptionEmptyID | UserExceptionEmptyFirstName | UserExceptionEmptyLastName e) {
            throw new RuntimeException(e);
        }
    }

    private void removeFriend(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Friendship ID: ");
        Long ID = scanner.nextLong();
        try {
            service.removeFriendship(ID);
        } catch (RepositoryException error) {
            System.out.println(error.getMessage());
        }
    }

    private void updateFriendship(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Friendship ID: ");
        Long ID = scanner.nextLong();
        scanner.nextLine();
        System.out.println("New friends from [YYYY-MM-DD]: ");
        String date = scanner.nextLine();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newFriendsFrom = LocalDate.parse(date, dateTimeFormatter);
        try {
            service.updateFriendship(ID, newFriendsFrom);
        } catch (RepositoryException | FriendshipExceptionAlreadyFriends | FriendshipExceptionNonexistentUser |
                 UserExceptionEmptyID | FriendshipExceptionSameUser | UserExceptionEmptyFirstName |
                 UserExceptionEmptyLastName error) {
            System.out.println(error.getMessage());
        }
    }

    private void showAllFriendships() {
        for (Friendship friendship : service.findAllFriendships()) {
            System.out.println(service.getFriendship(friendship));
        }
    }

    private void communitiesNumber() {
        System.out.println(service.getCommunitiesNumber());
    }

}