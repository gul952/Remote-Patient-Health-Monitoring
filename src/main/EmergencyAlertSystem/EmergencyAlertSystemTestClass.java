package com.example.project.EmergencyAlertSystem;

import com.example.project.Health_Data_Handling.VitalSign;
import com.example.project.User_Management.Doctor;
import com.example.project.User_Management.Patient;

import java.util.Arrays;

public class EmergencyAlertSystemTestClass {
    public static void main(String[] args) {
        Doctor doctor = new Doctor(
                "D12345", "Dr. Ali", "hraheem.bsds24seecs@seecs.edu.pk", "03001234567", "pass1234",
                "City Hospital", 45, "Male", true,
                Doctor.Specialization.CARDIOLOGIST, "LIC4567", "City Hospital",
                "10AM-4PM", 20, 1500.0,
                Arrays.asList("MONDAY", "WEDNESDAY", "FRIDAY")
        );

        Patient criticalPatient = new Patient();
        criticalPatient.setUserID("P10001");
        criticalPatient.setName("Ahmed");
        criticalPatient.setEmail("freefirebase5657@gmail.com");
        criticalPatient.setPhoneNumber("03111234567");
        criticalPatient.setPassword("Ahmed123");
        criticalPatient.setAddress("Street 45, Lahore");
        criticalPatient.setAge(32);
        criticalPatient.setGender("Male");
        criticalPatient.setAccountStatus(true);
        criticalPatient.setAssignedDoctor(doctor);

        Patient normalPatient = new Patient();
        normalPatient.setUserID("P10002");
        normalPatient.setName("Zara");
        normalPatient.setEmail("muzair.bsds24seecs@seecs.edu.pk");
        normalPatient.setPhoneNumber("03451234567");
        normalPatient.setPassword("Zara1234");
        normalPatient.setAddress("Model Town, Karachi");
        normalPatient.setAge(27);
        normalPatient.setGender("Female");
        normalPatient.setAccountStatus(true);
        normalPatient.setAssignedDoctor(doctor);

        VitalSign criticalVitals = new VitalSign();
        criticalVitals.setUserID("P10001");
        criticalVitals.setHeartRate(160); 
        criticalVitals.setOxygenLevel(85); 
        criticalVitals.setBloodPressure(180); 
        criticalVitals.setTemperature(39.5); 
        criticalVitals.setRespiratoryRate(30); 
        criticalVitals.setGlucoseLevel(250); 
        criticalVitals.setCholesterolLevel(300); 
        criticalVitals.setBmi(35.0); 
        criticalVitals.setHydrationLevel(30); 
        criticalVitals.setStressLevel(9); 

        VitalSign normalVitals = new VitalSign();
        normalVitals.setUserID("P10002");
        normalVitals.setHeartRate(75);
        normalVitals.setOxygenLevel(98);
        normalVitals.setBloodPressure(120);
        normalVitals.setTemperature(36.5);
        normalVitals.setRespiratoryRate(18);
        normalVitals.setGlucoseLevel(100);
        normalVitals.setCholesterolLevel(180);
        normalVitals.setBmi(22.5);
        normalVitals.setHydrationLevel(60);
        normalVitals.setStressLevel(3);

        NotificationService notifier = new NotificationService();

        EmergencyAlert alert1 = new EmergencyAlert(criticalPatient, criticalVitals, notifier);
        System.out.println("\n--- Testing Emergency Alert with Critical Vitals ---");
        alert1.checkVitals(criticalVitals);

        EmergencyAlert alert2 = new EmergencyAlert(normalPatient, normalVitals, notifier);
        PanicButton panicButton = new PanicButton(alert2);
        System.out.println("\n--- Testing Panic Button with Normal Vitals ---");
        panicButton.pressButton(); 



    }
}
