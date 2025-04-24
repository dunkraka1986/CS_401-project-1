# College Enrollment System

The College Enrollment System is a Java-based desktop application that facilitates student enrollment and course management across multiple universities. It allows administrators to create and manage course schedules, while students can enroll, drop, and manage their course selections. The system also supports prerequisite enforcement, waitlists, and reporting.

---

## 📌 Features

### 🧑‍🎓 For Students
- Register and manage a student profile
- Browse available courses and enroll/drop within deadlines
- Join course waitlists if classes are full
- View personalized course schedules
- Make tuition payments

### 🧑‍💼 For Administrators
- All student capabilities, plus:
- Create and delete courses with prerequisites
- Enroll/drop courses on behalf of students
- View any student’s schedule and holds
- Auto-generate reports on enrollments, waitlists, and seat availability

---

## 🧱 System Architecture

- **Language:** Java
- **Type:** Desktop GUI (Java Swing/JavaFX)
- **Architecture:** Client-Server model
- **Communication:** TCP/IP sockets
- **Storage:** In-memory (no database)
- **Design:** Modular OOP with layered components

---

## 🗂️ Folder Structure
<pre>
CollegeEnrollmentSystem/
├── src
├── College Enrollment System - Software Design Document/     
├── College Enrollment System - Software Requirements Specification/     
├── Gantt Chart/         
├── Meeting Minutues/    
├── Phase 1 Slides/          
└── README.md
</pre>

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 8 or later
- Git
- IDE (e.g., IntelliJ IDEA, Eclipse) or terminal
- Network access for local client-server communication

### 📦 Installation

1. Clone the repository:

<pre>
git clone https://github.com/dunkraka1986/CS_401-project-1.git
cd CS_401-project-1
</pre>

2. Compile the project:

<pre>
javac */*.java
</pre>

3. Run the server:

<pre>
java server.ServerMain
</pre>

4. In a separate terminal, run the client:

<pre>
java client.ClientMain
</pre>

---

### 🧪 Testing

The system includes basic tests and test cases for:
- Enrollment and waitlist logic
- Prerequisite enforcement
- Payment processing
- Auto report generation

You can run tests manually or use a test runner in your IDE.

---

### 📊 UML Diagrams & Documentation
Inside the /diagrams/ and /docs/ folders, you'll find:

- Class diagrams

- Sequence diagrams (per use case)

- Use case diagrams

- Software Requirements Specification (SRS)

- Software Design Document (SDD)

---

### 👨‍💻 Team Members
- Aakkash Muthukumar

- Christian Ramos

- Svein Quintos

- Felipe Gutierrez

---

### 🤝 Contributing
Contributions are welcome! Follow these steps:

<pre>
git checkout -b feature/your-feature-name
git commit -m "Add your feature"
git push origin feature/your-feature-name
</pre>
Then open a pull request on GitHub 🚀

---
