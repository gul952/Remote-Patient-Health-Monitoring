
# Remote Patient Monitoring System

A Java‑based Hospital Management System developed as a semester project for CS‑212 Object‑Oriented Programming under instructor Nazia Perwaiz. It supports patient vital sign tracking, appointment scheduling, doctor‑patient feedback, and has been extended with advanced modules for emergency alerts, chat/video consultation, and notifications/reminders.

---

## Table of Contents

1. [Project Overview](#project-overview)  
2. [Features](#features)  
   - [Original Features](#original-features)  
   - [New Features](#new-features)  
3. [Modules & Components](#modules--components)  
4. [Installation & Running the Project](#installation--running-the-project)  
   - [Prerequisites](#prerequisites)  
   - [Compile & Run](#compile--run)  
5. [Usage Guide](#usage-guide)  
6. [Semester Project Report](#semester-project-report)  
   1. [Background](#background)  
   2. [Introduction](#introduction)  
   3. [Literature Review](#literature-review)  
   4. [Methodology / System Overview](#methodology--system-overview)  
   5. [Conclusion](#conclusion)   
7. [Group Members](#group-members)  


---

## Project Overview

This system automates key hospital workflows:

- **User Roles**: Patients, Doctors, Administrators  
- **Core Functions**:  
  - Patient registration & authentication  
  - Vitals monitoring (CSV import & manual entry)  
  - Appointment request & management  
  - Doctor feedback & prescription writing  
  - Medical history tracking  

The architecture is fully object‑oriented, modular, and extensible for new features.

---

## Features

### Original Features

- **Patient Management**  
  - Unique Patient IDs, vitals entry (heart rate, BP, SpO₂, temperature)  
  - CSV import of timestamped readings with validation  
  - Manual vitals entry with threshold checks  
  - Medical history & feedback viewing

- **Doctor Management**  
  - View assigned patients, vitals trends (Chart.js graphs)  
  - Write feedback & prescriptions, trigger medication reminders  
  - Approve/reject appointment requests

- **Administrator Functions**  
  - Add/update/remove Users (Patients & Doctors)  
  - View system logs & all registered IDs

### New Features

- **Emergency Alert System**  
  - Automatic critical‑vitals detection & in‑app/email/SMS alerts  
  - Panic button for manual emergency notifications

- **Chat & Video Consultation**  
  - Real‑time chat via WebSocket (`ChatServer`/`ChatClient`)  
  - Video call link generation (Google Meet or Zoom API)

- **Notifications & Reminders**  
  - Appointment & medication reminders (Quartz scheduler)  
  - Unified `Notifiable` interface with `EmailNotification` & `SMSNotification`

- **Reporting & Billing**  
  - PDF/CSV export of health reports (iText)  
  - Invoice generation & payment tracking

---

## Modules & Components

1. **User Management & Authentication**  
   - `User`, `AuthService`, role‑based dashboards  

2. **Health Data Upload & Monitoring**  
   - `CSVReader`, `VitalSign`, `VitalsDatabase`, `EmergencyAlert`  

3. **Prescription & Feedback Workflow**  
   - `Prescription`, `MedicalHistory`, `ReminderService`  

4. **Emergency Alert System**  
   - `EmergencyAlert`, `NotificationService`, `PanicButton`  

5. **Chat & Video Consultation**  
   - `ChatServer`, `ChatClient`, `VideoCall`  

6. **Appointment Scheduling**  
   - `AppointmentManager`, calendar views  

7. **Notifications & Reminders**  
   - `ReminderService`, `Notifiable` interface  

8. **Data Visualization & Reporting**  
   - Chart.js graphs, PDF export via iText  

9. **Billing & Invoicing**  
   - `Bills`, invoice PDF generation, payment status  

---

## Installation & Running the Project

### Prerequisites

- JDK 8 or later (`java -version`)  
- (Optional) **Jakarta Mail** jars (`mail.jar`, `activation.jar`) in `lib/` for email features  

### Compile & Run

1. **Clone the repo**  
  
   git clone https://github.com/gul952/RemotePatientMonitoring.git
   cd RemotePatientMonitoring


2. **Create output directory**

   ```bash
   mkdir -p bin
   ```

3. **Compile sources**

   * **Without Email**

     ```bash
     javac -d bin -sourcepath src $(find src -name "*.java")
     ```
   * **With Email**

     ```bash
     javac -cp "lib/mail.jar:lib/activation.jar" \
       -d bin -sourcepath src \
       $(find src -name "*.java")
     ```

4. **Run application**

   * **Without Email**

     ```bash
     java -cp bin healthcare.main
     ```
   * **With Email**

     ```bash
     java -cp "bin:lib/mail.jar:lib/activation.jar" healthcare.main
     ```

---

## Usage Guide

Upon startup, select your role:

* **Patient**

  * Login with Patient ID
  * Upload vitals (CSV/manual), view history & charts
  * Request appointments, view feedback, see notifications & chat history

* **Doctor**

  * Login with Doctor ID
  * Review patient vitals & history, write feedback/prescriptions
  * Approve/reject appointments, receive emergency alerts & chat

* **Administrator**

  * Manage user accounts, view system logs
  * Generate system‑wide reports & invoices

---

## Semester Project Report

### Background

Hospital Management Systems (HMS) streamline patient records, appointments, billing, and clinical workflows. This project replaces paper‑based errors with a modular digital platform for small‑to‑mid‑sized clinics.

### Introduction

Objectives:

1. Secure role‑based access & user management
2. Remote vitals monitoring (CSV, manual, threshold checks)
3. Clinical workflows (feedback, prescriptions)
4. Real‑time communication (chat, video)
5. Appointments, notifications, billing
6. Analytical reporting & visualization

### Literature Review

* **MediTech Expanse** — Powerful but costly
* **GNU Health** — Open‑source public health focus
* **OpenMRS** — Modular but lacks built‑in alert/CSV import
  Our system balances real‑time monitoring, emergency alerts, and communication in an easy‑to‑extend architecture.

### Methodology / System Overview

Nine functional domains (User Management, Vitals, Prescriptions, Alerts, Chat, Appointments, Notifications, Reporting, Billing) each implemented by collaborating modules and services.

### Conclusion

A cohesive, object‑oriented HMS that enhances healthcare delivery with real‑time monitoring, alerts, communication, and reporting—designed for education and small‑scale deployment.


### CSV Samples

Sample vital‑sign CSVs in `/csv_samples/` demonstrate import schema and validation.

---

## Group Members

* Uzair
* Ali Ahmad
* Gulwarina Muska Saleem

