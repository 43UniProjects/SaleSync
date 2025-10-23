package org.oop_project;

import java.util.Scanner;

import org.oop_project.CLI.AdminPortal;
import org.oop_project.CLI.InventoryManager;
import org.oop_project.DatabaseHandler.Enums.Role;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Operations.EmployeeOperations;
import org.oop_project.View.gui;

public class Main {

    public static final String INITIAL_ITEM_FAMILIES_DATA_PATH = "data.json";

    // initialize DB operations
    static EmployeeOperations employeeManager = new EmployeeOperations();


    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        gui.main(args);

        System.out.println("\n===== SaleSync =====\n");
        System.out.println("Please Login\n");


        System.out.print("\tUsername: ");
        String username = sc.nextLine();
        System.out.print("\tPassword: ");
        String password = sc.nextLine();

        Employee employee = employeeManager.get(username);

        if(employeeManager.find(username)  && password.equals(employee.getPassword())) {
            Role role = Role.valueOf(employee.getRole().toString()); // get the role enum from the employee object
            

            switch (role) {
                case ADMIN:
                    AdminPortal.showMenu(employee);

                    break;

                case CASHIER:
                    System.out.println("cashier");
                    break;
                case PRODUCT_MANAGER:
                    InventoryManager.showMenu(employee);

                    break;
            }
        } else {
            System.out.println("\nLogin failed! Invalid username or password.");
        }

        // Close database connection
        employeeManager.closeConnection();
    }


}
