package com.example.project.User_Management;

import java.util.*;
import java.util.stream.Collectors;

import com.example.project.Doctor_Patient_Interaction.Feedback;
import com.example.project.Doctor_Patient_Interaction.MedicalHistory;
import com.example.project.Doctor_Patient_Interaction.Prescription;

/**
 * Represents an Administrator in the system who manages doctors, patients, and their interactions.
 */
public class Administrator extends User {

    /**
     * Enum representing the roles of an Administrator.
     */
    public enum AdminRole {
        SYSTEM_ADMIN,
        HOSPITAL_ADMIN,
        MEDICAL_RECORD_ADMIN
    }

    // Attributes
    private AdminRole role;
    private List<Doctor> managedDoctors;
    private List<Patient> managedPatients;
    private Set<String> permissions;
    private String department;

    /**
     * Default constructor for Administrator.
     */
    public Administrator() {
        this.managedDoctors = new ArrayList<>();
        this.managedPatients = new ArrayList<>();
    }

    /**
     * Parameterized constructor for Administrator.
     *
     * @param userID          The unique ID of the administrator.
     * @param name            The name of the administrator.
     * @param email           The email of the administrator.
     * @param phoneNumber     The phone number of the administrator.
     * @param password        The password of the administrator.
     * @param role            The role of the administrator.
     * @param managedDoctors  The list of doctors managed by the administrator.
     * @param managedPatients The list of patients managed by the administrator.
     * @param address         The address of the administrator.
     * @param age             The age of the administrator.
     * @param gender          The gender of the administrator.
     * @param accountStatus   The account status of the administrator.
     * @param permissions     The set of permissions assigned to the administrator.
     * @param department      The department of the administrator.
     */
    public Administrator(String userID, String name, String email, String phoneNumber, String password,
                         AdminRole role, List<Doctor> managedDoctors, List<Patient> managedPatients,
                         String address, int age, String gender, boolean accountStatus,
                         Set<String> permissions, String department) {
        super(userID, name, email, phoneNumber, password, address, age, gender, accountStatus);
        this.role = role;
        this.managedDoctors = managedDoctors;
        this.managedPatients = managedPatients;
        this.permissions = permissions;
        this.department = department;
    }

    // Setters with validations

    /**
     * Sets the role of the administrator.
     *
     * @param role The role to set.
     */
    public void setRole(AdminRole role) {
        this.role = role;
    }

    /**
     * Sets the list of managed doctors.
     *
     * @param managedDoctors The list of doctors to set.
     */
    public void setManagedDoctors(List<Doctor> managedDoctors) {
        this.managedDoctors = managedDoctors;
    }

    /**
     * Sets the list of managed patients.
     *
     * @param managedPatients The list of patients to set.
     */
    public void setManagedPatients(List<Patient> managedPatients) {
        this.managedPatients = managedPatients;
    }

    /**
     * Sets the permissions of the administrator.
     *
     * @param permissions The set of permissions to set.
     */
    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * Sets the department of the administrator.
     *
     * @param department The department to set.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    // Getters

    /**
     * Gets the role of the administrator.
     *
     * @return The role of the administrator.
     */
    public AdminRole getRole() {
        return role;
    }

    /**
     * Gets the list of managed doctors.
     *
     * @return The list of managed doctors.
     */
    public List<Doctor> getManagedDoctors() {
        return managedDoctors;
    }

    /**
     * Gets the list of managed patients.
     *
     * @return The list of managed patients.
     */
    public List<Patient> getManagedPatients() {
        return managedPatients;
    }

    /**
     * Gets the permissions of the administrator.
     *
     * @return The set of permissions.
     */
    public Set<String> getPermissions() {
        return permissions;
    }

