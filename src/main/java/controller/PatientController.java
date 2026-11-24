
package controller;

import dao.PatientDAO;
import model.Patient;

import java.sql.SQLException;
import java.util.List;

public class PatientController {

    private PatientDAO patientDAO;

    public PatientController() {
        patientDAO = new PatientDAO();
    }

    public boolean addPatient(Patient patient) throws SQLException {
        return patientDAO.addPatient(patient);
    }

    public List<Patient> getAllPatients() throws SQLException {
        return patientDAO.getAllPatients();
    }

    public boolean updatePatient(Patient patient) throws SQLException {
        return patientDAO.updatePatient(patient);
    }

    public boolean deletePatient(int patientId) throws SQLException {
        return patientDAO.deletePatient(patientId);
    }

    public int getPatientCount() throws SQLException {
        return patientDAO.getPatientCount();
    }

}

