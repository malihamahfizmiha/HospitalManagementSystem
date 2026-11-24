
package controller;

import dao.BillDAO;
import model.Bill;

import java.sql.SQLException;
import java.util.List;

public class BillingController {

    private BillDAO billDAO;

    public BillingController() {
        billDAO = new BillDAO();
    }

    public boolean addBill(Bill bill) throws SQLException {
        return billDAO.addBill(bill);
    }

    public List<Bill> getAllBills() throws SQLException {
        return billDAO.getAllBills();
    }

    public boolean updateBill(Bill bill) throws SQLException {
        return billDAO.updateBill(bill);
    }

    public boolean deleteBill(int billId) throws SQLException {
        return billDAO.deleteBill(billId);
    }
    public int getBillCount() throws SQLException {
        return billDAO.getBillCount();
    }
}

