package org.oop_project.CLI;

import java.util.List;

import org.oop_project.DatabaseHandler.Enums.UnitType;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Models.Product;
import org.oop_project.DatabaseHandler.Operations.ProductOperations;

public class InventoryManager {
    static  ProductOperations productManager = new ProductOperations();
    static  java.util.Scanner sc = new java.util.Scanner(System.in);

    public static void showMenu(Employee employee) {
        int option;

        do {
            System.out.println("\nWelcome to the Product Portal, " + employee.getUsername() + "!");

            System.out.println("Here you can manage product-related tasks.\n");
            System.out.println("\n\t1. Add New Product");
            System.out.println("\t2. Update Product Details");
            System.out.println("\t3. View Product List");
            System.out.println("\t4. Remove Product");
            System.out.println("\t5. Logout");
            System.out.print("\nCHOOSE AN OPTION: ");

            option = sc.nextInt();

            if (option == 1) {
                addProduct();                          

            }
            else if(option == 2) {
                showAll();
            }
            else if(option == 3) {
                showAll();
            }
            else if(option == 4) {
                showAll();
            }else{
                System.out.println("Logging out...");
            }

        } while (option < 5);
    }

    public static void addProduct() {
        sc.nextLine();
        System.out.println("\n\nEnter product details:");
        
        System.out.print("\n\tProduct Name: ");
        String productName = sc.nextLine();

        System.out.print("\tProduct description: ");
        String description = sc.nextLine();

        System.out.print("\tFamily: ");
        String family = sc.nextLine();

        System.out.print("\tSub Family: ");
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
        System.out.println(supplierID);

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

    public static void showAll(){

        System.out.println("Select the product");
        List<Product> productList = productManager.getAll();

        for(Product prod: productList) {
            System.out.printf("%s: %s\n", prod.getId(), prod.getName());
        }
    }
}
