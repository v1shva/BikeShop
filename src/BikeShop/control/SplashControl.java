package BikeShop.control;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class SplashControl  implements Initializable {

    @FXML
    private Button splashCancel;
    @FXML
    private ChoiceBox languageChoice;
    private ChangeListener listener;
    private Locale locale;
    private ResourceBundle rb;
    private static String languageChoiceVal;

    @FXML private void splashCancelAction(){
        if(!languageChoice.getValue().equals("English")){
            System.out.println("hre");
            locale = new Locale("sin");
            rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
        }
        ButtonType okay = new ButtonType(rb.getString("okay"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(rb.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        // get a handle to the stage
        Alert alert = new Alert(AlertType.CONFIRMATION,rb.getString("sure?"),okay,cancel);
        alert.setTitle(rb.getString("cancelLogin"));
        alert.setHeaderText(rb.getString("aboutToCancel"));
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().getButtonData()== ButtonBar.ButtonData.OK_DONE){
            System.exit(0);
        }
    }

    @FXML private void changeLanguage(){
        languageChoice.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rb = ResourceBundle.getBundle("BikeShop/Localization/language");
        languageChoiceVal = "Sin";
        listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (languageChoice.getValue().equals("English") ||languageChoice.getValue().equals("ඉංග්‍රීසි")) {
                    rb = ResourceBundle.getBundle("BikeShop/Localization/language");
                    languageChoiceVal = "Eng";
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("../fxml/SplashGUI.fxml"),rb);
                        Scene scene = languageChoice.getScene();
                        scene.setRoot(root);
                        ChoiceBox choiceBox = (ChoiceBox) scene.lookup("#languageChoice");
                        choiceBox.setItems(FXCollections.observableArrayList(
                                "English",
                                new Separator(), "Sinhala" )
                        );
                        choiceBox.getSelectionModel().selectFirst();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (languageChoice.getValue().equals("Sinhala")) {
                    locale = new Locale("sin");
                    rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
                    languageChoiceVal = "Sin";
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("../fxml/SplashGUI.fxml"),rb);
                        Scene scene = languageChoice.getScene();
                        scene.setRoot(root);
                        ChoiceBox choiceBox = (ChoiceBox) scene.lookup("#languageChoice");
                        choiceBox.setItems(FXCollections.observableArrayList(
                                "ඉංග්‍රීසි",
                                new Separator(), "සිංහල" )
                        );
                        choiceBox.getSelectionModel().selectLast();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(languageChoice.getValue());
            }
        };
    }

    @FXML
    private Button loginBtn;
    @FXML
    private TextField userNameInput;
    @FXML
    private TextField passInput;
    @FXML private void authUser() throws IOException {
        String userName = userNameInput.getText();
        String pass = passInput.getText();

        PasswordStorage.verifyPassword(pass,dbPass);

        //After authentication
        if(!languageChoice.getValue().equals("English")){
            System.out.println("here");
            locale = new Locale("sin");
            rb = ResourceBundle.getBundle("BikeShop/Localization/Main", locale);
        }
        else{
            rb = ResourceBundle.getBundle("BikeShop/Localization/Main");
        }
        Stage primaryStage = (Stage) passInput.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/Main.fxml"), rb);
        primaryStage.setTitle("BikeShop");
        Scene scene = new Scene(root);
        BorderPane borderPane = (BorderPane) scene.lookup("#mainBorderPane");
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}