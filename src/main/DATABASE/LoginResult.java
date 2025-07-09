package com.example.project.DATABASE;

/**
 * This class represents the result of a login attempt.
 * It contains the user's role and account status.
 */
public class LoginResult {
    private String role;
    private boolean accountStatus;

    /**
     * Default constructor for LoginResult.
     */
    public LoginResult(String role, boolean accountStatus) {
        this.role = role;
        this.accountStatus = accountStatus;
    }

    
    public String getRole() {
        return role;
    }

    public boolean isAccountStatus() {
        return accountStatus;
    }
}
