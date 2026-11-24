
package view;

import controller.BillingController;
import model.Bill;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class BillingView extends BorderPane {

    private BillingController billingController =
            new BillingController();

    private TextField appointmentIdField = new TextField();
    private TextField amountField = new TextField();

    private ComboBox<String> paymentStatusBox =
            new ComboBox<>();

    private TableView<Bill> billTable = new TableView<>();

    public BillingView() {

        setPadding(new Insets(25));
        setStyle("-fx-background-color: #F4F7FA;");

        // Heading
        Label heading = new Label("Billing Management");
        heading.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17324D;"
        );

        // Billing form
        VBox form = new VBox(10);

        Label formTitle = new Label("Bill Information");
        formTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        appointmentIdField.setPromptText("Appointment ID");
        amountField.setPromptText("Bill Amount");

        paymentStatusBox.getItems().addAll(
                "Paid", "Unpaid"
        );
        paymentStatusBox.setPromptText("Payment Status");

        HBox row1 = new HBox(10,
                appointmentIdField, amountField, paymentStatusBox);

        Button addButton = new Button("Add Bill");
        Button updateButton = new Button("Update Bill");
        Button deleteButton = new Button("Delete Bill");
        Button clearButton = new Button("Clear");

        HBox buttons = new HBox(10,
                addButton, updateButton, deleteButton, clearButton);

        form.getChildren().addAll(
                formTitle, row1, buttons
        );

        // Bill table
        TableColumn<Bill, Integer> idColumn =
                new TableColumn<>("Bill ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("billId"));

        TableColumn<Bill, Integer> appointmentColumn =
                new TableColumn<>("Appointment ID");
        appointmentColumn.setCellValueFactory(
                new PropertyValueFactory<>("appointmentId"));

        TableColumn<Bill, Double> amountColumn =
                new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount"));

        TableColumn<Bill, String> statusColumn =
                new TableColumn<>("Payment Status");
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("paymentStatus"));

        billTable.getColumns().addAll(
                idColumn, appointmentColumn,
                amountColumn, statusColumn
        );

        billTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // Button actions
        addButton.setOnAction(e -> addBill());
        updateButton.setOnAction(e -> updateBill());
        deleteButton.setOnAction(e -> deleteBill());
        clearButton.setOnAction(e -> clearFields());

        // Select a row to edit or delete
        billTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldBill, selectedBill) -> {

                    if (selectedBill != null) {
                        appointmentIdField.setText(
                                String.valueOf(selectedBill.getAppointmentId()));

                        amountField.setText(
                                String.valueOf(selectedBill.getAmount()));

                        paymentStatusBox.setValue(
                                selectedBill.getPaymentStatus());
                    }
                });

        VBox topSection = new VBox(20, heading, form);

        setTop(topSection);
        setCenter(billTable);

        refreshBills();
    }

    private void addBill() {
        try {
            if (appointmentIdField.getText().trim().isEmpty() ||
                    amountField.getText().trim().isEmpty() ||
                    paymentStatusBox.getValue() == null) {

                showMessage("Please fill in all billing information.");
                return;
            }
            if (Double.parseDouble(amountField.getText()) <= 0) {
                showMessage("Bill amount must be greater than zero.");
                return;
            }
            Bill bill = new Bill(
                    0,
                    Integer.parseInt(appointmentIdField.getText()),
                    Double.parseDouble(amountField.getText()),
                    paymentStatusBox.getValue()
            );

            if (billingController.addBill(bill)) {
                showMessage("Bill added successfully!");
                clearFields();
                refreshBills();
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter valid Appointment ID and Amount.");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        } catch (NullPointerException e) {
            showMessage("Please select a payment status.");
        }
    }

    private void updateBill() {
        Bill selected = billTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a bill first.");
            return;
        }

        try {
            Bill bill = new Bill(
                    selected.getBillId(),
                    Integer.parseInt(appointmentIdField.getText()),
                    Double.parseDouble(amountField.getText()),
                    paymentStatusBox.getValue()
            );

            if (billingController.updateBill(bill)) {
                showMessage("Bill updated successfully!");
                clearFields();
                refreshBills();
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter valid Appointment ID and Amount.");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        } catch (NullPointerException e) {
            showMessage("Please select a payment status.");
        }
    }

    private void deleteBill() {
        Bill selected = billTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a bill first.");
            return;
        }

        try {
            if (billingController.deleteBill(selected.getBillId())) {
                showMessage("Bill deleted successfully!");
                clearFields();
                refreshBills();
            }

        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void refreshBills() {
        try {
            ObservableList<Bill> bills =
                    FXCollections.observableArrayList(
                            billingController.getAllBills()
                    );

            billTable.setItems(bills);

        } catch (SQLException e) {
            showMessage("Could not load bills: " + e.getMessage());
        }
    }

    private void clearFields() {
        appointmentIdField.clear();
        amountField.clear();
        paymentStatusBox.setValue(null);
        billTable.getSelectionModel().clearSelection();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hospital Management System");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

