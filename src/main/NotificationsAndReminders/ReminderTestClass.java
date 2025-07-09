package com.example.project.NotificationsAndReminders;

import com.example.project.Appointment_Scheduling.AppointmentManager;
import com.example.project.EmergencyAlertSystem.NotificationService;
import com.example.project.User_Management.Administrator;
import com.example.project.User_Management.Doctor;
import com.example.project.User_Management.Patient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class ReminderTestClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        NotificationService notificationService = new NotificationService();
        ReminderService reminderService = new ReminderService();
        AppointmentManager appointmentManager = new AppointmentManager();

        Doctor doctor = new Doctor();
        doctor.setName("Ali");
        doctor.setUserID("D00001");
        doctor.setAvailableDays(java.util.Arrays.asList("MONDAY", "TUESDAY", "FRIDAY"));

        Patient patient = new Patient();
        patient.setName("Uzair");
        patient.setUserID("P00001");
        patient.setEmail("muzair.bsds24seecs@seecs.edu.pk");
        patient.setPhoneNumber("03111234567");

        Administrator admin = new Administrator();
        admin.addDoctor(doctor);
        admin.addPatient(patient);

        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);
        appointmentManager.scheduleAppointment("A00001", "P00001", "D00001", admin, futureTime, "Auto Checkup");

        boolean running = true;
        while (running) {
            System.out.println("\n========= 📢 REMINDER SYSTEM MENU =========");
            System.out.println("1. Create New Appointment");
            System.out.println("2. Load Reminders for Upcoming Appointments");
            System.out.println("3. Send All Reminders");
            System.out.println("4. Show Pending Reminders Count");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("🆔 Enter Appointment ID (e.g., A12345): ");
                    String appointmentID = scanner.nextLine();

                    System.out.print("📅 Enter date & time (yyyy-MM-dd HH:mm): ");
                    String dateInput = scanner.nextLine();

                    try {
                        LocalDateTime newDate = LocalDateTime.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                        System.out.print("📝 Reason: ");
                        String reason = scanner.nextLine();

                        boolean success = appointmentManager.scheduleAppointment(
                                appointmentID, "P00001", "D00001", admin, newDate, reason
                        );

                        if (success) {
                            System.out.println("✅ Appointment scheduled successfully.");

                            String message = "Reminder: You have an appointment with Dr. " + doctor.getName() + " on " + newDate;

                            // Create email & sms reminders
                            EmailNotification emailReminder = new EmailNotification(patient.getEmail(), message, notificationService);
                            SMSNotification smsReminder = new SMSNotification(patient.getPhoneNumber(), message, notificationService);

                            reminderService.addReminder(emailReminder);
                            reminderService.addReminder(smsReminder);

                            System.out.println("📬 Reminders added for this appointment.");
                        }

                    } catch (Exception e) {
                        System.out.println("❌ Invalid date/time format.");
                    }
                    break;

                case "2":
                    reminderService.loadUpcomingAppointmentReminders(appointmentManager, notificationService, admin);
                    System.out.println("✅ Reminders loaded from upcoming appointments.");
                    break;

                case "3":
                    reminderService.sendAllReminders();
                    System.out.println("✅ All reminders sent.");
                    break;

                case "4":
                    System.out.println("📦 Total pending reminders: " + reminderService.getPendingRemindersCount());
                    break;

                case "5":
                    running = false;
                    System.out.println("👋 Exiting Reminder System.");
                    break;

                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }

        scanner.close();
    }
}