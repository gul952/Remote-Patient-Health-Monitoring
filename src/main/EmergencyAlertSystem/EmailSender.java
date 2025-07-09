package com.example.project.EmergencyAlertSystem;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Utility class for sending emails using the JavaMail API.
 * This version uses STARTTLS on port 587 for better compatibility with modern Java versions.
 */
public class EmailSender {

    /**
     * Sends an email using STARTTLS over port 587 (recommended for Gmail).
     *
     * @param to          The recipient's email address.
     * @param fromEmail   The sender's email address (e.g., your Gmail address).
     * @param appPassword The application-specific password for the sender's Gmail account.
     * @param subject     The subject of the email.
     * @param messageText The body text of the email.
     */
    public static void sendEmail(String to, String fromEmail, String appPassword, String subject, String messageText) {
        // SMTP server properties for TLS (STARTTLS) configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Enforce TLS 1.2
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // TLS port

        // Create a mail session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            // Create and set up the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(messageText);

            // Send the message
            Transport.send(message);
            System.out.println("✅ Email sent successfully to " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to " + to);
            e.printStackTrace();
        }
    }
}
