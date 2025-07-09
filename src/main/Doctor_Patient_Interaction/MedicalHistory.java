package com.example.project.Doctor_Patient_Interaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.project.User_Management.Patient;
import com.example.project.User_Management.Patient.AllergyType;

/**
 * Represents the medical history of a patient, including prescriptions, feedback, allergies, surgeries, and family history.
 */
public class MedicalHistory {
    private String medicalHistoryID;         // Unique ID for medical history
    private String patientID;                // The patient this history belongs to
    private List<Prescription> prescriptions; // List of prescriptions given
    private List<Feedback> feedbacks;        // List of feedbacks from the patient
    private List<AllergyType> allergies;     // Allergies the patient has
    private String surgeries;                // List of surgeries the patient has undergone
    private String familyHistory;            // Family's medical history
    private LocalDateTime createdDate;       // The date the history record was created

    /**
     * Default constructor for MedicalHistory.
     * Initializes empty lists for prescriptions and feedbacks.
     */
    public MedicalHistory() {
        this.prescriptions = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
    }

    /**
     * Parameterized constructor for MedicalHistory.
     *
     * @param medicalHistoryID The unique ID for the medical history.
     * @param patientID        The ID of the patient this history belongs to.
     * @param prescriptions    The list of prescriptions given to the patient.
     * @param feedbacks        The list of feedbacks from the patient.
     * @param allergies        The list of allergies the patient has.
     * @param surgeries        The list of surgeries the patient has undergone.
     * @param familyHistory    The family's medical history.
     * @param createdDate      The date the history record was created.
     */
    public MedicalHistory(String medicalHistoryID, String patientID, List<Prescription> prescriptions,
                          List<Feedback> feedbacks, List<AllergyType> allergies, String surgeries, String familyHistory,
                          LocalDateTime createdDate) {
        this.medicalHistoryID = medicalHistoryID;
        this.patientID = patientID;
        this.prescriptions = prescriptions;
        this.feedbacks = feedbacks;
        this.allergies = allergies;
        this.surgeries = surgeries;
        this.familyHistory = familyHistory;
        this.createdDate = createdDate;
    }

    // Setters with validation

    /**
     * Sets the medical history ID.
     *
     * @param medicalHistoryID The unique ID for the medical history.
     * @throws IllegalArgumentException If the medical history ID is null or empty.
     */
    public void setMedicalHistoryID(String medicalHistoryID) {
        if (medicalHistoryID == null || medicalHistoryID.trim().isEmpty()) {
            throw new IllegalArgumentException("Medical History ID cannot be empty.");
        }
        this.medicalHistoryID = medicalHistoryID;
    }

    /**
     * Sets the patient ID.
     *
     * @param patientID The ID of the patient this history belongs to.
     * @throws IllegalArgumentException If the patient ID is null.
     */
    public void setPatient(String patientID) {
        if (patientID == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        this.patientID = patientID;
    }

    /**
     * Sets the list of prescriptions.
     *
     * @param prescriptions The list of prescriptions given to the patient.
     * @throws IllegalArgumentException If the prescriptions list is null or empty.
     */
    public void setPrescriptions(List<Prescription> prescriptions) {
        if (prescriptions == null || prescriptions.isEmpty()) {
            throw new IllegalArgumentException("Prescriptions cannot be empty.");
        }
        this.prescriptions = prescriptions;
    }

    /**
     * Sets the list of feedbacks.
     *
     * @param feedbacks The list of feedbacks from the patient.
     * @throws IllegalArgumentException If the feedbacks list is null or empty.
     */
    public void setFeedbacks(List<Feedback> feedbacks) {
        if (feedbacks == null || feedbacks.isEmpty()) {
            throw new IllegalArgumentException("Feedbacks cannot be empty.");
        }
        this.feedbacks = feedbacks;
    }

    /**
     * Sets the list of allergies.
     *
     * @param allergies The list of allergies the patient has.
     * @throws IllegalArgumentException If the allergies list is null.
     */
    public void setAllergies(List<AllergyType> allergies) {
        if (allergies == null) {
            throw new IllegalArgumentException("Allergies cannot be null.");
        }
        this.allergies = allergies;
    }

    /**
     * Sets the list of surgeries.
     *
     * @param surgeries The list of surgeries the patient has undergone.
     * @throws IllegalArgumentException If the surgeries string is null or empty.
     */
    public void setSurgeries(String surgeries) {
        if (surgeries == null || surgeries.trim().isEmpty()) {
            throw new IllegalArgumentException("Surgeries cannot be empty.");
        }
        this.surgeries = surgeries;
    }

    /**
     * Sets the family history.
     *
     * @param familyHistory The family's medical history.
     * @throws IllegalArgumentException If the family history string is null or empty.
     */
    public void setFamilyHistory(String familyHistory) {
        if (familyHistory == null || familyHistory.trim().isEmpty()) {
            throw new IllegalArgumentException("Family History cannot be empty.");
        }
        this.familyHistory = familyHistory;
    }

    /**
     * Sets the created date of the medical history.
     *
     * @param createdDate The date the history record was created.
     * @throws IllegalArgumentException If the created date is null.
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        if (createdDate == null) {
            throw new IllegalArgumentException("Created date cannot be null.");
        }
        this.createdDate = createdDate;
    }

    // Getters

    /**
     * Gets the medical history ID.
     *
     * @return The unique ID for the medical history.
     */
    public String getMedicalHistoryID() {
        return medicalHistoryID;
    }

    /**
     * Gets the patient ID.
     *
     * @return The ID of the patient this history belongs to.
     */
    public String getPatient() {
        return patientID;
    }

    /**
     * Gets the list of prescriptions.
     *
     * @return The list of prescriptions given to the patient.
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Gets the list of feedbacks.
     *
     * @return The list of feedbacks from the patient.
     */
    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    /**
     * Gets the list of allergies.
     *
     * @return The list of allergies the patient has.
     */
    public List<AllergyType> getAllergies() {
        return allergies;
    }

    /**
     * Gets the list of surgeries.
     *
     * @return The list of surgeries the patient has undergone.
     */
    public String getSurgeries() {
        return surgeries;
    }

    /**
     * Gets the family history.
     *
     * @return The family's medical history.
     */
    public String getFamilyHistory() {
        return familyHistory;
    }

    /**
     * Gets the created date of the medical history.
     *
     * @return The date the history record was created.
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    // Methods

    /**
     * Adds a prescription to the medical history.
     *
     * @param prescription The prescription to add.
     * @throws IllegalArgumentException If the prescription is null.
     */
    public void addPrescription(Prescription prescription) {
        if (prescription == null) {
            throw new IllegalArgumentException("Prescription cannot be null.");
        }
        this.prescriptions.add(prescription);
    }

    /**
     * Adds feedback to the medical history.
     *
     * @param feedback The feedback to add.
     * @throws IllegalArgumentException If the feedback is null.
     */
    public void addFeedback(Feedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException("Feedback cannot be null.");
        }
        this.feedbacks.add(feedback);
    }

    /**
     * Returns a string representation of the medical history.
     *
     * @return A formatted string containing the medical history details.
     */
    @Override
    public String toString() {
        return "MedicalHistory{" +
                "medicalHistoryID='" + medicalHistoryID +
                "\npatient=" + patientID +
                "\nprescriptions=" + prescriptions +
                "\nfeedbacks=" + feedbacks +
                "\nallergies=" + allergies +
                "\nsurgeries='" + surgeries +
                "\nfamilyHistory='" + familyHistory +
                "\ncreatedDate=" + createdDate +
                '}';
    }
}
