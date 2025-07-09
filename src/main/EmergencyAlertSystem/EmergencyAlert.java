package com.example.project.EmergencyAlertSystem;

import com.example.project.Health_Data_Handling.VitalSign;
import com.example.project.User_Management.Patient;

public class EmergencyAlert {
    private Patient patient;
    private VitalSign currentVitals;
    private NotificationService notifier;
    private String senderEmail;
    private String senderPassword;

    // Default constructor
    public EmergencyAlert() {

    }

    // Parameterized constructor to initialize EmergencyAlert with patient, vitals, and notifier
    public EmergencyAlert(Patient patient, VitalSign currentVitalSigns, NotificationService notifier) {
        this.patient = patient;
        this.currentVitals = currentVitalSigns;
        this.notifier = notifier;
    }

    // Setters and Getters for patient, vitals, and notifier
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setCurrentVitalSigns(VitalSign currentVitalSigns) {
        this.currentVitals = currentVitalSigns;
    }

    public void setNotifier(NotificationService notifier) {
        this.notifier = notifier;
    }

    public Patient getPatient() {
        return patient;
    }

    public VitalSign getCurrentVitalSigns() {
        return currentVitals;
    }

    public NotificationService getNotifier() {
        return notifier;
    }

    // Override toString to provide a formatted string representation of the EmergencyAlert object
    @Override
    public String toString() {
        return String.format("Emergency Alert System\nPatient: %s(%s)\nCurrent Vitals:\n%s",
                patient.getName(), patient.getUserID(), currentVitals.toString());
    }

    // Method to check if any vital signs are critical and trigger an alert if necessary
    public void checkVitals(VitalSign currentVitals) {
        boolean emergency = false;
        StringBuilder alertMessage = new StringBuilder("CRITICAL VITAL ALERT for patient " + patient.getName() + ": \n");

        // Check heart rate
        if (currentVitals.getHeartRate() < 50 || currentVitals.getHeartRate() > 130) {
            alertMessage.append("⚠️ Heart Rate: ").append(currentVitals.getHeartRate()).append("BPM\n");
            emergency = true;
        }

        // Check oxygen level
        if (currentVitals.getOxygenLevel() < 90) {
            alertMessage.append("⚠️ Oxygen Level: ").append(currentVitals.getOxygenLevel()).append("%\n");
            emergency = true;
        }

        // Check blood pressure
        if (currentVitals.getBloodPressure() < 80 || currentVitals.getBloodPressure() > 140) {
            alertMessage.append("⚠️ Blood Pressure: ").append(currentVitals.getBloodPressure()).append("mmHg\n");
            emergency = true;
        }

        // Check body temperature
        if (currentVitals.getTemperature() < 35.0 || currentVitals.getTemperature() > 39.0) {
            alertMessage.append("⚠️ Temperature: ").append(currentVitals.getTemperature()).append(" °C\n");
            emergency = true;
        }

        // Check respiratory rate
        if (currentVitals.getRespiratoryRate() < 12 || currentVitals.getRespiratoryRate() > 25) {
            alertMessage.append("⚠️ Respiratory Rate: ").append(currentVitals.getRespiratoryRate()).append(" breaths/min\n");
            emergency = true;
        }

        // Check glucose level
        if (currentVitals.getGlucoseLevel() < 70 || currentVitals.getGlucoseLevel() > 180) {
            alertMessage.append("⚠️ Glucose Level: ").append(currentVitals.getGlucoseLevel()).append(" mg/dL\n");
            emergency = true;
        }

        // Check cholesterol level
        if (currentVitals.getCholesterolLevel() > 240) {
            alertMessage.append("⚠️ High Cholesterol Level: ").append(currentVitals.getCholesterolLevel()).append(" mg/dL\n");
            emergency = true;
        }

        // Check BMI
        if (currentVitals.getBmi() < 18.5 || currentVitals.getBmi() > 30.0) {
            alertMessage.append("⚠️ Abnormal BMI: ").append(currentVitals.getBmi()).append(" kg/m²\n");
            emergency = true;
        }

        // Check hydration level
        if (currentVitals.getHydrationLevel() < 40.0 || currentVitals.getHydrationLevel() > 70.0) {
            alertMessage.append("⚠️ Hydration Level: ").append(currentVitals.getHydrationLevel()).append("%\n");
            emergency = true;
        }

        // Check stress level
        if (currentVitals.getStressLevel() > 8) {
            alertMessage.append("⚠️ Stress Level: ").append(currentVitals.getStressLevel()).append("/10\n");
            emergency = true;
        }

        // If any vital is critical, send an alert
        if (emergency) {
            sendAlert(alertMessage.toString());
        } else {
            System.out.println("✅ All vitals are within the safe range.");
        }
    }

