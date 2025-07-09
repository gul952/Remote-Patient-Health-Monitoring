package com.example.project.User_Management;

import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.Appointment_Scheduling.AppointmentManager;
import com.example.project.Doctor_Patient_Interaction.Feedback;
import com.example.project.Doctor_Patient_Interaction.MedicalHistory;
import com.example.project.Doctor_Patient_Interaction.Prescription;
import com.example.project.Health_Data_Handling.VitalSign;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a Doctor in the system who manages appointments, patients, and provides medical care.
 */
public class Doctor extends User {

    /**
     * Enum representing the specialization of a doctor.
     */
    public enum Specialization {
        CARDIOLOGIST,
        NEUROLOGIST,
        ORTHOPEDIC_SURGEON,
        PEDIATRICIAN,
        DERMATOLOGIST,
        PSYCHIATRIST,
        GENERAL_PHYSICIAN,
        ENT_SPECIALIST,
        RADIOLOGIST,
        OPHTHALMOLOGIST;
    }

    // Attributes
    private Specialization specialization;
    private String licenseNumber;
    private String hospitalName;
    private String availableTime;
    private int experienceYears;
    private double consultationFee;
    private List<String> availableDays;
    private List<LocalDateTime> appointmentDates = new ArrayList<>();
    private List<Patient> assignedPatients = new ArrayList<>();
    private List<Feedback> feedbackList = new ArrayList<>();
    private String startTime;
    private String endTIme;
    private List<Appointment> appointments = new ArrayList<>();
    private List<VitalSign> vitalSigns = new ArrayList<>();

    /**
     * Default constructor for Doctor.
     */
    public Doctor() {
        this.availableDays = new ArrayList<>();
        this.appointmentDates = new ArrayList<>();
        this.assignedPatients = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.feedbackList = new ArrayList<>();
        this.vitalSigns = new ArrayList<>();
    }

