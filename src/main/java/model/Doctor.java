
package model;

public class Doctor {

    // Doctor information
    private int doctorId;
    private String name;
    private String phone;
    private String specialization;
    private double consultationFee;

    // Constructor
    public Doctor(int doctorId, String name, String phone,
                  String specialization, double consultationFee) {
        this.doctorId = doctorId;
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    // Getters
    public int getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }
}



