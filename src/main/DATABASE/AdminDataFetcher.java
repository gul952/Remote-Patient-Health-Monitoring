package com.example.project.DATABASE;

import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.EmergencyAlertSystem.EmergencyAlert;
import com.example.project.EmergencyAlertSystem.NotificationService;
import com.example.project.NotificationsAndReminders.EmailNotification;
import com.example.project.User_Management.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminDataFetcher {

    /**
     * Fetches Admin data for the specified adminID.
     * @param adminID the ID of the admin
     * @return Admin object, or null if not found/error
     */
    public static Administrator getAdminData(String adminID) {
        Administrator admin = null;
        String sql = "SELECT a.adminID, a.role, a.department, " +
                "u.userID, u.name, u.email, u.phoneNumber, u.password, u.address, u.age, u.gender, u.accountStatus " +
                "FROM Administrators a " +
                "JOIN user u ON a.adminID = u.userID " +
                "WHERE a.adminID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adminID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                admin = new Administrator();
                admin.setRole(Administrator.AdminRole.valueOf(rs.getString("role")));
                admin.setDepartment(rs.getString("department"));

                // Set fields from the user table
                admin.setUserID(rs.getString("userID"));
                admin.setName(rs.getString("name"));
                admin.setEmail(rs.getString("email"));
                admin.setPhoneNumber(rs.getString("phoneNumber"));
                admin.setPassword(rs.getString("password"));
                admin.setAddress(rs.getString("address"));
                admin.setAge(rs.getInt("age"));
                admin.setGender(rs.getString("gender"));
                admin.setAccountStatus(rs.getBoolean("accountStatus"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use proper logging
        }
        return admin;
    }

    /**
     * Fetches all doctors from the database.
     * @return List of Doctor objects
     */
    public static List<Doctor> getAllDoctors() {
        String query = "SELECT d.doctorID, u.name, u.email, u.phoneNumber, u.address, u.age, u.gender, " +
                "d.specialization, d.licenseNumber, d.hospitalName, d.availableTime, d.experienceYears, d.consultationFee, d.startTime, d.endTime, " +
                "GROUP_CONCAT(DISTINCT dad.availableDay) AS availableDays " +
                "FROM Doctors d " +
                "JOIN user u ON d.doctorID = u.userID " +
                "LEFT JOIN DoctorAvailableDays dad ON d.doctorID = dad.doctorID " +
                "GROUP BY d.doctorID";

        List<Doctor> doctors = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setUserID(rs.getString("doctorID"));
                doctor.setUserID(rs.getString("doctorID")); // Assuming userID == doctorID
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

                // Parse available days, if any
                String availableDays = rs.getString("availableDays");
                if (availableDays != null) {
                    doctor.setAvailableDays(Arrays.stream(availableDays.split(","))
                            .map(String::trim)
                            .toList());
                    // If you have an enum, use .map(Doctor.AvailableDay::valueOf) instead
                }

                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    
    /**
     * Fetches all patients from the database.
     * @return List of Patient objects
        * @throws SQLException if a database access error occurs    
        * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public static List<Patient> getAllPatients() {
        String query = "SELECT p.patientID, u.name, u.email, u.phoneNumber, u.address, u.age, u.gender, " +
                "p.bloodGroup, p.appointmentDate, p.assignedDoctorID, " +
                "GROUP_CONCAT(DISTINCT pa.allergyType) AS allergies, " +
                "GROUP_CONCAT(DISTINCT pd.diseaseType) AS diseases " +
                "FROM Patients p " +
                "JOIN user u ON p.patientID = u.userID " +
                "LEFT JOIN PatientAllergies pa ON p.patientID = pa.patientID " +
                "LEFT JOIN PatientDiseases pd ON p.patientID = pd.patientID " +
                "GROUP BY p.patientID";
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

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
                patient.setBloodGroup(rs.getString("bloodGroup"));

                String docQ =
                        "SELECT d.doctorID, u.name, u.email, u.phoneNumber " +
                                "FROM DoctorAssignedPatients dap " +
                                " JOIN Doctors d ON dap.doctorID = d.doctorID " +
                                " JOIN user u    ON d.doctorID = u.userID " +
                                "WHERE dap.patientID = ? LIMIT 1";
                try ( PreparedStatement ds = connection.prepareStatement(docQ) ) {
                    ds.setString(1, patient.getUserID());
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

                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    /**
     * Fetches all appointments from the database.
     * @return List of Appointment objects
     */
    public static List<Appointment> getAllAppointments() {
        String query = "SELECT a.appointmentID, a.patientID, a.doctorID, a.appointmentDateTime, " +
                "a.notes, a.reason, a.appointmentStatus " +
                "FROM Appointments a";

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();

                appointment.setAppointmentID(rs.getString("appointmentID"));
                appointment.setPatient(rs.getString("patientID"));
                appointment.setDoctor(rs.getString("doctorID"));
                if (rs.getTimestamp("appointmentDateTime") != null) {
                    appointment.setAppointmentDateTime(rs.getTimestamp("appointmentDateTime").toLocalDateTime());
                }
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
     * Adds an appointment to the database and sends notifications to the patient and doctor.
     * @param appointment The appointment object to be added
     * @param patient The patient object associated with the appointment
     * @param doctor The doctor object associated with the appointment
     */
    public static void addAppointmentToDatabase( Appointment appointment, Patient patient, Doctor doctor) {
        String sql = "INSERT INTO Appointments (appointmentID, patientID, doctorID, appointmentDateTime, notes, reason, appointmentStatus) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointment.getAppointmentID());
            stmt.setString(2, appointment.getPatient());
            stmt.setString(3, appointment.getDoctor());
            stmt.setTimestamp(4, Timestamp.valueOf(appointment.getAppointmentDateTime()));
            stmt.setString(5, appointment.getNotes() != null ? appointment.getNotes() : "");
            stmt.setString(6, appointment.getReason());
            stmt.setString(7, appointment.getAppointmentStatus().name());
            stmt.executeUpdate();

            NotificationService notifier = new NotificationService();

            EmergencyAlert emergencyAlert = new EmergencyAlert();

            String message = "Reminder: appointment Scheduled Between Dr. " + doctor.getName() + " and Patient " + patient.getName() + " on " + appointment.getAppointmentDateTime() + ".\n" +
                    "Notes: " + appointment.getNotes() + "\n" +
                    "Reason: " + appointment.getReason();
            EmailNotification emailNotification = new EmailNotification(patient.getEmail(), message , notifier);
            emailNotification.sendRemainder();
            EmailNotification emailNotification2 = new EmailNotification(doctor.getEmail(), message , notifier);
            emailNotification2.sendRemainder();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the account status of a user.
     *
     * @param userID   The unique ID of the user.
     * @param newStatus The new status to set.
     */
    public static void updateAccountStatus(String userID, boolean newStatus) {
        String sql = "UPDATE User SET accountStatus = ? WHERE userID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, newStatus);
            stmt.setString(2, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Deletes a user from the database by their userID.
     *
     * @param userID The unique ID of the user to be deleted.
     */
    public static void deleteUserByID(String userID) {
        String sql = "DELETE FROM User WHERE userID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}