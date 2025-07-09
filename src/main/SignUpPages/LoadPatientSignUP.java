package com.example.project.SignUpPages;

import com.example.project.DATABASE.DBConnection;
import com.example.project.User_Management.Patient;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static com.example.project.Login_Home.LoginController.*;
import static com.example.project.Login_Home.Login_Signup.styleNeonButton;

public class LoadPatientSignUP implements SignUpHander {
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
        Spinner<Integer> ageSpinner = new Spinner<>(18, 60, 25);
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");

        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

        ListView<Patient.AllergyType> allergyList = new ListView<>();
        allergyList.getItems().addAll(Patient.AllergyType.values());
        allergyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        allergyList.setPrefHeight(100);

        ListView<Patient.DiseaseType> diseaseList = new ListView<>();
        diseaseList.getItems().addAll(Patient.DiseaseType.values());
        diseaseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        diseaseList.setPrefHeight(100);

        Button signupButton = new Button("Sign Up");
        styleNeonButton(signupButton, "#00FFAB", "#00D9A5");
        signupButton.setAlignment(Pos.CENTER);

        Label usernameLabel = createLabel("Username:");
        gridTop.add(usernameLabel, 0, 0);  // Row 0, Column 0
        gridTop.add(userID, 1, 0);         // Row 0, Column 1

        Label nameLabel = createLabel("Name:");
        gridTop.add(nameLabel, 0, 1);      // Row 1, Column 0
        gridTop.add(nameField, 1, 1);      // Row 1, Column 1

        Label emailLabel = createLabel("Email:");
        gridTop.add(emailLabel, 2, 0);     // Row 2, Column 0
        gridTop.add(emailField, 3, 0);     // Row 2, Column 1

        Label phoneLabel = createLabel("Phone Number:");
        gridTop.add(phoneLabel, 0, 2);    // Row 3, Column 0
        gridTop.add(phoneField, 1, 2);     // Row 3, Column 1

        Label passwordLabel = createLabel("Password:");
        gridTop.add(passwordLabel, 2, 2);  // Row 4, Column 0
        gridTop.add(passwordField, 3, 2);

        VBox verticalFields = new VBox(15);
        verticalFields.setPadding(new Insets(5, 0, 0, 0));

        VBox addressBox = new VBox(5, createLabel("Address:"), addressField);
        VBox ageBox = new VBox(5, createLabel("Age:"), ageSpinner);
        VBox genderBox = new VBox(5, createLabel("Gender:"), genderCombo);
        VBox bloodBox = new VBox(5, createLabel("Blood Group:"), bloodGroupCombo);
        VBox allergyBox = new VBox(5, createLabel("Allergies:"), allergyList);
        VBox diseaseBox = new VBox(5, createLabel("Diseases:"), diseaseList);

        verticalFields.getChildren().addAll(addressBox, ageBox, genderBox, bloodBox, allergyBox, diseaseBox, signupButton);
        mainLayout.getChildren().addAll(gridTop, verticalFields);


        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        double screenwidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenheight = Screen.getPrimary().getVisualBounds().getHeight();

        Scene scene = new Scene(scrollPane, screenwidth * 0.85, screenheight * 0.85);
        stage.setScene(scene);
        stage.setTitle("Patient Signup");

        styleBasicFields(userID, nameField, emailField, phoneField, passwordField, addressField, ageSpinner, genderCombo);
        styleMedicalFields(bloodGroupCombo, allergyList, diseaseList);

        signupButton.setOnAction(e -> {
            try {
                // Get data from the form
                String patientID = userID.getText();  // Also used as userID
                String name = nameField.getText();
                String email = emailField.getText();
                String phoneNumber = phoneField.getText();
                String password = passwordField.getText();
                String address = addressField.getText();
                int age = ageSpinner.getValue();
                String gender = genderCombo.getValue(); // Use full gender string: "Male", "Female", etc.
                String bloodGroup = bloodGroupCombo.getValue();
                ObservableList<Patient.AllergyType> selectedAllergies = allergyList.getSelectionModel().getSelectedItems();
                ObservableList<Patient.DiseaseType> selectedDiseases = diseaseList.getSelectionModel().getSelectedItems();

                try (Connection connection = DBConnection.getConnection()) {

                    // Step 1: Insert into User Table
                    String insertUserSQL = "INSERT INTO user (userID, name, email, phoneNumber, password, address, age, gender, accountStatus, role) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, FALSE, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
                        preparedStatement.setString(1, patientID);
                        preparedStatement.setString(2, name);
                        preparedStatement.setString(3, email);
                        preparedStatement.setString(4, phoneNumber);
                        preparedStatement.setString(5, password); // Ideally hash it
                        preparedStatement.setString(6, address);
                        preparedStatement.setInt(7, age);
                        preparedStatement.setString(8, gender); // Use full string: "Male", "Female", etc.
                        preparedStatement.setString(9, role); // Set the role
                        preparedStatement.executeUpdate();
                    }

                    // Step 2: Insert into Patients Table
                    String insertPatientSQL = "INSERT INTO Patients (patientID, bloodGroup, appointmentDate, assignedDoctorID) " +
                            "VALUES (?, ?, NULL, NULL)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertPatientSQL)) {
                        preparedStatement.setString(1, patientID);
                        preparedStatement.setString(2, bloodGroup);
                        preparedStatement.executeUpdate();
                    }

                    // Step 3: Insert Allergies
                    if (!selectedAllergies.isEmpty()) {
                        String insertAllergySQL = "INSERT INTO PatientAllergies (patientID, allergyType) VALUES (?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertAllergySQL)) {
                            for (Patient.AllergyType allergy : selectedAllergies) {
                                preparedStatement.setString(1, patientID);
                                preparedStatement.setString(2, allergy.name());
                                preparedStatement.addBatch();
                            }
                            preparedStatement.executeBatch();
                        }
                    }

                    // Step 4: Insert Diseases
                    if (!selectedDiseases.isEmpty()) {
                        String insertDiseaseSQL = "INSERT INTO PatientDiseases (patientID, diseaseType) VALUES (?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertDiseaseSQL)) {
                            for (Patient.DiseaseType disease : selectedDiseases) {
                                preparedStatement.setString(1, patientID);
                                preparedStatement.setString(2, disease.name());
                                preparedStatement.addBatch();
                            }
                            preparedStatement.executeBatch();
                        }
                    }

                    // Success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Patient signup successful!");
                    alert.show();
                }

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage());
                alert.show();
                ex.printStackTrace();
            }
        });


        stage.show();
    }

}
