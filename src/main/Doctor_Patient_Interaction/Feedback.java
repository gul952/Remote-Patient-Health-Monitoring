package com.example.project.Doctor_Patient_Interaction;

import java.time.LocalDateTime;

import com.example.project.User_Management.Doctor;
import com.example.project.User_Management.Patient;

/**
 * Represents feedback provided by a patient for a doctor.
 * Includes details such as rating, comments, and feedback status.
 */
public class Feedback {

    /**
     * Enum to represent the status of the feedback.
     */
    public enum FeedbackStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    private String feedbackID;
    private String doctorID;
    private String patientID;
    private int rating;
    private String comments;
    private LocalDateTime feedbackDate;
    private FeedbackStatus status;
    private boolean isAnonymous;

    /**
     * Default constructor for Feedback.
     */
    public Feedback() {
    }

    /**
     * Parameterized constructor for Feedback.
     *
     * @param feedbackID       The unique ID of the feedback.
     * @param doctorID         The ID of the doctor receiving the feedback.
     * @param patientID        The ID of the patient providing the feedback.
     * @param rating           The rating given by the patient (1 to 5).
     * @param comments         The comments provided by the patient.
     * @param feedbackDateTime The date and time the feedback was provided.
     * @param status           The status of the feedback (PENDING, APPROVED, REJECTED).
     * @param isAnonymous      Whether the feedback is anonymous.
     */
    public Feedback(String feedbackID, String doctorID, String patientID, int rating, String comments,
                    LocalDateTime feedbackDateTime, FeedbackStatus status, boolean isAnonymous) {
        this.feedbackID = feedbackID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        setRating(rating); // Using setter for validation
        setComments(comments); // Using setter for validation
        this.feedbackDate = feedbackDateTime;
        this.status = status;
        this.isAnonymous = isAnonymous;
    }

    // Setters with Validation

    /**
     * Sets the feedback ID.
     *
     * @param feedbackID The unique ID of the feedback.
     * @throws IllegalArgumentException If the feedback ID is null or empty.
     */
    public void setFeedbackID(String feedbackID) {
        if (feedbackID == null || feedbackID.trim().isEmpty()) {
            throw new IllegalArgumentException("Feedback ID cannot be null or empty.");
        }
        this.feedbackID = feedbackID;
    }

    /**
     * Sets the doctor ID associated with the feedback.
     *
     * @param doctorID The ID of the doctor.
     * @throws IllegalArgumentException If the doctor ID is null.
     */
    public void setDoctor(String doctorID) {
        if (doctorID == null) {
            throw new IllegalArgumentException("Doctor cannot be null.");
        }
        this.doctorID = doctorID;
    }

    /**
     * Sets the patient ID associated with the feedback.
     *
     * @param patientID The ID of the patient.
     * @throws IllegalArgumentException If the patient ID is null.
     */
    public void setPatient(String patientID) {
        if (patientID == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        this.patientID = patientID;
    }

    /**
     * Sets the rating for the feedback.
     *
     * @param rating The rating given by the patient (1 to 5).
     * @throws IllegalArgumentException If the rating is not between 1 and 5.
     */
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
    }

    /**
     * Sets the comments for the feedback.
     *
     * @param comments The comments provided by the patient.
     * @throws IllegalArgumentException If the comments are null or empty.
     */
    public void setComments(String comments) {
        if (comments == null || comments.trim().isEmpty()) {
            throw new IllegalArgumentException("Comments cannot be null or empty.");
        }
        this.comments = comments;
    }

    /**
     * Sets the date and time of the feedback.
     *
     * @param feedbackDate The date and time the feedback was provided.
     * @throws IllegalArgumentException If the feedback date is null.
     */
    public void setFeedbackDate(LocalDateTime feedbackDate) {
        if (feedbackDate == null) {
            throw new IllegalArgumentException("Feedback date cannot be null.");
        }
        this.feedbackDate = feedbackDate;
    }

    /**
     * Sets the status of the feedback.
     *
     * @param status The status of the feedback (PENDING, APPROVED, REJECTED).
     * @throws IllegalArgumentException If the feedback status is null.
     */
    public void setStatus(FeedbackStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Feedback status cannot be null.");
        }
        this.status = status;
    }

    /**
     * Sets whether the feedback is anonymous.
     *
     * @param isAnonymous True if the feedback is anonymous, false otherwise.
     */
    public void setIsAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    // Getters

    /**
     * Gets the feedback ID.
     *
     * @return The unique ID of the feedback.
     */
    public String getFeedbackID() {
        return feedbackID;
    }

    /**
     * Gets the doctor ID associated with the feedback.
     *
     * @return The ID of the doctor.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Gets the patient ID associated with the feedback.
     *
     * @return The ID of the patient.
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Gets the rating for the feedback.
     *
     * @return The rating given by the patient.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Gets the comments for the feedback.
     *
     * @return The comments provided by the patient.
     */
    public String getComments() {
        return comments;
    }

    /**
     * Gets the date and time of the feedback.
     *
     * @return The date and time the feedback was provided.
     */
    public LocalDateTime getFeedbackDate() {
        return feedbackDate;
    }

    /**
     * Gets the status of the feedback.
     *
     * @return The status of the feedback (PENDING, APPROVED, REJECTED).
     */
    public FeedbackStatus getStatus() {
        return status;
    }

    /**
     * Checks if the feedback is anonymous.
     *
     * @return True if the feedback is anonymous, false otherwise.
     */
    public boolean isAnonymous() {
        return isAnonymous;
    }

    /**
     * Approves the feedback by setting its status to APPROVED.
     */
    public void approveFeedback() {
        this.status = FeedbackStatus.APPROVED;
    }

    /**
     * Rejects the feedback by setting its status to REJECTED.
     */
    public void rejectFeedback() {
        this.status = FeedbackStatus.REJECTED;
    }

    /**
     * Returns a string representation of the feedback.
     *
     * @return A formatted string containing the feedback details.
     */
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackID='" + feedbackID +
                "\ndoctor=" + doctorID +
                "\npatient=" + (isAnonymous ? "Anonymous" : patientID) +
                "\nrating=" + rating +
                "\ncomments='" + comments +
                "\nfeedbackDate=" + feedbackDate +
                "\nstatus=" + status +
                "\nisAnonymous=" + (isAnonymous ? "Yes" : "No") +
                '}';
    }
}
