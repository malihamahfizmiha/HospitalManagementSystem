
package view;

import controller.AppointmentController;
import model.Appointment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;

public class AppointmentView extends BorderPane {

    private AppointmentController appointmentController =
            new AppointmentController();

    private TextField patientIdField = new TextField();
    private TextField doctorIdField = new TextField();

    private DatePicker datePicker = new DatePicker();

    private ComboBox<String> statusBox = new ComboBox<>();

    private TableView<Appointment> appointmentTable =
            new TableView<>();

    public AppointmentView() {

        setPadding(new Insets(25));
        setStyle("-fx-background-color: #F4F7FA;");

        // Heading
        Label heading = new Label("Appointment Management");
        heading.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17324D;"
        );

        // Appointment form
        VBox form = new VBox(10);

        Label formTitle = new Label("Appointment Information");
        formTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        patientIdField.setPromptText("Patient ID");
        doctorIdField.setPromptText("Doctor ID");
        datePicker.setPromptText("Appointment Date");

        statusBox.getItems().addAll(
                "Scheduled", "Confirmed", "Cancelled"
        );
        statusBox.setPromptText("Select Status");

        HBox row1 = new HBox(10, patientIdField, doctorIdField);
        HBox row2 = new HBox(10, datePicker, statusBox);

        Button addButton = new Button("Add Appointment");
        Button updateButton = new Button("Update Appointment");
        Button deleteButton = new Button("Delete Appointment");
        Button clearButton = new Button("Clear");

        HBox buttons = new HBox(10,
                addButton, updateButton, deleteButton, clearButton);

        form.getChildren().addAll(
                formTitle, row1, row2, buttons
        );

        // Appointment table
        TableColumn<Appointment, Integer> idColumn =
                new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("appointmentId"));

        TableColumn<Appointment, Integer> patientColumn =
                new TableColumn<>("Patient ID");
        patientColumn.setCellValueFactory(
                new PropertyValueFactory<>("patientId"));

        TableColumn<Appointment, Integer> doctorColumn =
                new TableColumn<>("Doctor ID");
        doctorColumn.setCellValueFactory(
                new PropertyValueFactory<>("doctorId"));

        TableColumn<Appointment, LocalDate> dateColumn =
                new TableColumn<>("Appointment Date");
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("appointmentDate"));

        TableColumn<Appointment, String> statusColumn =
                new TableColumn<>("Status");
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        appointmentTable.getColumns().addAll(
                idColumn, patientColumn, doctorColumn,
                dateColumn, statusColumn
        );

        appointmentTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // Button actions
        addButton.setOnAction(e -> addAppointment());
        updateButton.setOnAction(e -> updateAppointment());
        deleteButton.setOnAction(e -> deleteAppointment());
        clearButton.setOnAction(e -> clearFields());

        // Select a row to edit or delete
        appointmentTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldAppointment, selectedAppointment) -> {

                    if (selectedAppointment != null) {
                        patientIdField.setText(
                                String.valueOf(selectedAppointment.getPatientId()));

                        doctorIdField.setText(
                                String.valueOf(selectedAppointment.getDoctorId()));

                        datePicker.setValue(
                                selectedAppointment.getAppointmentDate());

                        statusBox.setValue(
                                selectedAppointment.getStatus());
                    }
                });

        VBox topSection = new VBox(20, heading, form);

        setTop(topSection);
        setCenter(appointmentTable);

        refreshAppointments();
    }

    private void addAppointment() {
        try {
            if (patientIdField.getText().trim().isEmpty() ||
                    doctorIdField.getText().trim().isEmpty() ||
                    datePicker.getValue() == null ||
                    statusBox.getValue() == null) {

                showMessage("Please fill in all appointment information.");
                return;
            }
            Appointment appointment = new Appointment(
                    0,
                    Integer.parseInt(patientIdField.getText()),
                    Integer.parseInt(doctorIdField.getText()),
                    datePicker.getValue(),
                    statusBox.getValue()
            );

            if (appointmentController.addAppointment(appointment)) {
                showMessage("Appointment added successfully!");
                clearFields();
                refreshAppointments();
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter valid Patient and Doctor IDs.");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        } catch (NullPointerException e) {
            showMessage("Please select an appointment date and status.");
        }
    }

    private void updateAppointment() {
        Appointment selected =
                appointmentTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select an appointment first.");
            return;
        }

        try {
            Appointment appointment = new Appointment(
                    selected.getAppointmentId(),
                    Integer.parseInt(patientIdField.getText()),
                    Integer.parseInt(doctorIdField.getText()),
                    datePicker.getValue(),
                    statusBox.getValue()
            );

            if (appointmentController.updateAppointment(appointment)) {
                showMessage("Appointment updated successfully!");
                clearFields();
                refreshAppointments();
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter valid Patient and Doctor IDs.");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        } catch (NullPointerException e) {
            showMessage("Please select an appointment date and status.");
        }
    }

    private void deleteAppointment() {
        Appointment selected =
                appointmentTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select an appointment first.");
            return;
        }

        try {
            if (appointmentController.deleteAppointment(
                    selected.getAppointmentId())) {

                showMessage("Appointment deleted successfully!");
                clearFields();
                refreshAppointments();
            }

        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void refreshAppointments() {
        try {
            ObservableList<Appointment> appointments =
                    FXCollections.observableArrayList(
                            appointmentController.getAllAppointments()
                    );

            appointmentTable.setItems(appointments);

        } catch (SQLException e) {
            showMessage("Could not load appointments: " + e.getMessage());
        }
    }

    private void clearFields() {
        patientIdField.clear();
        doctorIdField.clear();
        datePicker.setValue(null);
        statusBox.setValue(null);
        appointmentTable.getSelectionModel().clearSelection();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hospital Management System");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

