package org.oop_project.view.helpers;

import java.time.LocalDate;

import org.oop_project.DatabaseHandler.enums.Role;
import org.oop_project.DatabaseHandler.models.Employee;

/*
 *  Represents a row in employee manager of admin panel   
 *  Work as a interface to set and get Employee class attrib with compatible dtype for the javafx TreeView Class 
 */
public class EmployeeRow  {

    private final Employee emp;

    public EmployeeRow(Employee emp) {
        this.emp = emp;
    }

    public String getEmployeeNumber() {
        return this.emp != null && this.emp.getId() != null ? this.emp.getId() : "";
    }

    public String getFirstName() {
        return this.emp != null && this.emp.getFirstName() != null ? this.emp.getFirstName() : "";
    }

    public String getLastName() {
        return this.emp != null && this.emp.getLastName() != null ? this.emp.getLastName() : "";
    }

    public String getDob() {
        return this.emp != null && this.emp.getDob() != null ? this.emp.getDob().toString() : "";
    }

    public String getPhone() {
        return this.emp != null && this.emp.getPhoneNumber() != null ? this.emp.getPhoneNumber() : "";
    }

    public String getEmail() {
        return this.emp != null && this.emp.getEmail() != null ? this.emp.getEmail() : "";
    }

    public String getUsername() {
        return this.emp != null && this.emp.getUsername() != null ? this.emp.getUsername() : "";
    }

    public String getRole() {
        return this.emp != null && this.emp.getRole() != null ? this.emp.getRole().getLabel() : "";
    }

    public String getStartDate() {
        return this.emp != null && this.emp.getStartDate() != null ? this.emp.getStartDate().toString() : "";
    }

    public void setFirstName(String v) {
        if (this.emp != null) {
            this.emp.setFirstName(v);
        }
    }

    public void setLastName(String v) {
        if (this.emp != null) {
            this.emp.setLastName(v);
        }
    }

    public void setDob(LocalDate v) {
        if (this.emp != null) {
            this.emp.setDob(v);
        }
    }

    public void setPhone(String v) {
        if (this.emp != null) {
            this.emp.setPhoneNumber(v);
        }
    }

    public void setEmail(String v) {
        if (this.emp != null) {
            this.emp.setEmail(v);
        }
    }

    public void setUsername(String v) {
        if (this.emp != null) {
            this.emp.setUsername(v);
        }
    }

    public void setRole(String v) {
        if (this.emp != null) {
            this.emp.setRole(Role.valueOf(v));
        }
    }

    public void setStartDate(LocalDate v) {
        if (this.emp != null) {
            this.emp.setStartDate(v);
        }
    }
}
