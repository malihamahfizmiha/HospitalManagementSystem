

package view;

import controller.PatientController;
import controller.DoctorController;
import controller.AppointmentController;
import controller.BillingController;
import java.sql.SQLException;
import javafx.scene.chart.XYChart;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.stage.Stage;
import java.util.Map;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) {

        // Main layout
        BorderPane root = new BorderPane();

        // Sidebar
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(25));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #17324D;");

        Label systemName = new Label("HOSPITAL\nMANAGEMENT");
        systemName.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;"
        );

        Button dashboardBtn = createButton("Dashboard");
        Button patientsBtn = createButton("Patients");
        Button doctorsBtn = createButton("Doctors");
        Button appointmentsBtn = createButton("Appointments");
        Button billingBtn = createButton("Billing");

        sidebar.getChildren().addAll(
                systemName,
                dashboardBtn,
                patientsBtn,
                doctorsBtn,
                appointmentsBtn,
                billingBtn
        );

        // Main content area
        // Main content area
        VBox content = new VBox(25);
        content.setPadding(new Insets(35));
        content.setAlignment(Pos.TOP_LEFT);
        content.setStyle("-fx-background-color: #F4F7FA;");

        Label heading = new Label("Welcome to Hospital Management System");
        heading.setStyle(
                "-fx-font-size: 27px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17324D;"
        );

        Label description = new Label(
                "Manage patients, doctors, appointments and billing in one place."
        );
        description.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #64748B;"
        );
        // Dashboard statistic cards
        HBox statsCards = new HBox(15);

        VBox patientCard = createStatCard("Total Patients", "0");
        VBox doctorCard = createStatCard("Total Doctors", "0");
        VBox appointmentCard = createStatCard("Appointments", "0");
        VBox billCard = createStatCard("Total Bills", "0");
        Label patientCountLabel =
                (Label) patientCard.getChildren().get(1);
        Label doctorCountLabel =
                (Label) doctorCard.getChildren().get(1);
        Label appointmentCountLabel =
                (Label) appointmentCard.getChildren().get(1);
        Label billCountLabel =
                (Label) billCard.getChildren().get(1);

        statsCards.getChildren().addAll(
                patientCard,
                doctorCard,
                appointmentCard,
                billCard
        );

        content.getChildren().addAll(
                heading,
                description,
                statsCards
        );
        PatientController patientController = new PatientController();

        try {
            int totalPatients = patientController.getPatientCount();
            patientCountLabel.setText(String.valueOf(totalPatients));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DoctorController doctorController = new DoctorController();

        try {
            int totalDoctors = doctorController.getDoctorCount();
            doctorCountLabel.setText(String.valueOf(totalDoctors));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AppointmentController appointmentController =
                new AppointmentController();

        try {
            int totalAppointments =
                    appointmentController.getAppointmentCount();

            appointmentCountLabel.setText(
                    String.valueOf(totalAppointments));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        BillingController billingController = new BillingController();

        try {
            int totalBills = billingController.getBillCount();

            billCountLabel.setText(String.valueOf(totalBills));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Add appointments overview chart
        VBox appointmentsChart = createAppointmentsChart();
        content.getChildren().add(appointmentsChart);


        dashboardBtn.setOnAction(e -> {

            root.setCenter(content);
            content.getChildren().set(3, createAppointmentsChart());

            heading.setText("Dashboard");
            description.setText("Welcome to the hospital management system.");

            try {
                patientCountLabel.setText(
                        String.valueOf(patientController.getPatientCount()));

                doctorCountLabel.setText(
                        String.valueOf(doctorController.getDoctorCount()));

                appointmentCountLabel.setText(
                        String.valueOf(appointmentController.getAppointmentCount()));

                billCountLabel.setText(
                        String.valueOf(billingController.getBillCount()));

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        patientsBtn.setOnAction(e -> {
            root.setCenter(new PatientView());
        });

        doctorsBtn.setOnAction(e -> {
            root.setCenter(new DoctorView());
        });

        appointmentsBtn.setOnAction(e -> {
            root.setCenter(new AppointmentView());
        });

        billingBtn.setOnAction(e -> {
            root.setCenter(new BillingView());
        });

        // Add sidebar and content to the main layout
        root.setLeft(sidebar);
        root.setCenter(content);

        // Create window
        Scene scene = new Scene(root, 1000, 650);

        stage.setTitle("Hospital Management System");
        stage.setScene(scene);
        stage.show();
    }
    // Create dashboard statistic cards
    private VBox createStatCard(String title, String value) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setPrefWidth(190);
        card.setPrefHeight(110);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #64748B;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17324D;"
        );

        card.getChildren().addAll(titleLabel, valueLabel);

        return card;
    }

    // Create appointments overview chart
    private VBox createAppointmentsChart() {

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
        yAxis.setLabel("Appointments");

        LineChart<String, Number> chart =
                new LineChart<>(xAxis, yAxis);

        chart.setTitle("Monthly Appointments");
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefHeight(300);


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        try {
            AppointmentController controller = new AppointmentController();

            Map<String, Integer> monthlyCounts =
                    controller.getMonthlyAppointmentCounts();
            int maxAppointments = 0;

            for (int count : monthlyCounts.values()) {
                if (count > maxAppointments) {
                    maxAppointments = count;
                }
            }

            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(Math.max(5, maxAppointments));
            yAxis.setTickUnit(1);

            for (Map.Entry<String, Integer> entry : monthlyCounts.entrySet()) {

                series.getData().add(
                        new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        chart.getData().add(series);


        VBox chartBox = new VBox(10);
        chartBox.setPadding(new Insets(20));
        chartBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: #E2E8F0;"
        );

        chartBox.getChildren().add(chart);

        return chartBox;
    }
    // Create sidebar buttons
    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(170);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-padding: 10px;"
        );
        return button;
    }
}
