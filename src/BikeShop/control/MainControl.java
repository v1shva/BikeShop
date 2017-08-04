package BikeShop.control;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vishva on 3/8/2017.
 */
public class MainControl {
    private String language = "en";
    private String userLevel = "";
    @FXML Menu adminMenu;
    @FXML
    private Button purchaseButton;
    @FXML
    private Label userLabel;
    public void SetValues(String userName, String lan, String userAcess) {
        userLabel.setText(userName);
        language = lan;
        userLevel = userAcess;
        if(!userLevel.equals("Administrator")){
            disableMenu();
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
        Locale locale = new Locale("sin");
        ResourceBundle rb;
        if(language.equals("sin")){
            rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
        }
        else{
            rb = ResourceBundle.getBundle("BikeShop/Localization/language");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/PurchaseGUI.fxml"), rb);
        Parent purchaseGUI = (Parent)fxmlLoader.load();
        PurchaseControl controller = fxmlLoader.<PurchaseControl>getController();

        Scene scene = purchaseButton.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        controller.postInitialize();

        Tab tab = new Tab();
        tab.setText("Purchase new Bike");
        tab.setClosable(true);
        tab.setContent(purchaseGUI);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    private Button sellButton;
    @FXML private void onSellClick() throws IOException {
        // get a handle to the stage
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
        Parent SaleGUI = (Parent)fxmlLoader.load();
        SaleControl controller = fxmlLoader.<SaleControl>getController();
        controller.postInitialize();
        //controller.attachCancelAction();
        Tab tab = new Tab();
        tab.setText("Sell Bike");
        tab.setClosable(true);
        tab.setContent(SaleGUI);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    private Button cheqButton;
    @FXML private void onChequeClick() throws IOException {
        Parent cashCheqGUI = FXMLLoader.load(getClass().getResource("fxml/CashCheqGUI.fxml"));
        Scene scene = cheqButton.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        Tab tab = new Tab();
        tab.setText("Cash Cheque");
        tab.setClosable(true);
        tab.setContent(cashCheqGUI);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML
    private MenuBar menuBar;
    @FXML private void onShowPurchase() throws IOException {
        // get a handle to the stage
        Parent showPurchaseGUI = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/showPurchasesG.fxml"));
        Scene scene = menuBar.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        Tab tab = new Tab();
        tab.setText("Purchases");
        tab.setId("purchaseTab");
        tab.setClosable(true);
        tab.setContent(showPurchaseGUI);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML private void onShowStocks() throws IOException {
        Parent showPurchaseGUI = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/showStocksGUI.fxml"));
        Scene scene = menuBar.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        Tab tab = new Tab();
        tab.setText("Show Stocks");
        tab.setId("stockTab");
        tab.setClosable(true);
        tab.setContent(showPurchaseGUI);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML private void onShowSale() throws IOException {
        // get a handle to the stage
        Locale locale = new Locale("sin");
        ResourceBundle rb;
        if(language.equals("sin")){
            rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
        }
        else{
            rb = ResourceBundle.getBundle("BikeShop/Localization/language");
        }
        Parent showSaleGUI = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/showSalesGUI.fxml"),rb);
        Scene scene = menuBar.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        TableView tableView = (TableView) scene.lookup("saleDataTable");
        Tab tab = new Tab();
        tab.setText("Sales");
        tab.setClosable(true);
        tab.setContent(showSaleGUI);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML private void onAddUser() throws IOException {
        // get a handle to the stage
        //Locale locale = new Locale("sin");
        Parent addUser = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/AddUserGUI.fxml"));
        Scene scene = menuBar.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        Tab tab = new Tab();
        tab.setText("Add User");
        tab.setClosable(true);
        tab.setContent(addUser);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }


    @FXML private void onManageUsers() throws IOException {
        // get a handle to the stage
        //Locale locale = new Locale("sin");
        Parent addUser = FXMLLoader.load(getClass().getResource("/BikeShop/fxml/mma.fxml"));
        Scene scene = menuBar.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        Tab tab = new Tab();
        tab.setText("Show Users");
        tab.setClosable(true);
        tab.setContent(addUser);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
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
            System.out.println("here");
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

}
