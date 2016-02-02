package com.danielstone.eticket;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChoosePeople {

    static int people;

    static int currentColumn;
    static int currentRow;

    static GridPane layout;

    public static int display(String title, String message) {
        people = -1;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        layout = new GridPane();

        currentColumn = 0;
        currentRow = 0;

        Label label = new Label();
        label.setText("Number of" + message);
        GridPane.setConstraints(label, currentColumn, currentRow);
        currentRow++;
        layout.getChildren().addAll(label);

        generateButtons(9);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return people;
    }

    public static void generateButtons(int numberToGenerate) {
        for (int i = 0; i < numberToGenerate; i++) {
            Button currentButton = new Button(""+i);
            GridPane.setConstraints(currentButton, currentColumn, currentRow);
            layout.getChildren().add(currentButton);
            currentColumn++;
            if (currentColumn > 2) {
                currentRow ++;
                currentColumn = 0;
            }

        }

    }

}
