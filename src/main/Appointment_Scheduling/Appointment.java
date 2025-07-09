package com.example.project.Appointment_Scheduling;

import java.time.LocalDateTime;

/**
 * Represents an appointment in the system, including details such as patient, doctor, date, and status.
 */
public class Appointment {

    /**
     * Enum representing the status of an appointment.
     */
    public enum AppointmentStatus {
        SCHEDULED,
        CANCELED,
        PENDING,
        RESCHEDULED,
        COMPLETED;
    }

    private String appointmentID;
    private String patientID;
    private String doctorID;
    private LocalDateTime appointmentDateTime;
    private String notes;
    private String reason;
    private AppointmentStatus appointmentStatus;

    /**
     * Default constructor for Appointment.
     */
    public Appointment() {}

    /**
     * Parameterized constructor for Appointment.
     *
     * @param appointmentID      The unique ID of the appointment.
     * @param patientID          The ID of the patient associated with the appointment.
     * @param doctorID           The ID of the doctor associated with the appointment.
     * @param appointmentDateTime The date and time of the appointment.
     * @param notes              Additional notes for the appointment.
     * @param reason             The reason for the appointment.
     * @param appointmentStatus  The status of the appointment.
     */
    public Appointment(String appointmentID, String patientID, String doctorID,
                       LocalDateTime appointmentDateTime, String notes, 
                       String reason, AppointmentStatus appointmentStatus) {
        setAppointmentID(appointmentID);
        setPatient(patientID);
        setDoctor(patientID);
        setAppointmentDateTime(appointmentDateTime);
        setNotes(notes);
        setReason(reason);
        setAppointmentStatus(appointmentStatus);
    }

    /**
     * Sets the appointment ID.
     *
     * @param appointmentID The unique ID of the appointment.
     */
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Sets the patient ID associated with the appointment.
     *
     * @param patientID The ID of the patient.
     * @throws IllegalArgumentException If the patient ID is null.
     */
    public void setPatient(String patientID) {
        if (patientID == null) {
            throw new IllegalArgumentException("Patient cannot be null!");
        }
        this.patientID = patientID;
    }

    /**
     * Sets the doctor ID associated with the appointment.
     *
     * @param doctorID The ID of the doctor.
     * @throws IllegalArgumentException If the doctor ID is null.
     */
    public void setDoctor(String doctorID) {
        if (doctorID == null) {
            throw new IllegalArgumentException("Doctor cannot be null!");
        }
        this.doctorID = doctorID;
    }

    /**
     * Sets the date and time of the appointment.
     *
     * @param appointmentDateTime The date and time of the appointment.
     */
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    /**
     * Sets additional notes for the appointment.
     *
     * @param notes The notes to set.
     */
    public void setNotes(String notes) {
        this.notes = (notes != null) ? notes.trim() : "";
    }

    /**
     * Sets the reason for the appointment.
     *
     * @param reason The reason to set.
     */
    public void setReason(String reason) {
        this.reason = (reason != null) ? reason.trim() : "";
    }

    /**
     * Sets the status of the appointment.
     *
     * @param appointmentStatus The status to set.
     * @throws IllegalArgumentException If the appointment status is null.
     */
    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        if (appointmentStatus == null) {
            throw new IllegalArgumentException("Appointment status cannot be null!");
        }
        this.appointmentStatus = appointmentStatus;
    }

    /**
     * Gets the appointment ID.
     *
     * @return The unique ID of the appointment.
     */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the patient ID associated with the appointment.
     *
     * @return The ID of the patient.
     */
    public String getPatient() {
        return patientID;
    }

    /**
     * Gets the doctor ID associated with the appointment.
     *
     * @return The ID of the doctor.
     */
    public String getDoctor() {
        return doctorID;
    }

    /**
     * Gets the date and time of the appointment.
     *
     * @return The date and time of the appointment.
     */
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Gets the notes for the appointment.
     *
     * @return The notes for the appointment.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Gets the reason for the appointment.
     *
     * @return The reason for the appointment.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Gets the status of the appointment.
     *
     * @return The status of the appointment.
     */
    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    /**
     * Returns a string representation of the appointment.
     *
     * @return A formatted string containing the appointment details.
     */
    @Override
    public String toString() {
        return String.format("Appointment ID: %s\nPatient: %s\nDoctor: %s\nDate: %s\nStatus: %s\nReason: %s\nNotes: %s",
                appointmentID, patientID, doctorID,
                appointmentDateTime, appointmentStatus, reason, notes);
    }

    /**
     * Reschedules the appointment to a new date and time.
     *
     * @param newDateTime The new date and time for the appointment.
     */
    public void rescheduleAppointment(LocalDateTime newDateTime) {
        setAppointmentDateTime(newDateTime);
        setAppointmentStatus(AppointmentStatus.RESCHEDULED);
    }

    /**
     * Cancels the appointment.
     */
    public void cancelAppointment() {
        setAppointmentStatus(AppointmentStatus.CANCELED);
    }
}
