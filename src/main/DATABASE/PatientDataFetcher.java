package com.example.project.DATABASE;
import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.EmergencyAlertSystem.EmergencyAlert;
import com.example.project.EmergencyAlertSystem.NotificationService;
import com.example.project.NotificationsAndReminders.EmailNotification;
import com.example.project.User_Management.*;
import com.example.project.DashBoard.DoctorDashboard.ChatMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//GUL
import com.example.project.Doctor_Patient_Interaction.MedicalHistory;
import com.example.project.Doctor_Patient_Interaction.Prescription;
import com.example.project.Health_Data_Handling.VitalSign;
import javafx.scene.layout.VBox;

import static com.example.project.DATABASE.DataFetcher.getDoctorData;

public class PatientDataFetcher {

    /**
     * Fetch comprehensive Patient data for Dashboard tabs, mirroring getDoctorData.
     */
    public static Patient getPatientData(String patientID) {
        String query = "SELECT u.userID, u.name, u.email, u.phoneNumber, u.address, u.age, u.gender, " +
                "p.bloodGroup, p.appointmentDate " +
                "FROM user u " +
                "JOIN Patients p ON u.userID = p.patientID " +
                "WHERE p.patientID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, patientID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Patient patient = new Patient();
                patient.setUserID(rs.getString("userID"));
                patient.setName(rs.getString("name"));
                patient.setEmail(rs.getString("email"));
                patient.setPhoneNumber(rs.getString("phoneNumber"));
                patient.setAddress(rs.getString("address"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setBloodGroup(rs.getString("bloodGroup"));
                if (rs.getTimestamp("appointmentDate") != null) {
                    patient.setAppointmentDate(rs.getTimestamp("appointmentDate").toLocalDateTime());
                }

                // allergies
                String allergyQ = "SELECT allergyType FROM PatientAllergies WHERE patientID = ?";
                try (PreparedStatement aPs = connection.prepareStatement(allergyQ)) {
                    aPs.setString(1, patientID);
                    ResultSet aRs = aPs.executeQuery();
                    List<Patient.AllergyType> list = new ArrayList<>();
                    while (aRs.next()) {
                        list.add(Patient.AllergyType.valueOf(aRs.getString("allergyType")));
                    }
                    patient.setAllergies(list);
                }

                // diseases
                String diseaseQ = "SELECT diseaseType FROM PatientDiseases WHERE patientID = ?";
                try (PreparedStatement dPs = connection.prepareStatement(diseaseQ)) {
                    dPs.setString(1, patientID);
                    ResultSet dRs = dPs.executeQuery();
                    List<Patient.DiseaseType> list = new ArrayList<>();
                    while (dRs.next()) {
                        list.add(Patient.DiseaseType.valueOf(dRs.getString("diseaseType")));
                    }
                    patient.setDiseases(list);
                }

                // assigned doctor
                String docQ =
                        "SELECT d.doctorID, u.name, u.email, u.phoneNumber " +
                                "FROM DoctorAssignedPatients dap " +
                                " JOIN Doctors d ON dap.doctorID = d.doctorID " +
                                " JOIN user u    ON d.doctorID = u.userID " +
                                "WHERE dap.patientID = ? LIMIT 1";
                try ( PreparedStatement ds = connection.prepareStatement(docQ) ) {
                    ds.setString(1, patientID);
                    ResultSet drs = ds.executeQuery();
                    if (drs.next()) {
                        Doctor doc = new Doctor();
                        doc.setUserID(drs.getString("doctorID"));
                        doc.setName(drs.getString("name"));
                        doc.setEmail(drs.getString("email"));
                        doc.setPhoneNumber(drs.getString("phoneNumber"));
                        patient.setAssignedDoctor(doc);
                    }
                }

                return patient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetch all Appointments for a given patient.
     */
    public static List<Appointment> getPatientAppointments(String patientID) {
        String query = "SELECT appointmentID, patientID, doctorID, appointmentDateTime, notes, reason, appointmentStatus " +
                "FROM Appointments WHERE patientID = ?";
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentID(rs.getString("appointmentID"));
                a.setPatient(rs.getString("patientID"));
                a.setDoctor(rs.getString("doctorID"));
                a.setAppointmentDateTime(rs.getTimestamp("appointmentDateTime").toLocalDateTime());
                a.setNotes(rs.getString("notes"));
                a.setReason(rs.getString("reason"));
                a.setAppointmentStatus(Appointment.AppointmentStatus.valueOf(rs.getString("appointmentStatus")));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Fetch all VitalSigns recorded for a patient.
     */
    public static List<VitalSign> getPatientVitals(String patientID) {
        String query = "SELECT * FROM VitalSigns WHERE patientID = ?";
        List<VitalSign> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                VitalSign v = new VitalSign();
                v.setRecordID(rs.getString("recordID"));
                v.setUserID(rs.getString("patientID"));
                v.setDoctorID(rs.getString("doctorID"));
                v.setHeartRate(rs.getInt("heartRate"));
                v.setOxygenLevel(rs.getInt("oxygenLevel"));
                v.setBloodPressure(rs.getInt("bloodPressure"));
                v.setTemperature(rs.getDouble("temperature"));
                v.setRespiratoryRate(rs.getInt("respiratoryRate"));
                v.setGlucoseLevel(rs.getDouble("glucoseLevel"));
                v.setCholesterolLevel(rs.getDouble("cholesterolLevel"));
                v.setBmi(rs.getDouble("bmi"));
                v.setHydrationLevel(rs.getDouble("hydrationLevel"));
                v.setStressLevel(rs.getInt("stressLevel"));
                v.setRecordDateTime(rs.getTimestamp("recordDate").toLocalDateTime());
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Fetch medical history entries for a patient.
     */
    public static List<MedicalHistory> getPatientMedicalHistory(String patientID) {
        String query = "SELECT historyID, patientID, surgeries, familyHistory, createdDate " +
                "FROM MedicalHistory WHERE patientID = ?";
        List<MedicalHistory> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MedicalHistory mh = new MedicalHistory();
                mh.setMedicalHistoryID(rs.getString("historyID"));
                mh.setSurgeries(rs.getString("surgeries"));
                mh.setFamilyHistory(rs.getString("familyHistory"));
                mh.setCreatedDate(rs.getTimestamp("createdDate").toLocalDateTime());
                list.add(mh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * Fetch prescriptions for a patient.
     */
    public static List<Prescription> getPatientPrescriptions(String patientID) {
        String query = "SELECT prescriptionID, doctorID, dosageInstructions, startDate, endDate, dosageSchedule, quantity, createdDate " +
                "FROM Prescriptions WHERE patientID = ?";
        List<Prescription> list = new ArrayList<>();
        Patient basePatient = getPatientData(patientID);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionID(rs.getString("prescriptionID"));
                // set the full Patient object
                p.setPatient(basePatient);
                // fetch and set the Doctor object
                Doctor doc = getDoctorData(rs.getString("doctorID"));
                p.setDoctor(doc);
                p.setDosageInstructions(rs.getString("dosageInstructions"));
                p.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
                p.setEndDate(rs.getTimestamp("endDate").toLocalDateTime());
                p.setDosageSchedule(rs.getString("dosageSchedule"));
                p.setQuantity(rs.getString("quantity"));
                p.setCreatedDate(rs.getTimestamp("createdDate").toLocalDateTime());
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Fetch chat messages between patient and their assigned doctor.
     */
    public static List<ChatMessage> getPatientMessages(String patientID, String doctorID) {
        String query = "SELECT id, sender_id, receiver_id, message_text, timestamp, is_read " +
                "FROM ChatMessages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
                "ORDER BY timestamp ASC";
        List<ChatMessage> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, patientID);
            ps.setString(2, doctorID);
            ps.setString(3, doctorID);
            ps.setString(4, patientID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatMessage m = new ChatMessage();
                m.setId(rs.getInt("id"));
                m.setSenderId(rs.getString("sender_id"));
                m.setReceiverId(rs.getString("receiver_id"));
                m.setMessageText(rs.getString("message_text"));
                m.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                m.setRead(rs.getBoolean("is_read"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * Persist a new chat message from patient → doctor.
     */
    public static void sendPatientMessage(String patientID, String doctorID, String text) {
        if (text == null || text.trim().isEmpty()) return;
        String sql =
                "INSERT INTO ChatMessages(sender_id, receiver_id, message_text, timestamp, is_read) " +
                        "VALUES (?, ?, ?, CURRENT_TIMESTAMP, FALSE)";
        try ( Connection conn = DBConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql) ) {
            ps.setString(1, patientID);
            ps.setString(2, doctorID);
            ps.setString(3, text);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static VBox insertVitalSign(VitalSign vitalSign, Patient patient, Doctor doctor) {
        String insertQuery = "INSERT INTO VitalSigns (recordID, patientID, doctorID, heartRate, oxygenLevel, " +
                "bloodPressure, temperature, respiratoryRate, glucoseLevel, cholesterolLevel, bmi, " +
                "hydrationLevel, stressLevel, recordDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        VBox vBox = new VBox();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertQuery)) {

            ps.setString(1, vitalSign.getRecordID());
            ps.setString(2, vitalSign.getUserID());
            ps.setString(3, vitalSign.getDoctorID());
            ps.setInt(4, vitalSign.getHeartRate());
            ps.setInt(5, vitalSign.getOxygenLevel());
            ps.setInt(6, vitalSign.getBloodPressure());
            ps.setDouble(7, vitalSign.getTemperature());
            ps.setInt(8, vitalSign.getRespiratoryRate());
            ps.setDouble(9, vitalSign.getGlucoseLevel());
            ps.setDouble(10, vitalSign.getCholesterolLevel());
            ps.setDouble(11, vitalSign.getBmi());
            ps.setDouble(12, vitalSign.getHydrationLevel());
            ps.setInt(13, vitalSign.getStressLevel());
            ps.setTimestamp(14, Timestamp.valueOf(vitalSign.getRecordDateTime()));




            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Vital sign record inserted successfully.");
            } else {
                System.out.println("Failed to insert the vital sign record.");
            }

            NotificationService notifier = new NotificationService();

            EmergencyAlert emergencyAlert = new EmergencyAlert();
            if(emergencyAlert.checkOnlyVitals(vitalSign)) {
                EmailNotification emailNotification = new EmailNotification(patient.getEmail(), emergencyAlert.getMessage(vitalSign), notifier);
                emailNotification.sendRemainder();
                EmailNotification emailNotification2 = new EmailNotification(doctor.getEmail(), emergencyAlert.getMessage(vitalSign), notifier);
                emailNotification2.sendRemainder();
                String msg = "Patient username: " + patient.getUserID() + "\n" +
                        "Name: " + patient.getName() + "\n" +
                        "has uploaded vital on Time: " + vitalSign.getRecordDateTime() + "\n" +
                        "and it is exceeding the threshold.\n" +
                        "You have received an email, check it.";
                vBox.getChildren().add(new javafx.scene.control.Label(msg));
            }
            else {
                String msg = "No new Notifications";
                vBox.getChildren().add(new javafx.scene.control.Label(msg));
            }

        } catch (SQLException e) {
            System.err.println("Error occurred while inserting vital sign: " + e.getMessage());
        }
        return vBox;
    }

    /**
     * Add feedback from a patient to a doctor.
     *
     * @param patientID  The ID of the patient providing the feedback.
     * @param doctorID   The ID of the doctor receiving the feedback.
     * @param rating     The rating given by the patient (1-5).
     * @param comments   Additional comments from the patient.
     * @param isAnonymous Whether the feedback is anonymous or not.
     */
    public static void addFeedbacksByPatient(String patientID, String doctorID,
                                             int rating, String comments, boolean isAnonymous) {
        String feedbackID = UUID.randomUUID().toString();
        String insertQuery = "INSERT INTO Feedbacks (feedbackID, doctorID, patientID, rating, comments, feedbackDate, status, isAnonymous) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, 'PENDING', ?)";

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(insertQuery)) {

                ps.setString(1, feedbackID);
                ps.setString(2, doctorID);
                ps.setString(3, patientID);
                ps.setInt(4, rating);
                ps.setString(5, comments);
                ps.setBoolean(6, isAnonymous);

                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Feedback inserted successfully.");
                } else {
                    System.out.println("Failed to insert feedback.");
                }
            } catch (SQLException e) {
                System.err.println("Error occurred while inserting feedback: " + e.getMessage());
            }

    }


}