    /**
     * Gets the department of the administrator.
     *
     * @return The department.
     */
    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return String.format("%s\nRole: %s\nDepartment: %s\nManaged Doctors: %s\nManaged Patients: %s\nPermissions: %s",
                super.toString(), this.role, this.department, this.managedDoctors, this.managedPatients, this.permissions);
    }

    // Methods to manage doctors and patients

    /**
     * Adds a doctor to the list of managed doctors.
     *
     * @param doctor The doctor to add.
     */
    public void addDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        if (managedDoctors.stream().anyMatch(d -> d.getUserID().equals(doctor.getUserID()))) {
            throw new IllegalArgumentException("Doctor with this userID already exists.");
        }
        managedDoctors.add(doctor);
    }

    /**
     * Adds a patient to the list of managed patients.
     *
     * @param patient The patient to add.
     */
    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (managedPatients.stream().anyMatch(p -> p.getUserID().equals(patient.getUserID()))) {
            throw new IllegalArgumentException("Patient with this userID already exists.");
        }
        managedPatients.add(patient);
    }

    /**
     * Removes a doctor by their user ID.
     *
     * @param userID The user ID of the doctor to remove.
     */
    public void removeDoctor(String userID) {
        boolean removed = managedDoctors.removeIf(doctor -> doctor.getUserID().equals(userID));
        if (!removed) {
            throw new IllegalArgumentException("Doctor with userID " + userID + " not found.");
        }
    }

    /**
     * Removes a patient by their user ID.
     *
     * @param userID The user ID of the patient to remove.
     */
    public void removePatient(String userID) {
        boolean removed = managedPatients.removeIf(patient -> patient.getUserID().equals(userID));
        if (!removed) {
            throw new IllegalArgumentException("Patient with userID " + userID + " not found.");
        }
    }

    /**
     * Assigns a doctor to a patient.
     *
     * @param doctorUserID The user ID of the doctor.
     * @param patientUserID The user ID of the patient.
     */
    public void assignDoctorToPatient(String doctorUserID, String patientUserID) {
        Doctor doctor = managedDoctors.stream().filter(d -> d.getUserID().equals(doctorUserID)).findFirst().orElse(null);
        Patient patient = managedPatients.stream().filter(p -> p.getUserID().equals(patientUserID)).findFirst().orElse(null);

        if (doctor == null || patient == null) {
            throw new IllegalArgumentException("Doctor or Patient not found!");
        }

        patient.setAssignedDoctor(doctor);
        doctor.assignPatient(patient);
    }

    /**
     * Views all managed doctors.
     *
     * @return A list of all managed doctors.
     */
    public List<Doctor> viewAllDoctors() {
        return managedDoctors;
    }

    /**
     * Views all managed patients.
     *
     * @return A list of all managed patients.
     */
    public List<Patient> viewAllPatients() {
        return managedPatients;
    }

    /**
     * Counts the number of managed doctors.
     *
     * @return The number of managed doctors.
     */
    public int countDoctor() {
        return managedDoctors.size();
    }

    /**
     * Counts the number of managed patients.
     *
     * @return The number of managed patients.
     */
    public int countPatient() {
        return managedPatients.size();
    }

    /**
     * Finds a doctor by their user ID.
     *
     * @param doctorUserID The user ID of the doctor.
     * @return The doctor, or null if not found.
     */
    public Doctor findDoctorbyID(String doctorUserID) {
        return managedDoctors.stream().filter(d -> d.getUserID().equals(doctorUserID)).findFirst().orElse(null);
    }

    /**
     * Finds a patient by their user ID.
     *
     * @param patientUserID The user ID of the patient.
     * @return The patient, or null if not found.
     */
    public Patient findPatientbyID(String patientUserID) {
        return managedPatients.stream().filter(p -> p.getUserID().equals(patientUserID)).findFirst().orElse(null);
    }

    /**
     * Assigns a doctor to multiple patients.
     *
     * @param doctorUserID The user ID of the doctor.
     * @param patients      The list of patients to assign.
     */
    public void assignDoctorToMultiplePatient(String doctorUserID, List<Patient> patients) {
        Doctor doctor = findDoctorbyID(doctorUserID);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor with this User ID " + doctorUserID + " not found!");
        }

        for (Patient patient : patients) {
            if (patient != null) {
                patient.setAssignedDoctor(doctor);
                doctor.assignPatient(patient);
            }
        }
    }

    /**
     * Views feedback sent to a specific doctor.
     *
     * @param doctorID      The user ID of the doctor.
     * @param administrator The administrator instance.
     * @return A list of feedback for the doctor.
     */
    public List<Feedback> seeDoctorFeedbacks(String doctorID, Administrator administrator) {
        Doctor doctor = administrator.findDoctorbyID(doctorID);
        return doctor.getFeedbackList();
    }

    /**
     * Views feedback sent by a specific patient to a specific doctor.
     *
     * @param patientID     The user ID of the patient.
     * @param doctorID      The user ID of the doctor.
     * @param administrator The administrator instance.
     * @return A list of feedback from the patient to the doctor.
     */
    public List<Feedback> seePatientFeedbacks(String patientID, String doctorID, Administrator administrator) {
        Doctor doctor = administrator.findDoctorbyID(doctorID);

        List<Feedback> patientFeedbacks = doctor.getFeedbackList().stream()
                .filter(feedback -> feedback.getPatientID().equals(patientID))
                .collect(Collectors.toList());

        if (patientFeedbacks.isEmpty()) {
            throw new IllegalArgumentException("Sorry, this patient has provided no feedback to this doctor so far.");
        }

        return patientFeedbacks;
    }

    /**
     * Views prescriptions sent to a specific patient.
     *
     * @param patientID     The user ID of the patient.
     * @param administrator The administrator instance.
     * @return A list of prescriptions for the patient.
     */
    public List<Prescription> seePatientPrescriptions(String patientID, Administrator administrator) {
        Patient patient = administrator.findPatientbyID(patientID);
        return patient.getPrescriptions();
    }

    /**
     * Views prescriptions sent by a specific doctor to a specific patient.
     *
     * @param patientID     The user ID of the patient.
     * @param doctorID      The user ID of the doctor.
     * @param administrator The administrator instance.
     * @return A list of prescriptions from the doctor to the patient.
     */
    public List<Prescription> seeDoctorPrescriptions(String patientID, String doctorID, Administrator administrator) {
        Patient patient = administrator.findPatientbyID(patientID);

        List<Prescription> doctorPrescriptions = patient.getPrescriptions().stream()
                .filter(prescription -> prescription.getDoctor().getUserID().equals(doctorID))
                .collect(Collectors.toList());

        if (doctorPrescriptions.isEmpty()) {
            throw new IllegalArgumentException("Sorry, this doctor has provided no prescriptions to this patient.");
        }

        return doctorPrescriptions;
    }

    /**
     * Views the medical history of a specific patient.
     *
     * @param patientID The user ID of the patient.
     * @return A list of medical histories for the patient.
     */
    public List<MedicalHistory> seePatientMedicalHistories(String patientID) {
        Patient patient = findPatientbyID(patientID);
        return patient.getMedicalHistories();
    }
}
