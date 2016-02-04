package com.danielstone.weather;

import javafx.application.Application;
import javafx.stage.Stage;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import javax.swing.text.html.HTML;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main extends Application{

    static boolean running = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Weather");
        WorkerTask task = new WorkerTask();
        task.execute();

    }

    public static class WorkerTask extends SwingWorker<String, Integer> {

        @Override
        protected void done() {
            try {
                System.out.println(get());
                String result = get();
                JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
                JSONObject cityObject = (JSONObject) jsonObject.get("city");
                JSONArray listArray = (JSONArray) jsonObject.get("list");
                JSONObject zeroObject = (JSONObject) listArray.get(0);
                JSONObject tempObject = (JSONObject) zeroObject.get("temp");
                System.out.println(tempObject.get("max"));
                running = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            super.done();
        }

        @Override
        protected String doInBackground() throws Exception {

            HttpURLConnection httpURLConnection = null;
            String result = "";

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=London&mode=json&units=metric&cnt=7&APPID=9858e11d9d8a99516b8230dacdd9bb1d");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                int responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {


                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);

                    int progress = 0;
                    int data = reader.read();
                    while (data != -1) {
                        char currentChar = (char) data;

                        result = result + currentChar;

                        progress ++;
                        publish(progress);

                        data = reader.read();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection == null) httpURLConnection.disconnect();
            }
            return result;
        }

        @Override
        protected void process(List<Integer> chunks) {
            for (int i : chunks) {
                System.out.println(i);
            }
            super.process(chunks);
        }
    }
}
