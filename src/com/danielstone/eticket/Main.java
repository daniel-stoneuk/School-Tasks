package com.danielstone.eticket;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Main extends Application {

    Stage window;
    Scene scene;
    ArrayList<Station> stationArrayList;
    ChoiceBox<String> departChoiceBox, arriveChoiceBox;
    int removedDepart, removedArrive;
    Label priceLabel;
    boolean changeListenerActive = true;

    String ARRIVE_TAG = "ARRIVE";
    String DEPART_TAG = "DEPART";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20));
        layout.setVgap(10);
        layout.setHgap(10);

        setStationArrayList();

        departChoiceBox = new ChoiceBox<>();
        departChoiceBox.getItems().setAll(getStationNames(null, DEPART_TAG));
        departChoiceBox.setMinWidth(200);
        GridPane.setConstraints(departChoiceBox, 0, 1, 2, 1);

        arriveChoiceBox = new ChoiceBox<>();
        arriveChoiceBox.setMinWidth(200);
        arriveChoiceBox.getItems().setAll(getStationNames(null, ARRIVE_TAG));
        GridPane.setConstraints(arriveChoiceBox, 3, 1, 2, 1);

        departChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> changed(departChoiceBox, oldValue, newValue));
        arriveChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> changed(arriveChoiceBox, oldValue, newValue));

        priceLabel = new Label("£0.00");
        GridPane.setConstraints(priceLabel, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);

        layout.getChildren().addAll(departChoiceBox, arriveChoiceBox, priceLabel);

        scene = new Scene(layout);

        window.setScene(scene);
        window.setTitle("E-Ticket Machine");
        window.show();

    }

    private ArrayList<String> getStationNames(Integer removeIndex, String arriveOrDepart) {
        ArrayList<String> result = new ArrayList<>();

        int currentIndex = 0;

        for (Station station:
             stationArrayList) {
            if (removeIndex == null) {
                result.add(station.getNAME());
            } else if (removeIndex != currentIndex){
                result.add(station.getNAME());

            }
            currentIndex ++;
        }
        if (arriveOrDepart.equals(ARRIVE_TAG)) {
            removedArrive = removeIndex;
        } else {
            removedDepart = removeIndex;
        }
        return result;
    }

    private void setStationArrayList(){
        stationArrayList = new ArrayList<>();
        stationArrayList.addAll(Arrays.asList(
           new Station("Wallsall", 0),
           new Station("Birmingham New Street", 1),
           new Station("Birmingham International", 2),
           new Station("Coventry", 3),
           new Station("Rugby", 4),
           new Station("Northampton", 5),
           new Station("Milton Keynes", 6),
           new Station("Watford", 7),
           new Station("London Euston", 8)
        ));
    }

    private void changed(ChoiceBox<String> choiceBox, String oldValue, String newValue){
        if (newValue != null && changeListenerActive) {

            changeListenerActive = false;

            System.out.println(newValue);
            int position = findPosition(newValue, stationArrayList);
            System.out.println("" + position);

            String departChoiceBoxSelected = departChoiceBox.getValue();
            String arriveChoiceBoxSelected = arriveChoiceBox.getValue();

            if (choiceBox == departChoiceBox && position != -1) {
                arriveChoiceBox.getItems().setAll(getStationNames(position, ARRIVE_TAG));
                arriveChoiceBox.setValue(arriveChoiceBoxSelected);
                arriveChoiceBox.setMinWidth(200);
            } else if (position != -1) {
                departChoiceBox.getItems().setAll(getStationNames(position, DEPART_TAG));
                departChoiceBox.setValue(departChoiceBoxSelected);
                departChoiceBox.setMinWidth(200);
            }

            updateCost();
            changeListenerActive = true;
        }

    }

    private void updateCost() {
        int departChoiceBoxPosition = -1;
        if (departChoiceBox.getValue() != null) departChoiceBoxPosition = findPosition(departChoiceBox.getValue(), stationArrayList);


        int arriveChoiceBoxPosition = -1;
        if (arriveChoiceBox.getValue() != null) arriveChoiceBoxPosition = findPosition(arriveChoiceBox.getValue(), stationArrayList);
        Integer cost = 0;

        int numberOfStops = arriveChoiceBoxPosition - departChoiceBoxPosition;
        if (numberOfStops < 0) numberOfStops = (numberOfStops * -1) -1;

        System.out.println("Stops: " + numberOfStops);

        if (arriveChoiceBoxPosition != -1 && departChoiceBoxPosition != -1) {
            if (numberOfStops >= 1 && numberOfStops <= 4) {
                cost = 185;
            } else if (numberOfStops >= 5 && numberOfStops <= 6) {
                cost = 260;
            } else if (numberOfStops >= 7) {
                cost = 310;
            }
        }

        System.out.println(""+cost);

        priceLabel.setText(numberOfStops + " Stop(s) - £"+addZeros(cost.doubleValue() / 100));
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

    private int findPosition(String value, ArrayList<Station> searchStationArrayList) {
        for(Station station: searchStationArrayList) {
            if (station.getNAME().equals(value)) {
                return station.getPOSITION();
            }
        }
        return -1;
    }
}
