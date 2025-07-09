package com.example.project.DashBoard;

import com.example.project.DATABASE.DBConnection;
import com.example.project.DATABASE.DataFetcher;
import com.example.project.Doctor_Patient_Interaction.*;
import com.example.project.Doctor_Patient_Interaction.Feedback;
import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.EmergencyAlertSystem.EmailSender;
import com.example.project.Login_Home.Login_Signup;
import com.example.project.User_Management.Doctor;
import com.example.project.User_Management.Patient;
import com.example.project.Health_Data_Handling.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import static com.example.project.DATABASE.DataFetcher.getDoctorData;


// Doctor Dashboard extending Application Class for the GUI and implementing the Interface for the runtime polymorphism
public class DoctorDashboard extends Application implements DashBoards{

    private VBox mainContent;
    private Doctor doctor = new Doctor();
    private VitalsDatabase vitalsDatabase = new VitalsDatabase();
    private VBox messagesContainer = new VBox(10);
    private Patient selectedPatient;



    // THIS IS COMPILE TIME POLYMORPHISM (OVERLOADING)
    @Override
    public void start(Stage stage) throws Exception {

    }

    /**
     * Starts the doctor dashboard with the specified doctor ID.
     *
     * @param stage   The primary stage for this application.
     * @param doctorID The unique ID of the doctor.
     */
    @Override
    public void start(Stage stage, String doctorID) {

        initializeDoctorData(doctorID);

        BorderPane root = new BorderPane();

        VBox sidebar = buildSidebar();
        HBox topBar = buildTopBar(stage);
        mainContent = buildDashboard();

        mainContent.setStyle("-fx-background-color: #FAFAFA;");

        ScrollPane sideBarScrollPane = new ScrollPane(sidebar);
        sideBarScrollPane.setFitToWidth(true);
        sideBarScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sideBarScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sideBarScrollPane.setStyle("-fx-background: #f9fafb;");

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9fafb;");

        root.setLeft(sidebar);
        root.setTop(topBar);
        root.setCenter(scrollPane);
        root.setLeft(sideBarScrollPane);

        Scene scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.initStyle(StageStyle.DECORATED);  

        stage.setTitle("Doctor Dashboard");
        stage.show();

    }

    /**
     * Initializes the doctor data for the dashboard.
     *
     * @param doctorID The unique ID of the doctor.
     */
    private void initializeDoctorData(String doctorID) {
        doctor = getDoctorData(doctorID);
        loadAssignedPatientsView();
        loadFeedbacksView();
        loadVitalSignsView();
        if (doctor != null) {
            System.out.println("Doctor data loaded successfully for: " + doctor.getName());
        } else {
            System.out.println("Failed to load doctor data.");
        }
    }
    /**
     * Loads the assigned patients for the doctor.
     */
    private void loadAssignedPatientsView() {
        List<Patient> assignedPatients = DataFetcher.getAssignedPatients(doctor.getUserID());
        doctor.setAssignedPatients(assignedPatients);
        loadAppointmentsView();
    }

    /**
     * Loads the appointments for the doctor.
     */
    private void loadAppointmentsView() {
        List<Appointment> appointments = DataFetcher.getDoctorAppointments(doctor.getUserID());
        doctor.setAppointments(appointments);
    }

    /**
     * Loads the feedbacks for the doctor.
     */
    private void loadFeedbacksView() {
        List<Feedback> feedbacks = DataFetcher.getFeedbacks(doctor.getUserID());
        doctor.setFeedbackList(feedbacks);
    }

    /**
     * Loads the vital signs for the doctor.
     */
    private void loadVitalSignsView() {
        List<VitalSign> vitalSigns = DataFetcher.getVitalSignsByDoctor(doctor.getUserID());
        doctor.setVitalSigns(vitalSigns);
        for(VitalSign vitalSign : vitalSigns) {
            vitalsDatabase.addVitalSign(vitalSign);
        }
    }

    /**
     * Builds the sidebar for the dashboard.
     *
     * @return A VBox containing the sidebar elements.
     */
    @Override
    public VBox buildSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #F4F6FA;");
        sidebar.setPrefWidth(200);

        Label title = new Label("HopeLife Hospital");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button dashboardBtn = createSidebarButton("📊", "Dashboard");
        Button patientsBtn = createSidebarButton("👥", "Assigned Patients");
        Button appointmentsBtn = createSidebarButton("📅", "Appointments");
        Button detailsBtn = createSidebarButton("📝", "Details");
        Button messagesBtn = createSidebarButton("💬", "Messages");
        Button viewFeedbacksBtn = createSidebarButton("🗨️", "Feedbacks");
        Button createPrescriptionBtn = createSidebarButton("📝", "Prescriptions");
        Button viewVitalsBtn = createSidebarButton("📊", "Vitals");
        Button viewMedicalHistoryBtn = createSidebarButton("📜", "Medical History");
        Button sendMailBtn = createSidebarButton("✉️", "Send Mail");


        dashboardBtn.setOnAction(e -> setContent(buildDashboard()));
        patientsBtn.setOnAction(e -> setContent((VBox) buildAssignedPatientsView()));
        appointmentsBtn.setOnAction(e -> setContent(buildAppointmentsView()));
        viewFeedbacksBtn.setOnAction( e -> setContent(buildFeedbacksView()));
        createPrescriptionBtn.setOnAction( e -> setContent(buildAddPrescriptionView()));
        viewVitalsBtn.setOnAction(e -> setContent(buildViewVitals()));
        messagesBtn.setOnAction(e -> setContent(buildMessagesView()));
        viewMedicalHistoryBtn.setOnAction(e -> setContent(buildMedicalHistoryView()));
        sendMailBtn.setOnAction(e -> setContent(buildSendEmail()));
        detailsBtn.setOnAction(e -> setContent(buildViewDetails()));
        sidebar.getChildren().addAll(
                title,
                dashboardBtn,
                patientsBtn,
                appointmentsBtn,
                detailsBtn,
                messagesBtn,
                viewFeedbacksBtn,
                createPrescriptionBtn,
                viewVitalsBtn,
                viewMedicalHistoryBtn,
                sendMailBtn
        );

