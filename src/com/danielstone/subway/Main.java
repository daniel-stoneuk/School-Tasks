package com.danielstone.subway;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application implements ChangeListener<Boolean>{

    Stage window;
    Scene scene;

    ArrayList<ArrayList<Ingredient>> options;

    Boolean eatIn = true;

    int currentRow;
    int currentColumn;
    GridPane layout;

    Label price;

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
        price = new Label("£0.00");
        GridPane.setConstraints(price, currentColumn, currentRow);
        currentRow ++;
        Button button = new Button("Buy");
        button.setOnAction(event -> buttonPressed());
        GridPane.setConstraints(button, currentColumn, currentRow);

        layout.getChildren().addAll(price, button);

        scene = new Scene(layout, 400, 400);

        window.setScene(scene);
        window.setTitle("Subway");
        window.show();
    }

    private void addOptions() {

        Label title = new Label("Ingredient");
        GridPane.setConstraints(title, currentColumn, currentRow);
        layout.getChildren().add(title);
        currentRow ++;

        ArrayList<Ingredient> sizes = options.get(0);
        addOptionsFromArrayList(sizes, false);

        title = new Label("Bread");
        GridPane.setConstraints(title, currentColumn, currentRow);
        layout.getChildren().add(title);
        currentRow ++;

        ArrayList<Ingredient> breads = options.get(1);
        addOptionsFromArrayList(breads, false);

        title = new Label("Fillings");
        GridPane.setConstraints(title, currentColumn, currentRow);
        layout.getChildren().add(title);
        currentRow ++;

        ArrayList<Ingredient> fillings = options.get(2);
        addOptionsFromArrayList(fillings, true);
    }

    private void addOptionsFromArrayList(ArrayList<Ingredient> ingredientArrayList, boolean multi) {
        ToggleGroup group = new ToggleGroup();
        for (Ingredient ingredient : ingredientArrayList) {
            CheckBox currentBox;
            RadioButton currentRadio;
            if (multi) {
                currentBox = new CheckBox(ingredient.getNAME());
                currentBox.selectedProperty().addListener(this);
                GridPane.setConstraints(currentBox, currentColumn, currentRow);
                layout.getChildren().add(currentBox);
                ingredient.setINDEX(layout.getChildren().indexOf(currentBox));
            } else {
                currentRadio = new RadioButton(ingredient.getNAME());

                currentRadio.selectedProperty().addListener(this);

                currentRadio.setToggleGroup(group);
                GridPane.setConstraints(currentRadio, currentColumn, currentRow);
                layout.getChildren().add(currentRadio);
                ingredient.setINDEX(layout.getChildren().indexOf(currentRadio));
            }

            Label currentLabel = new Label("£" + addZeros((ingredient.getPRICE().doubleValue() / 100)));
            GridPane.setConstraints(currentLabel, (currentColumn + 1), currentRow);
            currentRow ++;

            layout.getChildren().add(currentLabel);
        }
    }

    private void initializePrices() {

        options = new ArrayList<>();

        ArrayList<Ingredient> sizes = new ArrayList<>();
        sizes.addAll(Arrays.asList(
                new Ingredient("Six-Inch", 165, false),
                new Ingredient("Twelve-Inch", 165, false)));

        ArrayList<Ingredient> breads = new ArrayList<>();
        breads.addAll(Arrays.asList(
                new Ingredient("Plain", 40, false),
                new Ingredient("Wheat", 65, false),
                new Ingredient("Italian", 75, false),
                new Ingredient("Cheese & Herbs", 80, false)
        ));

        ArrayList<Ingredient> fillings = new ArrayList<>();
        fillings.addAll(Arrays.asList(
                new Ingredient("Cheese", 45, true),
                new Ingredient("Tomato", 45, true),
                new Ingredient("Bacon", 55, true),
                new Ingredient("Pepperoni", 55, true),
                new Ingredient("Tuna", 45, true),
                new Ingredient("Mayo", 45, true),
                new Ingredient("Turkey", 70, true),
                new Ingredient("Ham", 70, true),
                new Ingredient("Chicken Teriyaki", 140, true),
                new Ingredient("Steak", 100, true),
                new Ingredient("Cheese", 100, true)
        ));

        options.addAll(Arrays.asList(
                sizes, breads, fillings
        ));

    }

    private String addZeros(Double priceDouble) {

        String returnPrice;

        String price = Double.toString(priceDouble);
        int integerPlaces = price.indexOf('.');
        int decimalPlaces = price.length() - integerPlaces - 1;

        if (decimalPlaces < 2) {
            returnPrice = price + "0";
        } else {
            returnPrice = price;
        }

        return returnPrice;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        System.out.println("Button Pressed: " + oldValue + " to " + newValue);

        Integer price = totalPrice();


        System.out.println("Price: " + (price.floatValue() / 100));


        this.price.setText("£" + addZeros((price.doubleValue() / 100)));
    }

    private Integer totalPrice() {
        Integer price = 0;
        for (ArrayList<Ingredient> ingredientArrayList : options) {
            for (Ingredient ingredient : ingredientArrayList) {
                int ingredientIndex = ingredient.getINDEX();
                if (ingredient.isMULTI()) {
                    CheckBox currentCheckBox = (CheckBox) layout.getChildren().get(ingredientIndex);
                    if (currentCheckBox.isSelected()) {
                        price += ingredient.getPRICE();
                    }
                } else {
                    RadioButton currentRadioButton = (RadioButton) layout.getChildren().get(ingredientIndex);
                    if (currentRadioButton.isSelected()) {
                        price += ingredient.getPRICE();
                    }
                }
            }
        }
        return price;
    }

    public void buttonPressed() {
        AlertBox.display("Purchased", "Purchased for £" + addZeros((totalPrice().doubleValue() / 100)));
    }
}
