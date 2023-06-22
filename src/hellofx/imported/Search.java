package hellofx.imported;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class Search extends Application {
    
    private ImageView imageView;
    private Color targetColor ;
    private List<Image> similarImages = new ArrayList<>();
    List<File> selectedDirectories;
    private ObservableList<Image> imagesList = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        imageView = new ImageView();
        selectedDirectories = new ArrayList<>();
        Button selectImageButton = new Button("Select Image");
        Button selectFoldersButton = new Button("Select Folders");
        Button displayImagesButton = new Button("Display Images");
        ListView<File> foldersListView = new ListView<>();
        ListView<Image> imagesListView = new ListView<>();
        
        
        selectImageButton.setOnAction(e -> {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                BufferedImage bufferedImage1 = reduceimagesize(bufferedImage);//هنا عم اخد الصورة الى اخترتها و اقةم بتعديل حجمها
                targetColor = calculateAverageColor(bufferedImage1);
                Image image = SwingFXUtils.toFXImage(bufferedImage1, null);

                double desiredWidth = 200;
                double desiredHeight = 200;
                imageView.setFitWidth(desiredWidth);
                imageView.setFitHeight(desiredHeight);
                imageView.setImage(image);
                //Color targetColor = calculateAverageColor(bufferedImage);
            } catch (IOException ex) {
                Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        });

        selectFoldersButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folders");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory != null) {
                foldersListView.getItems().add(selectedDirectory);
//                List<File> selectedDirectories = new ArrayList<>();
                selectedDirectories.add(selectedDirectory);

                File[] imageFiles = selectedDirectory.listFiles((dir, name) ->
                        name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

                if (imageFiles != null) {
                    for (File file : imageFiles) {
                        Image image = new Image(file.toURI().toString());
                        imagesList.add(image);
                    }
                }
            }
        });

        displayImagesButton.setOnAction(e -> {
            searchSimilarImages(targetColor, selectedDirectories);
            displaySimilarImages();
//            imagesListView.setItems(imagesList);
        });
//
//        foldersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                displayImagesButton.setDisable(false);
//            } else {
//                displayImagesButton.setDisable(true);
//            }
//        });

//        imagesListView.setCellFactory(param -> new ImageViewListCell());

//        BorderPane root = new BorderPane();
//        root.setTop(selectFoldersButton);
//        root.setLeft(foldersListView);
//        root.setCenter(imagesListView);
//        root.setBottom(displayImagesButton);
        
        HBox imageBox = new HBox(imageView,foldersListView);
        imageBox.setSpacing(10);

        HBox buttonBox = new HBox( selectImageButton,selectFoldersButton,displayImagesButton) ;
        buttonBox.setSpacing(200);

        VBox root = new VBox(buttonBox,imageBox );
        
        VBox.setMargin(root, new Insets(10, 10, 10, 10));
        root.setPadding(new Insets(25, 25, 25, 25));
        
        Scene scene = new Scene(root);
        primaryStage.setTitle("Multiple Folder Image Display");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private BufferedImage reduceimagesize(BufferedImage image) throws IOException{
        try{
      int newWidth = 300; // desired width in pixels
        int newHeight = 200; // desired height in pixels
        // Create a scaled version of the input image
        java.awt.Image scaledImage = image.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
        // Create a BufferedImage object with the desired size
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        // Draw the scaled image on the BufferedImage
        Graphics2D graphics = outputImage.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();
        
        // Write the resized image to the output file
        System.out.println("Image resized successfully.");
        return outputImage;
        }
       catch (Exception e) {
        System.out.println("Error while resizing the image: " + e.getMessage());
        IOException ioException = new IOException("Error while resizing the image.");
        ioException.initCause(e);
        throw ioException;
    }
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
        
    private void searchSimilarImages(Color targetColor, List<File> searchDirectories) {
        similarImages.clear();

        for (File searchDirectory : searchDirectories) {
            File[] files = searchDirectory.listFiles();
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
        }
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
    
        private void displaySimilarImages() {
        Stage stage = new Stage();
        stage.setTitle("Similar Images");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);

        for (Image image : similarImages) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setFitHeight(200);
            root.getChildren().add(imageView);
        }

        stage.setScene(new Scene(scrollPane, 1000, 800));
        stage.show();
    }

    private static class ImageViewListCell extends javafx.scene.control.ListCell<Image> {
        private ImageView imageView = new ImageView();

        @Override
        protected void updateItem(Image item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                imageView.setImage(item);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                setGraphic(imageView);
            }
        }
    }
}
