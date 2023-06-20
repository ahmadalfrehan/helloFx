
package hellofx;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class EnterImage extends Application {
    private Image image;
    private BufferedImage saveImage;
    private ImageView imageView;
    private Button chooseImageButton;
    private Button octreeButton;
    private Button kmeansButton;
    private Button indexedButton;
    private Button mediancutButton;
    private Button selectFolder;
    private Button saveButton;
    private Button showHistogramButton;
    private Button showImageColorPalette;

    private String selectedAlgorithm;

    public static void main(String[] args) {
        launch(args);
    }

    private boolean isImageFile(Path path) {
        String fileName = path.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.matches("png|jpe?g|gif|bmp"); // Modify the regex pattern as needed
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Enter Image to search");

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        chooseImageButton = new Button("Choose Image");
        selectFolder = new Button("select Folder");
        octreeButton = new Button("Octree");
        kmeansButton = new Button("K-Means");
        indexedButton = new Button("Indexed");
        mediancutButton = new Button("Median Cut");
        saveButton = new Button("Save");
        showHistogramButton = new Button("Show Color Histogram");
        showImageColorPalette = new Button("Show Image Color Palette");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png"));

        primaryStage.setTitle("creating color picker");

        TilePane r = new TilePane();

        Label l = new Label("This is a color picker example ");
        Label l1 = new Label("no selected color ");

        ColorPicker cp = new ColorPicker();

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Color c = cp.getValue();
                l1.setText("Red = " + c.getRed() + ", Green = " + c.getGreen()
                        + ", Blue = " + c.getBlue());
            }
        };

        cp.setOnAction(event);
        r.getChildren().add(l);
        r.getChildren().add(cp);
        r.getChildren().add(l1);
        selectFolder.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                try {
                    List<File> imageFiles = Files.walk(selectedDirectory.toPath())
                            .filter(p -> isImageFile(p))
                            .map(Path::toFile)
                            .collect(Collectors.toList());

                    for (File imageFile : imageFiles) {
                        Image image = new Image(imageFile.toURI().toString());
                        GetHistogram.imagePath = image.getUrl().replaceAll("%20", "");
                        System.out.println(image.getUrl().replaceAll("%20", ""));
                        GetHistogram getHistogram = new GetHistogram();
                        System.out.println(getHistogram.getMaximmum());
                        System.out.println("Image file: " + imageFile.getName());
                    }
                } catch (IOException error) {
                    error.printStackTrace();
                }
            } else {
                System.out.println(selectedDirectory.getAbsolutePath());
            }
        });

        chooseImageButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                    Histogram.imagePath = image.getUrl();
                    Histogram histogram = new Histogram();
                    histogram.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        octreeButton.setOnAction(e -> {
            selectedAlgorithm = "Octree";
            if (image != null) {
                BufferedImage outputImage = OctreeQuantizer.quantize(SwingFXUtils.fromFXImage(image, null));
                saveImage = outputImage;
                imageView.setImage(SwingFXUtils.toFXImage(outputImage, null));
            }
        });

        indexedButton.setOnAction(e -> {
            selectedAlgorithm = "Indexed";
            if (image != null) {
                BufferedImage outputImage = ToIndexedImage
                        .rgbaToIndexedBufferedImage(SwingFXUtils.fromFXImage(image, null));
                saveImage = outputImage;
                imageView.setImage(SwingFXUtils.toFXImage(outputImage, null));
            }
        });

        kmeansButton.setOnAction(e -> {
            selectedAlgorithm = "K-Means";
            if (image != null) {

                imageView.setImage(SwingFXUtils.toFXImage(saveImage, null));
            }
        });

        mediancutButton.setOnAction(e -> {
            selectedAlgorithm = "Median Cut";
            if (image != null) {
                imageView.setImage(SwingFXUtils.toFXImage(saveImage, null));
            }
        });

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

        VBox buttonsVBox = new VBox(10, chooseImageButton, selectFolder, octreeButton, kmeansButton, indexedButton,
                mediancutButton,
                saveButton, showHistogramButton, showImageColorPalette);
        buttonsVBox.setAlignment(Pos.CENTER);
        buttonsVBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(imageView);
        root.setRight(buttonsVBox);
        root.setBottom(r);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
