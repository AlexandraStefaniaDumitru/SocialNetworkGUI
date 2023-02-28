package com.example.socialnetworkgui;

import com.example.socialnetworkgui.model.Friendship;
import com.example.socialnetworkgui.model.FriendshipStatus;
import com.example.socialnetworkgui.model.Message;
import com.example.socialnetworkgui.model.User;
import com.example.socialnetworkgui.model.exceptions.*;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.utils.events.FriendshipChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NetworkController implements Observer<FriendshipChangeEvent> {
    private final ObservableList<Friendship> friendshipsModel = FXCollections.observableArrayList();
    private final ObservableList<Friendship> pendingFriendshipsModel = FXCollections.observableArrayList();
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();

    private final ObservableList<User> friendsModel = FXCollections.observableArrayList();

    private final ObservableList<Message> messagesModel = FXCollections.observableArrayList();
    @FXML
    public Long currentUserID;
    @FXML
    public Button unsendFriendshipRequestButton;
    @FXML
    private Text selectedFriendIDText;
    @FXML
    private Button sendMessageButton;
    @FXML
    private TextField messageTextField;
    @FXML
    private TableColumn<Message, Long> messageIdColumn;
    @FXML
    private TableColumn<Message, Long> messageSenderColumn;
    @FXML
    private TableColumn<Message, Long> messageReceiverColumn;
    @FXML
    private TableColumn<Message, LocalDate> messageDateOfColumn;
    @FXML
    private TableColumn<Message, String> messageMessageColumn;
    @FXML
    private TableColumn<Message, LocalTime> messageTimeOfColumn;
    @FXML
    private TableView<Message> messagesTableView;
    @FXML
    private TableView<Friendship> pendingFriendshipsTableView;
    @FXML
    private TableColumn<Friendship,Long> pendingFriendshipIdColumn;
    @FXML
    private TableColumn<Friendship, Long> pendingFriendshipSenderColumn;
    @FXML
    private TableColumn<Friendship, Long> pendingFriendshipReceiverColumn;
    @FXML
    private TableColumn<Friendship, LocalDate> pendingFriendshipFriendsFromColumn;
    @FXML
    private TableColumn<Friendship, FriendshipStatus> pendingFriendshipStatusColumn;
    @FXML
    private Button logOutButton;
    @FXML
    private AnchorPane generalPane;
    @FXML
    private Text newFriendText;
    @FXML
    private Text welcomeText;

    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField userIdTextField;
    @FXML
    private Button logInButton;
    @FXML
    private Button acceptRequestButton;
    @FXML
    private Button declineRequestButton;
    @FXML
    private TextField friendshipIdTextField;
    @FXML
    private Button addFriendButton;
    @FXML
    private Button deleteFriendButton;
    @FXML
    private Button findFriendButton;
    @FXML
    private Text currentUserIdText;
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private TableColumn<User, Long> userIdColum;
    @FXML
    private TableColumn<User, String> userFirstNameColumn;
    @FXML
    private TableColumn<User, String> userLastNameColumn;
    @FXML
    private TableView<User> friendsTableView;
    @FXML
    private TableColumn<User, Long> friendIdColumn;
    @FXML
    private TableColumn<User, String> friendFirstNameColumn;
    @FXML
    private TableColumn<User, String> friendLastNameColumn;
    @FXML
    private TableView<Friendship> friendshipsTableView;
    @FXML
    private TableColumn<Friendship, Long> friendshipIdColumn;
    @FXML
    private TableColumn<Friendship, String> friendshipSenderColumn;
    @FXML
    private TableColumn<Friendship, String> friendshipReceiverColumn;
    @FXML
    private TableColumn<Friendship, LocalDate> friendshipFriendsFromColumn;
    @FXML
    private TableColumn<Friendship, FriendshipStatus> friendshipStatusColumn;
    @FXML
    private TextField searchedUserLastNameTextField;
    private Service service;
    private Stage window;


    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
        initModel(currentUserID);
    }

    @FXML
    public void initialize(Service service, User user, Stage window) {
        this.service = service;
        this.window = window;
        currentUserID = user.getId();
        selectedFriendIDText.setText(String.valueOf(user.getId()));
        service.addObserver(this);
        initModel(user.getId());

        currentUserIdText.setText(String.valueOf(user.getId()));
        selectedFriendIDText.setText(String.valueOf(user.getId()));
        welcomeText.setText("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
        friendshipIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        friendshipSenderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        friendshipReceiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        friendshipFriendsFromColumn.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));
        friendshipStatusColumn.setCellValueFactory(new PropertyValueFactory<>("friendshipStatus"));
        friendshipsTableView.setItems(friendshipsModel);

        pendingFriendshipIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        pendingFriendshipSenderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        pendingFriendshipReceiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        pendingFriendshipFriendsFromColumn.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));
        pendingFriendshipStatusColumn.setCellValueFactory(new PropertyValueFactory<>("friendshipStatus"));
        pendingFriendshipsTableView.setItems(pendingFriendshipsModel);

        userIdColum.setCellValueFactory(new PropertyValueFactory<>("id"));
        userFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        userLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usersTableView.setItems(usersModel);

        friendIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        friendFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsTableView.setItems(friendsModel);

        messageIdColumn.setCellValueFactory(new  PropertyValueFactory<>("id"));
        messageSenderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        messageReceiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        messageMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageDateOfColumn.setCellValueFactory(new PropertyValueFactory<>("date_of"));
        messageTimeOfColumn.setCellValueFactory(new  PropertyValueFactory<>("time_of"));
        messagesTableView.setItems(messagesModel);

        searchedUserLastNameTextField.textProperty().addListener(find -> usersFilter());
    }

    public void initModel(Long ID) {
        currentUserIdText.setText(String.valueOf(ID));
        User user = service.findUser(ID);
        welcomeText.setText("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");

        Iterable<Friendship> friendships = service.findAllFriendshipRequests(Long.valueOf(currentUserIdText.getText()));
        List<Friendship> friendshipList = StreamSupport.stream(friendships.spliterator(), false).collect(Collectors.toList());
        friendshipsModel.setAll(friendshipList);

        Iterable<Friendship> pendingFriendships = service.findAllSentRequests(Long.valueOf(currentUserIdText.getText()));
        List<Friendship> pendingFriendshipList = StreamSupport.stream(pendingFriendships.spliterator(), false).collect(Collectors.toList());
        pendingFriendshipsModel.setAll(pendingFriendshipList);

        Iterable<User> users = service.findAllUsers();
        List<User> userList = StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
        usersModel.setAll(userList);

        Iterable<User> friends = service.findAllFriends(Long.valueOf(currentUserIdText.getText()));
        List<User> friendsList = StreamSupport.stream(friends.spliterator(), false).collect(Collectors.toList());
        friendsModel.setAll(friendsList);

        Iterable<Message> messages = service.findAllMessagesByUsers(Long.valueOf(currentUserIdText.getText()),Long.valueOf(selectedFriendIDText.getText()));
        List<Message> messageList = StreamSupport.stream(messages.spliterator(), false).collect(Collectors.toList());
        messagesModel.setAll(messageList);

    }

