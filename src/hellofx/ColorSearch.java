package hellofx;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ColorSearch extends Application {

    private List<Color> selectedColors;
    private File folder;
    private TextField folderNameField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Color Search App");

        // Create text field for color input
        TextField colorField = new TextField();
        colorField.setPromptText("Enter colors in HEX format (e.g., #FF0000, #00FF00, #0000FF)");

        // Create button to select folder
        Button folderButton = new Button("Select Folder");

        // Create button to search for images
        Button searchButton = new Button("Search");

        // Create container for color field and buttons
        HBox controlsContainer = new HBox(10);
        controlsContainer.setAlignment(Pos.CENTER);
        controlsContainer.getChildren().addAll(colorField, folderButton, searchButton);

        // Create container for displaying folder name
        folderNameField = new TextField();
        folderNameField.setEditable(false);
        folderNameField.setPromptText("No folder selected");

        // Create container for displaying images
        GridPane imagesContainer = new GridPane();
        imagesContainer.setAlignment(Pos.CENTER);
        imagesContainer.setPadding(new Insets(10));
        imagesContainer.setHgap(10);
        imagesContainer.setVgap(10);

        // Create scroll pane and set its content to the images container
        ScrollPane scrollPane = new ScrollPane(imagesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Create main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(controlsContainer, folderNameField, scrollPane);

        // Create scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add event handler for folder selection button
        folderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select Folder");
                File selectedFolder = directoryChooser.showDialog(primaryStage);
                if (selectedFolder != null && selectedFolder.isDirectory()) {
                    folder = selectedFolder;
                    folderNameField.setText(selectedFolder.getName());
                }
            }
        });

    
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchImages(colorField.getText(), imagesContainer);
            }
        });
    }

    private void searchImages(String colorsInput, GridPane imagesContainer) {
        if (folder == null || !folder.isDirectory()) {
            return;
        }

        selectedColors = parseColors(colorsInput);

        imagesContainer.getChildren().clear();

        List<File> imageFiles = listImageFiles(folder);
        List<File> filteredImages;

        if (selectedColors.size() > 1) {
            filteredImages = filterImagesByColors(imageFiles, selectedColors);
        } else if (selectedColors.size() == 1) {
            filteredImages = filterImagesByColor(imageFiles, selectedColors.get(0));
        } else {
            filteredImages = new ArrayList<>();
        }

        displayImages(filteredImages, imagesContainer);
    }

    private List<Color> parseColors(String colorsInput) {
        List<Color> colors = new ArrayList<>();
        String[] colorStrings = colorsInput.split(",");
        for (String colorString : colorStrings) {
            Color color = parseColor(colorString.trim());
            if (color != null) {
                colors.add(color);
            }
        }
        return colors;
    }

    private Color parseColor(String colorString) {
        try {
            return Color.web(colorString);
        } catch (Exception e) {
            return null;
        }
    }

    private List<File> listImageFiles(File folder) {
        File[] files = folder.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }

        List<File> imageFiles = new ArrayList<>();
        for (File file : files) {
            if (file.isFile() && isImageFile(file)) {
                imageFiles.add(file);
            }
        }
        return imageFiles;
    }

    private boolean isImageFile(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex >= 0 && lastDotIndex < name.length() - 1) {
            String extension = name.substring(lastDotIndex + 1).toLowerCase();
            return extension.matches("png|jpe?g|gif|bmp");
        }
        return false;
    }

    private List<File> filterImagesByColors(List<File> imageFiles, List<Color> selectedColors) {
        List<File> filteredImages = new ArrayList<>();

        for (File file : imageFiles) {
            Image image = new Image(file.toURI().toString());
            if (containsCombinedColors(image, selectedColors)) {
                filteredImages.add(file);
            }
        }

        return filteredImages;
    }

    private List<File> filterImagesByColor(List<File> imageFiles, Color selectedColor) {
        List<File> filteredImages = new ArrayList<>();

        for (File file : imageFiles) {
            Image image = new Image(file.toURI().toString());
            if (containsColor(image, selectedColor)) {
                filteredImages.add(file);
            }
        }

        return filteredImages;
    }

    private boolean containsCombinedColors(Image image, List<Color> selectedColors) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        boolean[] colorMatches = new boolean[selectedColors.size()];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = image.getPixelReader().getColor(x, y);
                for (int i = 0; i < selectedColors.size(); i++) {
                    if (!colorMatches[i] && colorMatches(selectedColors.get(i), pixelColor)) {
                        colorMatches[i] = true;
                        break;
                    }
                }

                boolean allColorsMatch = true;
                for (boolean match : colorMatches) {
                    if (!match) {
                        allColorsMatch = false;
                        break;
                    }
                }

                if (allColorsMatch) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean containsColor(Image image, Color selectedColor) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = image.getPixelReader().getColor(x, y);
                if (colorMatches(selectedColor, pixelColor)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean colorMatches(Color color1, Color color2) {
        double threshold = 0.05;
        return Math.abs(color1.getRed() - color2.getRed()) < threshold &&
                Math.abs(color1.getGreen() - color2.getGreen()) < threshold &&
                Math.abs(color1.getBlue() - color2.getBlue()) < threshold;
    }

    private void displayImages(List<File> imageFiles, GridPane imagesContainer) {
        int columnCount = 4;
        int rowCount = (int) Math.ceil((double) imageFiles.size() / columnCount);

        int row = 0;
        int column = 0;

        for (File file : imageFiles) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imagesContainer.add(imageView, column, row);

            column++;
            if (column >= columnCount) {
                column = 0;
                row++;
            }
        }
    }
}
