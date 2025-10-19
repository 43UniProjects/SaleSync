package org.oop_project.DatabaseHandler.Operations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.oop_project.DatabaseHandler.Enums.Role;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Models.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.EMPLOYEE_COLLECTION_NAME;
import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.PRODUCT_COLLECTION_NAME;

public class EmployeeOperations extends Operations {


    final MongoCollection<Employee> employeeCollection = dbClient.getCollection(EMPLOYEE_COLLECTION_NAME, Employee.class);

    public void add(String userID, String firstName, String lastName, Date dob, String phoneNumber, String email, String username, Role role, String pwd) {
        Employee newEmployee = new Employee(userID, firstName, lastName, dob, phoneNumber, email, username, role);
        newEmployee.setPassword(pwd);
        employeeCollection.insertOne(newEmployee);
        System.out.printf("\nEmployee saved: %s\n", newEmployee.getUsername());
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
        Employee e = employeeCollection.find().sort(Sorts.descending("id")).first();
        assert e != null;
        return e.getId();
    }

    public void delete(String username) {
        employeeCollection.deleteOne(eq("username", username));
    }

}
