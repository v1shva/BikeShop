package BikeShop;

import BikeShop.control.Loader;
import BikeShop.control.SplashControl;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.hibernate.Session;

import javax.swing.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    Session session;

    @Override
    public void start(Stage primaryStage){

        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override public Void call() {
                session = HibernateInit.getSessionFactory().openSession();
                Platform.runLater(() -> {
                    alertL.dispose();
                    ResourceBundle bundle = ResourceBundle.getBundle("BikeShop/Localization/language");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/SplashGUI.fxml"),bundle);
                    try {
                        Parent root = (Parent)fxmlLoader.load();
                        SplashControl controller = fxmlLoader.<SplashControl>getController();
                        controller.setSession(session);
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                return null;
            }
        };
        new Thread(task).start();


    }


    public static void main(String[] args) {
        launch(args);

    }


}
