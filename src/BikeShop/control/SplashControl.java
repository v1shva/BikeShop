package BikeShop.control;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.swing.*;
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
    private Session session;
    private static String languageChoiceVal;

    public void setSession(Session current){
        session = current;
    }

    @FXML private void splashCancelAction(){
        if(!languageChoice.getValue().equals("English")){
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
                        Parent root = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/SplashGUI.fxml"),rb);
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
                        Parent root = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/SplashGUI.fxml"),rb);
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
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override public Void call() {
                String userName = userNameInput.getText();
                String pass = passInput.getText();
                String dbPass = "";

                Transaction tx = null;
                try{
                    tx = session.beginTransaction();
                    Query query = session.createQuery("select password,name,userLevel,tax from UsersEntity where username='"+userName+"'");
                    if(query.list().size()==1){
                        Object[] tuple = (Object[]) query.list().get(0);
                        dbPass = (String) tuple[0];
                        String name = "Name: " + tuple[1];
                        String userLevel = (String) tuple[2];
                        Byte tax = (Byte) tuple[3];
                        try {
                            if (PasswordStorage.verifyPassword(pass,dbPass)){

                                Platform.runLater(() -> {
                                    String lan = "en";
                                    if(!languageChoice.getValue().equals("English")){
                                        System.out.println(languageChoice.getValue());
                                        locale = new Locale("sin");
                                        lan = "sin";
                                        rb = ResourceBundle.getBundle("BikeShop/Localization/Main", locale);
                                    }
                                    else{
                                        rb = ResourceBundle.getBundle("BikeShop/Localization/Main");
                                        lan = "en";
                                    }

                                    Stage primaryStage = (Stage) passInput.getScene().getWindow();
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/Main.fxml"), rb);
                                    Parent root = null;
                                    try {
                                        root = (Parent)fxmlLoader.load();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    MainControl controller = fxmlLoader.<MainControl>getController();
                                    controller.SetValues(name,lan,userLevel, session, tax);
                                    primaryStage.setTitle("BikeShop");
                                    Scene scene = new Scene(root);
                                    BorderPane borderPane = (BorderPane) scene.lookup("#mainBorderPane");
                                    borderPane.prefHeightProperty().bind(scene.heightProperty());
                                    borderPane.prefWidthProperty().bind(scene.widthProperty());
                                    primaryStage.setScene(scene);
                                    primaryStage.show();
                                    primaryStage.setMaximized(true);
                                    alertL.dispose();
                                });


                            }
                            else{
                                Platform.runLater(() -> {
                                    alertL.dispose();
                                    Alert alert = new Alert(AlertType.ERROR);
                                    alert.setTitle("Login Failed");
                                    alert.setHeaderText("Username or password is incorrect");
                                    alert.showAndWait();
                                });
                            }
                        } catch (PasswordStorage.CannotPerformOperationException e) {
                            e.printStackTrace();
                        } catch (PasswordStorage.InvalidHashException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Platform.runLater(() -> {
                            alertL.dispose();
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Login Failed");
                            alert.setHeaderText("Username or password is incorrect");
                            alert.showAndWait();
                        });
                    }

                }catch (HibernateException e) {
                    if (tx!=null) tx.rollback();
                    e.printStackTrace();
                }finally {
                    tx.commit();
                }

                return null;
            }
        };
        new Thread(task).start();




        //After authentication

    }


}