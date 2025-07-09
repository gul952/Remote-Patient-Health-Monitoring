package com.example.project.DATABASE;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBConnection is a utility class that provides a method to establish a connection to the database.
 */
public class DBConnection {

    // Database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String USER = "root";
    private static final String PASSWORD = "$trongholD1";

    /**
     * Establishes a connection to the database.
     *
     * @return a Connection object if successful, null otherwise.
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}