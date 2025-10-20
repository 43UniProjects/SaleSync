package org.oop_project.DatabaseHandler.operations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.oop_project.DatabaseHandler.models.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.EMPLOYEE_COLLECTION_NAME;

public class EmployeeOperations implements Operations<Employee> {


    private final MongoCollection<Employee> employeeCollection = dbClient.getCollection(EMPLOYEE_COLLECTION_NAME, Employee.class);

    @Override
    public void add(Employee newEmployee) {
        employeeCollection.insertOne(newEmployee);
        System.out.printf("\nEmployee saved: %s\n", newEmployee.getUsername());
    }

    @Override
    public boolean find(String username) {
        return employeeCollection.find(eq("username", username)).first() != null;
    }

    @Override
    public Employee get(String username) {
        return employeeCollection.find(eq("username", username)).first();
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        employeeCollection.find().into(employees);
        return employees.isEmpty() ? null : employees;
    }

    @Override
    public String getLastId() {
        Employee e = employeeCollection.find().sort(Sorts.descending("id")).first();
        return e != null ? e.getId() : null;
    }

    @Override
    public void update(String id, HashMap<String, Object> updatedRecords) {
        Bson filter = Filters.eq("id", id);

        Bson updateOperation = new Document("$set", new Document(updatedRecords));
        employeeCollection.updateOne(filter, updateOperation);
    }

    @Override
    public void delete(String username) {
        employeeCollection.deleteOne(eq("username", username));
    }

}
