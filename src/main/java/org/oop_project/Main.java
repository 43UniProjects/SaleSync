package org.oop_project;

import org.oop_project.database_handler.models.Employee;
import org.oop_project.database_handler.operations.EmployeeOperations;
import org.oop_project.database_handler.operations.Operations;
import org.oop_project.view.SaleSyncApp;

public class Main {

    final static Operations<Employee> employeeManager = new EmployeeOperations();

    public static void main(String[] args) {
        SaleSyncApp.run(args); 
        employeeManager.closeConnection();
    }
}