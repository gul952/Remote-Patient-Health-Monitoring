package com.example.project.Doctor_Patient_Interaction;

import java.util.*;
import java.time.*;
import com.example.project.User_Management.*;

/**
 * Represents a prescription issued by a doctor to a patient.
 * Includes details such as medications, dosage instructions, and schedule.
 */
public class Prescription {
    // attributes
    private String prescriptionID;            // Unique ID for the prescription
    private Patient patient;                  // The patient receiving the prescription
    private Doctor doctor;                    // The doctor issuing the prescription
    private List<String> medications;         // List of medications prescribed
    private String dosageInstructions;        // Instructions for dosage
    private LocalDateTime startDate;          // Start date of the prescription
    private LocalDateTime endDate;            // End date of the prescription
    private String dosageSchedule;            // Schedule for taking the medication
    private LocalDateTime createdDate;        // Date the prescription was created
    private String quantity;                     // Quantity of medication prescribed

    /**
     * Default constructor for Prescription.
     * Initializes an empty list for medications.
     */
    public Prescription() {
        this.medications = new ArrayList<>();
    }

    /**
     * Parameterized constructor for Prescription.
     *
     * @param prescriptionID     The unique ID for the prescription.
     * @param patient            The patient receiving the prescription.
     * @param doctor             The doctor issuing the prescription.
     * @param medications        The list of medications prescribed.
     * @param dosageInstructions The instructions for dosage.
     * @param startDate          The start date of the prescription.
     * @param endDate            The end date of the prescription.
     * @param dosageSchedule     The schedule for taking the medication.
     * @param createdDate        The date the prescription was created.
     * @param quantity           The quantity of medication prescribed.
     */
    public Prescription(String prescriptionID, Patient patient, Doctor doctor, List<String> medications,
                        String dosageInstructions, LocalDateTime startDate, LocalDateTime endDate,
                        String dosageSchedule, LocalDateTime createdDate, String quantity) {
        this.prescriptionID = prescriptionID;
        this.patient = patient;
        this.doctor = doctor;
        this.medications = medications;
        this.dosageInstructions = dosageInstructions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dosageSchedule = dosageSchedule;
        this.createdDate = createdDate;
        this.quantity = quantity;
    }

    // Setters with validation

    /**
     * Sets the prescription ID.
     *
     * @param prescriptionID The unique ID for the prescription.
     * @throws IllegalArgumentException If the prescription ID is null or empty.
     */
    public void setPrescriptionID(String prescriptionID) {
        if (prescriptionID == null || prescriptionID.trim().isEmpty()) {
            throw new IllegalArgumentException("Prescription ID cannot be null or empty.");
        }
        this.prescriptionID = prescriptionID;
    }

    /**
     * Sets the patient receiving the prescription.
     *
     * @param patient The patient receiving the prescription.
     * @throws IllegalArgumentException If the patient is null.
     */
    public void setPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        this.patient = patient;
    }

    /**
     * Sets the doctor issuing the prescription.
     *
     * @param doctor The doctor issuing the prescription.
     * @throws IllegalArgumentException If the doctor is null.
     */
    public void setDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null.");
        }
        this.doctor = doctor;
    }

    /**
     * Sets the list of medications prescribed.
     *
     * @param medications The list of medications.
     * @throws IllegalArgumentException If the medications list is null or empty.
     */
    public void setMedications(List<String> medications) {
        if (medications == null || medications.isEmpty()) {
            throw new IllegalArgumentException("Medications cannot be null or empty.");
        }
        this.medications = medications;
    }

    /**
     * Sets the dosage instructions.
     *
     * @param dosageInstructions The instructions for dosage.
     * @throws IllegalArgumentException If the dosage instructions are null or empty.
     */
    public void setDosageInstructions(String dosageInstructions) {
        if (dosageInstructions == null || dosageInstructions.trim().isEmpty()) {
            throw new IllegalArgumentException("Dosage Instructions cannot be null or empty.");
        }
        this.dosageInstructions = dosageInstructions;
    }

    /**
     * Sets the start date of the prescription.
     *
     * @param startDate The start date.
     * @throws IllegalArgumentException If the start date is null.
     */
    public void setStartDate(LocalDateTime startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start Date cannot be null.");
        }
        this.startDate = startDate;
    }

    /**
     * Sets the end date of the prescription.
     *
     * @param endDate The end date.
     * @throws IllegalArgumentException If the end date is null.
     */
    public void setEndDate(LocalDateTime endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End Date cannot be null.");
        }
        this.endDate = endDate;
    }

    /**
     * Sets the dosage schedule.
     *
     * @param dosageSchedule The schedule for taking the medication.
     * @throws IllegalArgumentException If the dosage schedule is null or empty.
     */
    public void setDosageSchedule(String dosageSchedule) {
        if (dosageSchedule == null || dosageSchedule.trim().isEmpty()) {
            throw new IllegalArgumentException("Dosage Schedule cannot be null or empty.");
        }
        this.dosageSchedule = dosageSchedule;
    }

    /**
     * Sets the created date of the prescription.
     *
     * @param createdDate The date the prescription was created.
     * @throws IllegalArgumentException If the created date is null.
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        if (createdDate == null) {
            throw new IllegalArgumentException("Created Date cannot be null.");
        }
        this.createdDate = createdDate;
    }

    /**
     * Sets the quantity of medication prescribed.
     *
     * @param quantity The quantity of medication.
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    // Getters

    /**
     * Gets the prescription ID.
     *
     * @return The unique ID for the prescription.
     */
    public String getPrescriptionID() {
        return prescriptionID;
    }

    /**
     * Gets the patient receiving the prescription.
     *
     * @return The patient receiving the prescription.
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Gets the doctor issuing the prescription.
     *
     * @return The doctor issuing the prescription.
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Gets the list of medications prescribed.
     *
     * @return The list of medications.
     */
    public List<String> getMedications() {
        return medications;
    }

    /**
     * Gets the dosage instructions.
     *
     * @return The instructions for dosage.
     */
    public String getDosageInstructions() {
        return dosageInstructions;
    }

    /**
     * Gets the start date of the prescription.
     *
     * @return The start date.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date of the prescription.
     *
     * @return The end date.
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Gets the dosage schedule.
     *
     * @return The schedule for taking the medication.
     */
    public String getDosageSchedule() {
        return dosageSchedule;
    }

    /**
     * Gets the created date of the prescription.
     *
     * @return The date the prescription was created.
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Gets the quantity of medication prescribed.
     *
     * @return The quantity of medication.
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * Returns a string representation of the prescription.
     *
     * @return A formatted string containing the prescription details.
     */
    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionID='" + prescriptionID +
                "\npatient=" + patient.getName() +
                "\ndoctor=" + doctor.getName() +
                "\nmedications=" + medications +
                "\ndosageInstructions='" + dosageInstructions +
                "\nstartDate=" + startDate +
                "\nendDate=" + endDate +
                "\ndosageSchedule='" + dosageSchedule +
                "\ncreatedDate=" + createdDate +
                "\nquantity=" + quantity +
                '}';
    }
}
