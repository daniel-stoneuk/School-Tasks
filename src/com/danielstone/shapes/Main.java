package com.danielstone.shapes;

import com.sun.istack.internal.Nullable;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Main extends Application {

    private int gridSize = 30;

    Stage window;

    GraphicsContext theGc;

    Label titleLabel, numberOfPointsLabel;
    Button resetButton;

    private final int FILL = 1;
    private final int FILL_WITH_STROKE = 2;
    private final int STROKE = 3;

    private final double canvasX = 510;
    private final double canvasY = 510;

    private final int circleWidth = 10;
    private final int circleHeight = 10;



    ArrayList<com.danielstone.shapes.Circle> pointsArray;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Triangles");
        GridPane root = new GridPane();
        Canvas canvas = new Canvas(canvasX, canvasY);

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                canvasClick(x, y);
            }
        });

        GraphicsContext gc = canvas.getGraphicsContext2D();
        theGc = gc;
        GridPane options = new GridPane();
        options.setPadding(new Insets(20));

        titleLabel = new Label("Draw Shapes!");
        titleLabel.setFont(new Font("Segoe UI", 30));
        options.add(titleLabel, 0, 0, 2, 1);

        numberOfPointsLabel = new Label("0 points");
        numberOfPointsLabel.setFont(new Font("Segoe UI", 15));
        options.add(numberOfPointsLabel, 0, 1, 1, 1);

        resetButton = new Button("Reset");
        resetButton.setFont(new Font("Segoe UI", 15));
        resetButton.setOnAction(event -> resetButtonClicked(event));
        options.setMargin(resetButton, new Insets(0, 0, 0, 20));
        options.add(resetButton, 1, 1, 1, 1);

        root.add(canvas, 0, 0);
        root.add(options, 0, 1);

        window.setScene(new Scene(root, canvasX, 700));
        window.show();

        pointsArray = new ArrayList<>();

        drawShapes(gc);
        drawGrid(gc);
    }

    private void resetButtonClicked(ActionEvent event) {
        pointsArray.clear();
        reRender();
    }

    private void canvasClick(int x, int y) {
        x = snapToGridPixels(x);
        y = snapToGridPixels(y);
        System.out.println("x: " +x + "   :   y: " + y);

        Integer check = checkIfCircleExists(x  - circleWidth /2, y  - circleHeight /2);
        if (check == null) {
            pointsArray.add(new com.danielstone.shapes.Circle(x - circleWidth / 2, y - circleHeight / 2, circleWidth, circleHeight, Color.RED));
        } else {
            pointsArray.remove((int) check);
        }
        reRender();
    }

    private void reRender() {
        theGc.clearRect(0, 0, canvasX, canvasY);
        drawGrid(theGc);
        int numberOfPoints = 0;
        ArrayList<double[]> gridPointsArray = new ArrayList<>();
        for (com.danielstone.shapes.Circle circle : pointsArray) {
            theGc.setFill(circle.color);
            theGc.fillOval(circle.x, circle.y, circle.width, circle.height);
            numberOfPoints ++;
            gridPointsArray.add(new double[] {pixelsToGrid(snapToGridPixels(circle.x)), pixelsToGrid(snapToGridPixels(circle.y))});
        }

        if (gridPointsArray.size() >= 3){
            double[] xPoints = new double[gridPointsArray.size()];
            double[] yPoints = new double[gridPointsArray.size()];
            for (int i = 0; i < gridPointsArray.size(); i++) {
                xPoints[i] = gridPointsArray.get(i)[0];
                yPoints[i] = gridPointsArray.get(i)[1];
            }
            theGc.setFill(Paint.valueOf("#2196F3"));
            drawPolygonWithGridPointsArray(theGc, xPoints, yPoints, FILL, gridPointsArray.size(), 3);
        }

        numberOfPointsLabel.setText(numberOfPoints + " points");
    }

    private int snapToGridPixels(int position) {
        return round(position, gridSize);
    }

    int round(double i, int v){
        return (int) Math.round(i/v) * v;
    }


    private void drawShapes(GraphicsContext gc) {
        gc.applyEffect(new DropShadow(3, Color.GREY));
    }

    private Integer checkIfCircleExists(int x, int y){
        for (int i = 0; i < pointsArray.size(); i++) {
            if (pointsArray.get(i).x == x && pointsArray.get(i).y == y) {
                return i;
            }
        }
        return null;
    }

    private void drawGrid(GraphicsContext gc) {

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);


        double height = canvasX;
        double width = canvasY;

        for (int i = 0; i < height; i = i + gridSize) {
            gc.strokeLine(i, 0, i, width);
        }
        for (int i = 0; i < width; i = i + gridSize) {
            gc.strokeLine(0, i, height, i);
        }


    }

    private void drawTriangleWithGridPoints(GraphicsContext gc,
                                            double[] coord1,
                                            double[] coord2,
                                            double[] coord3,
                                            int type,
                                            @Nullable Integer strokeSize) {
        gc.setLineWidth(strokeSize);
        if (type == FILL || type == FILL_WITH_STROKE)
            gc.fillPolygon(gridArrayToPixels(new double[]{coord1[0], coord2[0], coord3[0]}),
                    gridArrayToPixels(new double[]{coord1[1], coord2[1], coord3[1]}), 3);
        if (type == STROKE || type == FILL_WITH_STROKE) {
            gc.strokePolygon(gridArrayToPixels(new double[]{coord1[0], coord2[0], coord3[0]}),
                    gridArrayToPixels(new double[]{coord1[1], coord2[1], coord3[1]}), 3);
        }
    }

    private void drawPolygonWithGridPointsArray(GraphicsContext gc,
                                            double[] xPoints,
                                            double[] yPoints,
                                            int type, int numberOfPoints,
                                            @Nullable Integer strokeSize) {
        gc.setLineWidth(strokeSize);
        if (type == FILL || type == FILL_WITH_STROKE)
            gc.fillPolygon(gridArrayToPixels(xPoints),
                    gridArrayToPixels(yPoints), numberOfPoints);
        if (type == STROKE || type == FILL_WITH_STROKE) {
            gc.strokePolygon(gridArrayToPixels(xPoints),
                    gridArrayToPixels(yPoints), numberOfPoints);
        }
    }


    private double[] coord(double x, double y){
        return new double[]{x, y};
    }

    private double gridToPixels(double gridValue) {
        double pixels = gridValue * gridSize;
        return pixels;
    }

    /**
     * Takes a grid value double and converts it to the same value in pixels
     * @param gridValues a double[] grid point ([0] is x [1] is y)
     * @return a double[] pixel point ([0] is x [1] is y)
     */
    private double[] gridArrayToPixels(double[] gridValues) {
        double[] result = new double[gridValues.length];

        for (int i = 0; i < gridValues.length; i++) {
            result[i] = gridValues[i] * gridSize;
        }
        return result;
    }

    private double pixelsToGrid(double pixelValue) {
        return pixelValue / gridSize;
    }
}
