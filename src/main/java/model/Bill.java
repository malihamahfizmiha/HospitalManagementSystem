
package model;

public class Bill implements Payable {

    // Bill information
    private int billId;
    private int appointmentId;
    private double amount;
    private String paymentStatus;

    // Constructor
    public Bill(int billId, int appointmentId,
                double amount, String paymentStatus) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Getters
    public int getBillId() {
        return billId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    // Setters
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public void markAsPaid() {
        this.paymentStatus = "Paid";
    }
}

