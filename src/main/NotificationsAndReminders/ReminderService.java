package com.example.project.NotificationsAndReminders;

import com.example.project.Appointment_Scheduling.Appointment;
import com.example.project.Appointment_Scheduling.AppointmentManager;
import com.example.project.EmergencyAlertSystem.NotificationService;
import com.example.project.User_Management.Administrator;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing and sending reminders.
 * This class handles reminders for upcoming appointments and other notifications.
 */
public class ReminderService {
    // List of reminders implementing the Notifiable interface
    private List<Notifiable> reminders;

    /**
     * Constructor to initialize the list of reminders.
     */
    public ReminderService() {
        this.reminders = new ArrayList<>();
    }

    /**
     * Adds a reminder to the list of reminders.
     *
     * @param reminder The reminder to add.
     */
    public void addReminder(Notifiable reminder) {
        reminders.add(reminder);
    }

    /**
     * Sends all reminders in the list of reminders.
     */
    public void sendAllReminders() {
        for (Notifiable reminder : reminders) {
            reminder.sendRemainder();
        }
    }

    /**
     * Loads upcoming appointment reminders into the list of reminders.
     *
     * @param manager        The appointment manager to retrieve appointments.
     * @param service        The notification service used to send reminders.
     * @param administrator  The administrator to retrieve patient and doctor details.
     */
    public void loadUpcomingAppointmentReminders(AppointmentManager manager, NotificationService service, Administrator administrator) {
        List<Appointment> upcomings = manager.getUpcomingAppointmentsReminder();

        for (Appointment appt : upcomings) {
            String email = administrator.findPatientbyID(appt.getPatient()).getEmail();
            String phone = administrator.findPatientbyID(appt.getPatient()).getPhoneNumber();
            String doctorName = administrator.findDoctorbyID(appt.getDoctor()).getName();

            System.out.println("Appointment ID: " + appt.getAppointmentID());
            System.out.println("Doctor Name: " + doctorName);
            System.out.println("Patient Name: " + administrator.findPatientbyID(appt.getPatient()).getName());
            System.out.println("Appointment Date: " + appt.getAppointmentDateTime());

            String appointmentTime = appt.getAppointmentDateTime().toString();
            String message = "You have an appointment with Dr. " + doctorName + " on " + appointmentTime;

            addReminder(new EmailNotification(email, message, service));
            addReminder(new SMSNotification(phone, message, service));
        }
    }

    /**
     * Removes a reminder from the list of reminders.
     *
     * @param reminder The reminder to remove.
     */
    public void removeReminder(Notifiable reminder) {
        reminders.remove(reminder);
    }

    /**
     * Sends daily reminders for upcoming appointments.
     *
     * @param manager        The appointment manager to retrieve appointments.
     * @param service        The notification service used to send reminders.
     * @param administrator  The administrator to retrieve patient and doctor details.
     */
    public void sendDailyReminder(AppointmentManager manager, NotificationService service, Administrator administrator) {
        loadUpcomingAppointmentReminders(manager, service, administrator);
        sendAllReminders();
    }

    /**
     * Gets the count of pending reminders.
     *
     * @return The number of pending reminders.
     */
    public int getPendingRemindersCount() {
        return reminders.size();
    }
}
