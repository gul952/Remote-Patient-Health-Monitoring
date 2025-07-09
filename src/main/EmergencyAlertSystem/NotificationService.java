package com.example.project.EmergencyAlertSystem;

/**
 * Service class for sending notifications via email and SMS.
 * This class provides methods to send emergency alerts to patients and doctors.
 */
public class NotificationService {

    /**
     * Sends an email notification to the specified recipient.
     *
     * @param to          The recipient's email address.
     * @param from        The sender's email address.
     * @param appPassword The application-specific password for the sender's email account.
     * @param message     The message body of the email.
     */
    public void sendEmail(String to, String from, String appPassword, String message, String subject) {
        String subjeect = "🚨 Emergency Alert" + subject;
        EmailSender.sendEmail(to, from, appPassword, subjeect, message);
    }

    /**
     * Sends an SMS notification to the specified recipient.
     *
     * @param to      The recipient's phone number.
     * @param message The message body of the SMS.
     */
    public void sendSMS(String to, String message) {
        System.out.println("📱 Sending SMS to: " + to);
        System.out.println("Message:\n" + message);
        System.out.println("--------------------------------------------------");
    }
}