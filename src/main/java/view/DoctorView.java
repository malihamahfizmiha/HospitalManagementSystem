
package view;

import controller.DoctorController;
import model.Doctor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class DoctorView extends BorderPane {

    private DoctorController doctorController = new DoctorController();

    private TextField nameField = new TextField();
    private TextField phoneField = new TextField();
    private TextField specializationField = new TextField();
    private TextField feeField = new TextField();

    private TableView<Doctor> doctorTable = new TableView<>();

    public DoctorView() {

        setPadding(new Insets(25));
        setStyle("-fx-background-color: #F4F7FA;");

        // Heading
        Label heading = new Label("Doctor Management");
        heading.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17324D;"
        );

        // Doctor form
        VBox form = new VBox(10);

        Label formTitle = new Label("Doctor Information");
        formTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        nameField.setPromptText("Doctor Name");
        phoneField.setPromptText("Phone Number");
        specializationField.setPromptText("Specialization");
        feeField.setPromptText("Consultation Fee");

        HBox row1 = new HBox(10, nameField, phoneField);
        HBox row2 = new HBox(10, specializationField, feeField);

        Button addButton = new Button("Add Doctor");
        Button updateButton = new Button("Update Doctor");
        Button deleteButton = new Button("Delete Doctor");
        Button clearButton = new Button("Clear");

        HBox buttons = new HBox(10,
                addButton, updateButton, deleteButton, clearButton);

        form.getChildren().addAll(
                formTitle, row1, row2, buttons
        );

        // Doctor table
        TableColumn<Doctor, Integer> idColumn =
                new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("doctorId"));

        TableColumn<Doctor, String> nameColumn =
                new TableColumn<>("Name");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<Doctor, String> phoneColumn =
                new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone"));

        TableColumn<Doctor, String> specializationColumn =
                new TableColumn<>("Specialization");
        specializationColumn.setCellValueFactory(
                new PropertyValueFactory<>("specialization"));

        TableColumn<Doctor, Double> feeColumn =
                new TableColumn<>("Consultation Fee");
        feeColumn.setCellValueFactory(
                new PropertyValueFactory<>("consultationFee"));

        doctorTable.getColumns().addAll(
                idColumn, nameColumn, phoneColumn,
                specializationColumn, feeColumn
        );

        doctorTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // Button actions
        addButton.setOnAction(e -> addDoctor());
        updateButton.setOnAction(e -> updateDoctor());
        deleteButton.setOnAction(e -> deleteDoctor());
        clearButton.setOnAction(e -> clearFields());

        // Select a row to edit or delete
        doctorTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldDoctor, selectedDoctor) -> {
                    if (selectedDoctor != null) {
                        nameField.setText(selectedDoctor.getName());
                        phoneField.setText(selectedDoctor.getPhone());
                        specializationField.setText(selectedDoctor.getSpecialization());
                        feeField.setText(String.valueOf(selectedDoctor.getConsultationFee()));
                    }
                });

        VBox topSection = new VBox(20, heading, form);

        setTop(topSection);
        setCenter(doctorTable);

        refreshDoctors();
    }

    private void addDoctor() {
        try {
            if (nameField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty() ||
                    specializationField.getText().trim().isEmpty() ||
                    feeField.getText().trim().isEmpty()) {

                showMessage("Please fill in all doctor information.");
                return;
            }
            if (Double.parseDouble(feeField.getText()) <= 0) {
                showMessage("Consultation fee must be greater than zero.");
                return;
            }
            Doctor doctor = new Doctor(
                    0,
                    nameField.getText(),
                    phoneField.getText(),
                    specializationField.getText(),
                    Double.parseDouble(feeField.getText())
            );

            if (doctorController.addDoctor(doctor)) {
                showMessage("Doctor added successfully!");
                clearFields();
                refreshDoctors();
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid consultation fee.");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void updateDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a doctor first.");
            return;
        }

        try {
            Doctor doctor = new Doctor(
                    selected.getDoctorId(),
                    nameField.getText(),
                    phoneField.getText(),
                    specializationField.getText(),
                    Double.parseDouble(feeField.getText())
            );

            if (doctorController.updateDoctor(doctor)) {
                showMessage("Doctor updated successfully!");
                clearFields();
                refreshDoctors();
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid consultation fee.");
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void deleteDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage("Please select a doctor first.");
            return;
        }

        try {
            if (doctorController.deleteDoctor(selected.getDoctorId())) {
                showMessage("Doctor deleted successfully!");
                clearFields();
                refreshDoctors();
            }
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage());
        }
    }

    private void refreshDoctors() {
        try {
            ObservableList<Doctor> doctors =
                    FXCollections.observableArrayList(
                            doctorController.getAllDoctors()
                    );

            doctorTable.setItems(doctors);

        } catch (SQLException e) {
            showMessage("Could not load doctors: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        specializationField.clear();
        feeField.clear();
        doctorTable.getSelectionModel().clearSelection();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hospital Management System");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

