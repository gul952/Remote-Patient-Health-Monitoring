package com.example.project.Login_Home;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

/**
 * JavaFX application that serves as the homepage for a hospital management system.
 * It includes a top navigation bar, a center banner section with a slideshow,
 * and a bottom services section with buttons for various services.
 */
public class HomePage extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // --- Top Navigation Bar ---
        HBox navBar = createTopNavBar(primaryStage);
        root.setTop(navBar);
        navBar.setPrefHeight(60);

        // --- Center Banner Section ---
        StackPane banner = createBannerSection();
        root.setCenter(banner);
        banner.setPrefHeight(400);

        // --- Bottom Services Section ---
        HBox servicesBar = createServicesBar();
        root.setBottom(servicesBar);
        servicesBar.setPrefHeight(120);

        Scene scene = new Scene(root, 1080, 680);
        primaryStage.setTitle("Homepage");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the top navigation bar with various menu items and buttons.
     *
     * @param primaryStage The primary stage for this application.
     * @return A HBox containing the navigation bar.
     */
    private HBox createTopNavBar(Stage primaryStage) {
        HBox navBar = new HBox();
        navBar.setPadding(new Insets(10));
        navBar.setSpacing(20);
        navBar.setStyle("-fx-background-color: #009ddc;");
        navBar.setAlignment(Pos.CENTER_LEFT);

        // Logo
        Label logo = new Label("HopeLife Hospital");
        logo.setFont(Font.font("Arial", 20));
        logo.setTextFill(Color.WHITE);

        // Menus
        MenuButton aboutUs = new MenuButton("About Us");
        aboutUs.setStyle("-fx-background-color: transparent");
        aboutUs.getItems().addAll(new MenuItem("Vision"), new MenuItem("Team"));

        MenuButton hopelifeFamily = new MenuButton("Hopelife Family");
        hopelifeFamily.setStyle("-fx-background-color: transparent");
        hopelifeFamily.getItems().addAll(new MenuItem("Doctors"), new MenuItem("Staff"));

        MenuButton services = new MenuButton("Services");
        services.setStyle("-fx-background-color: transparent");
        services.getItems().addAll(new MenuItem("Lab"), new MenuItem("Emergency"));

        Button feedback = new Button("Feedback");
        feedback.setStyle("-fx-background-color: transparent");
        Button eShifa = new Button("eHopeLife");
        eShifa.setStyle("-fx-background-color: transparent");
        MenuButton onlineServices = new MenuButton("Online Services");
        onlineServices.setStyle("-fx-background-color: transparent");
        onlineServices.getItems().addAll(new MenuItem("Reports"), new MenuItem("Appointments"));

        Button careers = new Button("Careers");
        careers.setStyle("-fx-background-color: transparent");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Login/Signup Button
        Button loginSignupButton = new Button("Login/Signup");
        loginSignupButton.setStyle("-fx-background-color: white; -fx-text-fill: #009ddc; -fx-font-size: 14px;");

        loginSignupButton.setOnAction(e -> {
            Login_Signup loginSignup = new Login_Signup();
            loginSignup.start(primaryStage);
        });

        navBar.getChildren().addAll(
                logo, aboutUs, hopelifeFamily, services, feedback, eShifa, onlineServices, careers,
                spacer, loginSignupButton
        );

        return navBar;
    }


    /**
     * Creates the bottom services bar with buttons for various services.
     *
     * @return A HBox containing the services bar.
     */
    private HBox createServicesBar() {
        HBox servicesBar = new HBox(30);
        servicesBar.setPadding(new Insets(20));
        servicesBar.setAlignment(Pos.CENTER);
        servicesBar.setStyle("-fx-background-color: #e21b1b;");

        Button organTransplant = createServiceButton("🫁 Organ Transplant Services");
        Button findDoctor = createServiceButton("🔍 Find A Doctor");
        Button intlPatients = createServiceButton("🌍 International Patients");
        Button reports = createServiceButton("📋 Online Reports");

        servicesBar.getChildren().addAll(organTransplant, findDoctor, intlPatients, reports);
        return servicesBar;
    }

    /**
     * Creates a button for the services bar.
     *
     * @param text The text to display on the button.
     * @return A Button with the specified text and style.
     */
    private Button createServiceButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: white; -fx-text-fill: #e21b1b; -fx-font-size: 14px;");
        btn.setPrefWidth(220);
        btn.setPrefHeight(50);

        // Sample event
        btn.setOnAction(e -> {
            System.out.println(text + " clicked!");
            // add scene switching logic here
        });

        return btn;
    }

    /**
     * Creates the center banner section with a slideshow of images.
     *
     * @return A StackPane containing the banner section.
     */
    private StackPane createBannerSection() {
        StackPane bannerPane = new StackPane();

        // === Slideshow Images ===
        ImageView background = new ImageView();
        background.setFitWidth(1080);
        background.setFitHeight(400); 
        background.setPreserveRatio(false);


        List<Image> slideshowImages = List.of(
                new Image(getClass().getResource("/com/example/project/images (1).jpg").toExternalForm()),
                new Image(getClass().getResource("/com/example/project/images (2).jpg").toExternalForm()),
                new Image(getClass().getResource("/com/example/project/images (3).jpg").toExternalForm()),
                new Image(getClass().getResource("/com/example/project/image.jpg").toExternalForm())
        );



        // Index tracker
        final int[] index = {0};
        background.setImage(slideshowImages.get(index[0]));


        // === Timeline to switch images every 5 seconds ===
        Timeline slideshow = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    index[0] = (index[0] + 1) % slideshowImages.size();
                    background.setImage(slideshowImages.get(index[0]));
                })
        );
        slideshow.setCycleCount(Animation.INDEFINITE);
        slideshow.play();

        bannerPane.getChildren().addAll(background);
        return bannerPane;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
