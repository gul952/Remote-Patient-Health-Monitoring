package com.example.project.SignUpPages;

import com.example.project.DATABASE.DBConnection;
import com.example.project.User_Management.Administrator;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static com.example.project.Login_Home.LoginController.createLabel;
import static com.example.project.Login_Home.LoginController.styleBasicFields;
import static com.example.project.Login_Home.Login_Signup.styleNeonButton;

public class LoadAdminSignUP implements SignUpHander {
    @Override
    public void loadSignUPPage(String role) {
        Stage stage = new Stage();

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0F2027, #203A43, #2C5364);");

        GridPane gridTop = new GridPane();
        gridTop.setHgap(20);
        gridTop.setVgap(15);
        // Fields
        TextField userID = new TextField();
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField addressField = new TextField();
        Spinner<Integer> ageSpinner = new Spinner<>(1, 120, 25);
        ageSpinner.setEditable(true);
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Others");
        TextField departmentField = new TextField();
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("SYSTEM_ADMIN", "HOSPITAL_ADMIN", "MEDICAL_RECORD_ADMIN");

        Button signupButton = new Button("Sign Up");
        styleNeonButton(signupButton, "#00FFAB", "#00D9A5");

        // Grid layout
        gridTop.add(createLabel("Username:"), 0, 0);
        gridTop.add(userID, 1, 0);

        gridTop.add(createLabel("Name:"), 0, 1);
        gridTop.add(nameField, 1, 1);

        gridTop.add(createLabel("Email:"), 2, 1);
        gridTop.add(emailField, 3, 1);

        gridTop.add(createLabel("Phone Number:"), 0, 2);
        gridTop.add(phoneField, 1, 2);

        gridTop.add(createLabel("Password:"), 2, 2);
        gridTop.add(passwordField, 3, 2);

        VBox verticalFields = new VBox(15);
        verticalFields.setPadding(new Insets(5, 0, 0, 0));

        verticalFields.getChildren().addAll(
                new VBox(5, createLabel("Address:"), addressField),
                new VBox(5, createLabel("Age:"), ageSpinner),
                new VBox(5, createLabel("Gender:"), genderCombo),
                new VBox(5, createLabel("Department:"),departmentField),
                new VBox(5,createLabel("Role:"),roleCombo),
                signupButton
        );

        mainLayout.getChildren().addAll(gridTop, verticalFields);

        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        double screenwidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenheight = Screen.getPrimary().getVisualBounds().getHeight();

        Scene scene = new Scene(scrollPane, screenwidth * 0.85, screenheight * 0.85);
        stage.setScene(scene);
        stage.setTitle("Admin Signup");

        styleBasicFields(userID, nameField, emailField, phoneField, passwordField, addressField, ageSpinner, genderCombo);
        departmentField.setStyle("-fx-background-color: #112233; -fx-text-fill: white; -fx-border-color: #00FFAB;");
        roleCombo.setStyle("-fx-background-color: #112233; -fx-text-fill: white; -fx-border-color: #00FFAB;");

        signupButton.setOnAction(e -> {
            try {
                Administrator admin = new Administrator();
                admin.setUserID(userID.getText());
                admin.setName(nameField.getText());
                admin.setEmail(emailField.getText());
                admin.setPhoneNumber(phoneField.getText());
                admin.setPassword(passwordField.getText());
                admin.setAddress(addressField.getText());
                admin.setAge(Integer.parseInt(String.valueOf(ageSpinner.getValue())));
                admin.setDepartment(departmentField.getText());
                admin.setRole(Administrator.AdminRole.valueOf(roleCombo.getValue()));
                admin.setGender(genderCombo.getValue());

                try (Connection connection = DBConnection.getConnection()) {

                    // Step 2: Insert into user table
                    String userSQL = "INSERT INTO user (userID, name, email, phoneNumber, password, address, age, gender, accountStatus, role) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, TRUE, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(userSQL)) {
                        ps.setString(1, userID.getText());
                        ps.setString(2, nameField.getText());
                        ps.setString(3, emailField.getText());
                        ps.setString(4, phoneField.getText());
                        ps.setString(5, passwordField.getText());
                        ps.setString(6, addressField.getText());
                        ps.setInt(7, ageSpinner.getValue());
                        ps.setString(8, genderCombo.getValue());
                        ps.setString(9, role);
                        ps.executeUpdate();
                    }

                    String checkDeptSQL = "INSERT IGNORE INTO Departments (departmentName, description) VALUES (?, '')";
                    try (PreparedStatement ps = connection.prepareStatement(checkDeptSQL)) {
                        ps.setString(1, departmentField.getText());
                        ps.executeUpdate();
                    }

                    // Step 3: Insert into Doctors table
                    String doctorSQL = "INSERT INTO Administrators (adminId, role, department) " +
                            "VALUES (?, ?, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(doctorSQL)) {
                        ps.setString(1, userID.getText());
                        ps.setString(2, roleCombo.getValue());
                        ps.setString(3, departmentField.getText());

                        ps.executeUpdate();
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Administrator signup successful!");
                    alert.show();
                }



            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.show();
            }
        });

        stage.show();
    }
}
