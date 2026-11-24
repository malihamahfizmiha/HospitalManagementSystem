
# Hospital and Healthcare Management System

## About the Project
A JavaFX desktop application developed to manage hospital operations using a MySQL database.

## Features
- Patient Management
- Doctor Management
- Appointment Management
- Billing Management
- Dashboard with statistics and monthly appointment chart

## Technologies
- Java 20
- JavaFX
- MySQL
- JDBC
- Maven

## Requirements
- JDK 20
- MySQL Server

## Database Setup
1. Install and start MySQL Server.
2. Run the SQL commands in `hospital_db.sql` to create the database and tables.
3. Configure your own MySQL credentials using these environment variables:
   - `DB_USER`
   - `DB_PASSWORD`

## How to Run

### Windows (Command Prompt)
Set your database credentials:
```cmd
set DB_USER=root
set DB_PASSWORD=your_mysql_password
```

Run the application:
```cmd
mvnw.cmd javafx:run
```

### macOS / Linux (Terminal)
Set your database credentials:
```bash
export DB_USER=root
export DB_PASSWORD='your_mysql_password'
```

Run the application:
```bash
./mvnw javafx:run
```

The Maven Wrapper automatically downloads the required Maven version. You do not need to install Maven separately.

## Note
Use your own local MySQL username and password.
