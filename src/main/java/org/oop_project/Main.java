package org.oop_project;

import java.util.Scanner;

import org.oop_project.CLI.AdminPortal;
import org.oop_project.CLI.InventoryManager;
import org.oop_project.DatabaseHandler.enums.Role;
import org.oop_project.DatabaseHandler.models.Employee;
import org.oop_project.DatabaseHandler.operations.EmployeeOperations;
import org.oop_project.View.gui;

public class Main {

    public static final String INITIAL_ITEM_FAMILIES_DATA_PATH = "data.json";

    // initialize DB operations
    static EmployeeOperations employeeManager = new EmployeeOperations();


    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        gui.main(args);

        // Close database connection
        employeeManager.closeConnection();
    }


}
