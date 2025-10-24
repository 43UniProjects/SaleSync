package org.oop_project.DatabaseHandler.models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.oop_project.DatabaseHandler.enums.Role;

import java.time.LocalDate;

/*
 * Represents a Employee entity in the MongoDB 'Employee' collection.
 * This class is a Plain Old Java Object (POJO) that maps to a BSON document.
 * It contains essential employee details such as a unique identifier and other properties.
 * This act as the base class for all employees with different roles with in the store
 *
 * */

public class Employee {

    @BsonId
    private BsonId _id;

    @BsonProperty("id")
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

    public Employee(String userId, String firstName, String lastName, LocalDate dob, String phoneNumber, String email, String username, Role role, LocalDate startDate) {
        this.id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.role = role;
        this.startDate = startDate;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }   

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}