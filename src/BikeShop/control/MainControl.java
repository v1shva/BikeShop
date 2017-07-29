package BikeShop.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vishva on 3/8/2017.
 */
public class MainControl {
    @FXML
    private Button purchaseButton;
    @FXML private void onPurchaseClick() throws IOException {
        // get a handle to the stage
        Locale locale = new Locale("sin");
        ResourceBundle rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
        Parent purchaseGUI = FXMLLoader.load(getClass().getResource("../fxml/PurchaseG.fxml"),rb);
        Scene scene = purchaseButton.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");

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
        ResourceBundle rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);


        Scene scene = sellButton.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");

        Tab tab = new Tab();
        tab.setText("Sell Bike");
        tab.setClosable(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/SaleGUI.fxml"),rb);
        SaleControl controller = (SaleControl) fxmlLoader.getController();
        controller.setCurrentTab(tab);

        tab.setContent(fxmlLoader.load());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);


    }

    @FXML
    private Button cheqButton;
    @FXML private void onChequeClick() throws IOException {
        // get a handle to the stage
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
        Parent showPurchaseGUI = FXMLLoader.load(getClass().getResource("../fxml/showPurchasesG.fxml"));
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

    @FXML private void onShowSale() throws IOException {
        // get a handle to the stage
        Locale locale = new Locale("sin");
        ResourceBundle rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
        Parent showSaleGUI = FXMLLoader.load(getClass().getResource("../fxml/showSalesGUI.fxml"),rb);
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
        Parent addUser = FXMLLoader.load(getClass().getResource("../fxml/addUserGUI.fxml"));
        Scene scene = menuBar.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        TableView tableView = (TableView) scene.lookup("saleDataTable");
        Tab tab = new Tab();
        tab.setText("Sales");
        tab.setClosable(true);
        tab.setContent(addUser);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @FXML private void onManageUsers() throws IOException {
        // get a handle to the stage
        //Locale locale = new Locale("sin");
        Parent addUser = FXMLLoader.load(getClass().getResource("../fxml/addUserGUI.fxml"));
        Scene scene = menuBar.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        TableView tableView = (TableView) scene.lookup("saleDataTable");
        Tab tab = new Tab();
        tab.setText("Sales");
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
