package BikeShop.control;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.io.IOException;

/*public class Loader {
    public Stage showLoading() throws IOException {
        Stage alertL = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/loading.fxml"));
        Scene sceneLoading = new Scene(root, 257, 75);
        alertL.initStyle(StageStyle.UNDECORATED);
        alertL.setScene(sceneLoading);
        alertL.initStyle(StageStyle.UTILITY);
        alertL.show();
        return alertL;
    }

}*/


public class Loader extends JFrame {


    public Loader() {

        // decoration
        setType(Type.UTILITY);
        setUndecorated(true);

        setSize(257, 75);

        toBack();

        // position
        // setLocation(100, 100);
        setLocationRelativeTo(null); // centers on screen
        setAlwaysOnTop(true);
        // frame operations
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // embed fx into swing
        JFXPanel fxPanel = new JFXPanel();

        Loader.this.getContentPane().add(fxPanel);
        try {
            fxPanel.setScene( createFxScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Loader.this.setVisible(true);

    }

    private Scene createFxScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/loading.fxml"));
        Scene sceneLoading = new Scene(root, 257, 75);
        return sceneLoading;
    }

    public static void main(String[] args) {
        new Loader();
    }

}