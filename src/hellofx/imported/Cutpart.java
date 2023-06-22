package hellofx.imported;
import java.awt.Color;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ScrollPane;

public class Cutpart extends Application {
    private Image originalImage;
    private Image croppedImage;
    private Color targetColor ;
    private List<Image> similarImages = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Search App");

        // Create UI controls
        Label imageLabel = new Label("Selected Image:");
        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        Button selectImageButton = new Button("Select Image");
        selectImageButton.setOnAction(e -> selectImage(primaryStage, imageView));

        Label startXLabel = new Label("Start X:");
        TextField startXField = new TextField();
        startXField.setPrefWidth(50);

        Label startYLabel = new Label("Start Y:");
        TextField startYField = new TextField();
        startYField.setPrefWidth(50);

        Label widthLabel = new Label("Width:");
        TextField widthField = new TextField();
        widthField.setPrefWidth(50);

        Label heightLabel = new Label("Height:");
        TextField heightField = new TextField();
        heightField.setPrefWidth(50);

        Button cropButton = new Button("Crop Image");
        cropButton.setOnAction(e -> {
            int startX = Integer.parseInt(startXField.getText());
            int startY = Integer.parseInt(startYField.getText());
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            croppedImage = cropImage(startX, startY, width, height);
            imageView.setImage(croppedImage);
        });

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchSimilarImages(primaryStage));

        // Create layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(imageLabel, 0, 0);
        gridPane.add(imageView, 1, 0);
        gridPane.add(selectImageButton, 2, 0);

        gridPane.add(startXLabel, 0, 1);
        gridPane.add(startXField, 1, 1);
        gridPane.add(startYLabel, 2, 1);
        gridPane.add(startYField, 3, 1);

        gridPane.add(widthLabel, 0, 2);
        gridPane.add(widthField, 1, 2);
        gridPane.add(heightLabel, 2, 2);
        gridPane.add(heightField, 3, 2);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(cropButton, searchButton);
        gridPane.add(buttonBox, 0, 3, 4, 1);

        VBox vbox = new VBox(10);
        vbox.getChildren().add(gridPane);
        Scene scene = new Scene(vbox, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void selectImage(Stage primaryStage, ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                originalImage = new Image(selectedFile.toURI().toString());
                imageView.setImage(originalImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Image cropImage(int startX, int startY, int width, int height) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(originalImage, null);
        BufferedImage croppedBufferedImage = bufferedImage.getSubimage(startX, startY, width, height);
        targetColor = calculateAverageColor(croppedBufferedImage);
        try {
            File tempFile = File.createTempFile("cropped_image", ".png");
            ImageIO.write(croppedBufferedImage, "png", tempFile);
            return new Image(tempFile.toURI().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
            private Color calculateAverageColor(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                redSum += color.getRed();
                greenSum += color.getGreen();
                blueSum += color.getBlue();
            }
        }

        int totalPixels = width * height;
        int redAverage = redSum / totalPixels;
        int greenAverage = greenSum / totalPixels;
        int blueAverage = blueSum / totalPixels;

        return new Color(redAverage, greenAverage, blueAverage);
    }

    private void searchSimilarImages(Stage primaryStage) {
        if (croppedImage != null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory != null) {
                similarImages = findSimilarImages(targetColor,selectedDirectory);
                displaySimilarImages(primaryStage, similarImages);
            } else {
                System.out.println("No folder selected.");
            }
        } else {
            System.out.println("No image selected.");
        }
    }

    private List<Image> findSimilarImages(Color targetColor,File directory) {
        similarImages.clear();
                File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (isImageFile(file)) {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(file);
                        Color averageColor = calculateAverageColor(bufferedImage);

                        double colorDifference = getColorDifference(targetColor, averageColor);

                        if (colorDifference < 50) { // Adjust this threshold as needed
                            Image similarImage = SwingFXUtils.toFXImage(bufferedImage, null);
                            similarImages.add(similarImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // Add your code here to search for similar images within the specified directory based on the cropped image
        // This is just a sample implementation that returns a list of the original image repeated three times
//        similarImages.add(originalImage);
//        similarImages.add(originalImage);
//        similarImages.add(originalImage);

        return similarImages;
    }
        private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif");
    }
        
    private double getColorDifference(Color color1, Color color2) {
        int redDiff = color1.getRed() - color2.getRed();
        int greenDiff = color1.getGreen() - color2.getGreen();
        int blueDiff = color1.getBlue() - color2.getBlue();

        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }
        

    private void displaySimilarImages(Stage primaryStage, List<Image> images) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        for (Image image : images) {
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            vbox.getChildren().add(imageView);
        }

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane);
        Stage similarImagesStage = new Stage();
        similarImagesStage.setTitle("Similar Images");
        similarImagesStage.setScene(scene);
        similarImagesStage.show();
    }
}
