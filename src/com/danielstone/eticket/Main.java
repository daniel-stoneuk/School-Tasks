package com.danielstone.eticket;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Main extends Application implements ChangeListener<String> {

    Stage window;
    Scene scene;
    ArrayList<Station> stationArrayList;

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

        ChoiceBox<String> departChoiceBox = new ChoiceBox<>();
        departChoiceBox.getItems().setAll(getStationNames(null));
        GridPane.setConstraints(departChoiceBox, 0, 1, 2, 1);

        ChoiceBox<String> arriveChoiceBox = new ChoiceBox<>();
        arriveChoiceBox.getItems().setAll(getStationNames(null));
        GridPane.setConstraints(arriveChoiceBox, 3, 1, 2, 1);

        departChoiceBox.getSelectionModel().selectedItemProperty().addListener(this);
        arriveChoiceBox.getSelectionModel().selectedItemProperty().addListener(this);

        layout.getChildren().addAll(departChoiceBox, arriveChoiceBox);

        scene = new Scene(layout, 600, 200);

        window.setScene(scene);
        window.setTitle("E-Ticket Machine");
        window.show();

    }

    private ArrayList<String> getStationNames(Integer removeIndex) {
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

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println(newValue);
    }
}
