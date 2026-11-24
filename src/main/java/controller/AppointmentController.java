
package controller;

import dao.AppointmentDAO;
import model.Appointment;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AppointmentController {

    private AppointmentDAO appointmentDAO;

    public AppointmentController() {
        appointmentDAO = new AppointmentDAO();
    }

    public boolean addAppointment(Appointment appointment) throws SQLException {
        return appointmentDAO.addAppointment(appointment);
    }

    // 2. View All Appointments
    public List<Appointment> getAllAppointments() throws SQLException {
        return appointmentDAO.getAllAppointments();
    }

    public boolean updateAppointment(Appointment appointment) throws SQLException {
        return appointmentDAO.updateAppointment(appointment);
    }

    public boolean deleteAppointment(int appointmentId) throws SQLException {
        return appointmentDAO.deleteAppointment(appointmentId);
    }
    public int getAppointmentCount() throws SQLException {
        return appointmentDAO.getAppointmentCount();
    }
    public Map<String, Integer> getMonthlyAppointmentCounts()
            throws SQLException {
        return appointmentDAO.getMonthlyAppointmentCounts();
    }
}

