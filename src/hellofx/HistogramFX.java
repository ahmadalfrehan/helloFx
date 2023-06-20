package hellofx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.List;

import hellofx.EnterImage.SameImageModel;

public class HistogramFX extends Application {
    private List<SameImageModel> images;

    HistogramFX(List<SameImageModel> imags) {
        this.images = imags;
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // Populate the GridPane with images
        int row = 0;
        int col = 0;
        for (SameImageModel image : images) {
            ImageView imageView = new ImageView(new Image(image.filePath));
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            gridPane.add(imageView, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
        Scene scene1 = new Scene(gridPane, 800, 600);
        Stage secondaryStage = new Stage();
        secondaryStage.setScene(scene1);
        secondaryStage.setTitle("Image Grid");

        secondaryStage.show();
    }

    private boolean isImageFile(Path path) {
        String fileName = path.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.matches("png|jpe?g|gif|bmp"); // Modify the regex pattern as needed
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.scene.Scene;
// import javafx.scene.chart.BarChart;
// import javafx.scene.chart.CategoryAxis;
// import javafx.scene.chart.NumberAxis;
// import javafx.scene.chart.XYChart;
// import javafx.scene.control.CheckBox;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
// import javafx.stage.Stage;

// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import javax.imageio.ImageIO;

// public class HistogramFX extends Application {

// private static final int BINS = 256;
// private BufferedImage image;
// private XYChart.Series<String, Number> redSeries;
// private XYChart.Series<String, Number> greenSeries;
// private XYChart.Series<String, Number> blueSeries;

// @Override
// public void start(Stage primaryStage) {
// primaryStage.setTitle("Histogram");

// // Load image using BufferedImage
// try {
// image = ImageIO.read(new
// File("C:/users/dell/desktop/flutter/ahmadalfrehan.png"));
// } catch (IOException e) {
// e.printStackTrace();
// return;
// }

// // Create the chart axes
// CategoryAxis xAxis = new CategoryAxis();
// NumberAxis yAxis = new NumberAxis();
// xAxis.setLabel("Value");
// yAxis.setLabel("Count");

// // Create the bar chart
// BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
// barChart.setTitle("Histogram");

// // Create the series
// redSeries = new XYChart.Series<>();
// greenSeries = new XYChart.Series<>();
// blueSeries = new XYChart.Series<>();

// // Calculate the histogram for each color component
// int[] redHistogram = getHistogram(image, 0);
// int[] greenHistogram = getHistogram(image, 1);
// int[] blueHistogram = getHistogram(image, 2);

// // Add data to the series
// for (int i = 0; i < BINS; i++) {
// redSeries.getData().add(new XYChart.Data<>(String.valueOf(i),
// redHistogram[i]));
// greenSeries.getData().add(new XYChart.Data<>(String.valueOf(i),
// greenHistogram[i]));
// blueSeries.getData().add(new XYChart.Data<>(String.valueOf(i),
// blueHistogram[i]));
// }

// // Create checkboxes for series visibility control
// CheckBox redCheckBox = createCheckBox("Red", redSeries, Color.RED);
// CheckBox greenCheckBox = createCheckBox("Green", greenSeries, Color.GREEN);
// CheckBox blueCheckBox = createCheckBox("Blue", blueSeries, Color.BLUE);

// // Create a VBox for the checkboxes
// VBox checkBoxesBox = new VBox(5, redCheckBox, greenCheckBox, blueCheckBox);
// checkBoxesBox.setPadding(new Insets(10));

// // Add the series to the chart
// barChart.getData().addAll(redSeries, greenSeries, blueSeries);

// // Create the root layout
// BorderPane root = new BorderPane();
// root.setCenter(barChart);
// root.setRight(checkBoxesBox);

// // Create the scene and set it to the stage
// Scene scene = new Scene(root, 800, 600);
// primaryStage.setScene(scene);
// primaryStage.show();
// }

// private int[] getHistogram(BufferedImage image, int component) {
// int[] histogram = new int[BINS];
// for (int i = 0; i < image.getWidth(); i++) {
// for (int j = 0; j < image.getHeight(); j++) {
// int color = image.getRGB(i, j);
// int value = 0;
// switch (component) {
// case 0: // Red component
// value = (color >> 16) & 0xFF;
// break;
// case 1: // Green component
// value = (color >> 8) & 0xFF;
// break;
// case 2: // Blue component
// value = color & 0xFF;
// break;
// }
// histogram[value]++;
// }
// }
// return histogram;
// }

// private CheckBox createCheckBox(String label, XYChart.Series<String, Number>
// series, Color color) {
// CheckBox checkBox = new CheckBox(label);
// checkBox.setSelected(true);
// series.getNode().setVisible(true);
// series.getNode().setStyle("-fx-bar-fill: " + toRGBCode(color) + ";");
// checkBox.selectedProperty().addListener((observable, oldValue, newValue) ->
// series.getNode().setVisible(newValue));
// return checkBox;
// }

// private String toRGBCode(Color color) {
// return String.format("#%02X%02X%02X",
// (int) (color.getRed() * 255),
// (int) (color.getGreen() * 255),
// (int) (color.getBlue() * 255));
// }

// public static void main(String[] args) {
// launch(args);
// }
// }
