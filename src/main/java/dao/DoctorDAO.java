
package dao;

import database.DBConnection;
import model.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public boolean addDoctor(Doctor doctor) throws SQLException {

        String sql = "INSERT INTO Doctor (name, phone, specialization, consultation_fee) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getPhone());
            statement.setString(3, doctor.getSpecialization());
            statement.setDouble(4, doctor.getConsultationFee());

            int result = statement.executeUpdate();

            return result > 0;
        }
    }


    public List<Doctor> getAllDoctors() throws SQLException {

        List<Doctor> doctors = new ArrayList<>();

        String sql = "SELECT * FROM Doctor";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Doctor doctor = new Doctor(
                        resultSet.getInt("doctor_id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("specialization"),
                        resultSet.getDouble("consultation_fee")
                );

                doctors.add(doctor);
            }
        }

        return doctors;
    }


    public boolean updateDoctor(Doctor doctor) throws SQLException {

        String sql = "UPDATE Doctor SET name = ?, phone = ?, specialization = ?, consultation_fee = ? WHERE doctor_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getPhone());
            statement.setString(3, doctor.getSpecialization());
            statement.setDouble(4, doctor.getConsultationFee());
            statement.setInt(5, doctor.getDoctorId());

            int result = statement.executeUpdate();

            return result > 0;
        }
    }

    public boolean deleteDoctor(int doctorId) throws SQLException {

        String sql = "DELETE FROM Doctor WHERE doctor_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, doctorId);

            int result = statement.executeUpdate();

            return result > 0;
        }
    }
    public int getDoctorCount() throws SQLException {

        String query = "SELECT COUNT(*) FROM Doctor";

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

