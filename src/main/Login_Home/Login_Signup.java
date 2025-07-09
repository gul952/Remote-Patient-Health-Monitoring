package com.example.project.Login_Home;

import com.example.project.DATABASE.LoginResult;
import com.example.project.DATABASE.LoginServer;
import com.example.project.DashBoard.AdminDashboard;
import com.example.project.DashBoard.DashBoards;
import com.example.project.DashBoard.DoctorDashboard;
import com.example.project.DashBoard.PatientDashboard;
import com.example.project.SignUpPages.SignUpHander;
import com.example.project.SignUpPages.LoadAdminSignUP;
import com.example.project.SignUpPages.LoadDoctorSignUP;
import com.example.project.SignUpPages.LoadPatientSignUP;
import javafx.application.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;


public class Login_Signup extends Application {

    private static final String NEON_COLOR = "#00E5FF";


    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command line arguments (not used).
     */
    @Override
    public void start(Stage stage) {
        Label title = createTitle();
        TextField usernameField = new TextField();
        HBox usernameBox = createUsernameField(usernameField);

        ComboBox<String> roleBox = new ComboBox<>();
        HBox roleComboBox = createRoleComboBox(roleBox);
        PasswordField passwordField = new PasswordField();
        HBox passwordBox = createPasswordBox(passwordField);

        Button loginButton = new Button("Login");
        Button clearButton = new Button("Clear");
        Button homeButton = new Button("Home");
        HBox buttonBox = createButtons(stage, usernameField, passwordField, loginButton, clearButton, roleBox, homeButton);
        HBox topBar = new HBox(homeButton);
        topBar.setAlignment(Pos.TOP_CENTER);
        topBar.setPadding(new Insets(10, 0, 0, 10));
        VBox layout = new VBox();
        ToggleButton toggleButton = createToggleButton(title, loginButton, layout, roleComboBox, roleBox);

        layout.getChildren().addAll(topBar, title, usernameBox, passwordBox, buttonBox, toggleButton);
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #0F2027, #203A43, #2C5364);"
                + "-fx-padding: 40px;");

