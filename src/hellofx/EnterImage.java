
package hellofx;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.scene.text.Text.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EnterImage extends Application {
    private Image image;
    private BufferedImage saveImage;
    private ImageView imageView;
    private Button chooseImageButton;
    private Button octreeButton;
    private Button kmeansButton;
    private Button indexedButton;
    private Button mediancutButton;
    private Button selectColor;
    private Button saveButton;
    private Button showHistogramButton;
    private Button showImageColorPalette;

    private String selectedAlgorithm;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Enter Image to search");

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        chooseImageButton = new Button("Choose Image");
        selectColor = new Button("select Color");
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

		// create a tile pane
		TilePane r = new TilePane();

		// create a label
		Label l = new Label("This is a color picker example ");
		Label l1 = new Label("no selected color ");

		// create a color picker
		ColorPicker cp = new ColorPicker();

		// create a event handler
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e)
			{
				// color
				Color c = cp.getValue();

				// set text of the label to RGB value of color
				l1.setText("Red = " + c.getRed() + ", Green = " + c.getGreen()
												+ ", Blue = " + c.getBlue());
			}
		};

		// set listener
		cp.setOnAction(event);

		// add label
		r.getChildren().add(l);
		r.getChildren().add(cp);
		r.getChildren().add(l1);

		// create a scene
		// Scene sc = new Scene(r, 500, 200);

		// // set the scene
		// primaryStage.setScene(sc);

		// // s.show();
        // ColorPicker cp = new ColorPicker(Color.BLUE);
        selectColor.setOnAction(e -> {
            // cp.
            Color c = cp.getValue();
            // CustomColorDialog dialog = new CustomColorDialog(primaryStage.getOwner());
            // dialog.show();

        });
        // r.getChildren(cp);
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
                // MedianCut medianCut  = new MedianCut();
                // medianCut.

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

        VBox buttonsVBox = new VBox(10, chooseImageButton, selectColor, octreeButton, kmeansButton, indexedButton,
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

