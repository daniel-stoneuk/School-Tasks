package com.danielstone.shapes;

import com.sun.istack.internal.Nullable;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sun.corba.EncapsInputStreamFactory;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;


public class Main extends Application {

    private int gridSize = 30;

    Stage window;

    GraphicsContext theGc;

    Label tempLabel;

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
        tempLabel = new Label("Draw Shapes!");
        tempLabel.setFont(new Font("Segoe UI", 30));
        options.add(tempLabel, 0, 0, 2, 1);

        root.add(canvas, 0, 0);
        root.add(options, 0, 1);

        window.setScene(new Scene(root, canvasX, 700));
        window.show();

        pointsArray = new ArrayList<>();

        drawShapes(gc);
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);
        drawGrid(gc);
    }

    private void canvasClick(int x, int y) {
        x = snapToGrid(x);
        y = snapToGrid(y);
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
        for (com.danielstone.shapes.Circle circle : pointsArray) {
            theGc.setFill(circle.color);
            theGc.fillOval(circle.x, circle.y, circle.width, circle.height);
        }
    }

    private int snapToGrid(int position) {
        return round(position, gridSize);
    }

    int round(double i, int v){
        return (int) Math.round(i/v) * v;
    }


    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.LIGHTBLUE);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        drawTriangleWithGridPoints(gc, coord(1,1), coord(1,5), coord(5,5), FILL, 5);
        drawTriangleWithGridPoints(gc, coord(1,6), coord(1,11), coord(5,11), FILL, 5);
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

    private double[] coord(double x, double y){
        return new double[]{x, y};
    }

    private double gridToPixels(double gridValue) {
        double pixels = gridValue * gridSize;
        return pixels;
    }

    private double[] gridArrayToPixels(double[] gridValues) {
        double[] result = new double[gridValues.length];

        for (int i = 0; i < gridValues.length; i++) {
            result[i] = gridValues[i] * gridSize;
        }
        return result;
    }
}
