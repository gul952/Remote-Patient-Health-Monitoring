package com.example.project.Appointment_Scheduling;

import com.example.project.Appointment_Scheduling.Appointment.AppointmentStatus;
import com.example.project.User_Management.Administrator;
import com.example.project.User_Management.Doctor;
import com.example.project.User_Management.Patient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the scheduling, rescheduling, cancellation, and retrieval of appointments.
 */
public class AppointmentManager {
    // a list of the appointments
    private List<Appointment> appointments;

    /**
     * Default constructor for AppointmentManager.
     * Initializes an empty list to store appointments.
     */
    public AppointmentManager() {
        this.appointments = new ArrayList<>();
    }

    /**
     * Sets the list of appointments.
     *
     * @param appointments The list of appointments to set.
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    /**
     * Gets the list of appointments.
     *
     * @return The list of appointments.
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Schedules an appointment between a doctor and a patient.
     *
     * @param appointmentID      The unique ID of the appointment.
     * @param patientID          The ID of the patient.
     * @param doctorID           The ID of the doctor.
     * @param administrator      The administrator managing the scheduling.
     * @param appointmentDateTime The date and time of the appointment.
     * @param reason             The reason for the appointment.
     * @return True if the appointment is successfully scheduled, false otherwise.
     */
    public boolean scheduleAppointment(String appointmentID, String patientID, String doctorID, Administrator administrator, LocalDateTime appointmentDateTime, String reason) {
        if (!administrator.findDoctorbyID(doctorID).isAvailableOnDay(appointmentDateTime.getDayOfWeek().toString())) {
            System.out.printf("Sorry Doctor %s is not available on this Day!\n The available Days are %s", administrator.findDoctorbyID(doctorID).getName(), administrator.findDoctorbyID(doctorID).getAvailableDays());
            return false;
        }
        Appointment appointment = new Appointment(appointmentID, patientID, doctorID, appointmentDateTime, "", reason, AppointmentStatus.SCHEDULED);
        appointments.add(appointment);
        administrator.findDoctorbyID(doctorID).addAppointmentDate(appointmentDateTime);
        administrator.findPatientbyID(patientID).setAppointmentDate(appointmentDateTime);
        administrator.assignDoctorToPatient(doctorID, patientID);
        System.out.println("Appointment scheduled successfully!");
        return true;
    }

    /**
     * Adds an appointment to the list.
     *
     * @param appointment The appointment to add.
     */
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Finds an appointment by its ID.
     *
     * @param appointmentID The unique ID of the appointment.
     * @return The appointment if found, null otherwise.
     */
    public Appointment findAppointmentByID(String appointmentID) {
        Appointment appointment = appointments.stream().filter(d -> d.getAppointmentID().equals(appointmentID)).findFirst().orElse(null);
        if (appointment == null) {
            System.out.println("Sorry no appointment of this ID found");
        }
        return appointment;
    }

    /**
     * Approves or rejects an appointment request sent by a patient.
     *
     * @param appointmentID The unique ID of the appointment.
     * @param administrator The administrator managing the approval.
     * @return True if the appointment is approved, false otherwise.
     */
    public boolean approveOrRejectAppointmentsByPatients(String appointmentID, Administrator administrator) {
        Appointment appointment = findAppointmentByID(appointmentID);
        if (appointment != null && appointment.getAppointmentStatus() == AppointmentStatus.PENDING && administrator.findDoctorbyID(appointment.getDoctor()).getAvailableDays().contains(appointment.getAppointmentDateTime().getDayOfWeek().toString())) {
            appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
            System.out.println("Appointment approved!");
            return true;
        }
        System.out.println("Appointment either not found, already approved or rejected.");
        return false;
    }

    /**
     * Handles scheduling an appointment request from a doctor.
     *
     * @param appointment   The appointment to schedule.
     * @param administrator The administrator managing the scheduling.
     * @return True if the appointment is approved, false otherwise.
     */
    public boolean scheduleAppointmentRequestByDoctor(Appointment appointment, Administrator administrator) {
        if (appointment.getAppointmentStatus() == AppointmentStatus.PENDING) {
            return approveOrRejectAppointmentsByPatients(appointment.getAppointmentID(), administrator);
        }
        System.out.println("Appointment request is not in pending status.");
        return false;
    }

