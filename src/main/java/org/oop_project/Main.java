package org.oop_project;

import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Models.Product;
import org.oop_project.DatabaseHandler.Operations.EmployeeOperations;
import  org.oop_project.DatabaseHandler.Operations.ProductOperations;
import org.oop_project.DatabaseHandler.Enums.UnitType;
import org.oop_project.DatabaseHandler.Enums.Role;

import java.util.List;
import java.util.Scanner;

public class Main {

    // initialize DB operations
    static EmployeeOperations employeeManager = new EmployeeOperations();
    static  ProductOperations productManager = new ProductOperations();

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
                case Role.ADMIN:
                    // TODO
                    /*
                    * Manage Employee ( Add, Update, Remove )
                    * Manage Supplier ( Add, Update, Remove )
                    * Manage Branch Details
                    * Handle Discounts
                    * */


                    break;

                case Role.CASHIER:
                    System.out.println("cahier");
                    break;
                case Role.PRODUCT_MANAGER:
                    int option;

                    do {
                        System.out.println("Welcome to the Product Portal, " + employee.getUsername() + "!");

                        System.out.println("Here you can manage product-related tasks.\n");
                        System.out.println("\n\t1. Add New Product");
                        System.out.println("\t2. Update Product Details");
                        System.out.println("\t3. View Product List");
                        System.out.println("\t4. Remove Product");
                        System.out.println("\t5. Logout");
                        System.out.print("\nCHOOSE AN OPTION: ");

                        option = sc.nextInt();

                        if (option == 1) {

                            System.out.println("\nEnter product details:");
                            sc.nextLine();
                            System.out.print("\n\tProduct Name: ");
                            String productName = sc.nextLine();

                            System.out.print("\n\tProduct description: ");
                            String description = sc.nextLine();

                            System.out.print("\n\tFamily: ");
                            String family = sc.nextLine();

                            System.out.print("\n\tSub Family: ");
                            String subFamily = sc.nextLine();

                            System.out.print("\tUnit Price: ");
                            double unitPrice = sc.nextDouble();

                            System.out.print("\tTax Rate: ");
                            double taxRate = sc.nextDouble();

                            System.out.print("\tDiscount Rate: ");
                            double discountRate = sc.nextDouble();

                            System.out.print("\tStock Quantity: ");
                            int stockQuantity = sc.nextInt();

                            System.out.print("\tSupplier ID: ");
                            int supplierID = sc.nextInt();

                            int newID = Integer.parseInt(productManager.getLastId()) + 1;

                            productManager.add(
                                    String.valueOf(newID),
                                    productName,
                                    description,
                                    UnitType.UNIT,
                                    family,
                                    subFamily,
                                    unitPrice,
                                    taxRate,
                                    discountRate,
                                    supplierID,
                                    stockQuantity
                            );

                        }
                        else if(option == 2) {
                            System.out.println("Select the product");
                            List<Product> productList = productManager.getAll();
                            for(Product prod: productList) {
                                System.out.printf("%s: %s\n", prod.getName(), prod.getId());
                            }
                        }
                        else {
                            //logout();
                            System.out.println("Logging out...");
                        }
                    } while (option < 5);

                    break;
            }
        } else {
            System.out.println("\nLogin failed! Invalid username or password.");
        }

        // Close database connection
        employeeManager.closeConnection();
    }


}
