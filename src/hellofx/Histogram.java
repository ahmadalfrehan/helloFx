package hellofx;

import java.awt.image.BufferedImage;
// import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Histogram extends Application {
    static String imagePath = "C:/users/dell/desktop/flutter/ahmadalfrehan.png";

    @Override
    public void start(Stage primaryStage) {

        Label labelInfo = new Label();
        labelInfo.setText(
                "java.version: " + System.getProperty("java.version") + "\n"
                        + "javafx.runtime.version: " + System.getProperty("javafx.runtime.version"));

        TextField textSrc = new TextField();

        Button btnDo = new Button("Do Histogram");
        ImageView imageView = new ImageView();

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<String, Number> chartHistogram = new LineChart<>(xAxis, yAxis);
        chartHistogram.setCreateSymbols(false);
        Image image = new Image(imagePath);

        btnDo.setOnAction((ActionEvent event) -> {
            imageView.setImage(image);
            chartHistogram.getData().clear();

            ImageHistogram imageHistogram = new ImageHistogram();
            if (imageHistogram.isSuccess()) {
                System.out.println(imageHistogram.getMaximmum());
                chartHistogram.getData().addAll(
                        // imageHistogram.getSeriesAlpha(),
                        imageHistogram.getSeriesRed(),
                        imageHistogram.getSeriesGreen(),
                        imageHistogram.getSeriesBlue());
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(imageView, chartHistogram);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(labelInfo, textSrc, btnDo, hBox);

        StackPane root = new StackPane();
        root.getChildren().add(vBox);

        Scene scene = new Scene(root, 1000, 500);

        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    class ImageHistogram {

        private BufferedImage image;
        private long alpha[] = new long[256];
        private long red[] = new long[256];
        private long green[] = new long[256];
        private long blue[] = new long[256];

        XYChart.Series seriesAlpha;
        XYChart.Series seriesRed;
        XYChart.Series seriesGreen;
        XYChart.Series seriesBlue;

        private boolean success;

        ImageHistogram() {
            System.out.println(imagePath.substring(5, imagePath.length()));
            try {
                image = ImageIO.read(new File(imagePath.substring(5, imagePath.length())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            success = false;

            for (int i = 0; i < 256; i++) {
                alpha[i] = red[i] = green[i] = blue[i] = 0;
            }
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int argb = image.getRGB(x, y);
                    int a = (0xff & (argb >> 24));
                    int r = (0xff & (argb >> 16));
                    int g = (0xff & (argb >> 8));
                    int b = (0xff & argb);

                    alpha[a]++;
                    red[r]++;
                    green[g]++;
                    blue[b]++;

                }
            }

            seriesAlpha = new XYChart.Series();
            seriesRed = new XYChart.Series();
            seriesGreen = new XYChart.Series();
            seriesBlue = new XYChart.Series();
            seriesAlpha.setName("alpha");
            seriesRed.setName("red");
            seriesGreen.setName("green");
            seriesBlue.setName("blue");

            for (int i = 0; i < 256; i++) {

                seriesAlpha.getData().add(new XYChart.Data(String.valueOf(i), alpha[i]));
                seriesRed.getData().add(new XYChart.Data(String.valueOf(i), red[i]));
                seriesGreen.getData().add(new XYChart.Data(String.valueOf(i), green[i]));
                seriesBlue.getData().add(new XYChart.Data(String.valueOf(i), blue[i]));
            }

            success = true;
        }

        public static Map<String, Long> sortMapByValue(Map<String, Long> map) {
            List<Map.Entry<String, Long>> entryList = new ArrayList<>(map.entrySet());

            entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            Map<String, Long> sortedMap = new LinkedHashMap<>();

            for (Map.Entry<String, Long> entry : entryList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            return sortedMap;
        }

        public Map<String, Long> getMaximmum() {

            Arrays.sort(red);
            Arrays.sort(green);
            Arrays.sort(blue);
            Long redMax = red[255];
            Long blueMax = blue[255];
            Long greenMax = green[255];
            Map<String, Long> map = new HashMap<String, Long>();
            map.put("red", redMax);
            map.put("blue", blueMax);
            map.put("green", greenMax);
            Map<String, Long> sortedMap = sortMapByValue(map);
            return sortedMap;
        }

        public boolean isSuccess() {
            return success;
        }

        public XYChart.Series getSeriesAlpha() {
            return seriesAlpha;
        }

        public XYChart.Series getSeriesRed() {
            return seriesRed;
        }

        public XYChart.Series getSeriesGreen() {
            return seriesGreen;
        }

        public XYChart.Series getSeriesBlue() {
            return seriesBlue;
        }
    }
}