public void onAddFriend() {
        Long currentUserID = Long.valueOf(currentUserIdText.getText());
        Long newFriendID = usersTableView.getSelectionModel().getSelectedItem().getId();
        Long friendshipID = (long) new Random().nextInt();
    try {
        service.addFriendship(friendshipID, currentUserID, newFriendID, LocalDate.now());
        newFriendText.setText("Friendship request sent to " + service.findUser(newFriendID).getFirstName() + " " + service.findUser(newFriendID).getLastName());
    } catch (FriendshipExceptionAlreadyFriends | FriendshipExceptionNonexistentUser |
             UserExceptionEmptyID | UserExceptionEmptyFirstName | UserExceptionEmptyLastName e) {
        newFriendText.setText(service.findUser(newFriendID).getFirstName() + " " + service.findUser(newFriendID).getLastName() + " is already your friend!");
    } catch (FriendshipExceptionSameUser e) {
        newFriendText.setText("Can't send yourself a friendship request!");
    }
}

    public void onDeleteFriend() {
        Long user1 = Long.valueOf(currentUserIdText.getText());
        Long user2 = friendsTableView.getSelectionModel().getSelectedItem().getId();
        Friendship friendship = service.findFriendship(user1, user2);
        service.removeFriendship(friendship.getId());
    }

    public void onFindFriend() {
    }

    public void onSelectedFriend() {
        User user = friendsTableView.getSelectionModel().getSelectedItem();
        selectedFriendIDText.setText(String.valueOf(user.getId()));
        Iterable<Message> messages = service.findAllMessagesByUsers(Long.valueOf(currentUserIdText.getText()), user.getId());
        List<Message> messageList = StreamSupport.stream(messages.spliterator(), false).collect(Collectors.toList());
        messagesModel.setAll(messageList);
    }

    public void onSelectedFriendship() {
        Friendship friendship = friendshipsTableView.getSelectionModel().getSelectedItem();
        friendshipIdTextField.setText(friendship.getId().toString());
        newFriendText.setText(service.findUser(friendship.getSender()).getFirstName() + " " + service.findUser(friendship.getSender()).getLastName() + " wants to become your friend!");
    }

    public void onSelectedPendingFriendship() {
        Friendship friendship = pendingFriendshipsTableView.getSelectionModel().getSelectedItem();
        friendshipIdTextField.setText(friendship.getId().toString());
        newFriendText.setText("Waiting for " + service.findUser(friendship.getReceiver()).getFirstName() + " " + service.findUser(friendship.getReceiver()).getLastName() + " to become your friend!");
    }
    public void onSelectedUser() {
    }

    public void onAcceptRequest() {
        Long acceptBy = Long.valueOf(currentUserIdText.getText());
        Long acceptTo = friendshipsTableView.getSelectionModel().getSelectedItem().getSender();
        try {
            service.addFriendship(9999L, acceptBy, acceptTo, LocalDate.now());
        } catch (FriendshipExceptionSameUser e) {
            newFriendText.setText("Can't send yourself a friendship request!");
        } catch (FriendshipExceptionAlreadyFriends | FriendshipExceptionNonexistentUser | UserExceptionEmptyID |
                 UserExceptionEmptyFirstName | UserExceptionEmptyLastName e) {
            newFriendText.setText(service.findUser(acceptTo).getFirstName() + " " + service.findUser(acceptTo).getLastName() + " is already your friend!");
        }
    }

    public void onDeclineRequest(){
        Long deleteBy = Long.valueOf(currentUserIdText.getText());
        Long deleteTo = friendshipsTableView.getSelectionModel().getSelectedItem().getSender();
        Friendship friendship = service.findFriendship(deleteBy, deleteTo);
        service.removeFriendship(friendship.getId());
    }

    private void usersFilter() {
        String last_name = searchedUserLastNameTextField.getText();
        Predicate<User> userLastNamePredicate = user -> user.getLastName().contains(last_name);
        List<User> users = service.findAllUsers();
        List<User> friends = (List<User>) service.findAllFriends(Long.valueOf(currentUserIdText.getText()));
        usersModel.setAll(users.stream().filter(userLastNamePredicate).collect(Collectors.toList()));
        friendsModel.setAll(friends.stream().filter(userLastNamePredicate).collect(Collectors.toList()));
    }

    public void onLogIn() {
        String newUser = userIdTextField.getText();
        currentUserIdText.setText(newUser);
        this.currentUserID = Long.valueOf(newUser);
        initModel(currentUserID);
    }

    public void onPane() {
        usersTableView.getSelectionModel().clearSelection();
        friendsTableView.getSelectionModel().clearSelection();
        friendshipsTableView.getSelectionModel().clearSelection();
        newFriendText.setText("");
    }

    public void onUnsendFriendshipRequest() {
        Long deleteBy = pendingFriendshipsTableView.getSelectionModel().getSelectedItem().getSender();
        Long deleteTo = pendingFriendshipsTableView.getSelectionModel().getSelectedItem().getReceiver();
        Friendship friendship = service.findFriendship(deleteBy, deleteTo);
        service.removeFriendship(friendship.getId());
    }
    public void onLogOut() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logIn.fxml"));
        Parent root = fxmlLoader.load();
        LogInController logInController = fxmlLoader.getController();
        logInController.initialize(service, window);
        Scene scene = new Scene(root);
        window.setTitle("Social Network");
        window.setScene(scene);
    }

    public void onSelectedMessage(MouseEvent mouseEvent) {
    }

    public void onSendMessage(ActionEvent actionEvent) throws FriendshipExceptionAlreadyFriends, FriendshipExceptionNonexistentUser, UserExceptionEmptyID, FriendshipExceptionSameUser, UserExceptionEmptyFirstName, UserExceptionEmptyLastName {
        User user = friendsTableView.getSelectionModel().getSelectedItem();
        Long currentUserID = Long.valueOf(currentUserIdText.getText());
        Long otherUserID = user.getId();
        String message = messageTextField.getText();
        Long messageID = (long) new Random().nextInt();
        service.addMessage(messageID, currentUserID, otherUserID, message, LocalDate.now(), LocalTime.now());

        messageTextField.clear();
    }
}