        double screenwidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenheight = Screen.getPrimary().getVisualBounds().getHeight();
        homeButton.setOnAction(e -> {
            // Return to home page
            HomePage homePage = new HomePage();
            try {
                homePage.start(stage); // Reuse the same stage
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(layout, screenwidth*0.85, screenheight*0.85);
        stage.setTitle("Login/Signup");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    /**
     * Creates a title label with neon effect.
     *
     * @return A Label object representing the title.
     */
    private Label createTitle() {
        Label title = new Label("Patient Monitoring System");
        title.setFont(new Font("Arial", 28));
        setNeonTextColor(title, NEON_COLOR);
        title.setEffect(new DropShadow(10, Color.web("#00E5FF")));
        return title;
    }

    /**
     * Creates the username field for the login/signup form.
     *
     * @param usernameField The TextField for the username.
     * @return A HBox containing the username field.
     */
    private HBox createUsernameField(TextField usernameField) {
        usernameField.setPromptText("Username");
        styleTextField(usernameField);

        HBox usernameBox = new HBox(usernameField);
        usernameBox.setAlignment(Pos.CENTER);
        return usernameBox;
    }

    /**
     * Creates the password field for the login/signup form.
     *
     * @param passwordField The PasswordField for the password.
     * @return A HBox containing the password field.
     */
    private HBox createPasswordBox(PasswordField passwordField) {
        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Password");
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setPickOnBounds(false);
        passwordField.setPickOnBounds(false);

        passwordField.setPromptText("Password");

        styleTextField(passwordField);
        styleTextField(visiblePasswordField);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        Button toggleVisibility = new Button("👁");
        toggleVisibility.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        setNeonTextColor(toggleVisibility, NEON_COLOR);
        toggleVisibility.setFocusTraversable(false);
        toggleVisibility.setPrefSize(30, 30);

        DropShadow glow = new DropShadow(10, Color.web("#00E5FF"));
        toggleVisibility.setEffect(glow);

        StackPane fieldStack = new StackPane(passwordField, visiblePasswordField);
        fieldStack.setPrefWidth(300);

        visiblePasswordField.setMouseTransparent(true);

        toggleVisibility.setOnAction(e -> {
            boolean isShowing = visiblePasswordField.isVisible();

            // Toggle fields
            visiblePasswordField.setVisible(!isShowing);
            visiblePasswordField.setManaged(!isShowing);
            visiblePasswordField.setMouseTransparent(isShowing);

            passwordField.setVisible(isShowing);
            passwordField.setManaged(isShowing);
            passwordField.setMouseTransparent(!isShowing);

            toggleVisibility.setText(isShowing ? "👁" : "🙈");
        });

        StackPane eyeWrapper = new StackPane(toggleVisibility);
        StackPane.setAlignment(toggleVisibility, Pos.CENTER_RIGHT);
        eyeWrapper.setMouseTransparent(false);
        eyeWrapper.setPickOnBounds(false);

        StackPane combinedStack = new StackPane(fieldStack, eyeWrapper);
        StackPane.setAlignment(eyeWrapper, Pos.CENTER_RIGHT);
        combinedStack.setMaxWidth(300);

        HBox passwordBox = new HBox(combinedStack);
        passwordBox.setAlignment(Pos.CENTER);

        return passwordBox;
    }

    /**
     * Creates the buttons for the login/signup form.
     *
     * @param stage         The primary stage of the application.
     * @param usernameField The TextField for the username.
     * @param passwordField The PasswordField for the password.
     * @param loginButton   The Button for login/signup action.
     * @param clearButton   The Button to clear fields.
     * @param roleBox       The ComboBox for selecting user role.
     * @return A HBox containing the buttons.
     */
    public HBox createButtons(Stage stage, TextField usernameField, PasswordField passwordField, Button loginButton, Button clearButton, ComboBox<String> roleBox, Button homeButton) {
        styleNeonButton(loginButton, "#00FFAB", "#00D9A5");
        styleNeonButton(clearButton, "#FF1744", "#D50000");
        styleNeonButton(homeButton, "#FF1744", "#D50000");

        clearButton.setOnAction(e -> {
            usernameField.clear();
            passwordField.clear();
        });

        loginButton.setOnAction(e -> {
            if (loginButton.getText().equals("Sign Up")) {

                SignUpHander[] signUpHanders = new SignUpHander[3];
                String role = roleBox.getSelectionModel().getSelectedItem();

                if (Objects.equals(roleBox.getSelectionModel().getSelectedItem(), "Patient")) {
                    signUpHanders[0] = new LoadPatientSignUP();
                    signUpHanders[0].loadSignUPPage(role);
                }
                else if (Objects.equals(roleBox.getSelectionModel().getSelectedItem(), "Doctor")) {
                    signUpHanders[1] = new LoadDoctorSignUP();
                    signUpHanders[1].loadSignUPPage(role);
                }
                else if (Objects.equals(roleBox.getSelectionModel().getSelectedItem(), "Admin")) {
                    signUpHanders[2] = new LoadAdminSignUP();
                    signUpHanders[2].loadSignUPPage(role);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Role");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select a valid role");
                    alert.showAndWait();
                }

            } else {
                // Login mode
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();


                LoginResult loginResult = LoginServer.login(username, password);

                if (loginResult == null || loginResult.getRole() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid credentials");
                    alert.showAndWait();
                } else if (loginResult.isAccountStatus() == false) {
                    // Show message if account is inactive
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Account Pending Approval");
                    alert.setHeaderText(null);
                    alert.setContentText("Your account is not yet active.\nPlease wait for confirmation from the Admin.\nIt may take 1 to 2 days or less.");
                    alert.showAndWait();
                } else {
                    DashBoards[] dashBoards = new DashBoards[3];
                    dashBoards[0] = new DoctorDashboard();
                    dashBoards[1] = new PatientDashboard();
                    dashBoards[2] = new AdminDashboard();
                    // USING RUNTIME POLYMORPHISM
                    switch (loginResult.getRole()) {
                        case "Doctor":
                            dashBoards[0].start(stage, username);
                            break;
                        case "Patient":
                            dashBoards[1].start(stage, username);
                            break;
                        case "Admin":
                            dashBoards[2].start(stage, username);
                            break;
                        default:
                            // Fallback alert for unknown roles
                            Alert unknownRoleAlert = new Alert(Alert.AlertType.ERROR);
                            unknownRoleAlert.setTitle("Login Failed");
                            unknownRoleAlert.setHeaderText(null);
                            unknownRoleAlert.setContentText("Unknown user role.");
                            unknownRoleAlert.showAndWait();
                    }
                }

            }

        });

        HBox buttonBox = new HBox(15, clearButton, loginButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }


    /**
     * Creates a toggle button to switch between login and signup modes.
     *
     * @param title        The title label.
     * @param loginButton  The login/signup button.
     * @param layout       The main layout of the scene.
     * @param roleComboBox The HBox containing the role ComboBox.
     * @param roleBox      The ComboBox for selecting user role.
     * @return A ToggleButton to switch between login and signup modes.
     */
    public ToggleButton createToggleButton(Label title, Button loginButton, VBox layout, HBox roleComboBox, ComboBox<String> roleBox) {
        ToggleButton toggleButton = new ToggleButton("Switch to Sign Up");
        styleNeonToggleButton(toggleButton);

        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                loginButton.setText("Sign Up");
                toggleButton.setText("Switch to Login");
                title.setText("Create an Account");

                layout.getChildren().add(layout.getChildren().size() - 1, roleComboBox);
            } else {
                loginButton.setText("Login");
                toggleButton.setText("Switch to Sign Up");
                title.setText("Patient Monitoring System");
                roleBox.getSelectionModel().clearSelection();

                layout.getChildren().remove(roleComboBox);
            }
        });

        return toggleButton;
    }


