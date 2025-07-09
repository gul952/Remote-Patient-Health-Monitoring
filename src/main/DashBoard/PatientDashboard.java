package com.example.project.DashBoard;
import com.example.project.DashBoard.DoctorDashboard.ChatMessage;
import com.example.project.Doctor_Patient_Interaction.Feedback;
import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.Doctor_Patient_Interaction.MedicalHistory;
import com.example.project.Doctor_Patient_Interaction.Prescription;
import com.example.project.EmergencyAlertSystem.EmailSender;
import com.example.project.Login_Home.Login_Signup;
import com.example.project.DATABASE.PatientDataFetcher;
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
import javafx.stage.*;

import java.io.File;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import javafx.util.StringConverter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.imageio.ImageIO;

import static javafx.application.Application.launch;

/**
 * This class represents the patient dashboard.
 */
public class PatientDashboard extends Application implements DashBoards {

    /**
     * The main content area of the dashboard.
     */
    private VBox mainContent;
    private Doctor doctor = new Doctor();
    private Patient patient1 = new Patient();
    private VitalsDatabase vitalsDatabase = new VitalsDatabase();
    private VBox messagesContainer = new VBox(10);
    private Patient selectedPatient;
    private Doctor selectedDoctor;
    private Prescription prescription = new Prescription();
    public static List<VBox> notifications = new ArrayList<>();

    @Override
    public void start(Stage stage) {

    }

    /**
     * Starts the JavaFX application with the given patient ID.
     *
     * @param stage      The primary stage for this application.
     * @param patientID  The ID of the patient to be displayed on the dashboard.
     */
    @Override
    public void start(Stage stage, String patientID) {
        this.patient1 = PatientDataFetcher.getPatientData(patientID);
        if(patient1 == null) {
            System.out.println("Error while getting data.");
        }
        this.doctor   = patient1.getAssignedDoctor();

        BorderPane root = new BorderPane();

        VBox sidebar = buildSidebar();
        HBox topBar  = buildTopBar(stage);
        mainContent  = buildDashboard();
        ScrollPane sideBarScrollPane = new ScrollPane(sidebar);
        sideBarScrollPane.setFitToWidth(true);
        sideBarScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sideBarScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sideBarScrollPane.setStyle("-fx-background: #f9fafb;");

        // wrap main content in scroll pane
        ScrollPane mainScrollPane = new ScrollPane(mainContent);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setStyle("-fx-background: #f9fafb;");

        root.setLeft(sideBarScrollPane);
        root.setTop(topBar);
        root.setCenter(mainScrollPane);

        Scene scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Patient Dashboard - " + patient1.getName());
        stage.show();
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
        sidebar.setStyle("-fx-background-color: #f3f4f6;");
        sidebar.setPrefWidth(200);

        Label title = new Label("HopeLife Hospital");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button dashboardBtn = createSidebarButton("📊", "Dashboard");
        Button patientsBtn = createSidebarButton("👥", "My Doctors");
        Button appointmentsBtn = createSidebarButton("📅", "Appointments");
        Button detailsBtn = createSidebarButton("📝", "Details");
        Button messagesBtn = createSidebarButton("💬", "Messages");
        Button viewFeedbacksBtn = createSidebarButton("🗨️", "Feedbacks");
        Button createPrescriptionBtn = createSidebarButton("📝", "Prescriptions");
        Button viewVitalsBtn = createSidebarButton("📊", "My Vitals");
        Button addVitalsBtn = createSidebarButton("➕", "Add Vitals");
        Button viewMedicalHistoryBtn = createSidebarButton("📜", "Medical History");
        Button sendMailBtn = createSidebarButton("✉️", "Send Mail");

        dashboardBtn.setOnAction(e -> setContent(buildDashboard()));
        patientsBtn.setOnAction(e -> setContent((VBox) buildAssignedPatientsView()));
        appointmentsBtn.setOnAction(e -> setContent(buildAppointmentsView()));
        viewFeedbacksBtn.setOnAction(e -> {
            VBox feedbackView = buildFeedbackView();
            setContent(feedbackView);
        });
        createPrescriptionBtn.setOnAction(e -> {
            VBox prescriptionsView = buildViewPrescriptionsView(patient1.getUserID());
            setContent(prescriptionsView);
        });
        viewVitalsBtn.setOnAction(e -> {
            VBox vitalsView = buildViewVitals();
            setContent(vitalsView);
        });
        viewMedicalHistoryBtn.setOnAction(e -> {
            VBox MedicalView = buildMedicalHistoryView();
            setContent(MedicalView);
        });
        sendMailBtn.setOnAction(e -> {
            VBox SendEmail = buildSendEmail();
            setContent(SendEmail);
        });
        detailsBtn.setOnAction(e -> {
            VBox SeeDetails = buildViewDetails();
            setContent(SeeDetails);
        });
        messagesBtn.setOnAction(e -> setContent(buildMessagesView()));
        addVitalsBtn.setOnAction(e -> setContent(buildAddVitalsView(patient1)));

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
                addVitalsBtn,
                sendMailBtn
        );