    public boolean checkOnlyVitals(VitalSign currentVitals) {
        boolean emergency = false;
        StringBuilder alertMessage = new StringBuilder("CRITICAL VITAL ALERT for patient " + currentVitals.getUserID() + ": \n");

        // Check heart rate
        if (currentVitals.getHeartRate() < 50 || currentVitals.getHeartRate() > 130) {
            alertMessage.append("⚠️ Heart Rate: ").append(currentVitals.getHeartRate()).append("BPM\n");
            emergency = true;
        }

        // Check oxygen level
        if (currentVitals.getOxygenLevel() < 90) {
            alertMessage.append("⚠️ Oxygen Level: ").append(currentVitals.getOxygenLevel()).append("%\n");
            emergency = true;
        }

        // Check blood pressure
        if (currentVitals.getBloodPressure() < 80 || currentVitals.getBloodPressure() > 140) {
            alertMessage.append("⚠️ Blood Pressure: ").append(currentVitals.getBloodPressure()).append("mmHg\n");
            emergency = true;
        }

        // Check body temperature
        if (currentVitals.getTemperature() < 35.0 || currentVitals.getTemperature() > 39.0) {
            alertMessage.append("⚠️ Temperature: ").append(currentVitals.getTemperature()).append(" °C\n");
            emergency = true;
        }

        // Check respiratory rate
        if (currentVitals.getRespiratoryRate() < 12 || currentVitals.getRespiratoryRate() > 25) {
            alertMessage.append("⚠️ Respiratory Rate: ").append(currentVitals.getRespiratoryRate()).append(" breaths/min\n");
            emergency = true;
        }

        // Check glucose level
        if (currentVitals.getGlucoseLevel() < 70 || currentVitals.getGlucoseLevel() > 180) {
            alertMessage.append("⚠️ Glucose Level: ").append(currentVitals.getGlucoseLevel()).append(" mg/dL\n");
            emergency = true;
        }

        // Check cholesterol level
        if (currentVitals.getCholesterolLevel() > 240) {
            alertMessage.append("⚠️ High Cholesterol Level: ").append(currentVitals.getCholesterolLevel()).append(" mg/dL\n");
            emergency = true;
        }

        // Check BMI
        if (currentVitals.getBmi() < 18.5 || currentVitals.getBmi() > 30.0) {
            alertMessage.append("⚠️ Abnormal BMI: ").append(currentVitals.getBmi()).append(" kg/m²\n");
            emergency = true;
        }

        // Check hydration level
        if (currentVitals.getHydrationLevel() < 40.0 || currentVitals.getHydrationLevel() > 70.0) {
            alertMessage.append("⚠️ Hydration Level: ").append(currentVitals.getHydrationLevel()).append("%\n");
            emergency = true;
        }

        // Check stress level
        if (currentVitals.getStressLevel() > 8) {
            alertMessage.append("⚠️ Stress Level: ").append(currentVitals.getStressLevel()).append("/10\n");
            emergency = true;
        }

        return emergency;
    }

