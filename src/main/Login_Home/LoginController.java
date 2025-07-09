package com.example.project.Login_Home;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class LoginController {

    private static final String NEON_COLOR = "#00E5FF";
    @FXML private Label titleLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button clearButton;
    @FXML private ToggleButton toggleButton;

    @FXML
    public void initialize() {


        clearButton.setOnAction(e -> {
            usernameField.clear();
            passwordField.clear();
        });


        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                loginButton.setText("Sign Up");
                toggleButton.setText("Switch to Login");
                titleLabel.setText("Create an Account");
            } else {
                loginButton.setText("Login");
                toggleButton.setText("Switch to Sign Up");
                titleLabel.setText("Patient Monitoring System");
            }
        });
    }

    public static Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", 18));
        label.setTextFill(Color.WHITE);
        return label;
    }

    private static void applySpinnerStyle(Spinner<?> spinner) {
        spinner.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: " + NEON_COLOR + ";" +
                "-fx-border-width: 2px; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 5;");
    }



    public static void styleBasicFields(TextField userID, TextField nameField, TextField emailField, TextField phoneField,
                                        PasswordField passwordField, TextField addressField,
                                        Spinner<Integer> ageSpinner, ComboBox<String> genderCombo) {

        String textFieldStyle = "-fx-background-radius: 10px; -fx-border-radius: 10px;" +
                "-fx-padding: 10px; -fx-background-color: #1C1C1C;" +
                "-fx-border-color: " + NEON_COLOR + ";";

        String comboBoxStyle = "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #00bfff; " +
                "-fx-font-size: 14px;";

        userID.setStyle(String.valueOf(userID));
        userID.setFont(Font.font("Segoe UI", 14));
        setNeonTextColor(userID, NEON_COLOR);
        nameField.setStyle(textFieldStyle);
        nameField.setFont(Font.font("Segoe UI", 14));
        setNeonTextColor(nameField, NEON_COLOR);
        emailField.setStyle(textFieldStyle);
        emailField.setFont(Font.font("Segoe UI", 14));
        setNeonTextColor(emailField, NEON_COLOR);
        phoneField.setStyle(textFieldStyle);
        phoneField.setFont(Font.font("Segoe UI", 14));
        setNeonTextColor(phoneField, NEON_COLOR);
        passwordField.setStyle(textFieldStyle);
        passwordField.setFont(Font.font("Segoe UI", 14));
        setNeonTextColor(passwordField, NEON_COLOR);
        addressField.setStyle(textFieldStyle);
        addressField.setFont(Font.font("Segoe UI", 14));
        setNeonTextColor(addressField, NEON_COLOR);
        applySpinnerStyle(ageSpinner);
        setNeonTextColor(ageSpinner, NEON_COLOR);
        genderCombo.setStyle(comboBoxStyle);
        setNeonTextColor(genderCombo, NEON_COLOR);

    }


    public static void styleMedicalFields(ComboBox<String> bloodGroupCombo,
                                          ListView<?> allergyList, ListView<?> diseaseList) {

        String comboBoxStyle = "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #ff6347; " +  // Tomato color for visual contrast
                "-fx-font-size: 14px;";

        String listViewStyle = "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #ff6347; " +
                "-fx-padding: 5px; " +
                "-fx-font-size: 13px;";

        bloodGroupCombo.setStyle(comboBoxStyle);
        allergyList.setStyle(listViewStyle);
        diseaseList.setStyle(listViewStyle);
    }


    public static void setNeonTextColor(Node node, String color) {
        if (node instanceof Labeled) {
            ((Labeled) node).setTextFill(Color.web(color));
        } else if (node instanceof TextInputControl) {
            ((TextInputControl) node).setStyle(((TextInputControl) node).getStyle() + "-fx-text-fill: " + color + ";");
        }
    }
}
