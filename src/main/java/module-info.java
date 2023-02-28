module com.example.socialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialnetworkgui to javafx.fxml, javafx.base;
    opens com.example.socialnetworkgui.model to javafx.fxml, javafx.base;

    exports com.example.socialnetworkgui;
    exports com.example.socialnetworkgui.repository;
    exports com.example.socialnetworkgui.model;
    exports com.example.socialnetworkgui.utils.events;
    exports com.example.socialnetworkgui.utils.observer;
    exports com.example.socialnetworkgui.service;
    exports com.example.socialnetworkgui.ui;

    opens com.example.socialnetworkgui.ui to javafx.fxml;
}