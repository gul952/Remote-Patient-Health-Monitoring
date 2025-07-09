package com.example.project.DATABASE;
import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.Doctor_Patient_Interaction.Feedback;
import com.example.project.Doctor_Patient_Interaction.MedicalHistory;
import com.example.project.User_Management.*;
import com.example.project.DashBoard.DoctorDashboard.ChatMessage;
import com.example.project.Health_Data_Handling.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DataFetcher is a utility class that provides methods to fetch data from the database.
 */
public class DataFetcher {

    /**
     * Fetches doctor data from the database based on the provided doctor ID.
     *
     * @param doctorID The ID of the doctor to fetch data for.
     * @return A Doctor object containing the doctor's data, or null if not found.
     */
    public static Doctor getDoctorData(String doctorID) {
        String query = "SELECT u.userID, u.name, u.email, u.phoneNumber, u.address, u.age, u.gender, " +
                "d.specialization, d.licenseNumber, d.hospitalName, d.availableTime, d.experienceYears, " +
                "d.consultationFee, d.startTime, d.endTime " +
                "FROM user u " +
                "JOIN Doctors d ON u.userID = d.doctorID " +
                "WHERE d.doctorID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, doctorID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setUserID(rs.getString("userID"));
                doctor.setName(rs.getString("name"));
                doctor.setEmail(rs.getString("email"));
                doctor.setPhoneNumber(rs.getString("phoneNumber"));
                doctor.setAddress(rs.getString("address"));
                doctor.setAge(rs.getInt("age"));
                doctor.setGender(rs.getString("gender"));
                doctor.setSpecialization(Doctor.Specialization.valueOf(rs.getString("specialization")));
                doctor.setLicenseNumber(rs.getString("licenseNumber"));
                doctor.setHospitalName(rs.getString("hospitalName"));
                doctor.setAvailableTime(rs.getString("availableTime"));
                doctor.setExperienceYears(rs.getInt("experienceYears"));
                doctor.setConsultationFee(rs.getDouble("consultationFee"));
                doctor.setStartTime(rs.getString("startTime"));
                doctor.setEndTime(rs.getString("endTime"));

                List<String> availableDays = new ArrayList<>();
                String daysQuery = "SELECT availableDay FROM DoctorAvailableDays WHERE doctorID = ?";
                try (PreparedStatement daysPs = connection.prepareStatement(daysQuery)) {
                    daysPs.setString(1, doctorID);
                    ResultSet daysRs = daysPs.executeQuery();
                    while (daysRs.next()) {
                        availableDays.add(daysRs.getString("availableDay"));
                    }
                }
                doctor.setAvailableDays(availableDays);

                // Fetch assigned patients
                List<Patient> assignedPatients = getAssignedPatients(doctorID);
                doctor.setAssignedPatients(assignedPatients);

                return doctor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetches assigned patients for a given doctor ID.
     *
     * @param doctorID The ID of the doctor to fetch assigned patients for.
     * @return A list of Patient objects assigned to the doctor.
     */
    public static List<Patient> getAssignedPatients(String doctorID) {
        String query = "SELECT p.patientID, u.name, u.email, u.phoneNumber, u.address, u.age, u.gender, u.accountStatus, " +
                "p.bloodGroup, p.appointmentDate, " +
                "GROUP_CONCAT(DISTINCT pa.allergyType) AS allergies, " +
                "GROUP_CONCAT(DISTINCT pd.diseaseType) AS diseases " +
                "FROM DoctorAssignedPatients dap " +
                "JOIN Patients p ON dap.patientID = p.patientID " +
                "JOIN user u ON p.patientID = u.userID " +
                "LEFT JOIN PatientAllergies pa ON p.patientID = pa.patientID " +
                "LEFT JOIN PatientDiseases pd ON p.patientID = pd.patientID " +
                "WHERE dap.doctorID = ? " +
                "GROUP BY p.patientID";
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, doctorID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setUserID(rs.getString("patientID"));
                patient.setName(rs.getString("name"));
                patient.setEmail(rs.getString("email"));
                patient.setPhoneNumber(rs.getString("phoneNumber"));
                patient.setAddress(rs.getString("address"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setAccountStatus(rs.getInt("accountStatus") == 1);
                patient.setBloodGroup(rs.getString("bloodGroup"));

                // Handle null appointmentDate
                if (rs.getTimestamp("appointmentDate") != null) {
                    patient.setAppointmentDate(rs.getTimestamp("appointmentDate").toLocalDateTime());
                }

                // Parse allergies
                String allergies = rs.getString("allergies");
                if (allergies != null) {
                    patient.setAllergies(Arrays.stream(allergies.split(","))
                            .map(String::trim)
                            .map(Patient.AllergyType::valueOf)
                            .toList());
                }

                // Parse diseases
                String diseases = rs.getString("diseases");
                if (diseases != null) {
                    patient.setDiseases(Arrays.stream(diseases.split(","))
                            .map(String::trim)
                            .map(Patient.DiseaseType::valueOf)
                            .toList());
                }

                // Fetch and set medical histories
                List<MedicalHistory> histories = getMedicalHistoriesForPatient(patient.getUserID(), connection);
                patient.setMedicalHistories(histories);

                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    /**
     * Fetches all appointments for a given doctor ID.
     *
     * @param doctorID The ID of the doctor to fetch appointments for.
     * @return A list of Appointment objects for the specified doctor.
     */
    public static List<Appointment> getDoctorAppointments(String doctorID) {
        String query = "SELECT appointmentID, patientID, doctorID, appointmentDateTime, notes, reason, appointmentStatus " +
                "FROM Appointments WHERE doctorID = ?";
        List<Appointment> appointments = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, doctorID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(rs.getString("appointmentID"));
                appointment.setPatient(rs.getString("patientID"));
                appointment.setDoctor(rs.getString("doctorID"));
                appointment.setAppointmentDateTime(rs.getTimestamp("appointmentDateTime").toLocalDateTime());
                appointment.setNotes(rs.getString("notes"));
                appointment.setReason(rs.getString("reason"));
                appointment.setAppointmentStatus(Appointment.AppointmentStatus.valueOf(rs.getString("appointmentStatus")));

                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * Fetches all chat messages between a doctor and a patient.
     *
     * @param doctorID   The ID of the doctor.
     * @param receiverID The ID of the receiver (patient).
     * @return A list of ChatMessage objects representing the chat history.
     */
    public static List<ChatMessage> getMessagesByDoctor(String doctorID, String receiverID) {
        String query = "SELECT id, sender_id, receiver_id, message_text, timestamp, is_read " +
                "FROM ChatMessages " +
                "WHERE sender_id = ? AND receiver_id = ? " +
                "ORDER BY timestamp DESC";
        List<ChatMessage> messages = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, doctorID);
            ps.setString(2, receiverID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChatMessage message = new ChatMessage();
                message.setId(rs.getInt("id"));
                message.setSenderId(rs.getString("sender_id"));
                message.setReceiverId(rs.getString("receiver_id"));
                message.setMessageText(rs.getString("message_text"));
                message.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                message.setRead(rs.getBoolean("is_read"));

                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * Fetches all feedbacks for a given doctor ID.
     *
     * @param doctorID The ID of the doctor to fetch feedbacks for.
     * @return A list of Feedback objects for the specified doctor.
     */
    public static List<Feedback> getFeedbacks(String doctorID) {
        String query = "SELECT feedbackID, doctorID, patientID, rating, comments, feedbackDate, status, isAnonymous " +
                "FROM Feedbacks WHERE doctorID = ?";
        List<Feedback> feedbackList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, doctorID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackID(rs.getString("feedbackID"));
                feedback.setDoctor(rs.getString("doctorID"));
                feedback.setPatient(rs.getString("patientID"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setComments(rs.getString("comments"));
                feedback.setFeedbackDate(rs.getTimestamp("feedbackDate").toLocalDateTime());
                feedback.setStatus(Feedback.FeedbackStatus.valueOf(rs.getString("status")));
                feedback.setIsAnonymous(rs.getBoolean("isAnonymous"));

                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    /**
     * Fetches all vital signs for a given doctor ID.
     *
     * @param doctorID The ID of the doctor to fetch vital signs for.
     * @return A list of VitalSign objects for the specified doctor.
     */
    public static List<VitalSign> getVitalSignsByDoctor(String doctorID) {
        String query = "SELECT recordID, patientID, doctorID, heartRate, oxygenLevel, bloodPressure, temperature, " +
                "respiratoryRate, glucoseLevel, cholesterolLevel, bmi, hydrationLevel, stressLevel, recordDate " +
                "FROM VitalSigns WHERE doctorID = ?";
        List<VitalSign> vitalSignsList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, doctorID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                VitalSign vitalSign = new VitalSign();
                vitalSign.setRecordID(rs.getString("recordID"));
                vitalSign.setUserID(rs.getString("patientID"));
                vitalSign.setDoctorID(rs.getString("doctorID"));
                vitalSign.setHeartRate(rs.getInt("heartRate"));
                vitalSign.setOxygenLevel(rs.getInt("oxygenLevel"));
                vitalSign.setBloodPressure(rs.getInt("bloodPressure"));
                vitalSign.setTemperature(rs.getDouble("temperature"));
                vitalSign.setRespiratoryRate(rs.getInt("respiratoryRate"));
                vitalSign.setGlucoseLevel(rs.getDouble("glucoseLevel"));
                vitalSign.setCholesterolLevel(rs.getDouble("cholesterolLevel"));
                vitalSign.setBmi(rs.getDouble("bmi"));
                vitalSign.setHydrationLevel(rs.getDouble("hydrationLevel"));
                vitalSign.setStressLevel(rs.getInt("stressLevel"));
                vitalSign.setRecordDateTime(rs.getTimestamp("recordDate").toLocalDateTime());

                vitalSignsList.add(vitalSign);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vitalSignsList;
    }

    /**
     * Fetches all medical histories for a given patient ID.
     *
     * @param patientID The ID of the patient to fetch medical histories for.
     * @param connection The database connection to use.
     * @return A list of MedicalHistory objects for the specified patient.
     * @throws SQLException if a database access error occurs
     */
    private static List<MedicalHistory> getMedicalHistoriesForPatient(String patientID, Connection connection) throws SQLException {
        String query = "SELECT medicalHistoryID, patientID, createdDate, surgeries, familyHistory FROM MedicalHistory WHERE patientID = ?";
        List<MedicalHistory> histories = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, patientID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MedicalHistory history = new MedicalHistory();
                history.setMedicalHistoryID(rs.getString("medicalHistoryID"));
                history.setPatient(rs.getString("patientID"));
                history.setCreatedDate(rs.getTimestamp("createdDate").toLocalDateTime());
                history.setSurgeries(rs.getString("surgeries"));
                history.setFamilyHistory(rs.getString("familyHistory"));
                histories.add(history);
            }
        }

        return histories;
    }


    /**
     * Fetches all feedbacks for a given patient ID.
     *
     * @param patientID The ID of the patient to fetch feedbacks for.
     * @return A list of Feedback objects for the specified patient.
     */
    // This method should be implemented to update the appointment status in your database
    public static void updateAppointmentStatusInDB(String appointmentID, String newStatus) {
        // Example using JDBC, update as needed for your setup:
        String sql = "UPDATE Appointments SET appointmentStatus = ? WHERE appointmentID = ?";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, appointmentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Add this method to update status in the database
    /**
     * Updates the status of a feedback in the database.
     *
     * @param feedbackID The ID of the feedback to update.
     * @param newStatus  The new status to set.
     */
    public static void updateFeedbackStatusInDB(String feedbackID, String newStatus) {
        String sql = "UPDATE Feedbacks SET status = ? WHERE feedbackID = ?";
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, feedbackID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally show an alert here
        }
    }



}
