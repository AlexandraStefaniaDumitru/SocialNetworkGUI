package com.example.socialnetworkgui;

import com.example.socialnetworkgui.repository.database.FriendshipDBRepository;
import com.example.socialnetworkgui.repository.database.MessageDBRepository;
import com.example.socialnetworkgui.repository.database.UserDBRepository;
import com.example.socialnetworkgui.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    Scene loginScene;
    LogInController logInController;
    Service service;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.service = new Service(new UserDBRepository(), new FriendshipDBRepository(), new MessageDBRepository());
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("logIn.fxml"));
        loginScene = new Scene(fxmlLoader.load(), 420, 240);
        logInController  = fxmlLoader.getController();
        logInController.initialize(service, primaryStage);
        primaryStage.setTitle("Social Network");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}