    /**
     * Styles the TextField with a neon effect.
     *
     * @param field The TextField to style.
     */
    public void styleTextField(TextField field) {
        field.setFont(Font.font("Segoe UI", 14));
        field.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px;" +
                "-fx-padding: 10px; -fx-background-color: #1C1C1C;" +
                "-fx-border-color: " + NEON_COLOR + ";");
        setNeonTextColor(field, NEON_COLOR);
        field.setPrefWidth(300);
    }


    /**
     * Styles the Button with a neon effect.
     *
     * @param button      The Button to style.
     * @param color       The color of the button.
     * @param hoverColor  The hover color of the button.
     */
    public static void styleNeonButton(Button button, String color, String hoverColor) {
        DropShadow neonShadow = new DropShadow(20, Color.web(color));
        button.setFont(Font.font("Segoe UI", 14));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: " + color + ";"
                + "-fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        button.setEffect(neonShadow);
        button.setPrefWidth(130);
        button.setPrefHeight(45);
        setNeonTextColor(button, "white");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: black;"
                    + "-fx-border-color: " + hoverColor + "; -fx-border-radius: 10px;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-border-color: " + color + ";"
                    + "-fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;"
                    + "-fx-text-fill: white;");
        });
    }

    /**
     * Styles the ToggleButton with a neon effect.
     *
     * @param button The ToggleButton to style.
     */
    private void styleNeonToggleButton(ToggleButton button) {
        DropShadow neonShadow = new DropShadow(20, Color.web("#FFC107"));
        button.setFont(Font.font("Segoe UI", 14));
        button.setTextFill(Color.BLACK);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: " + "#FFC107" + ";"
                + "-fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        button.setEffect(neonShadow);
        button.setPrefWidth(180);
        button.setPrefHeight(45);
        setNeonTextColor(button, "white");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + "#FFB300" + "; -fx-text-fill: black;"
                    + "-fx-border-color: " + "#FFB300" + "; -fx-border-radius: 10px;");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-border-color: " + "#FFC107" + ";"
                    + "-fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;"
                    + "-fx-text-fill: white;");
        });
    }

    /**
     * Creates the role ComboBox for selecting user role.
     *
     * @param roleBox The ComboBox for selecting user role.
     * @return A HBox containing the role ComboBox.
     */
    private HBox createRoleComboBox(ComboBox<String> roleBox) {
        roleBox.getItems().addAll("Doctor", "Patient", "Admin");
        roleBox.setPromptText(null);
        roleBox.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px;" +
                "-fx-padding: 10px; -fx-background-color: #1C1C1C;" +
                "-fx-border-color: " + NEON_COLOR + ";");
        setNeonTextColor(roleBox, "white");

        // Apply styling to the options in the ComboBox
        roleBox.setCellFactory(lv -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: #1C1C1C;");
                        setNeonTextColor(this, "white");
                    }
                }
            };
        });

        // Styling when an item is selected
        roleBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-background-color: #1C1C1C;");
                    setNeonTextColor(this, "white");
                }
            }
        });

        Label roleLabel = new Label("Select Role");
        setNeonTextColor(roleLabel, "white");
        roleLabel.setStyle("-fx-font-size: 14px;");


        HBox box = new HBox(10, roleLabel, roleBox);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private static void setNeonTextColor(Node node, String color) {
        if (node instanceof Labeled) {
            ((Labeled) node).setTextFill(Color.web(color));
        } else if (node instanceof TextInputControl) {
            ((TextInputControl) node).setStyle(((TextInputControl) node).getStyle() + "-fx-text-fill: " + color + ";");
        }
    }




    public static void launchLoginSignup(Stage primaryStage) {
        Platform.runLater(() -> {

            try {
                Login_Signup loginPage = new Login_Signup();
                loginPage.start(new Stage()); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}