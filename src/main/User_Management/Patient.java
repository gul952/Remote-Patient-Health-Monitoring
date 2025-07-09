package com.example.project.User_Management;

import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.Appointment_Scheduling.AppointmentManager;
import com.example.project.Doctor_Patient_Interaction.Feedback;
import com.example.project.Doctor_Patient_Interaction.Feedback.FeedbackStatus;
import com.example.project.Doctor_Patient_Interaction.MedicalHistory;
import com.example.project.Doctor_Patient_Interaction.Prescription;
import com.example.project.Health_Data_Handling.VitalSign;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a Patient in the system who interacts with doctors, schedules appointments, and manages their health data.
 */
public class Patient extends User {

    /**
     * Enum representing types of allergies a patient may have.
     */
    public enum AllergyType {
        DUST,
        POLLEN,
        PEANUTS,
        SHELLFISH,
        DAIRY,
        GLUTEN,
        EGG,
        SOY,
        MEDICATIONS,
        INSECT_STINGS
    }

    /**
     * Enum representing types of diseases a patient may have.
     */
    public enum DiseaseType {
        DIABETES,
        HYPERTENSION,
        ASTHMA,
        CANCER,
        HEART_DISEASE,
        KIDNEY_DISEASE,
        ARTHRITIS,
        LIVER_DISEASE
    }

    // Attributes
    private String bloodGroup;
    private List<AllergyType> allergies;
    private Doctor assignedDoctor;
    private LocalDateTime appointmentDate;
    private List<DiseaseType> diseases;
    private List<MedicalHistory> medicalHistories;
    private List<Prescription> prescriptions;
    private List<VitalSign> vitalSigns;

    /**
     * Default constructor for Patient.
     */
    public Patient() {
        this.allergies = new ArrayList<>();
        this.diseases = new ArrayList<>();
        this.medicalHistories = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.vitalSigns = new ArrayList<>();
    }

    /**
     * Parameterized constructor for Patient.
     *
     * @param userID           The unique ID of the patient.
     * @param name             The name of the patient.
     * @param email            The email of the patient.
     * @param phoneNumber      The phone number of the patient.
     * @param password         The password of the patient.
     * @param address          The address of the patient.
     * @param age              The age of the patient.
     * @param gender           The gender of the patient.
     * @param accountStatus    The account status of the patient.
     * @param bloodGroup       The blood group of the patient.
     * @param appointmentDate  The appointment date of the patient.
     * @param allergies        The list of allergies of the patient.
     * @param diseases         The list of diseases of the patient.
     * @param medicalHistories The medical histories of the patient.
     * @param prescriptions    The prescriptions of the patient.
     * @param vitalSigns       The vital signs of the patient.
     */
    public Patient(String userID, String name, String email, String phoneNumber, String password,
                   String address, int age, String gender, boolean accountStatus,
                   String bloodGroup, LocalDateTime appointmentDate,
                   List<AllergyType> allergies, List<DiseaseType> diseases, List<MedicalHistory> medicalHistories,
                   List<Prescription> prescriptions, List<VitalSign> vitalSigns) {
        super(userID, name, email, phoneNumber, password, address, age, gender, accountStatus);
        this.bloodGroup = bloodGroup;
        this.assignedDoctor = null; // Initially no doctor is assigned
        this.appointmentDate = appointmentDate;
        this.allergies = allergies;
        this.diseases = diseases;
        this.medicalHistories = medicalHistories;
        this.prescriptions = prescriptions;
        this.vitalSigns = vitalSigns;
    }

    // Setters with validation

    /**
     * Sets the blood group of the patient.
     *
     * @param bloodGroup The blood group to set.
     */
    public void setBloodGroup(String bloodGroup) {
        if (bloodGroup == null || !bloodGroup.matches("^(A|B|AB|O)[+-]$")) {
            throw new IllegalArgumentException("Invalid blood group! Must be A+, A-, B+, B-, AB+, AB-, O+, or O-.");
        }
        this.bloodGroup = bloodGroup;
    }

    /**
     * Sets the assigned doctor for the patient.
     *
     * @param assignedDoctor The doctor to assign.
     */
    public void setAssignedDoctor(Doctor assignedDoctor) {
        if (assignedDoctor == null) {
            throw new IllegalArgumentException("Assigned doctor cannot be null.");
        }
        this.assignedDoctor = assignedDoctor;
    }

    /**
     * Sets the appointment date for the patient.
     *
     * @param appointmentDate The appointment date to set.
     */
    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    /**
     * Sets the allergies of the patient.
     *
     * @param allergies The list of allergies to set.
     */
    public void setAllergies(List<AllergyType> allergies) {
        if (allergies == null || allergies.isEmpty()) {
            throw new IllegalArgumentException("At least one allergy must be specified.");
        }
        this.allergies = new ArrayList<>(allergies);
    }

    /**
     * Sets the diseases of the patient.
     *
     * @param diseases The list of diseases to set.
     */
    public void setDiseases(List<DiseaseType> diseases) {
        if (diseases == null || diseases.isEmpty()) {
            throw new IllegalArgumentException("At least one disease must be specified.");
        }
        this.diseases = new ArrayList<>(diseases);
    }

    /**
     * Sets the vital signs of the patient.
     *
     * @param vitalSigns The list of vital signs to set.
     */
    public void setVitalSigns(List<VitalSign> vitalSigns) {
        this.vitalSigns = new ArrayList<>(vitalSigns);
    }

