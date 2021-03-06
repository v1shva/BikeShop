package BikeShop.control;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.Session;

import javax.swing.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vishva on 3/8/2017.
 */
public class MainControl {
    private String language = "en";
    private String userLevel = "";
    private Byte tax = 0;
    Session session;
    @FXML
    AnchorPane mainPane;
    @FXML Menu adminMenu;
    @FXML MenuItem showReportMenu;
    @FXML
    private Button purchaseButton, leaseButton, reportBtn;
    @FXML
    private Label userLabel;



    public void SetValues(String userName, String lan, String userAcess, Session current, Byte userTax) {
        userLabel.setText(userName);
        language = lan;
        userLevel = userAcess;
        if(!userLevel.equals("Administrator")){
            disableMenu();
        }
        session = current;
        tax = userTax;
        if(tax == Byte.valueOf("1")){
            adminMenu.setVisible(false);
            showReportMenu.setVisible(false);
            leaseButton.setVisible(false);
            reportBtn.setVisible(false);
        }
    }
    public void disableMenu(){
        adminMenu.setDisable(true);
    }
    @FXML private void logOut() throws IOException {
        Stage primaryStage = (Stage) userLabel.getScene().getWindow();
        ResourceBundle bundle = ResourceBundle.getBundle("BikeShop/Localization/language");
        Parent root = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/SplashGUI.fxml"),bundle);
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


    @FXML private void onPurchaseClick() throws IOException {
        // get a handle to the stage
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    Locale locale = new Locale("sin");
                    ResourceBundle rb;
                    if(language.equals("sin")){
                        rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
                    }
                    else{
                        rb = ResourceBundle.getBundle("BikeShop/Localization/language");
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/PurchaseGUI.fxml"), rb);
                    Parent purchaseGUI = null;
                    try {
                        purchaseGUI = (Parent)fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    PurchaseControl controller = fxmlLoader.<PurchaseControl>getController();

                    Scene scene = purchaseButton.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                    Tab tab = new Tab();
                    tab.setText("Purchase new Bike");
                    tab.setClosable(true);
                    tab.setContent(purchaseGUI);
                    tabPane.getTabs().add(tab);
                    controller.setSession(session, tab);
                    controller.postInitialize(tax);
                    tabPane.getSelectionModel().select(tab);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();

    }

    @FXML
    private Button sellButton;
    @FXML private void onSellClick() throws IOException {
        // get a handle to the stage

        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    Locale locale = new Locale("sin");
                    ResourceBundle rb;
                    if(language.equals("sin")){
                        rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
                    }
                    else{
                        rb = ResourceBundle.getBundle("BikeShop/Localization/language");
                    }


                    Scene scene = sellButton.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/SaleGUI.fxml"), rb);
                    Parent SaleGUI = null;
                    try {
                        SaleGUI = (Parent)fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SaleControl controller = fxmlLoader.<SaleControl>getController();


                    //controller.attachCancelAction();
                    Tab tab = new Tab();
                    tab.setText("Sell Bike");
                    tab.setClosable(true);
                    tab.setContent(SaleGUI);
                    tabPane.getTabs().add(tab);
                    controller.setSession(session, tab);
                    controller.postInitialize(tax);
                    tabPane.getSelectionModel().select(tab);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();

    }


    @FXML
    private MenuBar menuBar;
    @FXML private void onShowPurchase() throws IOException {
        // get a handle to the stage
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/showPurchasesG.fxml"));
                    Parent showPurchaseGUI = null;
                    try {
                        showPurchaseGUI = (Parent)fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    PurchaseViewControl controller = fxmlLoader.<PurchaseViewControl>getController();
                    controller.setSession(session, tax);
                    Scene scene = menuBar.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                    Tab tab = new Tab();
                    tab.setText("Purchases");
                    tab.setId("purchaseTab");
                    tab.setClosable(true);
                    tab.setContent(showPurchaseGUI);
                    tabPane.getTabs().add(tab);
                    tabPane.getSelectionModel().select(tab);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();

    }

    @FXML private void onShowStocks() throws IOException {
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/showStocksGUI.fxml"));
                    Parent showStockGUI = null;
                    try {
                        showStockGUI = (Parent)fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StockViewControl controller = fxmlLoader.<StockViewControl>getController();
                    controller.setSession(session, tax);
                    Scene scene = menuBar.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                    Tab tab = new Tab();
                    tab.setText("Show Stocks");
                    tab.setId("stockTab");
                    tab.setClosable(true);
                    tab.setContent(showStockGUI);
                    tabPane.getTabs().add(tab);
                    tabPane.getSelectionModel().select(tab);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();


    }

    @FXML private void onShowReport() throws IOException {
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/ReportView.fxml"));
                    Parent showReportGUI = null;
                    try {
                        showReportGUI = (Parent)fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ReportControl controller = fxmlLoader.<ReportControl>getController();
                    controller.setSession(session);
                    Scene scene = menuBar.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                    Tab tab = new Tab();
                    tab.setText("Show Report");
                    tab.setId("reportTab");
                    tab.setClosable(true);
                    tab.setContent(showReportGUI);
                    tabPane.getTabs().add(tab);
                    tabPane.getSelectionModel().select(tab);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();


    }
    @FXML private void onShowSale() throws IOException {
        // get a handle to the stage
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/showSalesGUI.fxml"));
                    Parent showSaleGUI = null;
                    try {
                        showSaleGUI = (Parent)fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SaleViewControl controller = fxmlLoader.<SaleViewControl>getController();
                    controller.setSession(session, tax);
                    Scene scene = menuBar.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                    TableView tableView = (TableView) scene.lookup("saleDataTable");
                    Tab tab = new Tab();
                    tab.setText("Sales");
                    tab.setClosable(true);
                    tab.setContent(showSaleGUI);
                    tabPane.getTabs().add(tab);
                    tabPane.getSelectionModel().select(tab);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();

    }

    @FXML private void onAddUser() throws IOException {
        // get a handle to the stage
        //Locale locale = new Locale("sin");
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/AddUserGUI.fxml"));
                    Parent addUser = null;
                    try {
                        addUser = (Parent)fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AddUserControl controller = fxmlLoader.<AddUserControl>getController();
                    controller.setSession(session);
                    Scene scene = menuBar.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                    Tab tab = new Tab();
                    tab.setText("Add User");
                    tab.setClosable(true);
                    tab.setContent(addUser);
                    tabPane.getTabs().add(tab);
                    tabPane.getSelectionModel().select(tab);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();

    }


    @FXML private void onManageUsers() throws IOException {
        // get a handle to the stage
        //Locale locale = new Locale("sin");
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/ManageUserView.fxml"));
                    Parent manageUsers = null;
                    try {
                        manageUsers = (Parent)fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ManageUserControl controller = fxmlLoader.<ManageUserControl>getController();
                    controller.setSession(session);
                    Scene scene = menuBar.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                    Tab tab = new Tab();
                    tab.setText("Show Users");
                    tab.setClosable(true);
                    tab.setContent(manageUsers);
                    tabPane.getTabs().add(tab);
                    tabPane.getSelectionModel().select(tab);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();

    }
    private double xOffSet = 0;
    private double yOffSet = 0;
    @FXML private void mousePrsWindow(javafx.scene.input.MouseEvent event) throws IOException{
        xOffSet = event.getSceneX();
        yOffSet = event.getSceneY();

    }

    @FXML private void mouseWindowDrag(javafx.scene.input.MouseEvent event) throws IOException{
        Stage primaryStage = (Stage) menuBar.getScene().getWindow();
        primaryStage.setX(event.getScreenX() - xOffSet);
        primaryStage.setY(event.getScreenY() - yOffSet);
    }
    @FXML private Button closeBtn;
    @FXML private Button rsBtn;
    @FXML private Button minBtn;
    @FXML private void closeWindow(){
        Stage primaryStage = (Stage) closeBtn.getScene().getWindow();
        primaryStage.close();
        System.exit(0);
    }

    @FXML private void rsWindow(){
        Stage  primaryStage = (Stage)closeBtn.getScene().getWindow();
        if(primaryStage.isMaximized()){
            primaryStage.setMaximized(false);
            rsBtn.getStyleClass().removeAll();
            rsBtn.getStyleClass().add("windowControlMax");
        }
        else{
            primaryStage.setMaximized(true);
            rsBtn.getStyleClass().removeAll();
            rsBtn.getStyleClass().add("windowControlRs");
        }
    }

    @FXML private void minWindow(){
        Stage  primaryStage = (Stage)closeBtn.getScene().getWindow();
        primaryStage.setIconified(true);
    }

    public void maximize(){
        Stage  primaryStage = (Stage)closeBtn.getScene().getWindow();
        primaryStage.setMaximized(true);
        rsBtn.getStyleClass().removeAll();
        rsBtn.getStyleClass().add("windowControlRs");
    }

}
