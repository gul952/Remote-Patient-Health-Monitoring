package com.example.project.Health_Data_Handling;

import com.example.project.User_Management.Administrator;

import java.time.LocalDateTime;

/**
 * Represents a vital sign record for a patient, including various health metrics such as heart rate, oxygen level, and more.
 */
public class VitalSign {

    // Attributes
    private String recordID;
    private String userID;
    private String doctorID;
    private int heartRate;
    private int oxygenLevel;
    private int bloodPressure;
    private double temperature;
    private int respiratoryRate;
    private double glucoseLevel;
    private double cholesterolLevel;
    private double bmi;
    private double hydrationLevel;
    private int stressLevel;
    private LocalDateTime recordDateTime;

    /**
     * Default constructor for VitalSign.
     */
    public VitalSign() {
    }

    /**
     * Parameterized constructor for VitalSign.
     *
     * @param recordID         The unique ID of the vital sign record.
     * @param userID           The user ID of the patient.
     * @param doctorID         The doctor ID associated with the record.
     * @param heartRate        The heart rate in beats per minute.
     * @param oxygenLevel      The oxygen saturation level in percentage.
     * @param bloodPressure    The blood pressure in mmHg.
     * @param temperature      The body temperature in degrees Celsius.
     * @param respiratoryRate  The respiratory rate in breaths per minute.
     * @param glucoseLevel     The glucose level in mg/dL.
     * @param cholesterolLevel The cholesterol level in mg/dL.
     * @param bmi              The body mass index (BMI).
     * @param hydrationLevel   The hydration level in percentage.
     * @param stressLevel      The stress level on a scale of 0 to 10.
     * @param admin            The administrator verifying the patient ID.
     * @throws IllegalArgumentException If the patient ID is not found.
     */
    public VitalSign(
        String recordID, String userID, String doctorID, int heartRate, int oxygenLevel, int bloodPressure, double temperature,
        int respiratoryRate, double glucoseLevel, double cholesterolLevel, double bmi, 
        double hydrationLevel, int stressLevel, Administrator admin
    ) {
        if (admin.findPatientbyID(userID) == null) {
            throw new IllegalArgumentException("Patient with User ID " + userID + " not found!");
        }
        setRecordID(recordID);
        setUserID(userID);
        setDoctorID(doctorID);
        setHeartRate(heartRate);
        setOxygenLevel(oxygenLevel);
        setBloodPressure(bloodPressure);
        setTemperature(temperature);
        setRespiratoryRate(respiratoryRate);
        setGlucoseLevel(glucoseLevel);
        setCholesterolLevel(cholesterolLevel);
        setBmi(bmi);
        setHydrationLevel(hydrationLevel);
        setStressLevel(stressLevel);
    }

    // Setters with validations

    /**
     * Sets the record ID.
     *
     * @param recordID The unique ID of the record.
     * @throws IllegalArgumentException If the record ID is null or empty.
     */
    public void setRecordID(String recordID) {
        if (recordID == null || recordID.isEmpty()) {
            throw new IllegalArgumentException("Record ID cannot be null or empty.");
        }
        this.recordID = recordID;
    }

