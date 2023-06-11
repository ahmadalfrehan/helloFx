package hellofx;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Histogram extends Application{ 
    public static int[] getHistogram(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] histogram = new int[256]; // Assuming 8-bit grayscale image

        // Iterate over each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Convert RGB to grayscale using the formula: 0.299R + 0.587G + 0.114B
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                // Increment the histogram bin for the corresponding grayscale value
                histogram[gray]++;
            }
        }

        return histogram;
    }

    public static void main(String[] args) {
        // BufferedImage image = null;
        // try {
        //     image = ImageIO.read(new File("aa.png"));
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // int[] histogram = getHistogram(image);

        // for (int i = 0; i < 256; i++) {
        //     System.out.println("Bin " + i + ": " + histogram[i]);
        // }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Histogram");

        // Load image using BufferedImage
        BufferedImage image;
        try {
            image = ImageIO.read(new File("C:/users/dell/desktop/flutter/ahmadalfrehan.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get histogram
        int[] histogram = getHistogram(image);
        int maxBinValue = 0;
        int maxBinIndex = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > maxBinValue) {
                maxBinValue = histogram[i];
                maxBinIndex = i;
            }
        }

        // Determine the highest color component based on the bin index
        String highestColorComponent;
        if (maxBinIndex == 0) {
            highestColorComponent = "Red";
        } else if (maxBinIndex == 1) { 
            highestColorComponent = "Green";
        } else {
            highestColorComponent = "Blue";
        }
        // Create the chart axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Color Component");
        yAxis.setLabel("Frequency");

        // Create the bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Image Histogram");

        // Create a series for the histogram data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Histogram");

        // Add data to the series
        // for (int i = 0; i < histogram.length; i++) {
        //     series.getData().add(new XYChart.Data<>(String.valueOf(i), histogram[i]));
        // }
        series.getData().add(new XYChart.Data<>("Red", histogram[0]));
        series.getData().add(new XYChart.Data<>("Green", histogram[1]));
        series.getData().add(new XYChart.Data<>("Blue", histogram[2]));

        // Add the series to the chart
        barChart.getData().add(series);

        // Create the scene and add the chart to it
        Scene scene = new Scene(barChart, 800, 600);

        // Show the stage
        primaryStage.setScene(scene);
        primaryStage.show(); 
    }

}