    // Method to send an alert to the patient and their assigned doctor
    public void sendAlert(String alertMessage) {
        System.out.println(alertMessage);

        // Notify the patient via email and SMS
        String patientEmail = patient.getEmail();
        String patientPhone = patient.getPhoneNumber();

        notifier.sendEmail(patientEmail, senderEmail, senderPassword, alertMessage, new String(""));
        notifier.sendSMS(patientPhone, alertMessage);

        // Notify the assigned doctor if available
        if (patient.getAssignedDoctor() != null) {
            String doctorEmail = patient.getAssignedDoctor().getEmail();
            String doctorPhone = patient.getAssignedDoctor().getPhoneNumber();

            notifier.sendEmail(doctorEmail, senderEmail, senderPassword, alertMessage, new String(""));
            notifier.sendSMS(doctorPhone, alertMessage);
        } else {
            System.out.println("⚠️ No doctor assigned to this patient. Only patient was notified.");
        }

        System.out.println("Emergency alert sent to patient and assigned doctor.");
    }

    // Method to check if any vital signs are critical and trigger an alert if necessary
    public String getMessage(VitalSign currentVitals) {
        boolean emergency = false;
        StringBuilder alertMessage = new StringBuilder("CRITICAL VITAL ALERT for patient " + currentVitals.getUserID() + ": \n");

        // Check heart rate
        if (currentVitals.getHeartRate() < 50 || currentVitals.getHeartRate() > 130) {
            alertMessage.append("⚠️ Heart Rate: ").append(currentVitals.getHeartRate()).append("BPM\n");
            emergency = true;
        }

        // Check oxygen level
        if (currentVitals.getOxygenLevel() < 90) {
            alertMessage.append("⚠️ Oxygen Level: ").append(currentVitals.getOxygenLevel()).append("%\n");
            emergency = true;
        }

        // Check blood pressure
        if (currentVitals.getBloodPressure() < 80 || currentVitals.getBloodPressure() > 140) {
            alertMessage.append("⚠️ Blood Pressure: ").append(currentVitals.getBloodPressure()).append("mmHg\n");
            emergency = true;
        }

        // Check body temperature
        if (currentVitals.getTemperature() < 35.0 || currentVitals.getTemperature() > 39.0) {
            alertMessage.append("⚠️ Temperature: ").append(currentVitals.getTemperature()).append(" °C\n");
            emergency = true;
        }

        // Check respiratory rate
        if (currentVitals.getRespiratoryRate() < 12 || currentVitals.getRespiratoryRate() > 25) {
            alertMessage.append("⚠️ Respiratory Rate: ").append(currentVitals.getRespiratoryRate()).append(" breaths/min\n");
            emergency = true;
        }

        // Check glucose level
        if (currentVitals.getGlucoseLevel() < 70 || currentVitals.getGlucoseLevel() > 180) {
            alertMessage.append("⚠️ Glucose Level: ").append(currentVitals.getGlucoseLevel()).append(" mg/dL\n");
            emergency = true;
        }

        // Check cholesterol level
        if (currentVitals.getCholesterolLevel() > 240) {
            alertMessage.append("⚠️ High Cholesterol Level: ").append(currentVitals.getCholesterolLevel()).append(" mg/dL\n");
            emergency = true;
        }

        // Check BMI
        if (currentVitals.getBmi() < 18.5 || currentVitals.getBmi() > 30.0) {
            alertMessage.append("⚠️ Abnormal BMI: ").append(currentVitals.getBmi()).append(" kg/m²\n");
            emergency = true;
        }

        // Check hydration level
        if (currentVitals.getHydrationLevel() < 40.0 || currentVitals.getHydrationLevel() > 70.0) {
            alertMessage.append("⚠️ Hydration Level: ").append(currentVitals.getHydrationLevel()).append("%\n");
            emergency = true;
        }

        // Check stress level
        if (currentVitals.getStressLevel() > 8) {
            alertMessage.append("⚠️ Stress Level: ").append(currentVitals.getStressLevel()).append("/10\n");
            emergency = true;
        }

        return alertMessage.toString();

    }
}