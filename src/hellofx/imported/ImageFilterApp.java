/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 package hellofx.imported;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImageFilterApp extends Application {

    private File selectedFolder;
    private List<File> filteredImages;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Filter");

        // Create UI controls
        Label folderLabel = new Label("Select a folder:");
        Button folderButton = new Button("Choose Folder");
        Label dateLabel = new Label("Enter a date:");
        DatePicker datePicker = new DatePicker();
        Label sizeLabel = new Label("Enter a size:");
        ComboBox<String> sizeComboBox = new ComboBox<>(FXCollections.observableArrayList("Small", "Medium", "Large"));
        Button filterButton = new Button("Filter");
        ListView<File> imageListView = new ListView<>();

        // Set event handlers
        folderButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            selectedFolder = directoryChooser.showDialog(primaryStage);
        });

        filterButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            String selectedSize = sizeComboBox.getValue();
            filteredImages = filterImages(selectedFolder, selectedDate, selectedSize);
            imageListView.setItems(FXCollections.observableArrayList(filteredImages));
        });

        imageListView.setCellFactory(param -> new ListCell<File>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                setGraphic(imageView);
            }

            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    imageView.setImage(null);
                } else {
                    imageView.setImage(new Image(item.toURI().toString()));
                }
            }
        });

        // Set up the layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(folderLabel, 0, 0);
        gridPane.add(folderButton, 1, 0);
        gridPane.add(dateLabel, 0, 1);
        gridPane.add(datePicker, 1, 1);
        gridPane.add(sizeLabel, 0, 2);
        gridPane.add(sizeComboBox, 1, 2);
        gridPane.add(filterButton, 0, 3);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(gridPane, imageListView);

        Scene scene = new Scene(vBox, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<File> filterImages(File folder, LocalDate date, String size) {
        List<File> filteredImages = new ArrayList<>();

        if (folder != null && date != null && size != null) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isImageFile(file) && hasMatchingDate(file, date) && hasMatchingSize(file, size)) {
                        filteredImages.add(file);
                    }
                }
            }
        }

        return filteredImages;
    }

    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    private boolean hasMatchingDate(File file, LocalDate date) {
        long fileTimestamp = file.lastModified();
        LocalDate fileDate = LocalDate.ofEpochDay(fileTimestamp / (24 * 60 * 60 * 1000));
        return fileDate.equals(date);
    }

    private boolean hasMatchingSize(File file, String size) {
        long fileSize = file.length();
        switch (size) {
            case "Small":
                return fileSize < 1024 * 1024; // 1 MB
            case "Medium":
                return fileSize >= 1024 * 1024 && fileSize < 10 * 1024 * 1024; // 1 MB - 10 MB
            case "Large":
                return fileSize >= 10 * 1024 * 1024; // 10 MB
            default:
                return false;
        }
    }
}
