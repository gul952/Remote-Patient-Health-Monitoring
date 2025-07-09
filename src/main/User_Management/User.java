package com.example.project.User_Management;

/**
 * Abstract base class representing a generic user in the system.
 * This class is extended by specific user types such as Administrator, Doctor, and Patient.
 */
public abstract class User {
    private String userID;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String address;
    private int age;
    private String gender;
    private boolean accountStatus;

    /**
     * Default constructor for User.
     */
    User() {
    }

    /**
     * Parameterized constructor to initialize all attributes of the User.
     *
     * @param userID        The unique ID of the user.
     * @param name          The name of the user.
     * @param email         The email address of the user.
     * @param phoneNumber   The phone number of the user.
     * @param password      The password of the user.
     * @param address       The address of the user.
     * @param age           The age of the user.
     * @param gender        The gender of the user.
     * @param accountStatus The account status of the user (active/inactive).
     */
    public User(String userID, String name, String email, String phoneNumber, String password,
                String address, int age, String gender, boolean accountStatus) {
        setUserID(userID);
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPassword(password);
        setAddress(address);
        setAge(age);
        setGender(gender);
        setAccountStatus(accountStatus);
    }

    // Setters with Validation

    /**
     * Sets the user ID.
     *
     * @param userID The unique ID of the user.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber The phone number of the user.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the address of the user.
     *
     * @param address The address of the user.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the age of the user.
     *
     * @param age The age of the user.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender The gender of the user.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the account status of the user.
     *
     * @param accountStatus The account status of the user (true for active, false for inactive).
     */
    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    // Getters

    /**
     * Gets the user ID.
     *
     * @return The unique ID of the user.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the address of the user.
     *
     * @return The address of the user.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the age of the user.
     *
     * @return The age of the user.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the gender of the user.
     *
     * @return The gender of the user.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the account status of the user.
     *
     * @return True if the account is active, false if inactive.
     */
    public boolean isAccountStatus() {
        return accountStatus;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return A formatted string containing the user's details.
     */
    @Override
    public String toString() {
        return String.format("User ID: %s\nName: %s\nEmail: %s\nPhone: %s\nAge: %d\nGender: %s\nAccount Status: %s",
                userID, name, email, phoneNumber, age, gender,
                accountStatus ? "Active" : "Inactive");
    }
}
