package com.example.project.DashBoard;

import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.Appointment_Scheduling.Appointment.AppointmentStatus;
import com.example.project.Appointment_Scheduling.AppointmentManager;
import com.example.project.EmergencyAlertSystem.NotificationService;
import com.example.project.Login_Home.Login_Signup;
import com.example.project.NotificationsAndReminders.EmailNotification;
import com.example.project.User_Management.Administrator;
import com.example.project.User_Management.Doctor;
import com.example.project.User_Management.Patient;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.example.project.DATABASE.AdminDataFetcher;

/**
 * The AdminDashboard class provides the graphical user interface for the system administrator.
 * It allows the admin to manage patients, doctors, appointments, and view system details.
 * The dashboard includes navigation, data tables, and forms for CRUD operations.
 */
public class AdminDashboard extends Application implements DashBoards {
    private VBox mainContent;
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private Administrator admin = new Administrator();
    private AppointmentManager appointmentManager = new AppointmentManager();

    /**
     * Default start method for JavaFX Application.
     *
     * @param stage The primary stage for this application.
     */
    public void start(Stage stage) {

    }
    
    /**
     * Initializes the administrator's data and loads all doctors, patients, and appointments.
     *
     * @param adminID The unique ID of the administrator.
     */
    private void intializeAdmin(String adminID) {
        admin = AdminDataFetcher.getAdminData(adminID);
        loadAllDcotors();
        loadAllPatients();
        loadAllAppointments();

    }

    /**
     * Loads all doctors from the data source and sets them in the admin object.
     */
    private void loadAllDcotors() {
        List<Doctor> doctors = new ArrayList<>();
        doctors = AdminDataFetcher.getAllDoctors();
        admin.setManagedDoctors(doctors);
        
    }

    /**
     * Loads all patients from the data source and sets them in the admin object.
     */
    private void loadAllPatients() {
        List<Patient> patients = new ArrayList<>();
        patients = AdminDataFetcher.getAllPatients();
        admin.setManagedPatients(patients);
    }

