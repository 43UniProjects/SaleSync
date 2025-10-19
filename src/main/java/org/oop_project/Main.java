package org.oop_project;

import java.util.Scanner;

import org.oop_project.CLI.AdminPortal;
import org.oop_project.CLI.InventoryManager;
import org.oop_project.DatabaseHandler.Enums.Role;
import org.oop_project.DatabaseHandler.Models.Employee;
import  org.oop_project.DatabaseHandler.Operations.EmployeeOperations;

public class Main {

    // initialize DB operations
    static EmployeeOperations employeeManager = new EmployeeOperations();


    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {


        System.out.println("\n===== SaleSync =====\n");
        System.out.println("Please Login\n");


        System.out.print("\tUsername: ");
        String username = sc.nextLine();
        System.out.print("\tPassword: ");
        String password = sc.nextLine();

        Employee employee = employeeManager.get(username);

        if(employeeManager.find(username)  && password.equals(employee.getPassword())) {
            Role role = employee.getRole();

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
