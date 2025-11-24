package org.oop_project.database_handler.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import static org.oop_project.database_handler.DatabaseConnectionManager.EMPLOYEE_COLLECTION_NAME;
import org.oop_project.database_handler.models.Employee;
import static org.oop_project.utils.EmployeeMapper.mapEmployee;
import static org.oop_project.utils.EmployeeMapper.mapEmployees;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Sorts;

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
        return mapEmployee(employeeCollection.find(eq("username", username)).first());
    }

    /**
     * Get an employee by their numeric/user id field (`id`).
     */
    public Employee getById(String id) {
        return mapEmployee(employeeCollection.find(eq("id", id)).first());
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        employeeCollection.find().into(employees);
        return employees.isEmpty() ? null : mapEmployees(employees);
    }

    @Override
    public String getLastId() {
        Employee e = employeeCollection.find().sort(Sorts.descending("id")).first();
        return e != null ? e.getId() : "0";
    }

    @Override
    public void update(String id, HashMap<String, Object> updatedRecords) {
        Bson filter = Filters.eq("id", id);

        Bson updateOperation = new Document("$set", new Document(updatedRecords));
        employeeCollection.updateOne(filter, updateOperation);
    }

    @Override
    public boolean delete(String username) {
        return employeeCollection.deleteOne(eq("username", username)).getDeletedCount() > 0;
    }

}
