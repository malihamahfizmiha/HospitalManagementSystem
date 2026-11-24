
package dao;

import database.DBConnection;
import model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public boolean addBill(Bill bill) throws SQLException {

        String sql = "INSERT INTO Bill (appointment_id, amount, payment_status) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bill.getAppointmentId());
            statement.setDouble(2, bill.getAmount());
            statement.setString(3, bill.getPaymentStatus());

            int result = statement.executeUpdate();

            return result > 0;
        }
    }


    public List<Bill> getAllBills() throws SQLException {

        List<Bill> bills = new ArrayList<>();

        String sql = "SELECT * FROM Bill";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Bill bill = new Bill(
                        resultSet.getInt("bill_id"),
                        resultSet.getInt("appointment_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("payment_status")
                );

                bills.add(bill);
            }
        }

        return bills;
    }

    public boolean updateBill(Bill bill) throws SQLException {

        String sql = "UPDATE Bill SET appointment_id = ?, amount = ?, payment_status = ? WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bill.getAppointmentId());
            statement.setDouble(2, bill.getAmount());
            statement.setString(3, bill.getPaymentStatus());
            statement.setInt(4, bill.getBillId());

            int result = statement.executeUpdate();

            return result > 0;
        }
    }

    public boolean deleteBill(int billId) throws SQLException {

        String sql = "DELETE FROM Bill WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, billId);

            int result = statement.executeUpdate();

            return result > 0;
        }
    }
    public int getBillCount() throws SQLException {

        String query = "SELECT COUNT(*) FROM Bill";

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