    /**
     * Sets the medical histories of the patient.
     *
     * @param medicalHistories The list of medical histories to set.
     */
    public void setMedicalHistories(List<MedicalHistory> medicalHistories) {
        this.medicalHistories = medicalHistories;
    }

    /**
     * Sets the prescriptions of the patient.
     *
     * @param prescriptions The list of prescriptions to set.
     */
    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    // Getters

    /**
     * Gets the blood group of the patient.
     *
     * @return The blood group of the patient.
     */
    public String getBloodGroup() {
        return bloodGroup;
    }

    /**
     * Gets the assigned doctor of the patient.
     *
     * @return The assigned doctor of the patient.
     */
    public Doctor getAssignedDoctor() {
        return assignedDoctor;
    }

    /**
     * Gets the appointment date of the patient.
     *
     * @return The appointment date of the patient.
     */
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Gets the allergies of the patient.
     *
     * @return The list of allergies of the patient.
     */
    public List<AllergyType> getAllergies() {
        return allergies;
    }

    /**
     * Gets the diseases of the patient.
     *
     * @return The list of diseases of the patient.
     */
    public List<DiseaseType> getDiseases() {
        return diseases;
    }

    /**
     * Gets the medical histories of the patient.
     *
     * @return The list of medical histories of the patient.
     */
    public List<MedicalHistory> getMedicalHistories() {
        return medicalHistories;
    }

    /**
     * Gets the prescriptions of the patient.
     *
     * @return The list of prescriptions of the patient.
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Gets the vital signs of the patient.
     *
     * @return The list of vital signs of the patient.
     */
    public List<VitalSign> getVitalSigns() {
        return vitalSigns;
    }

    @Override
    public String toString() {
        return String.format("%s\nBlood Group: %s\nAssigned Doctor: %s\nAppointment Date: %s\nName of Allergies: %s\nName of Diseases: %s",
                super.toString(), this.bloodGroup, this.assignedDoctor, this.appointmentDate, this.allergies, this.diseases);
    }

    // Methods

    /**
     * Adds a vital sign to the patient's record.
     *
     * @param vitalSign The vital sign to add.
     */
    public void addVitalSign(VitalSign vitalSign) {
        this.vitalSigns.add(vitalSign);
    }

    /**
     * Removes a vital sign from the patient's record.
     *
     * @param vitalSign The vital sign to remove.
     */
    public void removeVitalSign(VitalSign vitalSign) {
        this.vitalSigns.remove(vitalSign);
    }

    /**
     * Checks if the patient has a specific allergy.
     *
     * @param allergy The allergy to check.
     * @return True if the patient has the allergy, false otherwise.
     */
    public boolean hasAllergy(AllergyType allergy) {
        return allergies.contains(allergy);
    }

    /**
     * Checks if the patient has a specific disease.
     *
     * @param disease The disease to check.
     * @return True if the patient has the disease, false otherwise.
     */
    public boolean hasDisease(DiseaseType disease) {
        return diseases.contains(disease);
    }

    /**
     * Provides feedback to a doctor.
     *
     * @param doctor       The doctor to provide feedback to.
     * @param comments     The feedback comments.
     * @param rating       The feedback rating.
     * @param isAnonymous  Whether the feedback is anonymous.
     * @param feedbackID   The unique ID of the feedback.
     */
    public void provideFeedback(Doctor doctor, String comments, int rating, boolean isAnonymous, String feedbackID) {
        Feedback feedback = new Feedback(feedbackID, doctor.getUserID(), this.getUserID(), rating, comments, LocalDateTime.now(), FeedbackStatus.PENDING, isAnonymous);
        doctor.receiveFeedback(feedback);
    }

    /**
     * Adds a medical history record for the patient.
     *
     * @param history The medical history to add.
     */
    public void addMedicalHistory(MedicalHistory history) {
        medicalHistories.add(history);
    }

    /**
     * Retrieves the medical history of the patient.
     *
     * @return The list of medical histories.
     */
    public List<MedicalHistory> getMedicalHistory() {
        return medicalHistories;
    }

    /**
     * Schedules an appointment for the patient with a pending status.
     *
     * @param appointmentManager The appointment manager to handle the request.
     * @param doctorID           The user ID of the doctor.
     * @param appointmentDateTime The date and time of the appointment.
     * @param reason             The reason for the appointment.
     * @return The scheduled appointment.
     */
    public Appointment scheduleAppointment(AppointmentManager appointmentManager, String doctorID, LocalDateTime appointmentDateTime, String reason) {
        String appointmentID = "A" + String.format("%05d", new Random().nextInt(100000)); // Generate a unique appointment ID
        Appointment appointment = new Appointment(appointmentID, this.getUserID(), doctorID, appointmentDateTime, "", reason, Appointment.AppointmentStatus.PENDING);
        appointmentManager.addAppointment(appointment);  // Add the appointment to the manager
        this.setAppointmentDate(appointmentDateTime);  // Update the patient's appointment date
        System.out.println("Appointment scheduled, awaiting approval.");
        return appointment;
    }

    /**
     * Receives a prescription from a doctor.
     *
     * @param prescription The prescription to receive.
     */
    public void receivePrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }

    /**
     * Retrieves all prescriptions for the patient.
     *
     * @return The list of prescriptions.
     */
    public List<Prescription> seePrescriptions() {
        return prescriptions;
    }
}
