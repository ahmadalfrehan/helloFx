package hellofx;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ahmad Al_Frehan");
        Button b = new Button("Browse");
        StackPane stackPane = new StackPane();
        // StackPane root = new StackPane();
        stackPane.getChildren().add(b);
        b.setOnAction(e -> {
            // Create a FileChooser object
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

            // Show the file chooser dialog and get the selected file
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                // Create a new window to display the selected image
                Stage imageWindow = new Stage();
                Image image = new Image(selectedFile.toURI().toString());
                try {
                    quaImage(selectedFile, 0);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ImageView imageView = new ImageView(image);
                StackPane root = new StackPane(imageView);
                Scene scene = new Scene(root, 100, 100);
                imageWindow.setScene(scene);
                imageWindow.show();
            }
        });
        Scene sc = new Scene(stackPane, 500, 400);

        primaryStage.setScene(sc);

        primaryStage.show();
    }

    public static void quaImage(File image, int colorC) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(image);
        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
        System.out.println(fxImage);
        Image rgbImage = convertToRGB(fxImage);
        System.out.println(rgbImage);
        File output = new File("C:/Users/Dell/Desktop/output.png");
        BufferedImage bufferedRGBImage = SwingFXUtils.fromFXImage(rgbImage, null);
        ImageIO.write(bufferedRGBImage, "png", output);
    }

    public static Image convertToRGB(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);
                int red = (int) (color.getRed() * 255);
                int green = (int) (color.getGreen() * 255);
                int blue = (int) (color.getBlue() * 255);

                Color rgbColor = Color.rgb(red, green, blue);
                writableImage.getPixelWriter().setColor(x, y, rgbColor);
            }
        }

        return writableImage;
    }

    public static void main(String[] args) {
        MedianCut medianCut = new MedianCut();
        medianCut.main(args);
        // KMeans kMeans = new KMeans();
        // // KMeans.loadImage("C:/users/dell/desktop/m.png");
        // BufferedImage bufferedImage = kMeans.calculate(KMeans.loadImage("C:/users/dell/desktop/m.png"), 50, 3);
        // // BufferedImage bufferedImage = KMeans.loadImage("C:/users/dell/desktop/logo.png");
        // // kMeans.saveImage("aan2.png", bufferedImage);
        // Set<Integer> colors =new HashSet();
        // for (int i = 0; i < bufferedImage.getHeight(); i++){
        //     for (int j = 0; j < bufferedImage.getHeight(); j++) {
        //         colors.add(bufferedImage.getRGB(j, i));
        //     }
        // }
        // System.out.println(colors.size());
        // launch(args);
    }

}
