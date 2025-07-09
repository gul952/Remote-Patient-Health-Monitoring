package com.example.project.NotificationsAndReminders;

import com.example.project.EmergencyAlertSystem.NotificationService;

/**
 * Represents an email notification system that sends reminders or alerts to a recipient.
 * Implements the {@link Notifiable} interface to provide the functionality for sending notifications.
 */
public class EmailNotification implements com.example.project.NotificationsAndReminders.Notifiable {
    // attributes
    private String recipientEmail; // The recipient's email address
    private String message; // The message to be sent in the email
    private NotificationService service; // The notification service used to send the email
    private String senderEmail = "hospitalhopelife@gmail.com"; // The sender's email address
    private String senderPassword = "opnh ttel isma yelz"; // The sender's email password for authentication
    private String subject = ""; // The subject of the email

    /**
     * Default constructor for EmailNotification.
     */
    public EmailNotification() {}

    /**
     * Constructor to initialize the EmailNotification with recipient email, message, and NotificationService instance.
     *
     * @param recipientEmail The recipient's email address.
     * @param message        The message to be sent in the email.
     * @param service        The notification service used to send the email.
     */
    public EmailNotification(String recipientEmail, String message, NotificationService service) {
        this.recipientEmail = recipientEmail;
        this.message = message;
        this.service = service;
    }
    public EmailNotification(String recipientEmail, String message, NotificationService service, String subject) {
        this.recipientEmail = recipientEmail;
        this.message = message;
        this.service = service;
        this.subject = subject;
    }

    /**
     * Sends a reminder notification via email.
     * This method is overridden from the {@link Notifiable} interface.
     */
    @Override
    public void sendRemainder() {
        System.out.println("Email Notification Sent to: " + recipientEmail + " with message: " + message + "\n");
        service.sendEmail(recipientEmail, senderEmail, senderPassword, message, subject);
    }
}
