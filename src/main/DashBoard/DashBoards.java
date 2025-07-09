package com.example.project.DashBoard;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// this is interface of all Dashboards to implement the polymorphism all dashboards classes will implement this
// and will have the same methods to be used in the main class
public interface DashBoards {
    void start(Stage stage, String ID);
    VBox buildDashboard();
    VBox buildSidebar();
    HBox buildTopBar(Stage stage);
}
