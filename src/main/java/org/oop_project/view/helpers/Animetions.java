package org.oop_project.view.helpers;

import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animetions {

    public static Transition fadeIn(double duration, Node node) {

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);

        fadeTransition.setToValue(1.0); 
        fadeTransition.setFromValue(0.0);
        fadeTransition.setCycleCount(1); 
        
        return fadeTransition;

    }

    public static Transition fadeOut(double duration, Node node) {

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);

        fadeTransition.setToValue(0.0); 
        fadeTransition.setFromValue(1.0);
        fadeTransition.setCycleCount(1); 
        fadeTransition.setDelay(Duration.seconds(10));
        return fadeTransition;

    }

}
