
package dao;

import database.DBConnection;
import model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public boolean addPatient(Patient patient) throws SQLException {

        String sql = "INSERT INTO Patient (name, age, gender, phone, address) VALUES (?, ?, ?, ?, ?)";

        Connection connection = DBConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, patient.getName());
        statement.setInt(2, patient.getAge());
        statement.setString(3, patient.getGender());
        statement.setString(4, patient.getPhone());
        statement.setString(5, patient.getAddress());

        int result = statement.executeUpdate();

        statement.close();
        connection.close();

        return result > 0;
    }

    public List<Patient> getAllPatients() throws SQLException {

        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM Patient";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Patient patient = new Patient(
                        resultSet.getInt("patient_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("gender"),
                        resultSet.getString("phone"),
                        resultSet.getString("address")
                );

                patients.add(patient);
            }
        }

        return patients;
    }

    public boolean updatePatient(Patient patient) throws SQLException {

        String sql = "UPDATE Patient SET name = ?, age = ?, gender = ?, phone = ?, address = ? WHERE patient_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patient.getName());
            statement.setInt(2, patient.getAge());
            statement.setString(3, patient.getGender());
            statement.setString(4, patient.getPhone());
            statement.setString(5, patient.getAddress());
            statement.setInt(6, patient.getPatientId());

            int result = statement.executeUpdate();

            return result > 0;
        }
    }

    public boolean deletePatient(int patientId) throws SQLException {

        String sql = "DELETE FROM Patient WHERE patient_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);

            int result = statement.executeUpdate();

            return result > 0;
        }
    }


    public int getPatientCount() throws SQLException {

        String query = "SELECT COUNT(*) FROM Patient";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }

}

