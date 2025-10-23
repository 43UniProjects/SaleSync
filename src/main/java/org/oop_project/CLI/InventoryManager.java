package org.oop_project.CLI;

import org.oop_project.DatabaseHandler.Enums.UnitType;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Models.Product;
import org.oop_project.DatabaseHandler.Operations.ProductOperations;
import org.oop_project.Main;

import java.util.List;

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
                showDetails();
            }
            else if(option == 4) {
                showAll();
                deleteRecord();
            }else{
                System.out.println("\nLogged out!");
                Main.main(new String[]{""}); // Restart the application for login
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
        double stockQuantity = sc.nextInt();

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

    public static void showAll(){
        System.out.println("\nProduct List:\n");
        List<Product> productList = productManager.getAll();

        for(Product prod: productList) {
            System.out.printf("\t%s. %s\n", prod.getId(), prod.getName());
        }
    }

    public static void showDetails(){
        sc.nextLine();
        System.out.print("\nEnter the product ID: ");
        String prodId = sc.nextLine();
        Product prod = productManager.get(prodId);

        if(prod != null) {
            System.out.println("\nProduct Details:\n");
            System.out.println("\tID: " + prod.getId());
            System.out.println("\tName: " + prod.getName());
            System.out.println("\tDescription: " + prod.getDescription());
            System.out.println("\tFamily: " + prod.getFamily());
            System.out.println("\tSub Family: " + prod.getSubFamily());
            System.out.println("\tUnit Price: " + prod.getUnitPrice());
            System.out.println("\tTax Rate: " + prod.getTaxRate());
            System.out.println("\tDiscount Rate: " + prod.getDiscountRate());
            System.out.println("\tStock Quantity: " + prod.getAvailableQuantity());
            System.out.println("\tSupplier ID: " + prod.getSupplierId());
        } else {
            System.out.println("Product not found!");
        }
    }

    public static void deleteRecord(){
        sc.nextLine();
        System.out.print("\nEnter the product ID to delete: ");
        String prodId = sc.nextLine();
        boolean isDeleted = productManager.remove(prodId);
        if(isDeleted) {
            System.out.println("\nProduct deleted successfully.");
        } else {
            System.out.println("\nProduct not found or could not be deleted.");
        }
    }
}
