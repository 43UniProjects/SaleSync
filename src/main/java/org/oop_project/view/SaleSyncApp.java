package org.oop_project.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SaleSyncApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    SaleSyncApp.class.getResource("/org/oop_project/view/fxml/login.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            String cssPath = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);

            Image icon = new Image(getClass().getResourceAsStream("/org/oop_project/view/images/icon.png"));
            stage.getIcons().add(icon);

            stage.setTitle("SaleSync");
            stage.setResizable(false);
            stage.centerOnScreen();

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String[] args) {
        launch();
    }
}