    /**
     * Sets the user ID.
     *
     * @param userID The user ID of the patient.
     * @throws IllegalArgumentException If the user ID is null or empty.
     */
    public void setUserID(String userID) {
        if (userID == null || userID.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        this.userID = userID;
    }

    /**
     * Sets the doctor ID.
     *
     * @param doctorID The doctor ID associated with the record.
     * @throws IllegalArgumentException If the doctor ID is null or empty.
     */
    public void setDoctorID(String doctorID) {
        if (doctorID == null || doctorID.isEmpty()) {
            throw new IllegalArgumentException("Doctor ID cannot be null or empty.");
        }
        this.doctorID = doctorID;
    }

    /**
     * Sets the heart rate.
     *
     * @param heartRate The heart rate in beats per minute.
     * @throws IllegalArgumentException If the heart rate is not between 30 and 200 bpm.
     */
    public void setHeartRate(int heartRate) {
        if (heartRate < 30 || heartRate > 200) { 
            throw new IllegalArgumentException("Heart rate must be between 30 and 200 bpm.");
        }
        this.heartRate = heartRate;
    }

    /**
     * Sets the oxygen level.
     *
     * @param oxygenLevel The oxygen saturation level in percentage.
     * @throws IllegalArgumentException If the oxygen level is not between 50 and 100%.
     */
    public void setOxygenLevel(int oxygenLevel) {
        if (oxygenLevel < 50 || oxygenLevel > 100) { 
            throw new IllegalArgumentException("Oxygen level must be between 50 and 100%.");
        }
        this.oxygenLevel = oxygenLevel;
    }

    /**
     * Sets the blood pressure.
     *
     * @param bloodPressure The blood pressure in mmHg.
     * @throws IllegalArgumentException If the blood pressure is not between 50 and 200 mmHg.
     */
    public void setBloodPressure(int bloodPressure) {
        if (bloodPressure < 50 || bloodPressure > 200) { 
            throw new IllegalArgumentException("Blood pressure must be between 50 and 200 mmHg.");
        }
        this.bloodPressure = bloodPressure;
    }

    /**
     * Sets the body temperature.
     *
     * @param temperature The body temperature in degrees Celsius.
     * @throws IllegalArgumentException If the temperature is not between 30.0 and 45.0°C.
     */
    public void setTemperature(double temperature) {
        if (temperature < 30.0 || temperature > 45.0) { 
            throw new IllegalArgumentException("Temperature must be between 30.0 and 45.0°C.");
        }
        this.temperature = temperature;
    }

    /**
     * Sets the respiratory rate.
     *
     * @param respiratoryRate The respiratory rate in breaths per minute.
     * @throws IllegalArgumentException If the respiratory rate is not between 5 and 50 breaths per minute.
     */
    public void setRespiratoryRate(int respiratoryRate) {
        if (respiratoryRate < 5 || respiratoryRate > 50) { 
            throw new IllegalArgumentException("Respiratory rate must be between 5 and 50 breaths per minute.");
        }
        this.respiratoryRate = respiratoryRate;
    }

    /**
     * Sets the glucose level.
     *
     * @param glucoseLevel The glucose level in mg/dL.
     * @throws IllegalArgumentException If the glucose level is not between 50 and 300 mg/dL.
     */
    public void setGlucoseLevel(double glucoseLevel) {
        if (glucoseLevel < 50 || glucoseLevel > 300) { 
            throw new IllegalArgumentException("Glucose level must be between 50 and 300 mg/dL.");
        }
        this.glucoseLevel = glucoseLevel;
    }

    /**
     * Sets the cholesterol level.
     *
     * @param cholesterolLevel The cholesterol level in mg/dL.
     * @throws IllegalArgumentException If the cholesterol level is not between 50 and 400 mg/dL.
     */
    public void setCholesterolLevel(double cholesterolLevel) {
        if (cholesterolLevel < 50 || cholesterolLevel > 400) { 
            throw new IllegalArgumentException("Cholesterol level must be between 50 and 400 mg/dL.");
        }
        this.cholesterolLevel = cholesterolLevel;
    }

    /**
     * Sets the BMI.
     *
     * @param bmi The body mass index (BMI).
     * @throws IllegalArgumentException If the BMI is not between 10.0 and 50.0.
     */
    public void setBmi(double bmi) {
        if (bmi < 10.0 || bmi > 50.0) {
            throw new IllegalArgumentException("BMI must be between 10.0 and 50.0.");
        }
        this.bmi = bmi;
    }

    /**
     * Sets the hydration level.
     *
     * @param hydrationLevel The hydration level in percentage.
     * @throws IllegalArgumentException If the hydration level is not between 0 and 100%.
     */
    public void setHydrationLevel(double hydrationLevel) {
        if (hydrationLevel < 0 || hydrationLevel > 100) { 
            throw new IllegalArgumentException("Hydration level must be between 0 and 100%.");
        }
        this.hydrationLevel = hydrationLevel;
    }

    /**
     * Sets the stress level.
     *
     * @param stressLevel The stress level on a scale of 0 to 10.
     * @throws IllegalArgumentException If the stress level is not between 0 and 10.
     */
    public void setStressLevel(int stressLevel) {
        if (stressLevel < 0 || stressLevel > 10) {
            throw new IllegalArgumentException("Stress level must be between 0 and 10.");
        }
        this.stressLevel = stressLevel;
    }

    /**
     * Sets the record date and time.
     *
     * @param recordDateTime The date and time of the record.
     */
    public void setRecordDateTime(LocalDateTime recordDateTime) {
        this.recordDateTime = recordDateTime;
    }

    // Getters

    /**
     * Gets the record ID.
     *
     * @return The unique ID of the record.
     */
    public String getRecordID() { return recordID; }

    /**
     * Gets the user ID.
     *
     * @return The user ID of the patient.
     */
    public String getUserID() {  return userID; }

    /**
     * Gets the doctor ID.
     *
     * @return The doctor ID associated with the record.
     */
    public String getDoctorID() { return doctorID; }

    /**
     * Gets the heart rate.
     *
     * @return The heart rate in beats per minute.
     */
    public int getHeartRate() { return heartRate; }

    /**
     * Gets the oxygen level.
     *
     * @return The oxygen saturation level in percentage.
     */
    public int getOxygenLevel() { return oxygenLevel; }

    /**
     * Gets the blood pressure.
     *
     * @return The blood pressure in mmHg.
     */
    public int getBloodPressure() { return bloodPressure; }

    /**
     * Gets the body temperature.
     *
     * @return The body temperature in degrees Celsius.
     */
    public double getTemperature() { return temperature; }

    /**
     * Gets the respiratory rate.
     *
     * @return The respiratory rate in breaths per minute.
     */
    public int getRespiratoryRate() { return respiratoryRate; }

    /**
     * Gets the glucose level.
     *
     * @return The glucose level in mg/dL.
     */
    public double getGlucoseLevel() { return glucoseLevel; }

    /**
     * Gets the cholesterol level.
     *
     * @return The cholesterol level in mg/dL.
     */
    public double getCholesterolLevel() { return cholesterolLevel; }

    /**
     * Gets the BMI.
     *
     * @return The body mass index (BMI).
     */
    public double getBmi() { return bmi; }

    /**
     * Gets the hydration level.
     *
     * @return The hydration level in percentage.
     */
    public double getHydrationLevel() { return hydrationLevel; }

    /**
     * Gets the stress level.
     *
     * @return The stress level on a scale of 0 to 10.
     */
    public int getStressLevel() { return stressLevel; }

    /**
     * Gets the record date and time.
     *
     * @return The date and time of the record.
     */
    public LocalDateTime getRecordDateTime() { return recordDateTime; }

    /**
     * Returns a string representation of the vital sign record.
     *
     * @return A formatted string containing all the vital sign metrics.
     */
    @Override
    public String toString() {
        return String.format(
            "Vital Sign Record ID: %s\n" +
            "User ID: %s\n" +
            "Doctor ID: %s\n" +
            "Heart Rate: %d bpm\n" +
            "Oxygen Level: %d%%\n" +
            "Blood Pressure: %d mmHg\n" +
            "Temperature: %.1f °C\n" +
            "Respiratory Rate: %d breaths/min\n" +
            "Glucose Level: %.2f mg/dL\n" +
            "Cholesterol Level: %.2f mg/dL\n" +
            "BMI: %.2f kg/m²\n" +
            "Hydration Level: %.2f%%\n" +
            "Stress Level: %d/10",
            this.recordID, this.userID, this.doctorID, this.heartRate, this.oxygenLevel, this.bloodPressure, this.temperature,
            this.respiratoryRate, this.glucoseLevel, this.cholesterolLevel, this.bmi,
            this.hydrationLevel, this.stressLevel
        );
    }    
}

