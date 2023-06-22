package hellofx.imported;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    Search search = new Search();
    Cutpart cutpart = new Cutpart();
    ColorSearchApp colorSearch = new ColorSearchApp();
    ImageFilterApp imageFilter = new ImageFilterApp();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Button button1 = new Button("Search for similar images by selected image");
        button1.setOnAction(e -> {
            Platform.runLater(() -> {
                Stage searchStage = new Stage();
                search.start(searchStage);
            });
        });

        Button button2 = new Button("Search for similar images by cropped image");
        button2.setOnAction(e -> {
            Platform.runLater(() -> {
                Stage CutpartStage = new Stage();
                cutpart.start(CutpartStage);
            });
        });

        Button button3 = new Button("Search for similar images by color ");
        button3.setOnAction(e -> {
            Platform.runLater(() -> {
                Stage ColorSearchStage = new Stage();
                colorSearch.start(ColorSearchStage);
            });
        });

        Button button4 = new Button("Search for a photo by date and size ");
        button4.setOnAction(e -> {
            Platform.runLater(() -> {
                Stage imageFilterStage = new Stage();
                imageFilter.start(imageFilterStage);
            });
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(button1, button2, button3, button4);

        Scene scene = new Scene(root, 500, 450);
        scene.getStylesheets().add("styles.css"); // تحتاج إلى ملف CSS بنفس المسار الذي تم تعيينه هنا

        primaryStage.setTitle("Button Interface");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
