package org.oop_project.CLI;

import org.oop_project.DatabaseHandler.Enums.Role;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Operations.EmployeeOperations;
import org.oop_project.Main;

import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class AdminPortal {
    static Scanner sc = new Scanner(System.in);
    static EmployeeOperations employeeManager = new EmployeeOperations();

    public static void showMenu(Employee employee) {
        int option;

        do {
            System.out.println("\nWelcome to the Admin Portal, " + employee.getUsername() + "!");
            System.out.println("Here you can manage employee related tasks.\n");
            System.out.println("\n\t1. Add Employee");
            System.out.println("\t2. Update Employee Details");
            System.out.println("\t3. View Employee List"); 
            System.out.println("\t4. Remove Employee");
            System.out.println("\t5. Logout");
            System.out.print("\nCHOOSE AN OPTION: ");
            option = sc.nextInt();
            

            switch(option) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    // Update Employee Details
                    break;
                case 3:
                    // View Employee List
                    break;
                case 4:
                    // Remove Employee
                    break;
                case 5:
                    System.out.println("\nLogged out!");
                    Main.main(new String[]{""}); // Restart the application for login
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }

        }while(option < 5);

    }

    public static void addEmployee(){
        int newID = Integer.parseInt(employeeManager.getLastId()) + 1;
        sc.nextLine(); // Consume newline
        System.out.println("\n\nEnter employee details:");  
        System.out.print("\n\tFirst Name: ");
        String firstName = sc.nextLine();
        System.out.print("\tLast Name: ");
        String lastName = sc.nextLine();

        System.out.print("\tDate of Birth (YYYY-MM-DD): ");
        String dob = sc.nextLine();

        // Parse the string into a LocalDate object
        Date dob_ = java.sql.Date.valueOf(dob);

        System.out.print("\tPhone Number: ");
        String phoneNumber = sc.nextLine();
        System.out.print("\tEmail: ");
        String email = sc.nextLine();
        System.out.print("\tUsername: ");
        String username = sc.nextLine();
        System.out.print("\tPassword: ");
        String password = sc.nextLine();
        System.out.print("\tRole (ADMIN, CASHIER, PRODUCT_MANAGER): ");
        String roleInput = sc.nextLine();   
        Role role = Role.valueOf(roleInput.toUpperCase());
        String userID = String.valueOf(newID);
        
        employeeManager.add(userID, firstName, lastName, dob_, phoneNumber, email, username, role, password);

    }
}
