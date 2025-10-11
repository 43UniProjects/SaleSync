package org.oop_project.DatabaseHandler.Operations;

import com.mongodb.client.model.Sorts;
import org.oop_project.DatabaseHandler.Enums.Role;
import org.oop_project.DatabaseHandler.Models.Employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class EmployeeOperations extends Operations {

    public void add(String userID, String firstName, String lastName, Date dob, String phoneNumber, String email, String username, Role role, String pwd) {
        Employee newEmployee = new Employee(firstName, lastName, dob, phoneNumber, email, username, role);
        newEmployee.setId(userID);
        newEmployee.setPassword(pwd);
        employeeCollection.insertOne(newEmployee);
        System.out.println("Employee saved: " + newEmployee.getUsername());
    }

    public boolean find(String username) {
        return employeeCollection.find(eq("username", username)).first() != null;
    }

    public Employee get(String username) {
        return employeeCollection.find(eq("username", username)).first();
    }

    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        employeeCollection.find().into(employees);
        return employees.isEmpty() ? null : employees;

    }

    public String getLastId() {
        Employee u = employeeCollection.find().sort(Sorts.descending("id")).first();
        return u != null ? u.getId() : "0";
    }

    public void delete(String username) {
        employeeCollection.deleteOne(eq("username", username));
    }

}
