package com.danielstone.eticket;


import javafx.geometry.Insets;
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

    static Stage window;
    static GridPane gridPane;

    public static int display(String title, String message, int oldPeople) {
        people = oldPeople;
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        gridPane = new GridPane();

        currentColumn = 0;
        currentRow = 0;

        Label label = new Label();
        label.setText("Number of" + message);
        currentRow++;

        generateButtons(9);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(0, 20, 20, 20));

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 0, 0, 0));
        layout.getChildren().addAll(label, gridPane);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return people;
    }

    public static void generateButtons(int numberToGenerate) {
        for (int i = 0; i < numberToGenerate; i++) {
            int number = i;
            Button currentButton = new Button(""+number);
            currentButton.setOnAction(event -> {
                people = number;
                window.close();
            });
            GridPane.setConstraints(currentButton, currentColumn, currentRow);
            gridPane.getChildren().add(currentButton);
            currentColumn++;
            if (currentColumn > 2) {
                currentRow ++;
                currentColumn = 0;
            }

        }

    }

}
