package hellofx;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
                ImageView imageView = new ImageView(image);
                StackPane root = new StackPane(imageView);
                Scene scene = new Scene(root, image.getWidth(), image.getHeight());
                imageWindow.setScene(scene);
                imageWindow.show();
            }
        });
        Scene sc = new Scene(stackPane, 500, 400);

        primaryStage.setScene(sc);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}