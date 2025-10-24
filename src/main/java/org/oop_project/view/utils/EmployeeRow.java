package org.oop_project.view.utils;

import java.time.LocalDate;

public class EmployeeRow {
        private final String employeeNumber;
        private String firstName;
        private String lastName;
        private LocalDate dob;
        private String phone;
        private String email;
        private String username;
        private String role;
        private LocalDate startDate;

        public EmployeeRow(String employeeNumber, String firstName, String lastName, LocalDate dob, String phone, String email, String username, String role, LocalDate startDate) {
            this.employeeNumber = employeeNumber;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.phone = phone;
            this.email = email;
            this.username = username;
            this.role = role;
            this.startDate = startDate;
        }
        public String getEmployeeNumber() { return employeeNumber; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getDob() { return dob != null ? dob.toString() : ""; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getStartDate() { return startDate != null ? startDate.toString() : ""; }

        public void setFirstName(String v) { this.firstName = v; }
        public void setLastName(String v) { this.lastName = v; }
        public void setDob(LocalDate v) { this.dob = v; }
        public void setPhone(String v) { this.phone = v; }
        public void setEmail(String v) { this.email = v; }
        public void setUsername(String v) { this.username = v; }
        public void setRole(String v) { this.role = v; }
        public void setStartDate(LocalDate v) { this.startDate = v; }
    }