        return sidebar;
    }

    /**
     * Builds the main content area of the dashboard.
     *
     * @return A VBox containing the main content elements.
     */
    private void showFeedbackDialog() {
        Dialog<Feedback> dialog = new Dialog<>();
        dialog.setTitle("Provide Feedback");
        dialog.setHeaderText("Share your experience with your doctor");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        ComboBox<Integer> ratingCombo = new ComboBox<>();
        ratingCombo.getItems().addAll(1, 2, 3, 4, 5);
        ratingCombo.setPromptText("Select rating");

        TextArea commentsArea = new TextArea();
        commentsArea.setPromptText("Your comments...");
        commentsArea.setPrefRowCount(3);

        CheckBox anonymousCheck = new CheckBox("Submit anonymously");

        grid.add(new Label("Rating:"), 0, 0);
        grid.add(ratingCombo, 1, 0);
        grid.add(new Label("Comments:"), 0, 1);
        grid.add(commentsArea, 1, 1);
        grid.add(anonymousCheck, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                try {
                    String feedbackId = "FB-" + System.currentTimeMillis();
                    return new Feedback(
                            feedbackId,
                            patient1.getAssignedDoctor().getUserID(),
                            patient1.getUserID(),
                            ratingCombo.getValue(),
                            commentsArea.getText(),
                            LocalDateTime.now(),
                            Feedback.FeedbackStatus.PENDING,
                            anonymousCheck.isSelected()
                    );
                } catch (Exception e) {
                    showAlert("Error", "Invalid Input", e.getMessage(), Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<Feedback> result = dialog.showAndWait();
        result.ifPresent(feedback -> {
            patient1.getAssignedDoctor().receiveFeedback(feedback);
            showAlert("Success", "Thank You", "Your feedback has been submitted", Alert.AlertType.INFORMATION);
        });
    }

    /**
     * Builds the view for adding vital signs.
     *
     * @param patient1 The patient for whom the vital signs are being added.
     * @return A VBox containing the input fields for vital signs.
     */
    private VBox buildAddVitalsView(Patient patient1) {

        if (patient1 == null || patient1.getAssignedDoctor() == null) {
            System.out.println("No patient or doctor assigned");
        }
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9fafb;");

        HBox headerSection = new HBox(20);
        headerSection.setAlignment(Pos.CENTER_LEFT);

        Label vitalsIcon = new Label("❤️");
        vitalsIcon.setStyle("-fx-font-size: 24px;");

        VBox headerText = new VBox(5);
        Label title = new Label("Add Vital Signs");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label subtitle = new Label("Record your current health metrics");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");
        headerText.getChildren().addAll(title, subtitle);
        headerSection.getChildren().addAll(vitalsIcon, headerText);

        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(15);
        inputGrid.setHgap(20);
        inputGrid.setPadding(new Insets(10));

        Label heartRateLabel = new Label("Heart Rate (bpm):");
        heartRateLabel.setStyle("-fx-font-weight: bold;");
        TextField heartRateField = new TextField();
        heartRateField.setPromptText("e.g. 72");
        heartRateField.setPrefWidth(200);
        inputGrid.addRow(0, heartRateLabel, heartRateField);

        Label bpLabel = new Label("Blood Pressure (mmHg):");
        bpLabel.setStyle("-fx-font-weight: bold;");
        TextField bloodPressureField = new TextField();
        bloodPressureField.setPromptText("e.g. 120/80");
        inputGrid.addRow(1, bpLabel, bloodPressureField);

        Label tempLabel = new Label("Temperature (°C):");
        tempLabel.setStyle("-fx-font-weight: bold;");
        TextField temperatureField = new TextField();
        temperatureField.setPromptText("e.g. 36.8");
        inputGrid.addRow(2, tempLabel, temperatureField);

        Label oxygenLabel = new Label("Oxygen Level (%):");
        oxygenLabel.setStyle("-fx-font-weight: bold;");
        TextField oxygenField = new TextField();
        oxygenField.setPromptText("e.g. 98");
        inputGrid.addRow(3, oxygenLabel, oxygenField);

        Label respRateLabel = new Label("Respiratory Rate (breaths/min):");
        respRateLabel.setStyle("-fx-font-weight: bold;");
        TextField respRateField = new TextField();
        respRateField.setPromptText("e.g. 16");
        inputGrid.addRow(4, respRateLabel, respRateField);

        Label glucoseLabel = new Label("Glucose Level (mg/dL):");
        glucoseLabel.setStyle("-fx-font-weight: bold;");
        TextField glucoseField = new TextField();
        glucoseField.setPromptText("e.g. 90");
        inputGrid.addRow(5, glucoseLabel, glucoseField);

        Label cholesterolLabel = new Label("Cholesterol Level (mg/dL):");
        cholesterolLabel.setStyle("-fx-font-weight: bold;");
        TextField cholesterolField = new TextField();
        cholesterolField.setPromptText("e.g. 180");
        inputGrid.addRow(6, cholesterolLabel, cholesterolField);

        Label bmiLabel = new Label("BMI:");
        bmiLabel.setStyle("-fx-font-weight: bold;");
        TextField bmiField = new TextField();
        bmiField.setPromptText("e.g. 22.5");
        inputGrid.addRow(7, bmiLabel, bmiField);

        Label hydrationLabel = new Label("Hydration Level (%):");
        hydrationLabel.setStyle("-fx-font-weight: bold;");
        TextField hydrationField = new TextField();
        hydrationField.setPromptText("e.g. 60");
        inputGrid.addRow(8, hydrationLabel, hydrationField);

        Label stressLabel = new Label("Stress Level (/10):");
        stressLabel.setStyle("-fx-font-weight: bold;");
        TextField stressField = new TextField();
        stressField.setPromptText("e.g. 5");
        inputGrid.addRow(9, stressLabel, stressField);

        formContainer.getChildren().add(inputGrid);

        Button submitButton = new Button("Save Vitals");
        submitButton.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
        submitButton.setOnAction(e -> {
            try {
                    VitalSign vital = new VitalSign();
                    vital.setUserID(patient1.getUserID());
                    vital.setDoctorID(patient1.getAssignedDoctor().getUserID());
                    String recordID = "V" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                    vital.setRecordID(recordID);

                    vital.setHeartRate(Integer.parseInt(heartRateField.getText()));
                    String bpText = bloodPressureField.getText().replace("/", "");
                    vital.setBloodPressure(Integer.parseInt(bpText));
                    vital.setTemperature(Double.parseDouble(temperatureField.getText()));
                    vital.setOxygenLevel(Integer.parseInt(oxygenField.getText()));
                    vital.setRespiratoryRate(Integer.parseInt(respRateField.getText()));
                    vital.setGlucoseLevel(Integer.parseInt(glucoseField.getText()));
                    vital.setCholesterolLevel(Integer.parseInt(cholesterolField.getText()));
                    vital.setBmi(Double.parseDouble(bmiField.getText()));
                    vital.setHydrationLevel(Integer.parseInt(hydrationField.getText()));
                    vital.setStressLevel(Integer.parseInt(stressField.getText()));
                    vital.setRecordDateTime(LocalDateTime.now());

                    vitalsDatabase.addVitalSign(vital);
                    patient1.addVitalSign(vital);
                    System.out.println("Save Vitals");
                    VBox vBox = PatientDataFetcher.insertVitalSign(vital, patient1, doctor);
                    if(vBox != null) {
                        notifications.add(vBox);
                    }


                    showAlert("Success", "Vitals Recorded", "Your vital signs have been saved successfully!", Alert.AlertType.INFORMATION);

            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Invalid Input", "Please enter valid numbers in all fields", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error", "Save Failed", "Failed to save vitals: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button uploadButton = new Button("Upload Vitals");
        uploadButton.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
        uploadButton.setOnAction(e -> {
            System.out.println(">>> Upload Vitals clicked");

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Vitals CSV");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            File file = chooser.showOpenDialog(null);
            System.out.println(">>> File returned: " + file);

            if (file != null) {
                try {
                    List<VitalSign> vitals = parseCsvFile(file);
                    validateVitals(vitals);
                    for (VitalSign v : vitals) {
                        v.setUserID(patient1.getUserID());
                        v.setDoctorID(patient1.getAssignedDoctor().getUserID());
                        v.setRecordDateTime(LocalDateTime.now());
                        String recordID = "V" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                        v.setRecordID(recordID);

                        vitalsDatabase.addVitalSign(v);
                        patient1.addVitalSign(v);
                        VBox vBox = PatientDataFetcher.insertVitalSign(v, patient1, doctor);
                            notifications.add(vBox);

                    }
                    showAlert("Success", "Vitals Uploaded", "CSV data has been saved successfully!", Alert.AlertType.INFORMATION);
                    setContent(buildViewVitals());
                } catch (IOException ex) {
                    showAlert("Error", "Upload Failed", "Could not read CSV: " + ex.getMessage(), Alert.AlertType.ERROR);
                } catch (IllegalArgumentException ex) {
                    showAlert("Validation Error", "Invalid Data", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });

        HBox buttonBox = new HBox(10, uploadButton, submitButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        formContainer.getChildren().add(buttonBox);


        layout.getChildren().addAll(headerSection, formContainer);

        return layout;
    }


    /**
     * Parses a CSV file and returns a list of VitalSign objects.
     *
     * @param file The CSV file to parse.
     * @return A list of VitalSign objects.
     * @throws IOException If an error occurs while reading the file.
     */
    private List<VitalSign> parseCsvFile(File file) throws IOException {
        List<VitalSign> list = new ArrayList<>();
        try (Reader reader = new FileReader(file);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : parser) {
                VitalSign v = new VitalSign();
                v.setHeartRate(Integer.parseInt(record.get("Heart Rate (bpm)")));
                v.setBloodPressure(Integer.parseInt(record.get("Blood Pressure (mmHg)")));
                v.setOxygenLevel(Integer.parseInt(record.get("Oxygen Level (%)")));
                v.setTemperature(Double.parseDouble(record.get("Temperature (°C)")));
                v.setRespiratoryRate(Integer.parseInt(record.get("Respiratory Rate (breaths/min)")));
                v.setGlucoseLevel(Integer.parseInt(record.get("Glucose Level (mg/dL)")));
                v.setCholesterolLevel(Integer.parseInt(record.get("Cholesterol Level (mg/dL)")));
                v.setBmi(Double.parseDouble(record.get("BMI")));
                v.setHydrationLevel(Integer.parseInt(record.get("Hydration Level (%)")));
                v.setStressLevel(Integer.parseInt(record.get("Stress Level (/10)")));
                list.add(v);
            }
        }
        return list;
    }


    /**
     * Validates the vital signs to ensure they are within acceptable ranges.
     *
     * @param vitals The list of VitalSign objects to validate.
     * @return true if all vital signs are valid, false otherwise.
     */
    private boolean validateVitals(List<VitalSign> vitals) {
        for (VitalSign v : vitals) {
            if (v.getHeartRate() < 30 || v.getHeartRate() > 200)
                throw new IllegalArgumentException("Heart Rate " + v.getHeartRate() + " bpm is out of range (30-200).");
            if (v.getBloodPressure() < 50 || v.getBloodPressure() > 200)
                throw new IllegalArgumentException("Blood Pressure " + v.getBloodPressure() + " mmHg is out of range (50-200).");
            if (v.getOxygenLevel() < 50 || v.getOxygenLevel() > 100)
                throw new IllegalArgumentException("Oxygen Level " + v.getOxygenLevel() + "% is out of range (50-100).");
            if (v.getTemperature() < 30.0 || v.getTemperature() > 45.0)
                throw new IllegalArgumentException("Temperature " + v.getTemperature() + "°C is out of range (30.0-45.0).");
            if (v.getRespiratoryRate() < 5 || v.getRespiratoryRate() > 50)
                throw new IllegalArgumentException("Respiratory Rate " + v.getRespiratoryRate() + " is out of range (5-50).");
            if (v.getGlucoseLevel() < 50 || v.getGlucoseLevel() > 300)
                throw new IllegalArgumentException("Glucose Level " + v.getGlucoseLevel() + " mg/dL is out of range (50-300).");
            if (v.getCholesterolLevel() < 50 || v.getCholesterolLevel() > 400)
                throw new IllegalArgumentException("Cholesterol Level " + v.getCholesterolLevel() + " mg/dL is out of range (50-400).");
            if (v.getBmi() < 10.0 || v.getBmi() > 50.0)
                throw new IllegalArgumentException("BMI " + v.getBmi() + " is out of range (10.0-50.0).");
            if (v.getHydrationLevel() < 0 || v.getHydrationLevel() > 100)
                throw new IllegalArgumentException("Hydration Level " + v.getHydrationLevel() + "% is out of range (0-100).");
            if (v.getStressLevel() < 0 || v.getStressLevel() > 10)
                throw new IllegalArgumentException("Stress Level " + v.getStressLevel() + " is out of range (0-10).");
        }
        return true;
    }

    /**
     * Sets the main content area of the dashboard.
     *
     * @param content The VBox containing the new content.
     */
    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Clears the input fields in the form.
     */
    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    /**
     * Sets the main content area of the dashboard.
     *
     * @param content The VBox containing the new content.
     */
    private Button createSidebarButton(String icon, String text) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px;");

        HBox content = new HBox(10, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(content);
        button.setPrefWidth(180);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #333; -fx-font-size: 14px;");

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333; -fx-font-size: 14px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #333; -fx-font-size: 14px;"));

        return button;
    }

    /**
     * Sets the main content area of the dashboard.
     *
     * @param content The VBox containing the new content.
     */
    @Override
    public HBox buildTopBar(Stage stage) {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;");

        Button notificationButton = new Button("🔔");
        Button logout = new Button("🚪 Logout");

        logout.setOnAction(e-> {
            Login_Signup loginSignup = new Login_Signup();
            loginSignup.start(stage);
        });

        notificationButton.setOnAction(event -> {
            if (!notifications.isEmpty()) {
                VBox notificationContent = notifications.get(0);

                javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
                dialog.setTitle("Notification");
                dialog.getDialogPane().setContent(notificationContent);
                dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.OK);
                dialog.showAndWait();

            } else {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION,
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
     * Sets the main content area of the dashboard.
     *
     * @param content The VBox containing the new content.
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

        VBox patientinfo = new VBox(5);
        patientinfo.setAlignment(Pos.TOP_LEFT);

        Label name = new Label("Patient " + patient1.getName());
        name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label genderAge = new Label(patient1.getGender() + " - " + patient1.getAge());
        genderAge.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");

        HBox contactInfo = new HBox(40);
        Label email = new Label(patient1.getEmail());
        Label phone = new Label(patient1.getPhoneNumber());
        email.setStyle("-fx-text-fill: #4b5563;");
        phone.setStyle("-fx-text-fill: #4b5563;");
        contactInfo.getChildren().addAll(email, phone);

        patientinfo.getChildren().addAll(name, genderAge, contactInfo);
        headerSection.getChildren().addAll(profileImage, patientinfo);

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

        Label availabilityTitle = new Label("Health Information");
        availabilityTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        availabilityCard.add(availabilityTitle, 0, 0, 2, 1);

        Label daysLabel = new Label("Present Diseases:");
        daysLabel.setStyle("-fx-font-weight: bold;");
        Label daysValue = new Label(patient1.getDiseases().toString());
        availabilityCard.add(daysLabel, 0, 1);
        availabilityCard.add(daysValue, 1, 1);

        Label startTimeLabel = new Label("Allergies");
        startTimeLabel.setStyle("-fx-font-weight: bold;");
        Label startTimeValue = new Label(patient1.getAllergies().toString());
        availabilityCard.add(startTimeLabel, 0, 2);
        availabilityCard.add(startTimeValue, 1, 2);

        Label endTimeLabel = new Label("Blood Group");
        endTimeLabel.setStyle("-fx-font-weight: bold;");
        Label endTimeValue = new Label(patient1.getBloodGroup());
        availabilityCard.add(endTimeLabel, 0, 3);
        availabilityCard.add(endTimeValue, 1, 3);

        GridPane leftInfoGrid = new GridPane();
        leftInfoGrid.setVgap(12);
        leftInfoGrid.setHgap(20);
        leftInfoGrid.setPadding(new Insets(15));
        leftInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");
        addInfoRow(leftInfoGrid, 0, "User ID", patient1.getUserID());
        addInfoRow(leftInfoGrid, 1, "Address", patient1.getAddress());
        addInfoRow(leftInfoGrid, 2, "Account Status", patient1.isAccountStatus() ? "Active" : "Inactive");

        GridPane rightInfoGrid = new GridPane();
        rightInfoGrid.setVgap(12);
        rightInfoGrid.setHgap(20);
        rightInfoGrid.setPadding(new Insets(15));
        rightInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");
        addInfoRow(rightInfoGrid, 0, "Blood Group",
                patient1.getBloodGroup());
        addInfoRow(rightInfoGrid, 1, "Next Appointment",
                patient1.getAppointmentDate() != null
                        ? patient1.getAppointmentDate().toLocalDate().toString()
                        : "N/A");
        addInfoRow(rightInfoGrid, 2, "Primary Doctor",
                patient1.getAssignedDoctor().getName());

        GridPane bottomInfoGrid = new GridPane();
        bottomInfoGrid.setVgap(12);
        bottomInfoGrid.setHgap(40);
        bottomInfoGrid.setPadding(new Insets(15));
        bottomInfoGrid.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");
        addInfoRow(bottomInfoGrid, 0, "Upcoming Appointments", "");
        addInfoRow(bottomInfoGrid, 1, "Attended Appointments", "");


        HBox infoHBox = new HBox(20, leftInfoGrid, rightInfoGrid);
        VBox fullContent = new VBox(20, headerSection, availabilityCard, infoHBox, bottomInfoGrid);
        dashboard.getChildren().add(fullContent);

        return dashboard;
    }

    /**
     * Sets the main content area of the dashboard.
     *
     * @param content The VBox containing the new content.
     */
    private void addInfoRow(GridPane grid, int rowIndex, String label, String value) {
        Label title = new Label(label + " :");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        grid.add(title, 0, rowIndex);
        grid.add(val, 1, rowIndex);
    }

    /**
     * Builds the view for assigned patients.
     *
     * @return A VBox containing the assigned patients' information.
     
    */
    private Node buildAssignedPatientsView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label title = new Label("My Doctors");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label subtitle = new Label("List of doctors assigned to you");
        subtitle.setStyle("-fx-text-fill: #6b7280;");

        TableView<Doctor> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Doctor, String> nameCol = new TableColumn<>("Doctor");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Doctor, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserID()));

        TableColumn<Doctor, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Doctor, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));

        table.getColumns().addAll(nameCol, idCol, emailCol, phoneCol);
        table.setItems(FXCollections.observableArrayList(patient1.getAssignedDoctor()));

        container.getChildren().addAll(title, subtitle, table);
        return container;
    }

    /**
     * Builds the view for appointments.
     *
     * @return A VBox containing the appointments information.
     */
    private VBox buildAppointmentsView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label heading = new Label("Today's Appointments");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        TableColumn<Appointment, String> timeCol = new TableColumn<>("Date/Time");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAppointmentDateTime().format(formatter)
        ));

        TableColumn<Appointment, String> patientCol = new TableColumn<>("Doctor");
        patientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDoctor()));

        TableColumn<Appointment, String> patientName = new TableColumn<>("Patient Name");
        patientCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatient()));

        TableColumn<Appointment, String> idCol2 = new TableColumn<>("Appointment ID");
        idCol2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppointmentID()));

        appointmentTable.getColumns().addAll(timeCol, patientCol, patientName, idCol2);
        appointmentTable.setItems(FXCollections.observableArrayList(
                PatientDataFetcher.getPatientAppointments(patient1.getUserID())
        ));

        GridPane detailsPane = new GridPane();
        detailsPane.setVgap(10);
        detailsPane.setHgap(20);
        detailsPane.setPadding(new Insets(15));
        detailsPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #e5e7eb;");

        appointmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                detailsPane.getChildren().clear();
                addInfoRow(detailsPane, 0, "Appointment ID", newVal.getAppointmentID());
                addInfoRow(detailsPane, 1, "Patient ID", newVal.getPatient());
                addInfoRow(detailsPane, 2, "Doctor ID", newVal.getDoctor());
                addInfoRow(detailsPane, 3, "Date/Time", newVal.getAppointmentDateTime().toString());
                addInfoRow(detailsPane, 4, "Reason", newVal.getReason());
                addInfoRow(detailsPane, 5, "Notes", newVal.getNotes());
                addInfoRow(detailsPane, 6, "Status", newVal.getAppointmentStatus().toString());
            }
        });

        layout.getChildren().addAll(heading, appointmentTable, new Label("Details:"), detailsPane);
        return layout;
    }

    /**
     * Builds the view for feedback.
     *
     * @return A VBox containing the feedback form.
     */
    public VBox buildFeedbackView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9fafb;");

        HBox headerSection = new HBox(10);
        headerSection.setAlignment(Pos.CENTER_LEFT);

        Label feedbackIcon = new Label("🗨️");
        feedbackIcon.setStyle("-fx-font-size: 24px;");

        VBox headerText = new VBox(4);
        Label title = new Label("GIVE FEEDBACK");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label subtitle = new Label("Share your experience with your care");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");
        headerText.getChildren().addAll(title, subtitle);

        headerSection.getChildren().addAll(feedbackIcon, headerText);

        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(20);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label ratingLabel = new Label("Rating (1-5):");
        ComboBox<Integer> ratingCombo = new ComboBox<>();
        ratingCombo.getItems().addAll(1, 2, 3, 4, 5);
        form.add(ratingLabel, 0, 0);
        form.add(ratingCombo, 1, 0);

        Label commentsLabel = new Label("Comments:");
        TextArea commentsArea = new TextArea();
        commentsArea.setPrefRowCount(3);
        form.add(commentsLabel, 0, 1);
        form.add(commentsArea, 1, 1);

        CheckBox anonymousCheck = new CheckBox("Submit anonymously");
        form.add(anonymousCheck, 1, 2);

        Button submitBtn = new Button("Submit Feedback");
        submitBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white;");
        submitBtn.setOnAction(e -> {
            try {
                String feedbackId = "FB-" + System.currentTimeMillis();
                Feedback feedback = new Feedback(
                        feedbackId,
                        patient1.getAssignedDoctor().getUserID(),
                        patient1.getUserID(),
                        ratingCombo.getValue(),
                        commentsArea.getText(),
                        LocalDateTime.now(),
                        Feedback.FeedbackStatus.PENDING,
                        anonymousCheck.isSelected()
                );

                patient1.getAssignedDoctor().receiveFeedback(feedback);
                PatientDataFetcher.addFeedbacksByPatient(patient1.getUserID(), patient1.getAssignedDoctor().getUserID(),
                        ratingCombo.getValue(), commentsArea.getText(), anonymousCheck.isSelected());

                showAlert("Success", "Feedback Submitted", "Thank you for your feedback!", Alert.AlertType.INFORMATION);
                setContent(buildDashboard());
            } catch (Exception ex) {
                showAlert("Error", "Invalid Input", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        HBox buttonBox = new HBox(submitBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        form.add(buttonBox, 1, 3);

        layout.getChildren().addAll(headerSection, form);
        return layout;
    }

    /**
     * Builds the view for prescriptions.
     *
     * @param patientID The ID of the patient whose prescriptions are to be displayed.
     * @return A VBox containing the prescriptions information.
     */
    public VBox buildViewPrescriptionsView(String patientID) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #f9fafb;");

        HBox headerSection = new HBox(10);
        headerSection.setAlignment(Pos.CENTER_LEFT);

        Label presIcon = new Label("💊");
        presIcon.setStyle("-fx-font-size: 24px;");

        VBox headerText = new VBox(4);
        Label title = new Label("MY PRESCRIPTIONS");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label subtitle = new Label("Review your prescribed medications");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");
        headerText.getChildren().addAll(title, subtitle);

        headerSection.getChildren().addAll(presIcon, headerText);
        layout.getChildren().add(headerSection);

        List<Prescription> prescriptions = PatientDataFetcher.getPatientPrescriptions(patient1.getUserID());
        if (prescriptions.isEmpty()) {
            Label none = new Label("No prescriptions available");
            none.setStyle("-fx-font-style: italic;");
            layout.getChildren().add(none);
        } else {
            for (Prescription p : prescriptions) {
                layout.getChildren().add(createPrescriptionBox(p));
            }
        }

        return layout;
    }

    /**
     * Creates a VBox to display a prescription's details.
     *
     * @param p The Prescription object containing the details.
     * @return A VBox containing the prescription details.
     */
    private VBox createPrescriptionBox(Prescription p) {
        VBox box = new VBox(8);
        box.setStyle(
                "-fx-border-color: #d1d5db; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 6; " +
                        "-fx-padding: 12; " +
                        "-fx-background-color: #f9fafb;"
        );

        Label header = new Label("PRESCRIPTION #" + p.getPrescriptionID());
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        VBox medsBox = new VBox(4);
        Label medsLabel = new Label("Medications:");
        medsLabel.setStyle("-fx-font-weight: bold;");
        medsBox.getChildren().add(medsLabel);
        for (String med : p.getMedications()) {
            medsBox.getChildren().add(new Label("• " + med));
        }

        Label instructions = new Label("Instructions: " + p.getDosageInstructions());
        Label schedule = new Label("Schedule: " + p.getDosageSchedule());
        Label duration = new Label("Duration: " +
                p.getStartDate().toLocalDate() + " to " + p.getEndDate().toLocalDate());
        Label quantity = new Label("Total Quantity: " + p.getQuantity());
        Label doctor = new Label("Prescribed by: " +
                (p.getDoctor() != null ? p.getDoctor().getName() : "Unknown Doctor"));

        box.getChildren().addAll(
                header,
                new Separator(),
                medsBox,
                instructions,
                schedule,
                duration,
                quantity,
                doctor
        );

        return box;
    }


    /**
     * Creates a placeholder for the vitals section when no data is available.
     *
     * @return A VBox containing the placeholder message.
     */
    public VBox buildViewVitals() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9fafb;");

        HBox headerSection = new HBox(20);
        headerSection.setAlignment(Pos.CENTER_LEFT);

        Label vitalsIcon = new Label("📋");
        vitalsIcon.setStyle("-fx-font-size: 24px;");

        VBox headerText = new VBox(5);
        Label title = new Label("MY VITAL SIGNS");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label subtitle = new Label("Your recorded health metrics");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");
        headerText.getChildren().addAll(title, subtitle);

        headerSection.getChildren().addAll(vitalsIcon, headerText);

        VBox allVitals = new VBox(15);
        allVitals.setPadding(new Insets(10));
        allVitals.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #e5e7eb; " +
                        "-fx-border-radius: 10;"
        );

        List<VitalSign> vitalsList = PatientDataFetcher.getPatientVitals(patient1.getUserID());

        if (vitalsList.isEmpty()) {
            allVitals.getChildren().add(createVitalsPlaceholder());
        } else {
            for (VitalSign vital : vitalsList) {
                GridPane vitalsGrid = new GridPane();
                vitalsGrid.setVgap(10);
                vitalsGrid.setHgap(20);
                vitalsGrid.setPadding(new Insets(10));

                int row = 0;
                addVitalRow(vitalsGrid, row++, "Heart Rate", vital.getHeartRate() + " bpm");
                addVitalRow(vitalsGrid, row++, "Blood Pressure", vital.getBloodPressure() + " mmHg");
                addVitalRow(vitalsGrid, row++, "Oxygen Level", vital.getOxygenLevel() + "%");
                addVitalRow(vitalsGrid, row++, "Temperature", vital.getTemperature() + "°C");
                addVitalRow(vitalsGrid, row++, "Respiratory Rate", vital.getRespiratoryRate() + " breaths/min");
                addVitalRow(vitalsGrid, row++, "Glucose Level", vital.getGlucoseLevel() + " mg/dL");
                addVitalRow(vitalsGrid, row++, "Cholesterol Level", vital.getCholesterolLevel() + " mg/dL");
                addVitalRow(vitalsGrid, row++, "BMI", vital.getBmi() + "");
                addVitalRow(vitalsGrid, row++, "Hydration Level", vital.getHydrationLevel() + "%");
                addVitalRow(vitalsGrid, row++, "Stress Level", vital.getStressLevel() + "/10");

                Separator separator = new Separator();
                allVitals.getChildren().addAll(vitalsGrid, separator);
            }
        }

        layout.getChildren().addAll(headerSection, allVitals);

        HBox chartBox = new HBox(20);
        chartBox.setPadding(new Insets(10, 0, 0, 0));
        chartBox.setAlignment(Pos.CENTER);

        List<VitalSign> history = vitalsList;

        NumberAxis xAxisHr = new NumberAxis();
        xAxisHr.setLabel("Reading #");
        NumberAxis yAxisHr = new NumberAxis(30, 200, 10);
        yAxisHr.setLabel("BPM");
        LineChart<Number, Number> hrChart = new LineChart<>(xAxisHr, yAxisHr);
        hrChart.setTitle("Heart Rate Trend");
        XYChart.Series<Number, Number> hrSeries = new XYChart.Series<>();
        hrSeries.setName("Heart Rate");
        for (int i = 0; i < history.size(); i++) {
            hrSeries.getData().add(new XYChart.Data<>(i + 1, history.get(i).getHeartRate()));
        }
        hrChart.getData().add(hrSeries);

        NumberAxis xAxisBp = new NumberAxis();
        xAxisBp.setLabel("Reading #");
        NumberAxis yAxisBp = new NumberAxis(50, 200, 10);
        yAxisBp.setLabel("mmHg");
        LineChart<Number, Number> bpChart = new LineChart<>(xAxisBp, yAxisBp);
        bpChart.setTitle("Blood Pressure Trend");
        XYChart.Series<Number, Number> bpSeries = new XYChart.Series<>();
        bpSeries.setName("Blood Pressure");
        for (int i = 0; i < history.size(); i++) {
            bpSeries.getData().add(new XYChart.Data<>(i + 1, history.get(i).getBloodPressure()));
        }
        bpChart.getData().add(bpSeries);

        NumberAxis xAxisOx = new NumberAxis();
        xAxisOx.setLabel("Reading #");
        NumberAxis yAxisOx = new NumberAxis(50, 100, 5);
        yAxisOx.setLabel("% SpO₂");
        LineChart<Number, Number> oxChart = new LineChart<>(xAxisOx, yAxisOx);
        oxChart.setTitle("Oxygen Level Trend");
        XYChart.Series<Number, Number> oxSeries = new XYChart.Series<>();
        oxSeries.setName("O₂ Level");
        for (int i = 0; i < history.size(); i++) {
            oxSeries.getData().add(new XYChart.Data<>(i + 1, history.get(i).getOxygenLevel()));
        }
        oxChart.getData().add(oxSeries);

        chartBox.getChildren().addAll(hrChart, bpChart, oxChart);

        Button downloadChartsButton = new Button("Download Charts");
        downloadChartsButton.setStyle(
                "-fx-background-color: #3b82f6; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 6 12;"
        );
        downloadChartsButton.setOnAction(e -> {
            System.out.println(">> DownloadCharts clicked");
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Charts as PNG");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png")
            );
            Window owner = downloadChartsButton.getScene().getWindow();
            File file = chooser.showSaveDialog(owner);
            System.out.println(">> File chosen: " + file);
            if (file == null) return;

            WritableImage image = chartBox.snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println(">> Image written");
                showAlert("Success", "Charts Saved",
                        "Exported to:\n" + file.getAbsolutePath(),
                        Alert.AlertType.INFORMATION);
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error", "Save Failed",
                        ex.getMessage(), Alert.AlertType.ERROR);
            }
        });


        HBox downloadBox = new HBox(downloadChartsButton);
        downloadBox.setPadding(new Insets(10, 0, 0, 0));
        downloadBox.setAlignment(Pos.CENTER_RIGHT);

        layout.getChildren().add(downloadBox);

        layout.getChildren().add(chartBox);

        return layout;
    }

    /**
     * Creates a placeholder for the vitals section when no data is available.
     *
     * @return A GridPane containing the placeholder message.
     */
    private GridPane createVitalsPlaceholder() {
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(40);
        grid.setPadding(new Insets(10));

        int row = 0;
        addPlaceholderRow(grid, row++, "Heart Rate", "-- bpm");
        addPlaceholderRow(grid, row++, "Blood Pressure", "-- mmHg");
        addPlaceholderRow(grid, row++, "Oxygen Level", "-- %");
        addPlaceholderRow(grid, row++, "Temperature", "-- °C");
        addPlaceholderRow(grid, row++, "Respiratory Rate", "-- breaths/min");
        addPlaceholderRow(grid, row++, "Glucose Level", "-- mg/dL");
        addPlaceholderRow(grid, row++, "Cholesterol Level", "-- mg/dL");
        addPlaceholderRow(grid, row++, "BMI", "--");
        addPlaceholderRow(grid, row++, "Hydration Level", "-- %");
        addPlaceholderRow(grid, row++, "Stress Level", "-- /10");

        return grid;
    }

    /**
     * Creates a placeholder for the vitals section when no data is available.
     *
     * @return A GridPane containing the placeholder message.
     */
    private void addPlaceholderRow(GridPane grid, int rowIndex, String label, String placeholder) {
        Label title = new Label(label + ":");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label val = new Label(placeholder);
        val.setStyle("-fx-font-size: 14px; -fx-text-fill: #9ca3af; -fx-font-style: italic;");
        grid.add(title, 0, rowIndex);
        grid.add(val, 1, rowIndex);
    }

    private void addVitalRow(GridPane grid, int rowIndex, String label, String value) {
        Label title = new Label(label + ":");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        grid.add(title, 0, rowIndex);
        grid.add(val, 1, rowIndex);
    }

    /**
     * Builds the view for messages.
     *
     * @return A VBox containing the messages information.
     */
    private VBox buildMessagesView() {
        VBox mainContainer = new VBox(10);
        mainContainer.setStyle("-fx-padding: 10px;");

        VBox conversationList = new VBox(10);
        conversationList.setStyle("-fx-background-color: #f1f1f1; -fx-padding: 10px;");
        Label docLabel = new Label(patient1.getAssignedDoctor().getName());
        docLabel.setOnMouseClicked(e -> {
            List<ChatMessage> msgs = PatientDataFetcher.getPatientMessages(
                    patient1.getUserID(),
                    patient1.getAssignedDoctor().getUserID()
            );
            updateChatUI(msgs);
        });
        conversationList.getChildren().add(docLabel);

        ScrollPane scrollPane = new ScrollPane();
        messagesContainer.getChildren().clear();
        scrollPane.setContent(messagesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        TextField messageInput = new TextField();
        messageInput.setPromptText("Type a message…");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String txt = messageInput.getText().trim();
            if (!txt.isEmpty()) {
                PatientDataFetcher.sendPatientMessage(
                        patient1.getUserID(),
                        patient1.getAssignedDoctor().getUserID(),
                        txt
                );
                messageInput.clear();
                List<ChatMessage> updated = PatientDataFetcher.getPatientMessages(
                        patient1.getUserID(),
                        patient1.getAssignedDoctor().getUserID()
                );
                updateChatUI(updated);
            }
        });
        HBox inputArea = new HBox(10, messageInput, sendButton);
        inputArea.setPadding(new Insets(10));

        VBox chatView = new VBox(10, scrollPane, inputArea);

        mainContainer.getChildren().addAll(conversationList, chatView);
        return mainContainer;
    }

    /**
     * Updates the chat UI with new messages.
     *
     * @param msgs The list of messages to display.
     */
    private void updateChatUI(List<ChatMessage> msgs) {
        messagesContainer.getChildren().clear();
        for (ChatMessage m : msgs) {
            boolean isDoctor = m.getSenderId().equals(patient1.getAssignedDoctor().getUserID());

            HBox messageRow = new HBox();
            messageRow.setPadding(new Insets(5, 10, 5, 10));
            messageRow.setAlignment(isDoctor ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            Label messageLabel = new Label(m.getMessageText());
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(300);
            messageLabel.setPadding(new Insets(10));
            messageLabel.setStyle(
                    "-fx-background-color: " + (isDoctor ? "#dcf8c6" : "#ffffff") + ";" +  
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;" +
                            "-fx-font-size: 14px;" +
                            "-fx-text-fill: #000;"
            );

            messageLabel.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    // Copy to clipboard
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putString(m.getMessageText());
                    clipboard.setContent(content);

                    showAlert("Copied!", "Message copied to clipboard", m.getMessageText(), Alert.AlertType.INFORMATION);
                }
            });

            messageRow.getChildren().add(messageLabel);
            messagesContainer.getChildren().add(messageRow);
        }
    }



    /**
     * Loads the assigned patients into the conversation list.
     *
     * @param conversationList The VBox to load the patients into.
     */
    public void loadAssignedPatients(VBox conversationList) {
        List<Patient> patients = doctor.getAssignedPatients();
        for (Patient patient : patients) {
            Label patientNameLabel = new Label(patient.getName());
            patientNameLabel.setOnMouseClicked(event -> {
                selectedPatient = patient;
                openChatWindow(patient);
            });
            conversationList.getChildren().add(patientNameLabel);
        }
    }

    /**
     * Opens the chat window for a selected patient.
     *
     * @param patient The selected patient.
     */
    private void openChatWindow(Patient patient) {
        List<ChatMessage> messages = PatientDataFetcher.getPatientMessages(patient1.getUserID(), doctor.getUserID());
        updateChatUI(messages, patient);
    }

    /**
     * Updates the chat UI with new messages.
     *
     * @param messages The list of messages to display.
     * @param patient  The selected patient.
     */
    private void updateChatUI(List<ChatMessage> messages, Patient patient) {
        messagesContainer.getChildren().clear();
        for (ChatMessage message : messages) {
            HBox messageBubble = new HBox(10);
            Label messageLabel = new Label(message.getMessageText());
            if (message.getSenderId().equals(doctor.getUserID())) {
                messageLabel.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 10px; -fx-padding: 10px;");
            } else {
                messageLabel.setStyle("-fx-background-color: #d1ffd6; -fx-background-radius: 10px; -fx-padding: 10px;");
            }
            messageBubble.getChildren().add(messageLabel);
            messagesContainer.getChildren().add(messageBubble);
        }
    }

    /**
     * Sends a message to the selected patient.
     *
     * @param doctor      The doctor sending the message.
     * @param messageText The message text.
     * @param patient     The selected patient.
     */
    public void sendMessage(Doctor doctor, String messageText, Patient patient) {
        PatientDataFetcher.sendPatientMessage(patient1.getUserID(), doctor.getUserID(), messageText);
    }

    /**
     * Builds the view for medical history.
     *
     * @return A VBox containing the medical history information.
     */
    private VBox buildMedicalHistoryView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label heading = new Label("Patient Medical History");
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

        historyTable.getColumns().addAll(idCol, surgeriesCol, familyHistoryCol, createdDateCol);
        historyTable.setItems(FXCollections.observableArrayList(
                PatientDataFetcher.getPatientMedicalHistory(patient1.getUserID())
        ));

        Label graphHeading = new Label("Vital Signs Trends");
        graphHeading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox chartsContainer = new VBox(25);
        chartsContainer.setPadding(new Insets(20, 0, 0, 0));

        List<VitalSign> vitals = PatientDataFetcher.getPatientVitals(patient1.getUserID());
        if (!vitals.isEmpty()) {
            chartsContainer.getChildren().addAll(
                    createVitalChart("Oxygen Level (%)", vitals, VitalSign::getOxygenLevel),
                    createVitalChart("Heart Rate (bpm)", vitals, VitalSign::getHeartRate),
                    createVitalChart("Blood Pressure (mmHg)", vitals, VitalSign::getBloodPressure),
                    createVitalChart("Temperature (°C)", vitals, VitalSign::getTemperature)
            );
        } else {
            chartsContainer.getChildren().add(new Label("No vital signs data available"));
        }

        layout.getChildren().addAll(heading, historyTable, graphHeading, chartsContainer);
        return layout;
    }

    /**
     * Creates a line chart for the given vital sign data.
     *
     * @param title         The title of the chart.
     * @param vitals        The list of vital signs.
     * @param valueExtractor A function to extract the value from a VitalSign object.
     * @return A VBox containing the chart and download button.
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

        // Download button
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
     * Builds the view for sending emails.
     *
     * @return A VBox containing the email sending form.
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
        sendButton.setOnAction(e -> {
            String receiverEmail = receiverEmailField.getText();
            String doctorEmail = doctor.getEmail();
            String password = passwordField.getText();
            String subject = subjectField.getText();
            String message = messageArea.getText();

            if (receiverEmail.isEmpty() || doctorEmail.isEmpty() || password.isEmpty() || subject.isEmpty() || message.isEmpty()) {
                showAlert("Error", "Email Sending Failed", "An error occurred while sending the email. Please try again.", Alert.AlertType.ERROR);
            } else {
                sendEmail(receiverEmail, doctorEmail, password, subject, message);
                showAlert("Email Sent", "Email Sent Successfully", "Your email has been sent successfully.", Alert.AlertType.INFORMATION);
            }
        });

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
     * Sends an email using the EmailSender class.
     *
     * @param receiverEmail The receiver's email address.
     * @param doctorEmail   The doctor's email address.
     * @param password      The sender's email password.
     * @param subject       The subject of the email.
     * @param message       The message body of the email.
     */
    private void sendEmail(String receiverEmail, String doctorEmail, String password, String subject, String message) {
        EmailSender.sendEmail(receiverEmail, doctorEmail, password, subject, message);
    }

    /**
     * Builds the view for feature details.
     *
     * @return A VBox containing the feature details.
     */
    private VBox buildViewDetails() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label heading = new Label("Doctor Dashboard - Feature Details");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox featureDetails = new VBox(10);
        featureDetails.getChildren().addAll(
                createFeatureDetail("Dashboard", "View an overview of your profile, availability, and key metrics."),
                createFeatureDetail("My Doctors", "View and manage the list of doctors appointed to you."),
                createFeatureDetail("Appointments", "Check and manage your scheduled appointments."),
                createFeatureDetail("Messages", "Communicate with doctors via the messaging system."),
                createFeatureDetail("Feedbacks", "Give feedback for doctors."),
                createFeatureDetail("Prescriptions", "View your prescriptions given by doctors."),
                createFeatureDetail("My Vitals", "Monitor and review your vital signs."),
                createFeatureDetail("Medical History", "Access your medical history."),
                createFeatureDetail("Add Vitals", "Add your vital details."),
                createFeatureDetail("Send Mail", "Send emails to doctors or other staff members.")
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
     * Sets the content of the main content area.
     *
     * @param content The content to display.
     */
    private void setContent(VBox content) {
        mainContent.getChildren().setAll(content.getChildren());
    }

    public static void main(String[] args) {
        launch();
    }
}



