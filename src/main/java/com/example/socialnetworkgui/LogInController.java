package com.example.socialnetworkgui;

import com.example.socialnetworkgui.model.User;
import com.example.socialnetworkgui.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.control.Alert.AlertType.ERROR;

public class LogInController {
    private Stage window;
    private Service service;
    @FXML
    private TextField logInUserTextField;
    @FXML
    private PasswordField logInPasswordTextField;
    @FXML
    private Button logInButton;


    public void initialize(Service service, Stage primaryStage) {
        this.service = service;
        this.window = primaryStage;
    }

    public void onUserLogIn() throws IOException {
        User loggedUser = this.service.findUser(Long.valueOf(logInUserTextField.getText()));
        if (loggedUser == null) {
            Alert alert = new Alert(ERROR);
            alert.setTitle("LOG IN ERROR");
            alert.setHeaderText(null);
            alert.setContentText("User ID doesn't exist!");
            alert.showAndWait();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPane.fxml"));
        Parent root = fxmlLoader.load();
        NetworkController networkController = fxmlLoader.getController();
        assert loggedUser != null;
        networkController.initialize(service, loggedUser, window);
        Scene scene = new Scene(root);
        window.setTitle("Social Network");
        window.setScene(scene);
        window.setMaximized(true);

    }

    public void onEnterCredentials(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            onUserLogIn();
        }
    }
}