        return sidebar;
    }

    /**
     * Sets the main content of the dashboard.
     *
     * @param content The new content to display.
     */
    private Button createSidebarButton(String icon, String text) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007BFF;"); 

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50;"); 

        HBox content = new HBox(10, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(content);
        button.setPrefWidth(180);
        button.setStyle("-fx-background-color: transparent;");

        button.setOnMouseEntered(e -> {
            iconLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007BFF;");
            textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50;");
            button.setStyle("-fx-background-color: #E6F0FF;");
        });

        button.setOnMouseExited(e -> {
            iconLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #007BFF;");
            textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2C3E50;");
            button.setStyle("-fx-background-color: transparent;");
        });

        return button;
    }

    /**
     * Builds the top bar for the dashboard.
     *
     * @param stage The primary stage for this application.
     * @return A HBox containing the top bar elements.
     */
    @Override
    public HBox buildTopBar(Stage stage) {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0;");

        Button notificationButton = new Button("🔔");
        Button logout = new Button("🚪 Logout");

        logout.setOnAction(e-> {
            Login_Signup loginSignup = new Login_Signup();
            loginSignup.start(stage);
        });

        notificationButton.setOnAction(event -> {
            List<VBox> notifications = PatientDashboard.notifications;
            if (!notifications.isEmpty()) {
                VBox notificationContent = notifications.get(0);

                javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
                dialog.setTitle("Notification");
                dialog.getDialogPane().setContent(notificationContent);
                dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.OK);
                dialog.showAndWait();

                
            } else {
                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "No new notifications."
                );
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });


        topBar.getChildren().addAll(notificationButton, logout);
        return topBar;
    }

    /**
     * Sets the main content of the dashboard.
     *
     * @param content The new content to display.
     */
    @Override
    public VBox buildDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(20));
        dashboard.setStyle("-fx-background-color: #f9fafb;");
        System.out.println("Dashboard loaded");

        HBox headerSection = new HBox(20);
        headerSection.setAlignment(Pos.TOP_LEFT);

        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/project/images.jpeg")).toExternalForm());
        ImageView profileImage = new ImageView(image);

        profileImage.setFitWidth(80);
        profileImage.setFitHeight(80);
        profileImage.setStyle("-fx-background-radius: 50%; -fx-border-radius: 50%;");

        VBox doctorInfo = new VBox(5);
        doctorInfo.setAlignment(Pos.TOP_LEFT);

        Label name = new Label(doctor.getName());
        name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label genderAge = new Label(doctor.getGender() + ", " + doctor.getAge() + " years");
        genderAge.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");

        HBox contactInfo = new HBox(40);
        Label email = new Label(doctor.getEmail());
        Label phone = new Label(doctor.getPhoneNumber());
        email.setStyle("-fx-text-fill: #4b5563;");
        phone.setStyle("-fx-text-fill: #4b5563;");
        contactInfo.getChildren().addAll(email, phone);

        doctorInfo.getChildren().addAll(name, genderAge, contactInfo);
        headerSection.getChildren().addAll(profileImage, doctorInfo);

        GridPane availabilityCard = new GridPane();
        availabilityCard.setVgap(10);
        availabilityCard.setHgap(30);
        availabilityCard.setPadding(new Insets(15));
        availabilityCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        availabilityCard.getColumnConstraints().addAll(col1, col2);

        Label availabilityTitle = new Label("Availability");
        availabilityTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        availabilityCard.add(availabilityTitle, 0, 0, 2, 1);

        Label daysLabel = new Label("Available Days:");
        daysLabel.setStyle("-fx-font-weight: bold;");
        StringBuilder days = new StringBuilder();
        for(String day : doctor.getAvailableDays()) {
            days.append(day).append(", ");
        }
        if (days.length() > 0) {
            days.setLength(days.length() - 2);
        }
        System.out.println("Available Days " + days.toString());
        Label daysValue = new Label(days.toString());



        availabilityCard.add(daysLabel, 0, 1);
        availabilityCard.add(daysValue, 1, 1);

        Label startTimeLabel = new Label("Start Time:");
        startTimeLabel.setStyle("-fx-font-weight: bold;");
        Label startTimeValue = new Label(doctor.getStartTime());
        availabilityCard.add(startTimeLabel, 0, 2);
        availabilityCard.add(startTimeValue, 1, 2);

        Label endTimeLabel = new Label("End Time:");
        endTimeLabel.setStyle("-fx-font-weight: bold;");
        Label endTimeValue = new Label(doctor.getEndTime());
        availabilityCard.add(endTimeLabel, 0, 3);
        availabilityCard.add(endTimeValue, 1, 3);


        availabilityCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        GridPane leftInfoGrid = new GridPane();
        leftInfoGrid.setVgap(12);
        leftInfoGrid.setHgap(20);
        leftInfoGrid.setPadding(new Insets(15));
        leftInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        leftInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        addInfoRow(leftInfoGrid, 0, "User ID", doctor.getUserID());
        addInfoRow(leftInfoGrid, 1, "Address", doctor.getAddress());
        addInfoRow(leftInfoGrid, 2, "Account Status", "Active");


        GridPane rightInfoGrid = new GridPane();
        rightInfoGrid.setVgap(12);
        rightInfoGrid.setHgap(20);
        rightInfoGrid.setPadding(new Insets(15));
        rightInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        rightInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");


        addInfoRow(rightInfoGrid, 0, "Specialization", doctor.getSpecialization().toString());
        addInfoRow(rightInfoGrid, 1, "License Number", doctor.getLicenseNumber());
        addInfoRow(rightInfoGrid, 2, "Hospital Name", "Hope Life Hospital");


        GridPane bottomInfoGrid = new GridPane();
        bottomInfoGrid.setVgap(12);
        bottomInfoGrid.setHgap(40);
        bottomInfoGrid.setPadding(new Insets(15));
        bottomInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        bottomInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        addInfoRow(bottomInfoGrid, 0, "Consultation Fee", "$" + doctor.getConsultationFee());
        addInfoRow(bottomInfoGrid, 1, "Experience Years", doctor.getExperienceYears() + " Years");



        GridPane infoGrid = new GridPane();
        infoGrid.setVgap(12);
        infoGrid.setHgap(30);
        infoGrid.setPadding(new Insets(10, 0, 0, 0));

        int row = 0;
        addInfoRow(infoGrid, row++, "User ID", doctor.getUserID());
        addInfoRow(infoGrid, row++, "Address", doctor.getAddress());
        addInfoRow(infoGrid, row++, "Account Status", "Active");
        addInfoRow(infoGrid, row++, "Specialization", doctor.getSpecialization().toString());
        addInfoRow(infoGrid, row++, "License Number", doctor.getLicenseNumber());
        addInfoRow(infoGrid, row++, "Hospital Name", "Hope Life Hospital");
        addInfoRow(infoGrid, row++, "Available Time", "10:00 AM - 4:00 PM");
        addInfoRow(infoGrid, row++, "Experience Years", doctor.getExperienceYears() + " Years");
        addInfoRow(infoGrid, row++, "Consultation Fee", "$ " + doctor.getConsultationFee());

        HBox infoHBox = new HBox(20, leftInfoGrid, rightInfoGrid);
        VBox fullContent = new VBox(20, headerSection, availabilityCard, infoHBox, bottomInfoGrid);
        dashboard.getChildren().add(fullContent);
        return dashboard;
    }



    /**
     * Sets the main content of the dashboard.
     *
     * @param content The new content to display.
     */
    private void addInfoRow(GridPane grid, int rowIndex, String label, String value) {
        Label title = new Label(label + " :");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        grid.add(title, 0, rowIndex);
        grid.add(val, 1, rowIndex);
    }

    private Node buildAssignedPatientsView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label title = new Label("Assigned Patients");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label subtitle = new Label("List of patients assigned to you");
        subtitle.setStyle("-fx-text-fill: #6b7280;");

        TableView<Patient> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Patient, String> nameCol = new TableColumn<>("Patient");
        nameCol.setCellValueFactory(data -> {
            String name = data.getValue().getName();
            String disease = data.getValue().getDiseases().isEmpty() ? "No disease" : data.getValue().getDiseases().get(0).name().replace("_", " ");
            return new SimpleStringProperty(name + "\n" + disease);
        });

        TableColumn<Patient, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserID()));

        TableColumn<Patient, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Patient, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));

        TableColumn<Patient, String> statusCol = new TableColumn<>("Status");
        System.out.println("Status: " + doctor.getAssignedPatients().get(0).isAccountStatus());
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isAccountStatus() ? "Active" : "Inactive"));
        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: " +
                            (status.equalsIgnoreCase("Active") ? "#34d399" : "#f87171") +
                            "; -fx-background-radius: 10px; -fx-padding: 5px;");
                }
            }
        });

        table.getColumns().addAll(nameCol, idCol, emailCol, phoneCol, statusCol);

        table.setItems(FXCollections.observableArrayList(doctor.getAssignedPatients()));

        container.getChildren().addAll(title, subtitle, table);
        return container;
    }

    /**
     * Builds the appointments view for the doctor.
     *
     * @return A VBox containing the appointments view elements.
     */
    private VBox buildAppointmentsView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label heading = new Label("Appointments");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        TableColumn<Appointment, String> appointmentIDCol = new TableColumn<>("Appointment ID");
        appointmentIDCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentID()));

        TableColumn<Appointment, String> timeCol = new TableColumn<>("Date/Time");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAppointmentDateTime().format(formatter)
        ));

        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient ID");
        patientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatient()));

        TableColumn<Appointment, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReason()));

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentStatus().toString()));

        TableColumn<Appointment, Void> actionCol = new TableColumn<>("Change Status");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Set Status");

            {
                btn.setOnAction(event -> {
                    Appointment appt = getTableView().getItems().get(getIndex());
                    List<String> choices = List.of("SCHEDULED", "CANCELED", "PENDING", "RESCHEDULED", "COMPLETED");
                    ChoiceDialog<String> dialog = new ChoiceDialog<>(appt.getAppointmentStatus().toString(), choices);
                    dialog.setTitle("Change Status");
                    dialog.setHeaderText("Change Appointment Status");
                    dialog.setContentText("Choose new status:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(newStatus -> {
                        appt.setAppointmentStatus(Appointment.AppointmentStatus.valueOf(newStatus));
                        appointmentTable.refresh();  
                    
                        DataFetcher.updateAppointmentStatusInDB(appt.getAppointmentID(), newStatus);
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
        appointmentTable.getColumns().addAll(appointmentIDCol, timeCol, patientCol, reasonCol, statusCol, actionCol);
        appointmentTable.setItems(FXCollections.observableArrayList(doctor.getAppointments()));
        layout.getChildren().addAll(heading, appointmentTable);
        return layout;
    }

    /**
     * Builds the feedbacks view for the doctor.
     *
     * @return A VBox containing the feedbacks view elements.
     */
    private VBox buildFeedbacksView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label heading = new Label("Feedbacks");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Feedback> feedbackTable = new TableView<>();
        feedbackTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        TableColumn<Feedback, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFeedbackDate().format(formatter)
        ));

        TableColumn<Feedback, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(data -> {
            Feedback feedback = data.getValue();
            String patientName = feedback.isAnonymous() ? "Anonymous" : feedback.getPatientID();
            return new SimpleStringProperty(patientName);
        });

        TableColumn<Feedback, Integer> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<Feedback, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().toString()));

        TableColumn<Feedback, String> idCol = new TableColumn<>("Feedback ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("feedbackID"));

        TableColumn<Feedback, Void> actionCol = new TableColumn<>("Change Status");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Set Status");

            {
                btn.setOnAction(event -> {
                    Feedback feedback = getTableView().getItems().get(getIndex());
                    List<String> choices = List.of("PENDING", "APPROVED", "REJECTED");
                    ChoiceDialog<String> dialog = new ChoiceDialog<>(feedback.getStatus().toString(), choices);
                    dialog.setTitle("Change Feedback Status");
                    dialog.setHeaderText("Change Status for Feedback ID: " + feedback.getFeedbackID());
                    dialog.setContentText("Select new status:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(newStatus -> {
                        feedback.setStatus(Enum.valueOf((Class<Feedback.FeedbackStatus>) feedback.getStatus().getClass(), newStatus));
                        feedbackTable.refresh();
                        DataFetcher.updateFeedbackStatusInDB(feedback.getFeedbackID(), newStatus);
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        feedbackTable.getColumns().addAll(dateCol, patientCol, ratingCol, statusCol, idCol, actionCol);

        feedbackTable.setItems(FXCollections.observableArrayList(doctor.getFeedbackList()));

        GridPane detailsPane = new GridPane();
        detailsPane.setVgap(10);
        detailsPane.setHgap(20);
        detailsPane.setPadding(new Insets(15));
        detailsPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb;");

        feedbackTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                detailsPane.getChildren().clear();
                addInfoRow(detailsPane, 0, "Feedback ID", newVal.getFeedbackID());
                addInfoRow(detailsPane, 1, "Patient ID", newVal.isAnonymous() ? "Anonymous" : newVal.getPatientID());
                addInfoRow(detailsPane, 2, "Doctor ID", newVal.getDoctorID());
                addInfoRow(detailsPane, 3, "Date", newVal.getFeedbackDate().format(formatter));
                addInfoRow(detailsPane, 4, "Rating", String.valueOf(newVal.getRating()));
                addInfoRow(detailsPane, 5, "Comments", newVal.getComments());
                addInfoRow(detailsPane, 6, "Status", newVal.getStatus().toString());
            }
        });

        layout.getChildren().addAll(heading, feedbackTable, new Label("Details:"), detailsPane);
        return layout;
    }



    /** 
     * Builds the view for adding a new prescription.
     *
     * @return A VBox containing the new prescription form elements.
     */
    private VBox buildAddPrescriptionView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label heading = new Label("Add New Prescription");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Patient ID");

        TextField dosageInstructionsField = new TextField();
        dosageInstructionsField.setPromptText("Dosage Instructions");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        TextField dosageScheduleField = new TextField();
        dosageScheduleField.setPromptText("Dosage Schedule");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Button submitButton = new Button("Submit Prescription");
        submitButton.setOnAction(e -> {
            String patientID = patientIdField.getText();
            String doctorID = doctor.getUserID();
            String dosageInstructions = dosageInstructionsField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String dosageSchedule = dosageScheduleField.getText();
            String quantity = quantityField.getText();

            addPrescriptionToDatabase(patientID, doctorID, dosageInstructions, startDate, endDate, dosageSchedule, quantity);
        });

        layout.getChildren().addAll(
                heading,
                new Label("Patient ID:"), patientIdField,
                new Label("Dosage Instructions:"), dosageInstructionsField,
                new Label("Start Date:"), startDatePicker,
                new Label("End Date:"), endDatePicker,
                new Label("Dosage Schedule:"), dosageScheduleField,
                new Label("Quantity:"), quantityField,
                submitButton
        );

        return layout;
    }


    /**
     * Adds a new prescription to the database.
     *
     * @param patientID          The ID of the patient.
     * @param doctorID           The ID of the doctor.
     * @param dosageInstructions The dosage instructions for the prescription.
     * @param startDate          The start date of the prescription.
     * @param endDate            The end date of the prescription.
     * @param dosageSchedule     The dosage schedule for the prescription.
     * @param quantity           The quantity of the medication.
     */
    private void addPrescriptionToDatabase(String patientID, String doctorID, String dosageInstructions,
                                           LocalDate startDate, LocalDate endDate, String dosageSchedule, String quantity) {
        String prescriptionID = UUID.randomUUID().toString();  // generates unique ID
        String sql = "INSERT INTO Prescriptions (prescriptionID, patientID, doctorID, dosageInstructions, startDate, endDate, dosageSchedule, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prescriptionID);
            stmt.setString(2, patientID);
            stmt.setString(3, doctorID);
            stmt.setString(4, dosageInstructions);
            stmt.setTimestamp(5, Timestamp.valueOf(startDate.atStartOfDay()));
            stmt.setTimestamp(6, Timestamp.valueOf(endDate.atStartOfDay()));
            stmt.setString(7, dosageSchedule);
            stmt.setString(8, quantity);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Prescription added successfully!");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Builds the view for displaying vital signs.
     *
     * @return A VBox containing the vital signs view elements.
     */
    private VBox buildViewVitals() {
        TableView<VitalSign> tableView = new TableView<>();

        // Columns
        TableColumn<VitalSign, String> userIdCol = new TableColumn<>("Patient ID");
        userIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserID()));

        TableColumn<VitalSign, String> dateCol = new TableColumn<>("Date Created");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getRecordDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));

        TableColumn<VitalSign, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("View");

            {
                viewButton.setOnAction(event -> {
                    VitalSign selectedVital = getTableView().getItems().get(getIndex());
                    showVitalDetailsPopup(selectedVital);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        });

        tableView.getColumns().addAll(userIdCol, dateCol, actionCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setItems(FXCollections.observableArrayList(doctor.getVitalSigns()));

        // Layout
        VBox vbox = new VBox(tableView);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        return vbox;
    }

    /**
     * Displays a popup with detailed information about a selected vital sign.
     *
     * @param vital The selected vital sign.
     */
    private void showVitalDetailsPopup(VitalSign vital) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Vital Sign Details");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(20);

        grid.add(new Label("Patient ID:"), 0, 0);
        grid.add(new Label(vital.getUserID()), 1, 0);

        grid.add(new Label("Heart Rate:"), 0, 1);
        grid.add(new Label(vital.getHeartRate() + " bpm"), 1, 1);

        grid.add(new Label("Oxygen Level:"), 0, 2);
        grid.add(new Label(vital.getOxygenLevel() + " %"), 1, 2);

        grid.add(new Label("Blood Pressure:"), 0, 3);
        grid.add(new Label(vital.getBloodPressure() + " mmHg"), 1, 3);

        grid.add(new Label("Temperature:"), 0, 4);
        grid.add(new Label(vital.getTemperature() + " °C"), 1, 4);

        grid.add(new Label("Respiratory Rate:"), 0, 5);
        grid.add(new Label(vital.getRespiratoryRate() + " breaths/min"), 1, 5);

        grid.add(new Label("Glucose Level:"), 0, 6);
        grid.add(new Label(vital.getGlucoseLevel() + " mg/dL"), 1, 6);

        grid.add(new Label("Cholesterol Level:"), 0, 7);
        grid.add(new Label(vital.getCholesterolLevel() + " mg/dL"), 1, 7);

        grid.add(new Label("BMI:"), 0, 8);
        grid.add(new Label(vital.getBmi() + " kg/m²"), 1, 8);

        grid.add(new Label("Hydration Level:"), 0, 9);
        grid.add(new Label(vital.getHydrationLevel() + " %"), 1, 9);

        grid.add(new Label("Stress Level:"), 0, 10);
        grid.add(new Label(vital.getStressLevel() + " /10"), 1, 10);

        Button downloadButton = new Button("Download as CSV");
        downloadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Vital as CSV");
            fileChooser.setInitialFileName("vital.csv");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            File file = fileChooser.showSaveDialog(popup);
            if (file != null) {
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new File(file.getAbsolutePath() + ".csv");
                }
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.println("Patient ID,Heart Rate,Oxygen Level,Blood Pressure,Temperature,Respiratory Rate,Glucose Level,Cholesterol Level,BMI,Hydration Level,Stress Level");
                    writer.println(vital.getUserID() + "," + vital.getHeartRate() + "," + vital.getOxygenLevel() + "," +
                            vital.getBloodPressure() + "," + vital.getTemperature() + "," + vital.getRespiratoryRate() + "," +
                            vital.getGlucoseLevel() + "," + vital.getCholesterolLevel() + "," + vital.getBmi() + "," +
                            vital.getHydrationLevel() + "," + vital.getStressLevel());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Vital sign details downloaded successfully!");
                    alert.showAndWait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        downloadButton.setAlignment(Pos.CENTER);

        VBox box = new VBox(10);
        box.getChildren().addAll(grid, downloadButton);



        Scene scene = new Scene(box, 400, 500);
        popup.setScene(scene);
        popup.showAndWait();
    }
    /**
     * Builds the messages view for the doctor.
     *
     * @return A VBox containing the messages view elements.
     */
    private VBox buildMessagesView() {
        VBox mainContainer = new VBox(10);
        mainContainer.setStyle("-fx-padding: 10px;");

        VBox conversationList = new VBox(10);
        conversationList.setStyle("-fx-background-color: #f1f1f1; -fx-padding: 10px;");

        Button openVideoCallButton = new Button("Start Video Call");


        loadAssignedPatients(conversationList);

        VBox chatView = new VBox(15);
        chatView.setStyle("-fx-padding: 10px;");
        ScrollPane scrollPane = new ScrollPane();
        VBox messagesContainer = new VBox(10);
        messagesContainer.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        scrollPane.setContent(messagesContainer);
        chatView.getChildren().add(scrollPane);

        HBox inputArea = new HBox(10);
        inputArea.setStyle("-fx-padding: 10px; -fx-background-color: #f1f1f1;");
        TextField messageInput = new TextField();
        messageInput.setPromptText("Type a message...");
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #25d366; -fx-text-fill: white;");
        sendButton.setOnAction(event -> {
            if (selectedPatient != null) {
                sendMessage(doctor, messageInput.getText(), selectedPatient);  
                messageInput.clear();  
                List<ChatMessage> updatedMessages = getChatMessagesForPatient(selectedPatient);
                updateChatUI(updatedMessages, selectedPatient);  
            } else {
                System.out.println("No patient selected");
            }
        });

        openVideoCallButton.setOnAction(e-> {
            openVideoCallScene();
        });

        inputArea.getChildren().addAll(openVideoCallButton , messageInput, sendButton);
        chatView.getChildren().add(inputArea);
        mainContainer.getChildren().addAll(conversationList, chatView);

        return mainContainer;
    }


    /**
     * Opens a new scene for starting a video call with a selected patient.
     */
    private void openVideoCallScene() {
        Stage stage = new Stage();
        stage.setTitle("Start Video Call");

        ComboBox<Patient> patientComboBox = new ComboBox<>();
        patientComboBox.setPromptText("Select Patient");
        List<Patient> patients = doctor.getAssignedPatients();
        patientComboBox.getItems().addAll(patients);
        TextField videoCallLinkField = new TextField();
        videoCallLinkField.setPromptText("Paste Video Call Link");

        Button startVideoCallBtn = new Button("Start Video Call");
        Button sendLinkButton = new Button("Send");
        sendLinkButton.setDisable(true);

        videoCallLinkField.textProperty().addListener((obs, oldV, newV) ->
                sendLinkButton.setDisable(patientComboBox.getValue() == null || newV.trim().isEmpty()));
        patientComboBox.valueProperty().addListener((obs, oldV, newV) ->
                sendLinkButton.setDisable(newV == null || videoCallLinkField.getText().trim().isEmpty()));

        startVideoCallBtn.setOnAction(event -> {
            String link = videoCallLinkField.getText();
            if (!link.trim().isEmpty()) {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(link));
                } catch (Exception ex) {
                    // Handle exception
                }
            }
        });

        sendLinkButton.setOnAction(event -> {
            Patient selectedPatient = patientComboBox.getValue();
            String link = videoCallLinkField.getText();
            if (selectedPatient != null && !link.trim().isEmpty()) {
                sendMessage(doctor, link, selectedPatient);

                Alert sentAlert = new Alert(Alert.AlertType.INFORMATION, "Video call link sent to " + selectedPatient.getName());
                sentAlert.showAndWait();

                videoCallLinkField.clear();
            }
        });

        VBox layout = new VBox(10,
                new Label("Start Video Call with Patient"),
                patientComboBox,
                videoCallLinkField,
                new HBox(10, startVideoCallBtn, sendLinkButton)
        );
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 200));
        stage.show();
    }


    /**
     * Loads the assigned patients into the conversation list.
     *
     * @param conversationList The VBox to load the patients into.
     */
    public void loadAssignedPatients(VBox conversationList) {
        conversationList.getChildren().clear();
        List<Patient> patients = doctor.getAssignedPatients(); 

        for (Patient patient : patients) {
            HBox patientRow = new HBox(10);
            patientRow.setAlignment(Pos.CENTER_LEFT);

            // Patient name label
            Label patientNameLabel = new Label(patient.getName());
            patientNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

            // Open Chat button
            Button openChatButton = new Button("Open Chat");
            openChatButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px;");
            openChatButton.setOnAction(event -> {
                selectedPatient = patient; 
                openChatWindow(patient); 
            });

            patientRow.getChildren().addAll(patientNameLabel, openChatButton);
            conversationList.getChildren().add(patientRow);
        }
    }

    /**
     * Opens a new chat window for the selected patient.
     *
     * @param patient The selected patient.
     */
    private void openChatWindow(Patient patient) {
        List<ChatMessage> messages = getChatMessagesForPatient(patient);

        // Create a new window (Stage)
        Stage chatStage = new Stage();
        chatStage.setTitle("Chat with " + patient.getName());

        // Root layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f9f9f9;");

        ScrollPane scrollPane = new ScrollPane();
        VBox messagesContainer = new VBox(10);
        scrollPane.setContent(messagesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        if (messages != null && !messages.isEmpty()) {
            for (ChatMessage message : messages) {
                HBox messageBubble = new HBox(10);
                Label messageLabel = new Label(message.getMessageText());
                messageLabel.setWrapText(true);
                messageLabel.setMaxWidth(250); // Max width for better readability

                if (message.getSenderId().equals(doctor.getUserID())) {
                    messageLabel.setStyle("-fx-background-color: #d0e6ff; -fx-background-radius: 15px; -fx-padding: 10px; -fx-text-fill: #000000;");
                    messageBubble.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    messageLabel.setStyle("-fx-background-color: #dcf8c6; -fx-background-radius: 15px; -fx-padding: 10px; -fx-text-fill: #000000;");
                    messageBubble.setAlignment(Pos.CENTER_LEFT);
                }

                messageBubble.getChildren().add(messageLabel);
                messagesContainer.getChildren().add(messageBubble);
            }
        } else {
            Label noMessagesLabel = new Label("No chat history available.");
            noMessagesLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");
            messagesContainer.getChildren().add(noMessagesLabel);
        }

        root.getChildren().add(scrollPane);

        Scene scene = new Scene(root, 400, 500); 
        chatStage.setScene(scene);
        chatStage.initModality(Modality.APPLICATION_MODAL); 
        chatStage.show();
    }



    /**
     * Updates the chat UI with the latest messages.
     *
     * @param messages The list of chat messages to display.
     * @param patient  The selected patient.
     */
    private void updateChatUI(List<ChatMessage> messages, Patient patient) {
        messagesContainer.getChildren().clear(); 

        for (ChatMessage message : messages) {
            HBox messageBubble = new HBox(10);
            Label messageLabel = new Label(message.getMessageText());
            if (message.getSenderId().equals(doctor.getUserID())) {
                messageLabel.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 10px; -fx-padding: 10px;");
                messageBubble.setAlignment(Pos.CENTER_RIGHT);
            } else {
                messageLabel.setStyle("-fx-background-color: #d1ffd6; -fx-background-radius: 10px; -fx-padding: 10px;");
                messageBubble.setAlignment(Pos.CENTER_LEFT);
            }
            messageBubble.getChildren().add(messageLabel);
            messagesContainer.getChildren().add(messageBubble);
        }
    }

    /**
     * Sends a message from the doctor to the patient.
     *
     * @param doctor      The doctor sending the message.
     * @param messageText The text of the message.
     * @param patient     The patient receiving the message.
     */
    public void sendMessage(Doctor doctor, String messageText, Patient patient) {
        if (messageText == null || messageText.trim().isEmpty()) {
            return; 
        }

        String query = "INSERT INTO ChatMessages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, doctor.getUserID());
            ps.setString(2, patient.getUserID());
            ps.setString(3, messageText);

            ps.executeUpdate();

            ChatMessage newMessage = new ChatMessage();
            newMessage.setSenderId(doctor.getUserID());
            newMessage.setReceiverId(patient.getUserID());
            newMessage.setMessageText(messageText);
            newMessage.setTimestamp(LocalDateTime.now());
            updateChatUI(List.of(newMessage), patient);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves chat messages between the doctor and the specified patient.
     *
     * @param patient The patient whose chat messages to retrieve.
     * @return A list of chat messages between the doctor and the patient.
     */
    public List<ChatMessage> getChatMessagesForPatient(Patient patient) {
        List<ChatMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM ChatMessages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, doctor.getUserID());
            ps.setString(2, patient.getUserID());
            ps.setString(3, patient.getUserID());
            ps.setString(4, doctor.getUserID());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatMessage message = new ChatMessage(
                        rs.getInt("id"),
                        rs.getString("sender_id"),
                        rs.getString("receiver_id"),
                        rs.getString("message_text"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getBoolean("is_read")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    /**
     * Represents a chat message.
     */
    public static class ChatMessage {
        private int id;
        private String senderId;
        private String receiverId;
        private String messageText;
        private LocalDateTime timestamp;
        private boolean isRead;

        public ChatMessage() {
        }

        public ChatMessage(int id, String senderId, String receiverId, String messageText, LocalDateTime timestamp, boolean isRead) {
            this.id = id;
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.messageText = messageText;
            this.timestamp = timestamp;
            this.isRead = isRead;
        }

        // Setters
        public void setId(int id) {
            this.id = id;
        }
        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }
        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }
        public void setMessageText(String messageText) {
            this.messageText = messageText;
        }
        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
        public void setRead(boolean read) {
            isRead = read;
        }
        // Getters
        public int getId() {
            return id;
        }
        public String getSenderId() {
            return senderId;
        }
        public String getReceiverId() {
            return receiverId;
        }
        public String getMessageText() {
            return messageText;
        }
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        public boolean isRead() {
            return isRead;
        }


    }

    /**
     * Builds the medical history view for the doctor.
     *
     * @return A VBox containing the medical history view elements.
     */
    private VBox buildMedicalHistoryView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label heading = new Label("All Assigned Patients' Medical History");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<MedicalHistory> historyTable = new TableView<>();
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MedicalHistory, String> idCol = new TableColumn<>("History ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMedicalHistoryID()));

        TableColumn<MedicalHistory, String> surgeriesCol = new TableColumn<>("Surgeries");
        surgeriesCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurgeries()));

        TableColumn<MedicalHistory, String> familyHistoryCol = new TableColumn<>("Family History");
        familyHistoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFamilyHistory()));

        TableColumn<MedicalHistory, String> createdDateCol = new TableColumn<>("Created Date");
        createdDateCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));

        TableColumn<MedicalHistory, String> patientNameCol = new TableColumn<>("Patient");
        patientNameCol.setCellValueFactory(data -> new SimpleStringProperty(doctor.getPatient(data.getValue().getPatient()).getName()));

        TableColumn<MedicalHistory, Void> actionCol = new TableColumn<>("Vital Trends");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("View Vital Trends");

            {
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    MedicalHistory history = getTableView().getItems().get(getIndex());
                    showVitalTrends(doctor.getPatient(history.getPatient()));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        historyTable.getColumns().addAll(idCol, patientNameCol, surgeriesCol, familyHistoryCol, createdDateCol, actionCol);

        List<MedicalHistory> allHistories = new ArrayList<>();
        for (Patient p : doctor.getAssignedPatients()) {
            List<MedicalHistory> patientHistories = p.getMedicalHistories(); 
            if (patientHistories != null) {
                for (MedicalHistory history : patientHistories) {
                    history.setPatient(p.getUserID()); 
                    allHistories.add(history);
                }
            }
        }


        historyTable.setItems(FXCollections.observableArrayList(allHistories));

        layout.getChildren().addAll(heading, historyTable);
        return layout;
    }

    /**
     * Displays the vital trends for a specific patient.
     *
     * @param patient The patient whose vital trends to display.
     */
    private void showVitalTrends(Patient patient) {
        Stage stage = new Stage();
        stage.setTitle("Vital Signs for " + patient.getName());

        VBox chartsContainer = new VBox(25);
        chartsContainer.setPadding(new Insets(20));


        List<VitalSign> vitals = vitalsDatabase.findPatientViatls(patient.getUserID());

        if (vitals != null && !vitals.isEmpty()) {
            chartsContainer.getChildren().addAll(
                    createVitalChart("Oxygen Level (%)", vitals, VitalSign::getOxygenLevel),
                    createVitalChart("Heart Rate (bpm)", vitals, VitalSign::getHeartRate),
                    createVitalChart("Blood Pressure (mmHg)", vitals, VitalSign::getBloodPressure),
                    createVitalChart("Temperature (°C)", vitals, VitalSign::getTemperature)
            );
        } else {
            chartsContainer.getChildren().add(new Label("No vital signs data available."));
        }

        ScrollPane scrollPane = new ScrollPane(chartsContainer);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 900, 700);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Creates a chart for displaying vital signs.
     *
     * @param title         The title of the chart.
     * @param vitals        The list of vital signs to display.
     * @param valueExtractor A function to extract the value from a VitalSign object.
     * @return A Node representing the chart and download button.
     */
    private Node createVitalChart(String title, List<VitalSign> vitals, Function<VitalSign, Number> valueExtractor) {
        VBox chartBox = new VBox(10);
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Date/Time");
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return Instant.ofEpochMilli(object.longValue())
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(title);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setPrefSize(800, 400);
        chart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (VitalSign v : vitals) {
            long epochMillis = v.getRecordDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            series.getData().add(new XYChart.Data<>(epochMillis, valueExtractor.apply(v)));
        }

        chart.getData().add(series);

        Button downloadBtn = new Button("Download Chart");
        downloadBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        downloadBtn.setOnAction(e -> {
            WritableImage image = chart.snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Chart");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    showAlert("Success", "Chart Saved",
                            "Chart saved to: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
                } catch (IOException ex) {
                    showAlert("Error", "Save Failed",
                            "Failed to save chart: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });

        chartBox.getChildren().addAll(chart, downloadBtn);
        return chartBox;
    }

    /**
     * Displays an alert dialog with the specified parameters.
     *
     * @param title   The title of the alert.
     * @param header  The header text of the alert.
     * @param content The content text of the alert.
     * @param type    The type of the alert (e.g., INFORMATION, ERROR).
     */
    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Builds the send email view for the doctor.
     *
     * @return A VBox containing the send email view elements.
     */
    private VBox buildSendEmail() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label heading = new Label("Send Email");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField receiverEmailField = new TextField();
        receiverEmailField.setPromptText("Receiver Email");


        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Your Password");

        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write your message here...");
        messageArea.setPrefHeight(200);

        Button sendButton = new Button("Send Email");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        try {
            sendButton.setOnAction(e -> {
                String receiverEmail = receiverEmailField.getText();
                String doctorEmail = doctor.getEmail();
                String password = passwordField.getText();
                String subject = subjectField.getText();
                String message = messageArea.getText();

                if (receiverEmail.isEmpty() || doctorEmail.isEmpty() || password.isEmpty() || subject.isEmpty() || message.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Email Sending Failed");
                    alert.setContentText("An error occurred while sending the email. Please try again.");
                    alert.showAndWait();
                } else {
                    sendEmail(receiverEmail, doctorEmail, password, subject, message);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Email Sent");
                    alert.setHeaderText("Email Sent Successfully");
                    alert.setContentText("Your email has been sent successfully.");
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }


        layout.getChildren().addAll(
                heading,
                new Label("Receiver Email:"), receiverEmailField,
                new Label("Your Password:"), passwordField,
                new Label("Subject:"), subjectField,
                new Label("Message:"), messageArea,
                sendButton
        );

        return layout;
    }

    /**
     * Sends an email using the provided parameters.
     *
     * @param receiverEmail The email address of the receiver.
     * @param doctorEmail   The email address of the doctor.
     * @param password      The password of the doctor's email account.
     * @param subject       The subject of the email.
     * @param message       The body of the email.
     */
    private void sendEmail(String receiverEmail, String doctorEmail, String password, String subject, String message) {
        System.out.println("Sending email to: " + receiverEmail);
        System.out.println("From: " + doctorEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        EmailSender.sendEmail(receiverEmail, doctorEmail, password, subject, message);
    }


    /**
     * Builds the view details section of the doctor dashboard.
     *
     * @return A VBox containing the view details elements.
     */
    private VBox buildViewDetails() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label heading = new Label("Doctor Dashboard - Feature Details");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        VBox featureDetails = new VBox(10);
        featureDetails.getChildren().addAll(
                createFeatureDetail("Dashboard", "View an overview of your profile, availability, and key metrics."),
                createFeatureDetail("Assigned Patients", "View and manage the list of patients assigned to you."),
                createFeatureDetail("Appointments", "Check and manage your scheduled appointments."),
                createFeatureDetail("Messages", "Communicate with patients via the messaging system."),
                createFeatureDetail("Feedbacks", "View feedback provided by patients."),
                createFeatureDetail("Prescriptions", "Create and manage prescriptions for patients."),
                createFeatureDetail("Vitals", "Monitor and review patients' vital signs."),
                createFeatureDetail("Medical History", "Access the medical history of your patients."),
                createFeatureDetail("Send Mail", "Send emails to patients or other staff members.")
        );

        HBox footer = new HBox(20);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10, 0, 0, 0));
        footer.setStyle("-fx-border-color: #e5e7eb; -fx-border-width: 1 0 0 0; -fx-padding: 10px 0 0 0;");

        Label contactLabel = new Label("For any queries, contact us:");
        contactLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label emailLabel = new Label("Email: support@hopelifehospital.com");
        emailLabel.setStyle("-fx-font-size: 14px;");

        Label phoneLabel = new Label("Phone: +1-800-123-4567");
        phoneLabel.setStyle("-fx-font-size: 14px;");

        footer.getChildren().addAll(contactLabel, emailLabel, phoneLabel);

        layout.getChildren().addAll(heading, featureDetails, footer);
        return layout;
    }

    /**
     * Creates a feature detail row with a label and description.
     *
     * @param featureName The name of the feature.
     * @param description The description of the feature.
     * @return An HBox containing the feature name and description.
     */
    private HBox createFeatureDetail(String featureName, String description) {
        HBox featureRow = new HBox(10);
        featureRow.setAlignment(Pos.TOP_LEFT);

        Label featureLabel = new Label(featureName + ":");
        featureLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");

        featureRow.getChildren().addAll(featureLabel, descriptionLabel);
        return featureRow;
    }
    /**
     * Initializes the Doctor Dashboard application.
     *
     * @param primaryStage The primary stage for this application.
     */
    private void setContent(VBox content) {
        mainContent.getChildren().setAll(content.getChildren());
    }

    /**
     * Main method to launch the Doctor Dashboard application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