    /**
     * Loads all appointments from the data source and sets them in the appointment manager.
     */
    private void loadAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        appointments = AdminDataFetcher.getAllAppointments();
        appointmentManager.setAppointments(appointments);
    }

    /**
     * Starts the admin dashboard with the specified admin ID.
     *
     * @param stage   The primary stage for this application.
     * @param adminID The unique ID of the administrator.
     */
    @Override
    public void start(Stage stage, String adminID) {

        intializeAdmin(adminID);

        BorderPane root = new BorderPane();
        VBox sidebar = this.buildSidebar();
        HBox topBar = this.buildTopBar(stage);
        this.mainContent = this.buildDashboard();
        ScrollPane sideBarScrollPane = new ScrollPane(sidebar);
        sideBarScrollPane.setFitToWidth(true);
        sideBarScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sideBarScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        sideBarScrollPane.setStyle("-fx-background: #f9fafb;");
        ScrollPane scrollPane = new ScrollPane(this.mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9fafb;");
        root.setLeft(sideBarScrollPane);
        root.setTop(topBar);
        root.setCenter(scrollPane);
        Scene scene = new Scene(root, 1080.0, 720.0);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    /**
     * Builds the sidebar navigation for the admin dashboard.
     *
     * @return A VBox containing sidebar navigation buttons.
     */
    @Override
    public VBox buildSidebar() {
        VBox sidebar = new VBox(20.0);
        sidebar.setPadding(new Insets(20.0));
        sidebar.setStyle("-fx-background-color: #f3f4f6;");
        sidebar.setPrefWidth(250.0);
        Label title = new Label("HopeLife Hospital");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button dashboardBtn = this.createSidebarButton("\ud83d\udccb", "Dashboard");
        Button patientsBtn = this.createSidebarButton("\ud83d\udc65", "Manage Patients");
        Button doctorsBtn = this.createSidebarButton("\ud83d\udc67", "Manage Doctors");
        Button detailsBtn = this.createSidebarButton("\ud83d\udcca", "Details");
        Button appointmentsBtn = this.createSidebarButton("\ud83d\udcc5", "Manage Appointments");
        Button sendMailBtn = this.createSidebarButton("\ud83d\udce7", "Send Reminders"); // Updated icon to 📧
        dashboardBtn.setOnAction(e -> this.setContent(this.buildDashboard()));
        patientsBtn.setOnAction(e -> this.setContent(this.buildManagePatientsView()));
        doctorsBtn.setOnAction(e -> this.setContent(this.buildManageDoctorsView()));
        detailsBtn.setOnAction(e -> this.setContent(this.buildDetailsView()));
        appointmentsBtn.setOnAction(e -> this.setContent(this.buildManageAppointmentsView()));
        sendMailBtn.setOnAction(e -> this.setContent(this.buildSendEmail()));
        sidebar.getChildren().addAll(title, dashboardBtn, patientsBtn, doctorsBtn, detailsBtn, appointmentsBtn, sendMailBtn);
        return sidebar;
    }

    /**
     * Creates a styled sidebar button with an icon and text.
     *
     * @param icon The icon to display.
     * @param text The text to display.
     * @return A styled Button for the sidebar.
     */
    private Button createSidebarButton(String icon, String text) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");
        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px;");
        HBox content = new HBox(10.0, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);
        Button button = new Button();
        button.setGraphic(content);
        button.setPrefWidth(230.0);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #333; -fx-font-size: 14px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333; -fx-font-size: 14px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #333; -fx-font-size: 14px;"));
        return button;
    }

    /**
     * Builds the top bar for the admin dashboard, including notifications and logout buttons.
     *
     * @param stage The primary stage for this application.
     * @return An HBox containing the top bar UI components.
     */
    @Override
    public HBox buildTopBar(Stage stage) {
        HBox topBar = new HBox(20.0);
        topBar.setPadding(new Insets(10.0, 20.0, 10.0, 20.0));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;");
        Button notifications = new Button("\ud83d\udd14");
        Button logout = new Button("\ud83d\udeaa Logout");

        logout.setOnAction(e-> {
            Login_Signup loginSignup = new Login_Signup();
            loginSignup.start(stage);
        });

        topBar.getChildren().addAll(notifications, logout);
        return topBar;
    }

    /**
     * Builds the main dashboard view for the admin, showing profile, details, and system overview.
     *
     * @return A VBox containing the dashboard UI components.
     */
    @Override
    public VBox buildDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(20));
        dashboard.setStyle("-fx-background-color: #f9fafb;");

        HBox headerSection = new HBox(20);
        headerSection.setAlignment(Pos.TOP_LEFT);

        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/project/images.jpeg")).toExternalForm());
        ImageView profileImage = new ImageView(image);
        profileImage.setFitWidth(80);
        profileImage.setFitHeight(80);
        profileImage.setStyle("-fx-background-radius: 50%; -fx-border-radius: 50%;");

        VBox adminInfo = new VBox(5);
        adminInfo.setAlignment(Pos.TOP_LEFT);

        Label name = new Label(admin.getName());
        name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label role = new Label("System Administrator");
        role.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");

        HBox contactInfo = new HBox(40);
        Label emailLabel = new Label(admin.getEmail());
        Label phoneLabel = new Label(admin.getPhoneNumber() != null ? admin.getPhoneNumber() : "N/A");
        emailLabel.setStyle("-fx-text-fill: #4b5563;");
        phoneLabel.setStyle("-fx-text-fill: #4b5563;");
        contactInfo.getChildren().addAll(emailLabel, phoneLabel);

        adminInfo.getChildren().addAll(name, role, contactInfo);
        headerSection.getChildren().addAll(profileImage, adminInfo);

        GridPane adminDetailsGrid = new GridPane();
        adminDetailsGrid.setVgap(12);
        adminDetailsGrid.setHgap(30);
        adminDetailsGrid.setPadding(new Insets(15));
        adminDetailsGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        int row = 0;
        addInfoRow(adminDetailsGrid, row++, "Admin ID", admin.getUserID());
        addInfoRow(adminDetailsGrid, row++, "Address", admin.getAddress() != null ? admin.getAddress() : "N/A");
        addInfoRow(adminDetailsGrid, row++, "Account Status", admin.isAccountStatus() ? "Active" : "Inactive");

        GridPane overviewCard = new GridPane();
        overviewCard.setVgap(10);
        overviewCard.setHgap(30);
        overviewCard.setPadding(new Insets(15));
        overviewCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        overviewCard.getColumnConstraints().addAll(col1, col2);

        Label overviewTitle = new Label("System Overview");
        overviewTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        overviewCard.add(overviewTitle, 0, 0, 2, 1);

        Label patientsLabel = new Label("Total Patients:");
        patientsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label patientsValue = new Label();
        patientsValue.setText(String.valueOf((admin.getManagedPatients().size())));
        patientsValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        overviewCard.add(patientsLabel, 0, 1);
        overviewCard.add(patientsValue, 1, 1);

        Label doctorsLabel = new Label("Total Doctors:");
        doctorsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label doctorsValue = new Label();
        doctorsValue.setText(String.valueOf((admin.getManagedDoctors().size())));
        doctorsValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        overviewCard.add(doctorsLabel, 0, 2);
        overviewCard.add(doctorsValue, 1, 2);

        Label appointmentsLabel = new Label("Total Appointments:");
        appointmentsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label appointmentsValue = new Label();
        appointmentsValue.setText(String.valueOf(appointmentManager.getAppointments().size()));
        appointmentsValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        overviewCard.add(appointmentsLabel, 0, 3);
        overviewCard.add(appointmentsValue, 1, 3);

        dashboard.getChildren().addAll(headerSection, adminDetailsGrid, overviewCard);
        return dashboard;
    }

    /**
     * Adds a row of information to a GridPane with a label and value.
     *
     * @param grid  The GridPane to add the row to.
     * @param row   The row index.
     * @param label The label text.
     * @param value The value text.
     */
    private void addInfoRow(GridPane grid, int row, String label, String value) {
        Label key = new Label(label + ":");
        key.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        grid.add(key, 0, row);
        grid.add(val, 1, row);
    }

    /**
     * Builds the view for managing patients, including a table of patients and actions.
     *
     * @return A VBox containing the manage patients UI components.
     */
    private VBox buildManagePatientsView() {
        VBox layout = new VBox(20.0);
        layout.setPadding(new Insets(20.0));
        layout.setStyle("-fx-background-color: #f9fafb;");
        Label title = new Label("Manage Patients");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label subtitle = new Label("View and manage all patients");
        subtitle.setStyle("-fx-text-fill: #6b7280;");

        TableView<Patient> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        TableColumn<Patient, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserID()));
        TableColumn<Patient, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        TableColumn<Patient, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));
        TableColumn<Patient, Void> statusCol = new TableColumn<>("Status");
        statusCol.setCellFactory(col -> new TableCell<Patient, Void>() {
            private final Button statusButton = new Button();

            {
                statusButton.setPrefWidth(80);
                statusButton.setOnAction(e -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    boolean newStatus = !patient.isAccountStatus();
                    patient.setAccountStatus(newStatus);
                    AdminDataFetcher.updateAccountStatus(patient.getUserID(), newStatus);
                    updateButtonStyle(statusButton, newStatus);
                });
            }

            private void updateButtonStyle(Button button, boolean isActive) {
                if (isActive) {
                    button.setText("Active");
                    button.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
                } else {
                    button.setText("Inactive");
                    button.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    Patient patient = getTableView().getItems().get(getIndex());
                    updateButtonStyle(statusButton, patient.isAccountStatus());
                    setGraphic(statusButton);
                }
            }
        });
        TableColumn<Patient, Void> deleteCol = new TableColumn<>("Delete");
        deleteCol.setCellFactory(col -> new TableCell<Patient, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
                deleteButton.setOnAction(e -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    AdminDataFetcher.deleteUserByID(patient.getUserID());
                    admin.getManagedPatients().remove(patient);
                    table.setItems(FXCollections.observableArrayList(admin.getManagedPatients()));

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        table.getColumns().addAll(nameCol, idCol, emailCol, phoneCol, statusCol, deleteCol);


        ObservableList<Patient> adminPatients;
        if (admin.getManagedPatients() instanceof ObservableList) {
            adminPatients = (ObservableList<Patient>) admin.getManagedPatients();
        } else {
            adminPatients = FXCollections.observableArrayList(admin.getManagedPatients());
        }

        table.setItems(adminPatients);

        layout.getChildren().addAll(title, subtitle, table);
        return layout;
    }


    /**
     * Builds the view for managing doctors, including a table of doctors and actions.
     *
     * @return A VBox containing the manage doctors UI components.
     */
    private VBox buildManageDoctorsView() {
        VBox layout = new VBox(20.0);
        layout.setPadding(new Insets(20.0));
        layout.setStyle("-fx-background-color: #f9fafb;");
        Label title = new Label("Manage Doctors");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label subtitle = new Label("View and manage all doctors");
        subtitle.setStyle("-fx-text-fill: #6b7280;");

        TableView<Doctor> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Doctor, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        TableColumn<Doctor, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserID()));
        TableColumn<Doctor, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        TableColumn<Doctor, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));
        TableColumn<Doctor, Void> statusCol = new TableColumn<>("Status");
        statusCol.setCellFactory(col -> new TableCell<Doctor, Void>() {
            private final Button statusButton = new Button();

            {
                statusButton.setPrefWidth(80);
                statusButton.setOnAction(e -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    boolean newStatus = !doctor.isAccountStatus();
                    doctor.setAccountStatus(newStatus);
                    AdminDataFetcher.updateAccountStatus(doctor.getUserID(), newStatus);
                    table.refresh();
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    if (doctor.isAccountStatus()) {
                        statusButton.setText("Active");
                        statusButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
                    } else {
                        statusButton.setText("Inactive");
                        statusButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
                    }
                    setGraphic(statusButton);
                }
            }
        });
        TableColumn<Doctor, Void> deleteCol = new TableColumn<>("Delete");
        deleteCol.setCellFactory(col -> new TableCell<Doctor, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
                deleteButton.setOnAction(e -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    AdminDataFetcher.deleteUserByID(doctor.getUserID());
                    admin.getManagedDoctors().remove(doctor);
                    table.setItems(FXCollections.observableArrayList(admin.getManagedDoctors()));

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        table.getColumns().addAll(nameCol, idCol, emailCol, phoneCol, statusCol, deleteCol);


        ObservableList<Doctor> adminDoctors;
        if (admin.getManagedDoctors() instanceof ObservableList) {
            adminDoctors = (ObservableList<Doctor>) admin.getManagedDoctors();
        } else {
            adminDoctors = FXCollections.observableArrayList(admin.getManagedDoctors());
        }

        table.setItems(adminDoctors);

        layout.getChildren().addAll(title, subtitle, table);
        return layout;
    }





    /**
     * Builds the view for managing doctors, including a table of doctors and actions.
     *
     * @return A VBox containing the manage doctors UI components.
     */
    private VBox buildManageAppointmentsView() {
        VBox layout = new VBox(20.0);
        layout.setPadding(new Insets(20.0));
        layout.setStyle("-fx-background-color: #f9fafb;");
        Label heading = new Label("Manage Appointments");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Button addAppointmentBtn = new Button("Add New Appointment");
        addAppointmentBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
        addAppointmentBtn.setOnAction(e -> this.setContent(this.buildAddAppointmentView()));
        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        TableColumn<Appointment, String> timeCol = new TableColumn<>("Date/Time");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentDateTime().format(formatter)));
        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient ID");
        patientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatient()));
        TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor ID");
        doctorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDoctor()));
        TableColumn<Appointment, String> idCol = new TableColumn<>("Appointment ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentID()));

        TableColumn<Appointment, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<Appointment, Void>() {
            private final Button cancelButton = new Button("Cancel");

            {
                cancelButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
                cancelButton.setOnAction(e -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    appointmentManager.getAppointments().remove(appointment);
                    appointmentTable.setItems(FXCollections.observableArrayList(appointmentManager.getAppointments()));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelButton);
                }
            }
        });
        appointmentTable.getColumns().addAll(timeCol, patientCol, doctorCol, idCol, actionCol);


        ObservableList<Appointment> managerAppointments;
        if (appointmentManager.getAppointments() instanceof ObservableList) {
            managerAppointments = (ObservableList<Appointment>)appointmentManager.getAppointments();
        } else {
            managerAppointments = FXCollections.observableArrayList(appointmentManager.getAppointments());
        }
        appointmentTable.setItems(managerAppointments);
        appointmentTable.setPlaceholder(new Label("No appointments available"));
        layout.getChildren().addAll(heading, addAppointmentBtn, appointmentTable);
        return layout;
    }

    /**
     * Builds the view for sending email reminders to patients.
     *
     * @return A VBox containing the send email UI components.
     */
    private VBox buildAddAppointmentView() {
        VBox layout = new VBox(15.0);
        layout.setPadding(new Insets(20.0));
        layout.setStyle("-fx-background-color: #f9fafb;");
        HBox headerSection = new HBox(20.0);
        headerSection.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("\ud83d\udcc5");
        icon.setStyle("-fx-font-size: 24px;");
        VBox headerText = new VBox(5.0);
        Label title = new Label("Add New Appointment");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label subtitle = new Label("Schedule an appointment");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");
        headerText.getChildren().addAll(title, subtitle);
        headerSection.getChildren().addAll(icon, headerText);
        GridPane form = new GridPane();
        form.setVgap(10.0);
        form.setHgap(20.0);
        form.setPadding(new Insets(15.0));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");
        TextField idField = new TextField();
        idField.setPromptText("Appointment ID");
        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Patient ID");
        TextField doctorIdField = new TextField();
        doctorIdField.setPromptText("Doctor ID");
        TextField dateTimeField = new TextField();
        dateTimeField.setPromptText("Date/Time (yyyy-MM-dd HH:mm)");
        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason");
        form.add(new Label("Appointment ID:"), 0, 0);
        form.add(idField, 1, 0);
        form.add(new Label("Patient ID:"), 0, 1);
        form.add(patientIdField, 1, 1);
        form.add(new Label("Doctor ID:"), 0, 2);
        form.add(doctorIdField, 1, 2);
        form.add(new Label("Date/Time:"), 0, 3);
        form.add(dateTimeField, 1, 3);
        form.add(new Label("Reason:"), 0, 4);
        form.add(reasonField, 1, 4);
        Button submitBtn = new Button("Schedule Appointment");
        submitBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white;");
        submitBtn.setOnAction(e -> {
            try {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(idField.getText());
                appointment.setPatient(patientIdField.getText());
                appointment.setDoctor(doctorIdField.getText());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                appointment.setAppointmentDateTime(LocalDateTime.parse(dateTimeField.getText(), formatter));
                appointment.setReason(reasonField.getText());
                appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
                appointmentManager.addAppointment(appointment);
                setContent(buildManageAppointmentsView());
                AdminDataFetcher.addAppointmentToDatabase(appointment, admin.findPatientbyID(patientIdField.getText()), admin.findDoctorbyID(doctorIdField.getText()));
                showAlert("Success", "Appointment Scheduled", "Appointment has been scheduled successfully!", AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error", "Invalid Input", ex.getMessage(), AlertType.ERROR);
            }
        });
        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white;");
        backBtn.setOnAction(e -> setContent(buildManageAppointmentsView()));
        HBox buttonBox = new HBox(10.0, submitBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        form.add(buttonBox, 1, 5);
        layout.getChildren().addAll(headerSection, form);
        return layout;
    }

    /**
     * Sets the main content of the dashboard to the specified view.
     *
     * @param content The Node to set as the main content.
     */
    private VBox buildDetailsView() {
        VBox layout = new VBox(20.0);
        layout.setPadding(new Insets(20.0));
        layout.setStyle("-fx-background-color: #f9fafb;");

        Label heading = new Label("Details");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane reportGrid = new GridPane();
        reportGrid.setVgap(12.0);
        reportGrid.setHgap(20.0);
        reportGrid.setPadding(new Insets(15.0));
        reportGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        Label patientsLabel = new Label("Total Patients :");
        patientsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label patientsValue = new Label(String.valueOf(admin.getManagedPatients().size()));
        patientsValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        reportGrid.add(patientsLabel, 0, 0);
        reportGrid.add(patientsValue, 1, 0);

        Label doctorsLabel = new Label("Total Doctors :");
        doctorsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label doctorsValue = new Label(String.valueOf(admin.getManagedDoctors().size()));
        doctorsValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        reportGrid.add(doctorsLabel, 0, 1);
        reportGrid.add(doctorsValue, 1, 1);

        Label appointmentsLabel = new Label("Total Appointments :");
        appointmentsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label appointmentsValue = new Label(String.valueOf(appointmentManager.getAppointments().size()));
        appointmentsValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        reportGrid.add(appointmentsLabel, 0, 2);
        reportGrid.add(appointmentsValue, 1, 2);

        Label appointmentHistoryLabel = new Label("Appointment History");
        appointmentHistoryLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");

        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        appointmentTable.setPlaceholder(new Label("No data"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        TableColumn<Appointment, String> timeCol = new TableColumn<>("Date/Time");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentDateTime().format(formatter)));

        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient ID");
        patientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatient()));

        TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor ID");
        doctorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDoctor()));

        TableColumn<Appointment, String> idCol = new TableColumn<>("Appointment ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentID()));

        TableColumn<Appointment, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReason()));

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentStatus().toString()));

        appointmentTable.getColumns().addAll(idCol, patientCol, doctorCol, timeCol, reasonCol, statusCol);

        // Convert ArrayList to ObservableList
        ObservableList<Appointment> appointmentData = FXCollections.observableArrayList(appointmentManager.getAppointments());
        appointmentTable.setItems(appointmentData);

        layout.getChildren().addAll(heading, reportGrid, appointmentHistoryLabel, appointmentTable);
        return layout;
    }


    /**
     * Builds the view for sending email reminders to patients.
     *
     * @return A VBox containing the send email UI components.
     */
    private VBox buildSendEmail() {
        VBox layout = new VBox(15.0);
        layout.setPadding(new Insets(20.0));
        layout.setStyle("-fx-background-color: #f9fafb;");
        Label heading = new Label("Send Email");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField receiverEmailField = new TextField();
        receiverEmailField.setPromptText("Receiver Email");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write your message here...");
        messageArea.setPrefHeight(200.0);
        Button sendButton = new Button("Send Email");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        sendButton.setOnAction(e -> {
            String receiverEmail = receiverEmailField.getText();
            String subject = subjectField.getText();
            String message = messageArea.getText();
            if (!receiverEmail.isEmpty() && !subject.isEmpty() && !message.isEmpty()) {
                this.sendEmail(receiverEmail, subject, message);
                this.showAlert("Email Sent", "Email Sent Successfully", "Your email has been sent successfully.", AlertType.INFORMATION);
            } else {
                this.showAlert("Error", "Email Sending Failed", "Please fill in all fields.", AlertType.ERROR);
            }
        });
        layout.getChildren().addAll(heading, new Label("Receiver Email:"), receiverEmailField, new Label("Subject:"), subjectField, new Label("Message:"), messageArea, sendButton);
        return layout;
    }

    /**
     * Sends an email using the NotificationService.
     *
     * @param receiverEmail The email address of the recipient.
     * @param subject       The subject of the email.
     * @param message       The content of the email.
     */
    private void sendEmail(String receiverEmail, String subject, String message) {
        NotificationService notificationService = new NotificationService();
        EmailNotification emailNotification = new EmailNotification(receiverEmail, message, notificationService, subject);
        emailNotification.sendRemainder();
    }

    /**
     * Displays an alert dialog with the specified title, header, content, and alert type.
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
     * Starts the JavaFX application.
     *
     * @param primaryStage The primary stage for this application.
     */
    private void setContent(VBox content) {
        this.mainContent.getChildren().setAll(content.getChildren());
    }


    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
