
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/hospital_db";

    private static final String SERVER_URL =
            "jdbc:mysql://localhost:3306/";

    private static final String USER =
            System.getenv("DB_USER");

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    private static boolean databaseInitialized = false;

    public static Connection getConnection() throws SQLException {

        if (USER == null || PASSWORD == null) {
            throw new SQLException(
                    "Database username or password is not configured."
            );
        }

        if (!databaseInitialized) {
            synchronized (DBConnection.class) {
                if (!databaseInitialized) {
                    initializeDatabase();
                    databaseInitialized = true;
                }
            }
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void initializeDatabase() throws SQLException {

        try (Connection connection =
                     DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS hospital_db"
            );
        }

        try (Connection connection =
                     DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Patient (" +
                            "patient_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "age INT, " +
                            "gender VARCHAR(20), " +
                            "phone VARCHAR(20), " +
                            "address VARCHAR(255))"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Doctor (" +
                            "doctor_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "phone VARCHAR(20), " +
                            "specialization VARCHAR(100), " +
                            "consultation_fee DECIMAL(10,2))"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Appointment (" +
                            "appointment_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "patient_id INT NOT NULL, " +
                            "doctor_id INT NOT NULL, " +
                            "appointment_date DATE, " +
                            "status VARCHAR(30), " +
                            "FOREIGN KEY (patient_id) REFERENCES Patient(patient_id), " +
                            "FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id))"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Bill (" +
                            "bill_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "appointment_id INT NOT NULL, " +
                            "amount DECIMAL(10,2), " +
                            "payment_status VARCHAR(30), " +
                            "FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id))"
            );
        }
    }
}


