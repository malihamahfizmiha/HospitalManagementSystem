
package view;

import controller.PatientController;
import model.Patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class PatientView extends BorderPane {

    private PatientController patientController = new PatientController();

    private TextField nameField = new TextField();
    private TextField ageField = new TextField();
    private TextField genderField = new TextField();
    private TextField phoneField = new TextField();
    private TextField addressField = new TextField();

    private TableView<Patient> patientTable = new TableView<>();

    public PatientView() {

        setPadding(new Insets(25));
        setStyle("-fx-background-color: #F4F7FA;");

        // Heading
        Label heading = new Label("Patient Management");
        heading.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17324D;"
        );

        // Patient form
        VBox form = new VBox(10);

        Label formTitle = new Label("Patient Information");
        formTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        nameField.setPromptText("Patient Name");
        ageField.setPromptText("Age");
        genderField.setPromptText("Gender");
        phoneField.setPromptText("Phone Number");
        addressField.setPromptText("Address");

        HBox row1 = new HBox(10, nameField, ageField, genderField);
        HBox row2 = new HBox(10, phoneField, addressField);

        Button addButton = new Button("Add Patient");
        Button updateButton = new Button("Update Patient");
        Button deleteButton = new Button("Delete Patient");
        Button clearButton = new Button("Clear");

        HBox buttons = new HBox(10,
                addButton, updateButton, deleteButton, clearButton);

        form.getChildren().addAll(
                formTitle, row1, row2, buttons
        );

        // Patient table
        TableColumn<Patient, Integer> idColumn =
                new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("patientId"));

        TableColumn<Patient, String> nameColumn =
                new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<Patient, Integer> ageColumn =
                new TableColumn<>("Age");
        ageColumn.setCellValueFactory(
                new PropertyValueFactory<>("age"));

        TableColumn<Patient, String> genderColumn =
                new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(
                new PropertyValueFactory<>("gender"));

        TableColumn<Patient, String> phoneColumn =
                new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone"));

        TableColumn<Patient, String> addressColumn =
                new TableColumn<>("Address");
        addressColumn.setCellValueFactory(
                new PropertyValueFactory<>("address"));

        patientTable.getColumns().addAll(
                idColumn, nameColumn, ageColumn,
                genderColumn, phoneColumn, addressColumn
        );

        patientTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // Button actions
        addButton.setOnAction(e -> addPatient());
        updateButton.setOnAction(e -> updatePatient());
        deleteButton.setOnAction(e -> deletePatient());
        clearButton.setOnAction(e -> clearFields());

        // Select a row to edit or delete
        patientTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldPatient, selectedPatient) -> {
                    if (selectedPatient != null) {
                        nameField.setText(selectedPatient.getName());
                        ageField.setText(String.valueOf(selectedPatient.getAge()));
                        genderField.setText(selectedPatient.getGender());
                        phoneField.setText(selectedPatient.getPhone());
                        addressField.setText(selectedPatient.getAddress());
                    }
                });

        VBox topSection = new VBox(20, heading, form);

        setTop(topSection);
        setCenter(patientTable);

        refreshPatients();
    }

    private void addPatient() {
        try {
            if (nameField.getText().trim().isEmpty() ||
                    ageField.getText().trim().isEmpty() ||
                    genderField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty() ||
                    addressField.getText().trim().isEmpty()) {

                showMessage("Please fill in all patient information.");
                return;
            }
            if (Integer.parseInt(ageField.getText()) <= 0) {
                showMessage("Age must be greater than zero.");
                return;
            }
            Patient patient = new Patient(
                    0,
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    genderField.getText(),
                    phoneField.getText(),
                    addressField.getText()
            );

            if (patientController.addPatient(patient)) {
                showMessage("Patient added successfully!");
                clearFields();
                refreshPatients();
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid age.");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void updatePatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a patient first.");
            return;
        }

        try {
            Patient patient = new Patient(
                    selected.getPatientId(),
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    genderField.getText(),
                    phoneField.getText(),
                    addressField.getText()
            );

            if (patientController.updatePatient(patient)) {
                showMessage("Patient updated successfully!");
                clearFields();
                refreshPatients();
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid age.");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void deletePatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a patient first.");
            return;
        }

        try {
            if (patientController.deletePatient(selected.getPatientId())) {
                showMessage("Patient deleted successfully!");
                clearFields();
                refreshPatients();
            }
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void refreshPatients() {
        try {
            ObservableList<Patient> patients =
                    FXCollections.observableArrayList(
                            patientController.getAllPatients()
                    );

            patientTable.setItems(patients);

        } catch (SQLException e) {
            showMessage("Could not load patients: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.clear();
        ageField.clear();
        genderField.clear();
        phoneField.clear();
        addressField.clear();
        patientTable.getSelectionModel().clearSelection();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hospital Management System");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

