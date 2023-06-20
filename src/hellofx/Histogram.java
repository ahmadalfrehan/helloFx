package hellofx;

import java.awt.image.BufferedImage;
// import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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

    String defaultImage = "http://goo.gl/kYEQl";

    @Override
    public void start(Stage primaryStage) {

        Label labelInfo = new Label();
        labelInfo.setText(
                "java.version: " + System.getProperty("java.version") + "\n"
                        + "javafx.runtime.version: " + System.getProperty("javafx.runtime.version"));

        TextField textSrc = new TextField();
        textSrc.setText(defaultImage);
        Button btnDo = new Button("Do Histogram");
        ImageView imageView = new ImageView();
        // imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<String, Number> chartHistogram = new LineChart<>(xAxis, yAxis);
        chartHistogram.setCreateSymbols(false);
        Image image = new Image("C:/users/dell/desktop/flutter/ahmadalfrehan.png");

        btnDo.setOnAction((ActionEvent event) -> {
            imageView.setImage(image);
            chartHistogram.getData().clear();

            ImageHistogram imageHistogram = new ImageHistogram();
            if (imageHistogram.isSuccess()) {
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
            try {
                image = ImageIO.read(new File("C:/users/dell/desktop/flutter/ahmadalfrehan.png"));
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