package org.oop_project.view.helpers;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.oop_project.database_handler.models.Admin;
import org.oop_project.database_handler.models.Cashier;
import org.oop_project.database_handler.models.Employee;
import org.oop_project.database_handler.models.ProductManager;
import org.oop_project.view.controllers.AdminController;
import org.oop_project.view.controllers.CashierController;
import org.oop_project.view.controllers.LoginController;
import org.oop_project.view.controllers.ProductController;

public class Navigators {

    public static void navigateToLoginPanel(Stage stage, Label label) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigators.class.getResource("/org/oop_project/view/fxml/login.fxml"));
            Scene scene = new Scene(loader.load());
            // Apply dark mode stylesheet
            try {
                String cssPath = Navigators.class.getResource("/org/oop_project/view/css/style.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (Exception e) {
                System.out.println("Warning: style.css not found, continuing without styling.");
            }

            stage.setScene(scene);
            stage.setTitle("SaleSync - Login");
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (Exception e) {
            label.setText("Error loading login!");
            label.setStyle("-fx-text-fill: red;");
        }
    }

    public static void navigateToAdminPanel(Stage stage, Label label, Employee admin) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigators.class.getResource("/org/oop_project/view/fxml/admin-panel.fxml"));
            Scene scene = new Scene(loader.load());
            AdminController ac = loader.getController();
            ac.setAdmin(admin);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("SaleSync - Admin Panel");

        } catch (Exception e) {
            e.printStackTrace();
            label.setText("Error loading admin panel!");
            label.setStyle("-fx-text-fill: red;");
        }
    }

    public static void navigateToCashierPortal(Stage stage, Label label, Employee cashier) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Navigators.class.getResource("/org/oop_project/view/fxml/cashier-portal.fxml"));
            Scene scene = new Scene(loader.load());
            CashierController cc = loader.getController();
            cc.setCashier(cashier);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("SaleSync - Cashier Portal");
        } catch (Exception e) {
            e.printStackTrace();
            label.setText("Error loading cashier portal!");
            label.setStyle("-fx-text-fill: red;");
        }
    }

    public static void navigateToProductManagerPanel(Stage stage, Label label, Employee productManager) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Navigators.class.getResource("/org/oop_project/view/fxml/product-dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            ProductController pc = loader.getController();
            pc.setProductManager(productManager);
            stage.setScene(scene);
            stage.setTitle("SaleSync - Product Dashboard");
            stage.centerOnScreen();
        } catch (Exception e) {
            label.setText("Error loading product dashboard!");
            label.setStyle("-fx-text-fill: red;");
        }
    }

    
}