    /**
     * Parameterized constructor for Doctor.
     *
     * @param userID          The unique ID of the doctor.
     * @param name            The name of the doctor.
     * @param email           The email of the doctor.
     * @param phoneNumber     The phone number of the doctor.
     * @param password        The password of the doctor.
     * @param address         The address of the doctor.
     * @param age             The age of the doctor.
     * @param gender          The gender of the doctor.
     * @param accountStatus   The account status of the doctor.
     * @param specialization  The specialization of the doctor.
     * @param licenseNumber   The license number of the doctor.
     * @param hospitalName    The hospital name where the doctor works.
     * @param availableTime   The available time of the doctor.
     * @param experienceYears The years of experience of the doctor.
     * @param consultationFee The consultation fee of the doctor.
     * @param availableDays   The days the doctor is available.
     */
    public Doctor(String userID, String name, String email, String phoneNumber, String password,
                  String address, int age, String gender, boolean accountStatus,
                  Specialization specialization, String licenseNumber, String hospitalName,
                  String availableTime, int experienceYears, double consultationFee,
                  List<String> availableDays) {
        super(userID, hospitalName, email, phoneNumber, password, address, age, gender, accountStatus);
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
        this.availableTime = availableTime;
        this.hospitalName = hospitalName;
        this.consultationFee = consultationFee;
        this.experienceYears = experienceYears;
        this.availableDays = (availableDays != null) ? new ArrayList<>(availableDays) : new ArrayList<>();
        this.appointmentDates = new ArrayList<>();
        this.assignedPatients = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    // Setters with validation

    /**
     * Sets the specialization of the doctor.
     *
     * @param specialization The specialization to set.
     */
    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    /**
     * Sets the license number of the doctor.
     *
     * @param licenseNumber The license number to set.
     */
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    /**
     * Sets the hospital name of the doctor.
     *
     * @param hospitalName The hospital name to set.
     */
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    /**
     * Sets the available time of the doctor.
     *
     * @param availableTime The available time to set.
     */
    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    /**
     * Sets the years of experience of the doctor.
     *
     * @param experienceYears The years of experience to set.
     */
    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            throw new IllegalArgumentException("Experience years cannot be less than 0!");
        }
        this.experienceYears = experienceYears;
    }

    /**
     * Sets the consultation fee of the doctor.
     *
     * @param consultationFee The consultation fee to set.
     */
    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    /**
     * Sets the available days of the doctor.
     *
     * @param availableDays The available days to set.
     */
    public void setAvailableDays(List<String> availableDays) {
        this.availableDays = availableDays;
    }

    /**
     * Sets the appointment dates of the doctor.
     *
     * @param appointmentDates The appointment dates to set.
     */
    public void setAppointmentDates(List<LocalDateTime> appointmentDates) {
        this.appointmentDates = new ArrayList<>(appointmentDates);
    }

    /**
     * Sets the assigned patients of the doctor.
     *
     * @param assignedPatients The assigned patients to set.
     */
    public void setAssignedPatients(List<Patient> assignedPatients) {
        this.assignedPatients = new ArrayList<>(assignedPatients);
    }

    /**
     * Sets the appointments of the doctor.
     *
     * @param appointments The appointments to set.
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    /**
     * Sets the feedback list of the doctor.
     *
     * @param feedbackList The feedback list to set.
     */
    public void setFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    /**
     * Sets the start time of the doctor.
     *
     * @param startTime The start time to set.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the end time of the doctor.
     *
     * @param endTime The end time to set.
     */
    public void setEndTime(String endTime) {
        this.endTIme = endTime;
    }

    /**
     * Sets the vital signs of the doctor.
     *
     * @param vitalSigns The vital signs to set.
     */
    public void setVitalSigns(List<VitalSign> vitalSigns) {
        this.vitalSigns = new ArrayList<>(vitalSigns);
    }

    // Getters

    /**
     * Gets the specialization of the doctor.
     *
     * @return The specialization of the doctor.
     */
    public Specialization getSpecialization() {
        return specialization;
    }

    /**
     * Gets the license number of the doctor.
     *
     * @return The license number of the doctor.
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * Gets the hospital name of the doctor.
     *
     * @return The hospital name of the doctor.
     */
    public String getHospitalName() {
        return hospitalName;
    }

    /**
     * Gets the available time of the doctor.
     *
     * @return The available time of the doctor.
     */
    public String getAvailableTime() {
        return availableTime;
    }

    /**
     * Gets the years of experience of the doctor.
     *
     * @return The years of experience of the doctor.
     */
    public int getExperienceYears() {
        return experienceYears;
    }

    /**
     * Gets the consultation fee of the doctor.
     *
     * @return The consultation fee of the doctor.
     */
    public double getConsultationFee() {
        return consultationFee;
    }

    /**
     * Gets the available days of the doctor.
     *
     * @return The available days of the doctor.
     */
    public List<String> getAvailableDays() {
        return availableDays;
    }

    /**
     * Gets the appointment dates of the doctor.
     *
     * @return The appointment dates of the doctor.
     */
    public List<LocalDateTime> getAppointmentDates() {
        return (appointmentDates == null) ? new ArrayList<>() : appointmentDates;
    }

    /**
     * Gets the assigned patients of the doctor.
     *
     * @return The assigned patients of the doctor.
     */
    public List<Patient> getAssignedPatients() {
        return new ArrayList<>(assignedPatients);
    }

    /**
     * Gets the feedback list of the doctor.
     *
     * @return The feedback list of the doctor.
     */
    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    /**
     * Gets the start time of the doctor.
     *
     * @return The start time of the doctor.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the doctor.
     *
     * @return The end time of the doctor.
     */
    public String getEndTime() {
        return endTIme;
    }

    /**
     * Gets the appointments of the doctor.
     *
     * @return The appointments of the doctor.
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Gets the vital signs of the doctor.
     *
     * @return The vital signs of the doctor.
     */
    public List<VitalSign> getVitalSigns() {
        return vitalSigns;
    }

    @Override
    public String toString() {
        return String.format("%s%nSpecialization: %s%nLicense Number: %s%nHospital Name: %s%nAvailable Time: %s%nYear of Experience: %d%nConsultation Fee :%.2f%nAvailable Days: %s",
                super.toString(), this.specialization, this.licenseNumber, this.hospitalName, this.availableTime, this.experienceYears, this.consultationFee, this.availableDays);
    }

    // Methods

    public void assignPatient(Patient patient) {
        if (patient != null) {
            assignedPatients.add(patient);
        }
    }


    /**
     * Removes a patient from the doctor's assigned patients list.
     *
     * @param patient The patient to remove.
     */
    public void removePatient(Patient patient) {
        assignedPatients.remove(patient);
    }

    /**
     * Views all the patients assigned to the doctor.
     *
     * @return A list of all assigned patients.
     */
    public List<Patient> viewAllAssignedPatients() {
        return assignedPatients;
    }

    /**
     * Counts the number of patients assigned to the doctor.
     *
     * @return The number of assigned patients.
     */
    public int countAssignedPatients() {
        return assignedPatients.size();
    }

    /**
     * Checks if the doctor is available on a specific day.
     *
     * @param day The day to check.
     * @return True if the doctor is available, false otherwise.
     */
    public boolean isAvailableOnDay(String day) {
        return availableDays.contains(day);
    }

    /**
     * Updates the consultation fee of the doctor.
     *
     * @param newFee The new consultation fee.
     */
    public void updateConsultationFee(double newFee) {
        setConsultationFee(newFee);
    }

    /**
     * Adds an appointment date to the doctor's schedule.
     *
     * @param appointmentDate The appointment date to add.
     */
    public void addAppointmentDate(LocalDateTime appointmentDate) {
        appointmentDates.add(appointmentDate);
    }

    /**
     * Adds an appointment to the doctor's list of appointments.
     *
     * @param appointment The appointment to add.
     */
    public void addAppointment(Appointment appointment) {
        if (appointment != null) {
            appointments.add(appointment);
        }
    }

    /**
     * Receives feedback from a patient.
     *
     * @param feedback The feedback to add.
     */
    public void receiveFeedback(Feedback feedback) {
        feedbackList.add(feedback);
    }

    /**
     * Retrieves a patient by their user ID.
     *
     * @param patientID The user ID of the patient.
     * @return The patient, or null if not found.
     */
    public Patient getPatient(String patientID) {
        for (Patient patient : assignedPatients) {
            if (patient.getUserID().equals(patientID)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Views all feedback sent to the doctor.
     *
     * @return A list of feedback.
     */
    public List<Feedback> seeFeedbacks() {
        return feedbackList;
    }

    /**
     * Views the medical history of a specific patient.
     *
     * @param patient The patient whose medical history is to be viewed.
     * @return A list of medical histories for the patient.
     */
    public List<MedicalHistory> viewPatientMedicalHistory(Patient patient) {
        return patient.getMedicalHistory();
    }

    /**
     * Requests an appointment for a patient.
     *
     * @param patientID          The user ID of the patient.
     * @param appointmentDateTime The date and time of the appointment.
     * @param reason             The reason for the appointment.
     * @param appointmentManager The appointment manager to handle the request.
     * @param administrator      The administrator to approve the request.
     * @return True if the appointment request was successful, false otherwise.
     */
    public boolean requestAppointment(String patientID, LocalDateTime appointmentDateTime, String reason, AppointmentManager appointmentManager, Administrator administrator) {
        String appointmentID = "A" + String.format("%05d", new Random().nextInt(100000)); // Generate a unique appointment ID

        if (!isAvailableOnDay(appointmentDateTime.getDayOfWeek().toString())) {
            System.out.println("Doctor is not available on this day.");
            return false;
        }

        Appointment appointment = new Appointment(appointmentID, patientID, this.getUserID(), appointmentDateTime, "", reason, Appointment.AppointmentStatus.PENDING);
        return appointmentManager.scheduleAppointmentRequestByDoctor(appointment, administrator);
    }

    /**
     * Provides a prescription to a patient.
     *
     * @param patient      The patient to receive the prescription.
     * @param prescription The prescription to provide.
     */
    public void providePrescription(Patient patient, Prescription prescription) {
        patient.receivePrescription(prescription);
    }
}
