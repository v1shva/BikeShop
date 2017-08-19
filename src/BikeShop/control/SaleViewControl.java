package BikeShop.control;

/**
 * Created by Vishva on 3/27/2017.
 */

import BikeShop.Entity.SalesEntity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class SaleViewControl {
    String language = "";
    boolean togglePendingFinace = false;
    public void setValus(String lan){
        language = lan;
    }
    public Session session;
    @FXML
    TextField searchInput;
    @FXML
    private TableView<BikeShop.Entity.SalesEntity> saleDataTable;
    @FXML
    private TableColumn saleDateColumn;


    ObservableList<SalesEntity> masterData;
    public void setSession(Session current){
        session = current;
        setSaleDataTable();
    }

    @FXML ScrollPane scrollPane;
    @FXML
    public void initialize() {
        saleDataTable.prefHeightProperty().bind(scrollPane.heightProperty());
    }
    @FXML
    public void cashCheque() throws IOException {
        List<SalesEntity> sales = saleDataTable.getItems();
        int i = 0,checkedi =0,count=0;
        for(SalesEntity sl:sales){
            if(sl.getChecked()!=null &&sl.getChecked()==true){
                count++;checkedi = i;
            }
            i++;
        }
        if(count==1){
            Locale locale = new Locale("sin");
            ResourceBundle rb;
            if(language.equals("sin")){
                rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
            }
            else{
                rb = ResourceBundle.getBundle("BikeShop/Localization/language");
            }
            Scene scene = searchInput.getScene();
            TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/CashCheqGUI.fxml"), rb);
            Parent root = (Parent)fxmlLoader.load();
            CashChequeControl controller = fxmlLoader.<CashChequeControl>getController();
            SalesEntity current = sales.get(checkedi);
            //controller.attachCancelAction();
            controller.setValues(current.getInvoiceNo(),current.getBikeNo(),current.getBikeModal(),current.getBikeColor(),current.getFinanceValue());
            controller.setSession(session);
            Tab tab = new Tab();
            tab.setText("Cash Cheque");
            tab.setClosable(true);
            tab.setContent(root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING,"Select only single item to Edit");
            alert.setTitle("More than one item selected");
            alert.setHeaderText("More than one item selected");
            alert.show();
        }
    }
    private void setSaleDataTable(){
        saleDataTable.setItems(null);
        saleDataTable.setPlaceholder(new Label("Content is loading"));
        masterData = LoadTable();
        FilteredList<SalesEntity> filteredData = new FilteredList<>(masterData, p -> true);
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(salesItem -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (Integer.toString(salesItem.getInvoiceNo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                else if(salesItem.getBikeNo()!=null && salesItem.getBikeNo().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else if(salesItem.getBikeModal()!=null && salesItem.getBikeModal().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else if(salesItem.getOwnerName()!=null && salesItem.getOwnerName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else if(salesItem.getOwnerNic()!=null && salesItem.getOwnerNic().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else if(salesItem.getSaleDate()!=null && salesItem.getSaleDate().toString().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });
        SortedList<SalesEntity> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(saleDataTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        saleDataTable.setItems(sortedData);
        saleDateColumn.setComparator(saleDateColumn.getComparator().reversed());
        saleDataTable.getSortOrder().add(saleDateColumn);
    }
    private ObservableList<SalesEntity> LoadTable() {
        Transaction tx = null;
        ObservableList<SalesEntity> data = null;
        try {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SalesEntity> cq = cb.createQuery(BikeShop.Entity.SalesEntity.class);
            Root<SalesEntity> criteriaSale = cq.from(SalesEntity.class);
            cq.select(criteriaSale);
            TypedQuery<SalesEntity> query = session.createQuery(cq);
            List<SalesEntity> list = query.getResultList();
            data = FXCollections.observableList(list);
            saleDataTable.setItems(data);
            saleDataTable.setRowFactory(tv -> new TableRow<SalesEntity>() {
                @Override
                public void updateItem(SalesEntity item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (item == null) {
                        setStyle("");
                    } else if (item.getChequeResolved() == Byte.valueOf("0")) {
                        setStyle("-fx-background-color: #ffa900;");
                    } else {
                        setStyle("");
                    }
                }
            });
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.clear();
        }
        return data;
    }


    @FXML private void togglePending(){
        if(!togglePendingFinace){
            togglePendingFinace = true;

        }
    }



    @FXML private void EditItems() throws IOException {
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    List<SalesEntity> sales = saleDataTable.getItems();
                    int i = 0,checkedi =0,count=0;
                    for(SalesEntity sl:sales){
                        if(sl.getChecked()!=null &&sl.getChecked()==true){
                            count++;checkedi = i;
                        }
                        i++;
                    }
                    if(count==1){
                        Locale locale = new Locale("sin");
                        ResourceBundle rb;
                        if(language.equals("sin")){
                            rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
                        }
                        else{
                            rb = ResourceBundle.getBundle("BikeShop/Localization/language");
                        }

                        Scene scene = searchInput.getScene();
                        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/SaleGUI.fxml"), rb);
                        Parent root = null;
                        try {
                            root = (Parent)fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SaleControl controller = fxmlLoader.<SaleControl>getController();
                        SalesEntity current = sales.get(checkedi);
                        //controller.attachCancelAction();
                        controller.setValues(true, current);
                        controller.setSession(session);
                        Tab tab = new Tab();
                        tab.setText("Sell Bike");
                        tab.setClosable(true);
                        tab.setContent(root);
                        tabPane.getTabs().add(tab);
                        tabPane.getSelectionModel().select(tab);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.WARNING,"Select only single item to Edit");
                        alert.setTitle("More than one item selected");
                        alert.setHeaderText("More than one item selected");
                        alert.show();
                    }
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();



    }

    @FXML private void DeleteItems(){
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    ButtonType okay = new ButtonType("Okay",  ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    // get a handle to the stage
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Selected items will be deleted. Proceed?",okay,cancel);
                    alert.setTitle("Delete Selected Items");
                    alert.setHeaderText("Deleting Items");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get().getButtonData()== ButtonBar.ButtonData.OK_DONE){
                        List<SalesEntity> sales = saleDataTable.getItems();
                        Transaction tx = session.beginTransaction();
                        int i = 0;
                        for(SalesEntity sl:sales){
                            i++;
                            if(sl.getChecked()!=null &&sl.getChecked()==true){
                                session.delete(sl);
                            }
                            if ( i % 20 == 0 ) { //20, same as the JDBC batch size
                                //flush a batch of inserts and release memory:
                                session.flush();
                                session.clear();
                            }
                        }
                        tx.commit();
                        session.clear();
                        setSaleDataTable();
                    }

                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();


    }

    @FXML private void LoadItems(){
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                setSaleDataTable();
                Platform.runLater(() -> {
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML private void toggleTax(){

    }

    @FXML private void AddToTax() {

    }

    @FXML private void RemoveTax() {

    }


}
