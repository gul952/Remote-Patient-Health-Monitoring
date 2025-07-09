package com.example.project.EmergencyAlertSystem;

import com.example.project.Health_Data_Handling.VitalSign;

/**
 * Represents a panic button system that allows patients to trigger emergency alerts.
 * The panic button can notify the patient, their assigned doctor, and check vital signs.
 */
public class PanicButton {
    private EmergencyAlert alertSystem; // The emergency alert system associated with the panic button
    public String senderEmail; // The sender's email address for notifications
    private String senderPassword; // The sender's email password for authentication

    /**
     * Constructor to initialize the PanicButton with an EmergencyAlert instance.
     *
     * @param alertSystem The emergency alert system to associate with the panic button.
     */
    public PanicButton(EmergencyAlert alertSystem) {
        this.alertSystem = alertSystem;
    }

    /**
     * Simulates pressing the panic button.
     * This triggers a check of the patient's vital signs and logs the action.
     */
    public void pressButton() {
        System.out.println("PANIC BUTTON PRESSED BY: " + alertSystem.getPatient().getName());
        System.out.println("CHECKING VITAL SIGNS FOR PATIENT: " + alertSystem.getPatient().getName());
        alertSystem.checkVitals(new VitalSign());
    }

    /**
     * Simulates pressing the panic button with a custom message.
     * This sends an emergency alert to the patient and their assigned doctor.
     *
     * @param customMessage The custom message to include in the alert.
     */
    public void pressButton(String customMessage) {
        System.out.println("PANIC BUTTON PRESSED with custom message.");
        alertSystem.getNotifier().sendEmail(alertSystem.getPatient().getEmail(), senderEmail, senderPassword, customMessage, new String(""));
        alertSystem.getNotifier().sendSMS(alertSystem.getPatient().getPhoneNumber(), customMessage);

        if (alertSystem.getPatient().getAssignedDoctor() != null) {
            String doctorEmail = alertSystem.getPatient().getAssignedDoctor().getEmail();
            String doctorPhone = alertSystem.getPatient().getAssignedDoctor().getPhoneNumber();
            alertSystem.getNotifier().sendEmail(doctorEmail, senderEmail, senderPassword, customMessage, new String(""));
            alertSystem.getNotifier().sendSMS(doctorPhone, customMessage);
        }
    }
}
