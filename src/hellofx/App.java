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
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.stage.FileChooser;

public class App extends Application {
    @Override

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ahmad Al_Frehan");
        Text text = new Text("Hey please select Photo");

        Button b = new Button("Browse");

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(0, text);
        stackPane.getChildren().add(b);

        b.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
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
        String imagePath = "C:/users/dell/desktop/logo.png";

        Image inputImage = new Image(imagePath);
        Median median = new Median();

        Median.main(args);
        // BufferedImage buffereImage = SwingFXUtils.fromFXImage(inputImage, null);
        // // BufferedImage quantizedImage = median.quantize(buffereImage, 10);
        // // MedianCut.saveImage("C:/Users/User/Desktop/aan5.png", quantizedImage);
        // String outputFolderPath = "C:/Users/dell/Desktop/";
        // // Median.savedImage(quantizedImage, outputFolderPath, "image.png");

        // // Median.main(args);
        // MedianCut medianCut = new MedianCut();
        MedianCut.main(args);

        KMeans kMeans = new KMeans();
        KMeans.loadImage(imagePath);
        BufferedImage bufferedImage = kMeans.calculate(KMeans.loadImage(imagePath), 50, 3);
        KMeans.saveImage("aan2.png", bufferedImage);
        String outputFolderPath = "C:/Users/dell/Desktop/";
        KMeans.savedImage(bufferedImage,outputFolderPath, "output.png");
        BufferedImage sampleImg = null;
        try {
            sampleImg = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // medianCut.medianCutQuantize(sampleImg, 16);
        BufferedImage img = ToIndexedImage.rgbaToIndexedBufferedImage(bufferedImage);
        try {
            ImageIO.write(img, "png", new File("C:/Users/dell/Desktop/img"));

        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println(ToIndexedImage.rgbaToIndexedBufferedImage(bufferedImage));
        BufferedImage sampleImage2 = null;
        try {
            sampleImage2 = ImageIO.read(new File("C:/Users/dell/Desktop/girl.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        OctreeQuantizer octreeQuantizer = new OctreeQuantizer();
        BufferedImage out = octreeQuantizer.quantize(sampleImage2);
        File outputImageFile = new File(System.getProperty("user.home") + "/Desktop/output5.png");
        try {
            ImageIO.write(out, "png", outputImageFile);
            System.out.println("image saved successfully: " + outputImageFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to save image: " + e.getMessage());
        }
        launch(args);
    }

}
