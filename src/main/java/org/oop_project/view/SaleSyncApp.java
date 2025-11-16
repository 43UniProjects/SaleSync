package org.oop_project.view;

import javafx.application.Application;
import javafx.stage.Stage;

import static org.oop_project.view.helpers.Navigators.navigateToLoginPanel;

import java.io.IOException;

public class SaleSyncApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        navigateToLoginPanel(stage, null);

    }

    public static void run(String[] args) {
        launch();
    }
}