package hellofx;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Saveimage extends Application {
    private Image image;
    private BufferedImage saveImage;
    private ImageView imageView;
    private Button chooseImageButton;
    private Button octreeButton;
    private Button kmeansButton;
    private Button indexedButton;
    private Button mediancutButton;
    private Button saveButton;
    private Button showHistogramButton;
    private Button showImageColorPalette;

    // Variable to track the selected algorithm
    private String selectedAlgorithm;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Loader");

        // Create components
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        chooseImageButton = new Button("Choose Image");
        octreeButton = new Button("Octree");
        kmeansButton = new Button("K-Means");
        indexedButton = new Button("Indexed");
        mediancutButton = new Button("Median Cut");
        saveButton = new Button("Save");
        showHistogramButton = new Button("Show Color Histogram");
        showImageColorPalette = new Button("Show Image Color Palette");

        // Configure file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png"));

        // Add action listener to choose image button
        chooseImageButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Add action listener to octree button
        octreeButton.setOnAction(e -> {
            selectedAlgorithm = "Octree"; // Set the selected algorithm
            if (image != null) {
                BufferedImage outputImage = OctreeQuantizer.quantize(SwingFXUtils.fromFXImage(image, null));
                saveImage = outputImage;
                imageView.setImage(SwingFXUtils.toFXImage(outputImage, null));
            }
        });

        // Add action listener to indexed button
        indexedButton.setOnAction(e -> {
            selectedAlgorithm = "Indexed"; // Set the selected algorithm
            if (image != null) {
                BufferedImage outputImage = ToIndexedImage
                        .rgbaToIndexedBufferedImage(SwingFXUtils.fromFXImage(image, null));
                saveImage = outputImage;
                imageView.setImage(SwingFXUtils.toFXImage(outputImage, null));
            }
        });

        // Add action listener to k-means button
        kmeansButton.setOnAction(e -> {
            selectedAlgorithm = "K-Means"; // Set the selected algorithm
            if (image != null) {
                // Implement the K-Means algorithm logic here
                // ...

                // Example code for setting the saveImage variable
                // saveImage = ...;

                imageView.setImage(SwingFXUtils.toFXImage(saveImage, null));
            }
        });

        // Add action listener to median cut button
        mediancutButton.setOnAction(e -> {
            selectedAlgorithm = "Median Cut"; // Set the selected algorithm
            if (image != null) {
                // Implement the Median Cut algorithm logic here
                // ...

                // Example code for setting the saveImage variable
                // saveImage = ...;
                // Median.main();

                imageView.setImage(SwingFXUtils.toFXImage(saveImage, null));
            }
        });

        // Add action listener to save button
        saveButton.setOnAction(e -> {
            if (image != null && saveImage != null) {
                FileChooser saveFileChooser = new FileChooser();
                saveFileChooser.setInitialFileName(selectedAlgorithm + "_" + "image.jpg");
                saveFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Images", "*.jpg"));

                File selectedFile = saveFileChooser.showSaveDialog(primaryStage);
                if (selectedFile != null) {
                    try {
                        ImageIO.write(saveImage, "jpg", selectedFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Add components to the layout
        VBox buttonsVBox = new VBox(10, chooseImageButton, octreeButton, kmeansButton, indexedButton,
                mediancutButton,
                saveButton, showHistogramButton, showImageColorPalette);
        buttonsVBox.setAlignment(Pos.CENTER);
        buttonsVBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(imageView);
        root.setRight(buttonsVBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
