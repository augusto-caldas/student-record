package assignment1.part2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.Optional;

public class studentRecord extends Application {
    /* Main Methods */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Creating Tab Pane
        TabPane mainTabPane = new TabPane();
        mainTabPane.setTabMinWidth(40);

        // Creating Tabs
        tabCreator myTabs = new tabCreator();

        // Adding tabs to pane
        mainTabPane.getTabs().add(myTabs.getStudentTab());
        mainTabPane.getTabs().add(myTabs.getModuleTab());
        mainTabPane.getTabs().add(myTabs.getReviewTab());

        // Setting stage scene
        stage.setTitle("MTU Student Record System");
        stage.getIcons().add(new Image("/icon.png"));
        StackPane layout = new StackPane();
        layout.getChildren().add(mainTabPane);
        stage.setScene(new Scene(layout, 360, 500));
        stage.setMinWidth(350);
        stage.setMinHeight(400);

        // Stage close handler
        stage.setOnCloseRequest(event -> {
            Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            closeAlert.setTitle("Exit Program");
            closeAlert.setHeaderText("Confirm Exit");
            closeAlert.setContentText("Unsaved information will be lost\nExit Program?");
            Optional<ButtonType> request = closeAlert.showAndWait();
            if (request.isPresent() && (request.get() == ButtonType.OK)){
                Platform.exit();
            } else {
                event.consume();
            }
        });

        // Start stage
        stage.show();
    }

}