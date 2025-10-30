package org.oop_project;

import org.oop_project.DatabaseHandler.operations.EmployeeOperations;
import org.oop_project.view.gui;

public class Main {

    // initialize DB operations
    static EmployeeOperations employeeManager = new EmployeeOperations();

    public static void main(String[] args) {

        gui.main(args);

        // Close database connection
        employeeManager.closeConnection();
    }


}
