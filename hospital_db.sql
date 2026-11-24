
CREATE DATABASE IF NOT EXISTS hospital_db;

USE hospital_db;

CREATE TABLE IF NOT EXISTS Patient (
                                       patient_id INT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
    age INT,
    gender VARCHAR(20),
    phone VARCHAR(20),
    address VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS Doctor (
                                      doctor_id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    specialization VARCHAR(100),
    consultation_fee DECIMAL(10,2)
    );

CREATE TABLE IF NOT EXISTS Appointment (
                                           appointment_id INT AUTO_INCREMENT PRIMARY KEY,
                                           patient_id INT NOT NULL,
                                           doctor_id INT NOT NULL,
                                           appointment_date DATE,
                                           status VARCHAR(30),
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
    );

CREATE TABLE IF NOT EXISTS Bill (
                                    bill_id INT AUTO_INCREMENT PRIMARY KEY,
                                    appointment_id INT NOT NULL,
                                    amount DECIMAL(10,2),
    payment_status VARCHAR(30),
    FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id)
    );
