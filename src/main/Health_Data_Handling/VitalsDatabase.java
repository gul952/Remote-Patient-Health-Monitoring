package com.example.project.Health_Data_Handling;

import java.util.*;

/**
 * Represents a database for managing vital sign records of patients.
 * This class provides methods to add, find, update, and delete vital sign records.
 */
public class VitalsDatabase {
    private List<VitalSign> vitalList; // a list that will store all the vital signs of patients

    /**
     * Default constructor for VitalsDatabase.
     * Initializes an empty list to store vital sign records.
     */
    public VitalsDatabase() {
        this.vitalList = new ArrayList<>();
    }

    /**
     * Adds a new vital sign record to the database.
     *
     * @param record The vital sign record to add.
     */
    public void addVitalSign(VitalSign record) {
        vitalList.add(record); // adds the vitals sign of patient naming record in the list
    }

    /**
     * Finds all vital sign records for a specific patient based on their user ID.
     *
     * @param PatientUserID The user ID of the patient.
     * @return A list of vital sign records for the specified patient.
     */
    public List<VitalSign> findPatientViatls(String PatientUserID) {
        List<VitalSign> patientVitals = new ArrayList<>();

        for (VitalSign vital : vitalList) {
            if (vital.getUserID().equals(PatientUserID)) {
                patientVitals.add(vital);
            }
        }
        return patientVitals;
    }

    /**
     * Updates an existing vital sign record for a specific patient based on their user ID.
     *
     * @param PatientUserID   The user ID of the patient.
     * @param newVitalRecord  The new vital sign record to update.
     */
    public void updateVitalRecord(String PatientUserID, VitalSign newVitalRecord) {
        for (int i = 0; i < vitalList.size(); i++) {
            if (vitalList.get(i).getUserID().equals(PatientUserID)) {
                vitalList.set(i, newVitalRecord);
                return;
            }
        }
    }

    /**
     * Deletes all vital sign records for a specific patient based on their user ID.
     *
     * @param PatientUserID The user ID of the patient whose records are to be deleted.
     */
    public void deleteVitalRecord(String PatientUserID) {
        vitalList.removeIf(v -> v.getUserID().equals(PatientUserID));
    }
}
