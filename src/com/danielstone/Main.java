package com.danielstone;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main extends Application implements ChangeListener<Boolean>{

    Stage window;
    Scene scene;

    //price
    Map<String, Float> sizes;
    Map<String, Float> breads;
    Map<String, Float> filling;

    Boolean eatIn = true;

    int currentRow;
    int currentColumn;

    GridPane layout;

    public static void main(String[] args) {
	launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializePrices();

        window = primaryStage;
        layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);

        currentRow = 0;
        currentColumn = 0;


        addOptions();

        currentColumn += 2;
        currentRow = 1;
        Button button = new Button("Buy");
        GridPane.setConstraints(button, currentColumn, currentRow);

        layout.getChildren().add(button);

        scene = new Scene(layout, 400, 400);

        window.setScene(scene);
        window.setTitle("Subway");
        window.show();
    }

    private void addOptions() {
        Label title = new Label("Size");
        GridPane.setConstraints(title, currentColumn, currentRow);
        layout.getChildren().add(title);
        currentRow ++;

        Set<Map.Entry<String, Float>> set = sizes.entrySet();
        addOptionsFromSet(set, false);

        title = new Label("Bread");
        GridPane.setConstraints(title, currentColumn, currentRow);
        layout.getChildren().add(title);
        currentRow ++;

        set = breads.entrySet();
        addOptionsFromSet(set, false);

        title = new Label("Fillings");
        GridPane.setConstraints(title, currentColumn, currentRow);
        layout.getChildren().add(title);
        currentRow ++;

        set = filling.entrySet();
        addOptionsFromSet(set, true);
    }

    private void addOptionsFromSet(Set<Map.Entry<String, Float>> set, boolean multi) {
        ToggleGroup group = new ToggleGroup();
        for (Map.Entry<String, Float> currentMap : set) {
            CheckBox currentBox;
            RadioButton currentRadio;
            if (multi) {
                currentBox = new CheckBox(currentMap.getKey());

                currentBox.selectedProperty().addListener(this);

                GridPane.setConstraints(currentBox, currentColumn, currentRow);
                layout.getChildren().add(currentBox);
            } else {
                currentRadio = new RadioButton(currentMap.getKey());

                currentRadio.selectedProperty().addListener(this);

                currentRadio.setToggleGroup(group);
                GridPane.setConstraints(currentRadio, currentColumn, currentRow);
                layout.getChildren().add(currentRadio);

            }

            Label currentLabel = new Label("" + currentMap.getValue());
            GridPane.setConstraints(currentLabel, (currentColumn + 1), currentRow);
            currentRow ++;

            layout.getChildren().add(currentLabel);
        }
    }

    private void initializePrices() {
        sizes = new HashMap<>();
        sizes.put("Six-Inch", 1.65f);
        sizes.put("Twelve-Inch", 2.05f);

        breads = new HashMap<>();
        breads.put("Plain", 0.40f);
        breads.put("Wheat", 0.65f);
        breads.put("Italian", 0.75f);
        breads.put("Cheese & Herbs", 0.80f);

        filling = new HashMap<>();
        filling.put("Cheese & Tomato", 0.95f);
        filling.put("Italian Bacon & Peperoni", 1.10f);
        filling.put("Tuna & Mayo", 0.95f);
        filling.put("Turkey & Ham", 1.35f);
        filling.put("Chicken Teriyaki", 1.40f);
        filling.put("Steak & Cheese", 1.95f);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        System.out.println("Button Pressed: " + oldValue + " to " + newValue);
    }
}
