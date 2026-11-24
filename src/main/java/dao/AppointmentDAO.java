
package dao;

import database.DBConnection;
import model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class AppointmentDAO {

    public boolean addAppointment(Appointment appointment) throws SQLException {

        String sql = "INSERT INTO Appointment (patient_id, doctor_id, appointment_date, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointment.getPatientId());
            statement.setInt(2, appointment.getDoctorId());
            statement.setDate(3, java.sql.Date.valueOf(appointment.getAppointmentDate()));
            statement.setString(4, appointment.getStatus());

            int result = statement.executeUpdate();

            return result > 0;
        }
    }

    public List<Appointment> getAllAppointments() throws SQLException {

        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT * FROM Appointment";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Appointment appointment = new Appointment(
                        resultSet.getInt("appointment_id"),
                        resultSet.getInt("patient_id"),
                        resultSet.getInt("doctor_id"),
                        resultSet.getDate("appointment_date").toLocalDate(),
                        resultSet.getString("status")
                );

                appointments.add(appointment);
            }
        }

        return appointments;
    }

    public boolean updateAppointment(Appointment appointment) throws SQLException {

        String sql = "UPDATE Appointment SET patient_id = ?, doctor_id = ?, appointment_date = ?, status = ? WHERE appointment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointment.getPatientId());
            statement.setInt(2, appointment.getDoctorId());
            statement.setDate(3, java.sql.Date.valueOf(appointment.getAppointmentDate()));
            statement.setString(4, appointment.getStatus());
            statement.setInt(5, appointment.getAppointmentId());

            int result = statement.executeUpdate();

            return result > 0;
        }
    }

    public boolean deleteAppointment(int appointmentId) throws SQLException {

        String sql = "DELETE FROM Appointment WHERE appointment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            int result = statement.executeUpdate();

            return result > 0;
        }
    }
    public int getAppointmentCount() throws SQLException {

        String query = "SELECT COUNT(*) FROM Appointment";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }

    public Map<String, Integer> getMonthlyAppointmentCounts() throws SQLException {

        Map<String, Integer> monthlyCounts = new LinkedHashMap<>();

        String[] months = {
                "Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"
        };

        for (String month : months) {
            monthlyCounts.put(month, 0);
        }

        String query = "SELECT MONTH(appointment_date), COUNT(*) " +
                "FROM Appointment " +
                "WHERE YEAR(appointment_date) = YEAR(CURDATE()) " +
                "GROUP BY MONTH(appointment_date)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int monthNumber = resultSet.getInt(1);
                int count = resultSet.getInt(2);

                monthlyCounts.put(months[monthNumber - 1], count);
            }
        }

        return monthlyCounts;
    }

}

