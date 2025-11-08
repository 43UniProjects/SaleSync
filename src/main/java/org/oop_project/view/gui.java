package org.oop_project.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class gui extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/login.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        // Load CSS if it exists
        try {
            String cssPath = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (NullPointerException e) {
            System.out.println("Warning: style.css not found, continuing without styling.");
        }

        // Setup icon if it exists
        try {
            Image icon = new Image(getClass().getResourceAsStream("/org/oop_project/view/images/icon.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Warning: icon.png not found, continuing without icon.");
        }



        stage.setTitle("SaleSync");
        stage.setResizable(false);
        stage.centerOnScreen();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}