    /**
     * Finds all appointments of a specific patient.
     *
     * @param patientID The ID of the patient.
     * @return A list of appointments for the specified patient.
     */
    public List<Appointment> findAppointmentsofPatient(String patientID) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().equals(patientID)) {
                result.add(appointment);
            }
        }
        if (result.isEmpty()) {
            System.out.println("There is no appointment of this patient found");
        }
        return result;
    }

    /**
     * Finds all appointments of a specific doctor.
     *
     * @param doctorID The ID of the doctor.
     * @return A list of appointments for the specified doctor.
     */
    public List<Appointment> findAppointmentsofDoctor(String doctorID) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().equals(doctorID)) {
                result.add(appointment);
            }
        }
        if (result.isEmpty()) {
            System.out.println("There is no appointment of this doctor found");
        }
        return result;
    }

    /**
     * Reschedules an appointment to a new date and time.
     *
     * @param appointmentID       The unique ID of the appointment.
     * @param newAppointmenDateTime The new date and time for the appointment.
     * @return True if the appointment is successfully rescheduled, false otherwise.
     */
    public boolean reScheduleAppointment(String appointmentID, LocalDateTime newAppointmenDateTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.rescheduleAppointment(newAppointmenDateTime);
                System.out.println("The Appointment has been Rescheduled!");
                return true;
            }
        }
        System.out.println("Sorry this appointment does not exist!");
        return false;
    }

    /**
     * Cancels an appointment.
     *
     * @param appointmentID The unique ID of the appointment.
     * @return True if the appointment is successfully canceled, false otherwise.
     */
    public boolean cancelAppointment(String appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.cancelAppointment();
                appointments.remove(appointment);
                System.out.println("The Appointment has been Cancelled!");
                return true;
            }
        }
        System.out.println("Sorry this appointment does not exist!");
        return false;
    }

    /**
     * Finds all appointments with a specific status.
     *
     * @param appointmentStatus The status to filter appointments by.
     * @return A list of appointments with the specified status.
     */
    public List<Appointment> findSpecificStatusAppointments(AppointmentStatus appointmentStatus) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentStatus().equals(appointmentStatus)) {
                result.add(appointment);
            }
        }
        return result;
    }

    /**
     * Finds all appointments between specific dates.
     *
     * @param starDateTime The start date and time.
     * @param endDateTime  The end date and time.
     * @return A list of appointments between the specified dates.
     */
    public List<Appointment> getAppointmentsBetween(LocalDateTime starDateTime, LocalDateTime endDateTime) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDateTime().isAfter(starDateTime) && appointment.getAppointmentDateTime().isBefore(endDateTime)) {
                result.add(appointment);
            }
        }
        return result;
    }

    /**
     * Gets the upcoming appointments of a specific doctor.
     *
     * @param doctorID The ID of the doctor.
     * @return A list of upcoming appointments for the doctor.
     */
    public List<Appointment> getDoctorUpcomingAppointments(String doctorID) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().equals(doctorID) && appointment.getAppointmentDateTime().isAfter(LocalDateTime.now())) {
                result.add(appointment);
            }
        }
        return result;
    }

    /**
     * Gets the upcoming appointments of a specific patient.
     *
     * @param patientID The ID of the patient.
     * @return A list of upcoming appointments for the patient.
     */
    public List<Appointment> getPatientUpcomingAppointments(String patientID) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().equals(patientID) && appointment.getAppointmentDateTime().isAfter(LocalDateTime.now())) {
                result.add(appointment);
            }
        }
        return result;
    }

    /**
     * Counts the number of appointments for a specific doctor.
     *
     * @param doctor The doctor to count appointments for.
     * @return The number of appointments for the doctor.
     */
    public int getAppointmentCountForDoctor(Doctor doctor) {
        int count = 0;
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().equals(doctor)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of appointments for a specific patient.
     *
     * @param patient The patient to count appointments for.
     * @return The number of appointments for the patient.
     */
    public int getAppointmentCountForPatient(Patient patient) {
        int count = 0;
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().equals(patient)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the upcoming appointments that are within 24 hours.
     *
     * @return A list of upcoming appointments within 24 hours.
     */
    public List<Appointment> getUpcomingAppointmentsReminder() {
        List<Appointment> reminders = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now().plusHours(24)) && appointment.getAppointmentDateTime().isAfter(LocalDateTime.now())) {
                reminders.add(appointment);
            }
        }
        return reminders;
    }
}
