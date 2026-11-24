
package controller;

import dao.DoctorDAO;
import model.Doctor;

import java.sql.SQLException;
import java.util.List;

public class DoctorController {

    private DoctorDAO doctorDAO;

    public DoctorController() {
        doctorDAO = new DoctorDAO();
    }

    public boolean addDoctor(Doctor doctor) throws SQLException {
        return doctorDAO.addDoctor(doctor);
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        return doctorDAO.getAllDoctors();
    }

    public boolean updateDoctor(Doctor doctor) throws SQLException {
        return doctorDAO.updateDoctor(doctor);
    }

    public boolean deleteDoctor(int doctorId) throws SQLException {
        return doctorDAO.deleteDoctor(doctorId);
    }
    public int getDoctorCount() throws SQLException {
        return doctorDAO.getDoctorCount();
    }
}

