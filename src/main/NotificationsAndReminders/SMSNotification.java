package com.example.project.NotificationsAndReminders;

import com.example.project.EmergencyAlertSystem.NotificationService;

/**
 * Represents an SMS notification system that sends reminders or alerts to a recipient's phone.
 * Implements the {@link Notifiable} interface to provide the functionality for sending notifications.
 */
public class SMSNotification implements Notifiable {
    // attributes
    private String recipientPhone; // The recipient's phone number
    private String message; // The message to be sent in the SMS
    private NotificationService service; // The notification service used to send the SMS

    /**
     * Default constructor for SMSNotification.
     */
    public SMSNotification() {}

    /**
     * Constructor to initialize the SMSNotification with recipient phone, message, and NotificationService instance.
     *
     * @param recipientPhone The recipient's phone number.
     * @param message        The message to be sent in the SMS.
     * @param service        The notification service used to send the SMS.
     */
    public SMSNotification(String recipientPhone, String message, NotificationService service) {
        this.recipientPhone = recipientPhone;
        this.message = message;
        this.service = service;
    }

    /**
     * Sends a reminder notification via SMS.
     * This method is overridden from the {@link Notifiable} interface.
     */
    @Override
    public void sendRemainder() {
        service.sendSMS(recipientPhone, message);
    }
}
