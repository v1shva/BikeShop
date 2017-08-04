package BikeShop;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ResourceBundle;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle bundle = ResourceBundle.getBundle("BikeShop/Localization/language");
        Parent root = FXMLLoader.load(getClass().getResource("fxml/SplashGUI.fxml"),bundle);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root, 600, 400);
        ChoiceBox choiceBox = (ChoiceBox) scene.lookup("#languageChoice");
        choiceBox.setItems(FXCollections.observableArrayList(
                "English",
                new Separator(), "Sinhala" )
        );
        choiceBox.getSelectionModel().selectFirst();
        primaryStage.setScene(scene);
        primaryStage.show();
        FadeTransition showSplash = new FadeTransition(Duration.millis(500),root);
        showSplash.setFromValue(0);
        showSplash.setToValue(1);
        showSplash.play();

    }


    public static void main(String[] args) {
        launch(args);

    }
}
