package org.oop_project.DatabaseHandler.Models;

import java.time.LocalDate;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.oop_project.DatabaseHandler.Enums.Role;

/*
 * Represents a Employee entity in the MongoDB 'Employee' collection.
 * This class is a Plain Old Java Object (POJO) that maps to a BSON document.
 * It contains essential employee details such as a unique identifier and other properties.
 * This act as the base class for all employees with different roles with in the company
 *
 * */

public class Employee {

    @BsonId
    private ObjectId _id;
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String phoneNumber;
    private String email;
    private LocalDate startDate;
    private String username;
    private String password;
    private Role role;

    // REQUIRED for the pojo codec provider
    public Employee() {
        // left empty intentionally
    }

    public Employee(String firstName, String lastName, LocalDate dob, String phoneNumber, String email, String username, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.role = role;
        this.setStartDate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    private void setStartDate() {
        LocalDate currentDate = LocalDate.now();  // YYYY-MM-DD
        this.startDate = currentDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}