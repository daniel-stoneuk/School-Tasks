package com.danielstone.eticket;


import com.sun.istack.internal.Nullable;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    Stage window;
    Scene scene;
    ArrayList<Station> stationArrayList;
    ChoiceBox<String> departChoiceBox, arriveChoiceBox;
    int removedDepart, removedArrive = 0;
    Label priceLabel;
    boolean changeListenerActive = true;
    RadioButton singleRadio;

    int numberOfAdults = -1;
    int numberOfChildren = -1;

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

        ToggleGroup amountGroup = new ToggleGroup();
        singleRadio = new RadioButton("Single");
        singleRadio.setToggleGroup(amountGroup);
        singleRadio.setSelected(true);
        GridPane.setConstraints(singleRadio, 3, 3);

        RadioButton returnRadio = new RadioButton("Return");
        returnRadio.setToggleGroup(amountGroup);
        GridPane.setConstraints(returnRadio, 4, 3);

        singleRadio.selectedProperty().addListener((observable, oldValue, newValue) -> updateCost());
        returnRadio.selectedProperty().addListener((observable, oldValue, newValue) -> updateCost());



        priceLabel = new Label("Enter ticket quantity");
        GridPane.setConstraints(priceLabel, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);

        Button children = new Button("Children: -click-");
        children.setOnAction(event -> {
            int selected = ChoosePeople.display("Children", "children", numberOfChildren);
            if (selected != -1) numberOfChildren = selected;
            setPeopleButton(children, "Children: ", selected);
        });
        children.setMinWidth(200);
        children.setAlignment(Pos.CENTER);
        GridPane.setConstraints(children, 0, 0, 2, 1);

        Button adults = new Button("Adults: -click-");
        adults.setOnAction(event -> {
            int selected = ChoosePeople.display("Adults", "adults", numberOfAdults);
            if (selected != -1) numberOfAdults = selected;
            setPeopleButton(adults, "Adults: ", selected);
        });
        adults.setMinWidth(200);
        adults.setAlignment(Pos.CENTER);
        GridPane.setConstraints(adults, 3, 0, 2, 1);

        layout.getChildren().addAll(departChoiceBox, arriveChoiceBox, priceLabel, singleRadio, returnRadio, children, adults);

        scene = new Scene(layout);

        window.setScene(scene);
        window.setTitle("E-Ticket Machine");
        window.show();

    }

    private void setPeopleButton(Button button, String prefix, int choice) {

        if (choice != -1) {
            button.setText(prefix + choice);
            updateCost();
        }
    }

    private ArrayList<String> getStationNames(@Nullable Integer removeIndex, String arriveOrDepart) {
        ArrayList<String> result = new ArrayList<>();

        int currentIndex = 0;
        int removed = -1;
        boolean isNull = false;
        if (removeIndex == null) {
            isNull = true;
        } else {
            removed = removeIndex;
        }

        for (Station station:
             stationArrayList) {
            if (isNull) {
                result.add(station.getNAME());
            } else if (removeIndex != currentIndex){
                result.add(station.getNAME());

            }
            currentIndex ++;
        }
        if (!isNull) {
            if (arriveOrDepart.equals(ARRIVE_TAG) && removed != -1) {
                removedArrive = removed;
            } else if (removed != -1) {
                removedDepart = removed;
            }
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

            //System.out.println(newValue);
            int position = findPosition(newValue, stationArrayList);
            //System.out.println("" + position);

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
        Integer cost = 0;

        boolean single = singleRadio.isSelected();

        Integer numberOfStops = generateNumberOfStops();

        System.out.println("Stops: " + numberOfStops);

        String resultText = "";

        if (numberOfStops != -1 && peopleSelectedBool()) {
            if (numberOfStops >= 1 && numberOfStops <= 4) {
                cost = 1850000;
            } else if (numberOfStops >= 5 && numberOfStops <= 6) {
                cost = 2600000;
            } else if (numberOfStops >= 7) {
                cost = 3100000;
            }

            if (!single) {
                int discount = cost / 100 * 13;
                cost = cost + (cost - discount);
            }

            Integer totalCost = 0;

            for (int i = 0; i < numberOfAdults; i++) {
                totalCost = totalCost + cost;
            }

            int discount = cost / 200 * 25;
            int childCost = cost - discount;

            for (int i = 0; i < numberOfChildren; i++) {
                totalCost = totalCost + childCost;
            }

            System.out.println(""+totalCost);

            resultText = numberOfStops + " Stop(s) - £"+ addZeros(totalCost.doubleValue() / 1000000);
        } else {
            if (!peopleSelectedBool()) {
                resultText = "Enter ticket quantity";
            } else {
                resultText = "Please choose two stations";
            }
        }



        priceLabel.setText(resultText);
    }

    private boolean peopleSelectedBool() {
        if (numberOfAdults != -1 && numberOfChildren != -1) {
            return true;
        }
        return false;
    }

    private Integer generateNumberOfStops() {
        boolean complicatedStuff = false;

        int departChoiceBoxPosition = -1;
        if (departChoiceBox.getValue() != null) departChoiceBoxPosition = findPosition(departChoiceBox.getValue(), stationArrayList);
        if (departChoiceBoxPosition != -1 && departChoiceBoxPosition >= removedDepart) {
            departChoiceBoxPosition ++;
            complicatedStuff = true;
        }

        int arriveChoiceBoxPosition = -1;
        if (arriveChoiceBox.getValue() != null) arriveChoiceBoxPosition = findPosition(arriveChoiceBox.getValue(), stationArrayList);
        if (arriveChoiceBoxPosition != -1 && arriveChoiceBoxPosition >= removedArrive) {
            arriveChoiceBoxPosition ++;
            complicatedStuff = true;
        }

        int numberOfStops = arriveChoiceBoxPosition - departChoiceBoxPosition;
        if (numberOfStops < 0) numberOfStops = (numberOfStops * -1) -1;

        if (arriveChoiceBoxPosition < departChoiceBoxPosition) numberOfStops = numberOfStops + 1;

        if (complicatedStuff) numberOfStops = numberOfStops - 1;

        return (arriveChoiceBoxPosition != -1 && departChoiceBoxPosition != -1) ? numberOfStops : -1;
    }

    private String addZeros(Double priceDouble) {

        String returnPrice;

        String price = Double.toString(priceDouble);
        int integerPlaces = price.indexOf('.');
        int decimalPlaces = price.length() - integerPlaces - 1;

        if (decimalPlaces < 2) {
            returnPrice = price + "0";
        } else if (decimalPlaces > 2){
            while (decimalPlaces > 2) {
                price = price.substring(0, price.length() -1 );
                decimalPlaces = price.length() - integerPlaces - 1;
            }
            returnPrice = price;
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
