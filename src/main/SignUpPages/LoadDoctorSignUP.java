package com.example.project.SignUpPages;

import com.example.project.DATABASE.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static com.example.project.Login_Home.LoginController.createLabel;
import static com.example.project.Login_Home.LoginController.styleBasicFields;
import static com.example.project.Login_Home.Login_Signup.styleNeonButton;

public class LoadDoctorSignUP implements SignUpHander {
    @Override
    public void loadSignUPPage(String role) {
        Stage stage = new Stage();

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0F2027, #203A43, #2C5364);");

        GridPane gridTop = new GridPane();
        gridTop.setHgap(20);
        gridTop.setVgap(15);

        // Input fields
        TextField userID = new TextField();  // New username field
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField addressField = new TextField();
        Spinner<Integer> ageSpinner = new Spinner<>(25, 70, 30);
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");

        ComboBox<String> specializationCombo = new ComboBox<>();
        specializationCombo.getItems().addAll("CARDIOLOGIST", "NEUROLOGIST", "ORTHOPEDIC_SURGEON", "PEDIATRICIAN", "DERMATOLOGIST", "OPHTHALMOLOGIST", "RADIOLOGIST", "ENT_SPECIALIST", "GENERAL_PHYSICIAN", "PSYCHIATRIST");

        TextField licenseNumberField = new TextField();
        Spinner<Integer> experienceSpinner = new Spinner<>(1, 50, 5);
        TextField consultationFeeField = new TextField();

        ComboBox<String> startTimeCombo = new ComboBox<>();
        ComboBox<String> endTimeCombo = new ComboBox<>();
        List<String> timeOptions = generate24HourTimeOptions("07:00", "21:00", 30);
        startTimeCombo.setItems(FXCollections.observableArrayList(timeOptions));
        endTimeCombo.setItems(FXCollections.observableArrayList(timeOptions));
        startTimeCombo.getSelectionModel().selectFirst();
        endTimeCombo.getSelectionModel().selectLast();

        // Styling for combos
        String comboStyle = "-fx-background-color: #112233; -fx-text-fill: white; -fx-border-color: #00FFAB;";
        startTimeCombo.setStyle(comboStyle);
        endTimeCombo.setStyle(comboStyle);

        // HBox for time range
        Label fromLabel = createLabel("From:");
        Label toLabel = createLabel("To:");
        HBox timeRangeBox = new HBox(10, fromLabel, startTimeCombo, toLabel, endTimeCombo);
        timeRangeBox.setAlignment(Pos.CENTER_LEFT);

        ListView<String> availableDaysList = new ListView<>();
        availableDaysList.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        availableDaysList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        availableDaysList.setPrefHeight(100);

        TextField timeRangeField = new TextField(); // Optional: Replace with something more advanced later

        Button signupButton = new Button("Sign Up");
        styleNeonButton(signupButton, "#00FFAB", "#00D9A5");

        // Grid layout
        gridTop.add(createLabel("Username:"), 0, 0);  // Username label
        gridTop.add(userID, 1, 0);  // Username input field

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
                new VBox(5, createLabel("Specialization:"), specializationCombo),
                new VBox(5, createLabel("License No:"), licenseNumberField),
                new VBox(5, createLabel("Experience (Years):"), experienceSpinner),
                new VBox(5, createLabel("Consultation Fee:"), consultationFeeField),
                new VBox(5, createLabel("Working Time:"), timeRangeBox),
                new VBox(5, createLabel("Start Time:"), startTimeCombo),
                new VBox(5, createLabel("End Time:"), endTimeCombo),
                new VBox(5, createLabel("Available Days:"), availableDaysList),
                new VBox(5, createLabel("Available Time Range:"), timeRangeField),
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
        stage.setTitle("Doctor Signup");

        styleBasicFields(userID, nameField, emailField, phoneField, passwordField, addressField, ageSpinner, genderCombo);
        specializationCombo.setStyle("-fx-background-color: #112233; -fx-text-fill: white; -fx-border-color: #00FFAB;");
        licenseNumberField.setStyle("-fx-background-color: #112233; -fx-text-fill: white; -fx-border-color: #00FFAB;");
        consultationFeeField.setStyle("-fx-background-color: #112233; -fx-text-fill: white; -fx-border-color: #00FFAB;");
        timeRangeField.setStyle("-fx-background-color: #112233; -fx-text-fill: white; -fx-border-color: #00FFAB;");

        signupButton.setOnAction(e -> {
            try {
                // Step 1: Extract values from form
                String doctorID = userID.getText(); // same as userID
                String name = nameField.getText();
                String email = emailField.getText();
                String phoneNumber = phoneField.getText();
                String password = passwordField.getText();
                String address = addressField.getText();
                int age = ageSpinner.getValue();
                String gender = genderCombo.getValue();
                String specialization = specializationCombo.getValue();
                String licenseNumber = licenseNumberField.getText();
                int experienceYears = experienceSpinner.getValue();
                double consultationFee = Double.parseDouble(consultationFeeField.getText());
                String startTime = startTimeCombo.getValue();
                String endTime = endTimeCombo.getValue();
                ObservableList<String> availableDays = availableDaysList.getSelectionModel().getSelectedItems();

                try (Connection connection = DBConnection.getConnection()) {

                    // Step 2: Insert into user table
                    String userSQL = "INSERT INTO user (userID, name, email, phoneNumber, password, address, age, gender, accountStatus, role) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, FALSE, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(userSQL)) {
                        ps.setString(1, doctorID);
                        ps.setString(2, name);
                        ps.setString(3, email);
                        ps.setString(4, phoneNumber);
                        ps.setString(5, password);
                        ps.setString(6, address);
                        ps.setInt(7, age);
                        ps.setString(8, gender);
                        ps.setString(9, role);
                        ps.executeUpdate();
                    }

                    // Step 3: Insert into Doctors table
                    String doctorSQL = "INSERT INTO Doctors (doctorID, specialization, licenseNumber, hospitalName, availableTime, experienceYears, consultationFee, startTime, endTime) " +
                            "VALUES (?, ?, ?, NULL, NULL, ?, ?, ?, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(doctorSQL)) {
                        ps.setString(1, doctorID);
                        ps.setString(2, specialization);
                        ps.setString(3, licenseNumber);
                        ps.setInt(4, experienceYears);
                        ps.setDouble(5, consultationFee);
                        ps.setString(6, startTime);
                        ps.setString(7, endTime);
                        ps.executeUpdate();
                    }

                    // Step 4: Insert into DoctorAvailableDays
                    String daySQL = "INSERT INTO DoctorAvailableDays (doctorID, availableDay) VALUES (?, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(daySQL)) {
                        for (String day : availableDays) {
                            ps.setString(1, doctorID);
                            ps.setString(2, day);
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Doctor signup successful!");
                    alert.show();
                }

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Signup failed: " + ex.getMessage());
                alert.show();
                ex.printStackTrace();
            }
        });



        stage.show();
    }




    private static List<String> generate24HourTimeOptions(String startTimeStr, String endTimeStr, int intervalMinutes) {
        List<String> times = new ArrayList<>();
        String[] startParts = startTimeStr.split(":");
        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        String[] endParts = endTimeStr.split(":");
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);
        int currentHour = startHour;
        int currentMinute = startMinute;
        while (currentHour < endHour || (currentHour == endHour && currentMinute <= endMinute)) {
            String time = String.format("%02d:%02d", currentHour, currentMinute);
            times.add(time);
            currentMinute += intervalMinutes;
            if (currentMinute >= 60) {
                currentMinute = currentMinute % 60;
                currentHour++;
            }
        }
        return times;
    }
}
