package com.example.project.DATABASE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the login process for users.
 * It checks the user's credentials and retrieves their role and account status.
 */
public class LoginServer {
    /**
     * Attempts to log in a user with the provided userID and password.
     *
     * @param userID   The user's ID.
     * @param password The user's password.
     * @return A LoginResult object containing the user's role and account status, or null if login fails.
     */
    public static LoginResult login(String userID, String password) {
        String query = "SELECT role, accountStatus FROM user WHERE userID = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userID);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                boolean accountStatus = rs.getInt("accountStatus") == 1;
                return new LoginResult(role, accountStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
