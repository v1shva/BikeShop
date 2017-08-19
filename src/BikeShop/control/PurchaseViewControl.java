package BikeShop.control;



/**
 * Created by Geeth on 5/12/2017.
 */

import BikeShop.Entity.PurchasesEntity;
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

public class PurchaseViewControl {
    private String language = "en";
    Session session;
    ObservableList<PurchasesEntity> masterData;
    FilteredList<PurchasesEntity> filteredData;
    @FXML
    CheckBox taxToggle, unregToggle;
    @FXML
    TextField searchInput;
    @FXML
    private TableView<BikeShop.Entity.PurchasesEntity> purchaseDataTable;
    @FXML
    private TableColumn purchaseColumn;
    public void setSession(Session current){
        session = current;
        setpurchaseDataTable();
    }

    @FXML ScrollPane scrollPane;
    @FXML
    public void initialize() {
        purchaseDataTable.prefHeightProperty().bind(scrollPane.heightProperty());
    }

    private void setpurchaseDataTable(){
        purchaseDataTable.setItems(null);
        purchaseDataTable.setPlaceholder(new Label("Content is loading"));
        masterData = LoadTable();
        filteredData = new FilteredList<>(masterData, p -> true);
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
                else if(salesItem.getPurchaseDate()!=null && salesItem.getPurchaseDate().toString().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });
        SortedList<PurchasesEntity> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(purchaseDataTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        purchaseDataTable.setItems(sortedData);
        purchaseColumn.setComparator(purchaseColumn.getComparator().reversed());
        purchaseDataTable.getSortOrder().add(purchaseColumn);
    }

    private ObservableList<PurchasesEntity> LoadTable() {

        Transaction tx = null;
        ObservableList<PurchasesEntity> data = null;
        try {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PurchasesEntity> cq = cb.createQuery(BikeShop.Entity.PurchasesEntity.class);
            Root<PurchasesEntity> criteriaSale = cq.from(PurchasesEntity.class);
            cq.select(criteriaSale);
            TypedQuery<PurchasesEntity> query = session.createQuery(cq);
            List<PurchasesEntity> list = query.getResultList();
            data = FXCollections.observableList(list);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.clear();
        }
        return data;
    }

    @FXML private void EditItems() throws IOException {
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    List<PurchasesEntity> purchase = purchaseDataTable.getItems();
                    int i = 0,checkedi =0,count=0;
                    for(PurchasesEntity sl:purchase){
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
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BikeShop/fxml/PurchaseGUI.fxml"), rb);
                        Parent root = null;
                        try {
                            root = (Parent)fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        PurchaseControl controller = fxmlLoader.<PurchaseControl>getController();
                        PurchasesEntity current = purchase.get(checkedi);
                        //controller.attachCancelAction();
                        controller.setValues(true, current);
                        controller.setSession(session);
                        Tab tab = new Tab();
                        tab.setText("Purchase Bike");
                        tab.setClosable(true);
                        tab.setContent(root);
                        tabPane.getTabs().add(tab);
                        tabPane.getSelectionModel().select(tab);
                        alertL.dispose();
                    }
                    else{
                        alertL.dispose();
                        Alert alert = new Alert(Alert.AlertType.WARNING,"Select only single item to Edit");
                        alert.setTitle("More than one item selected");
                        alert.setHeaderText("More than one item selected");
                        alert.show();
                    }

                });
                return null;
            }
        };
        new Thread(task).start();




    }

    @FXML private void DeleteItems(){
        ButtonType okay = new ButtonType("Okay",  ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        // get a handle to the stage
        Alert alert = new Alert(Alert.AlertType.WARNING,"Selected items will be deleted. Proceed?",okay,cancel);
        alert.setTitle("Delete Selected Items");
        alert.setHeaderText("Deleting Items");
        Optional<ButtonType> result = alert.showAndWait();
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    if(result.get().getButtonData()== ButtonBar.ButtonData.OK_DONE){
                        List<PurchasesEntity> sales = purchaseDataTable.getItems();
                        Transaction tx = session.beginTransaction();
                        int i = 0;
                        for(PurchasesEntity sl:sales){
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
                        setpurchaseDataTable();
                        alertL.dispose();
                    }
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
                setpurchaseDataTable();
                Platform.runLater(() -> {
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();

    }

    private boolean unreg = false;
    @FXML private void toggleUnreg(){
        if(!unreg){
            filteredData.setPredicate(salesItem -> {
                if (salesItem.getUnregistered() == Byte.valueOf("1")) {
                    return true;
                }
                return false; // Does not match.
            });
            SortedList<PurchasesEntity> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(purchaseDataTable.comparatorProperty());
            purchaseDataTable.setItems(sortedData);
            purchaseColumn.setComparator(purchaseColumn.getComparator().reversed());
            purchaseDataTable.getSortOrder().add(purchaseColumn);
            taxToggle.setSelected(false);
            tax = false;
            unreg = true;
        }
        else{
            setpurchaseDataTable();
            taxToggle.setSelected(false);
            tax = false;
            unreg = false;
        }

    }
    private boolean tax = false;
    @FXML private void toggleTax(){
        if(!tax){
            filteredData.setPredicate(salesItem -> {
                if (salesItem.getTax() == Byte.valueOf("1")) {
                    return true;
                }
                return false; // Does not match.
            });
            SortedList<PurchasesEntity> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(purchaseDataTable.comparatorProperty());
            purchaseDataTable.setItems(sortedData);
            purchaseColumn.setComparator(purchaseColumn.getComparator().reversed());
            purchaseDataTable.getSortOrder().add(purchaseColumn);
            unregToggle.setSelected(false);
            unreg = false;
            tax = true;

        }
        else{
            setpurchaseDataTable();
            unregToggle.setSelected(false);
            unreg = false;
            tax = false;
        }
    }

    @FXML private void AddToTax() {
        ButtonType okay = new ButtonType("Okay",  ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        // get a handle to the stage
        Alert alert = new Alert(Alert.AlertType.WARNING,"Selected items will be added to Tax. Proceed?",okay,cancel);
        alert.setTitle("Add to Tax");
        alert.setHeaderText("Add to Tax");
        Optional<ButtonType> result = alert.showAndWait();
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    if(result.get().getButtonData()== ButtonBar.ButtonData.OK_DONE){
                        List<PurchasesEntity> sales = purchaseDataTable.getItems();
                        Transaction tx = session.beginTransaction();
                        int i = 0;
                        for(PurchasesEntity sl:sales){
                            i++;
                            if(sl.getChecked()!=null &&sl.getChecked()==true){
                                sl.setTax(Byte.valueOf("1"));
                                if(sl.getSaleInvoice() != null){
                                    SalesEntity sale = session.get(SalesEntity.class,sl.getSaleInvoice());
                                    sale.setPurchaseTax(Byte.valueOf("1"));
                                    session.update(sale);
                                }
                                session.update(sl);
                            }
                            if ( i % 20 == 0 ) { //20, same as the JDBC batch size
                                //flush a batch of inserts and release memory:
                                session.flush();
                                session.clear();
                            }
                        }
                        tx.commit();
                        session.clear();
                        setpurchaseDataTable();
                        alertL.dispose();
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML private void RemoveTax() {
        ButtonType okay = new ButtonType("Okay",  ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        // get a handle to the stage
        Alert alert = new Alert(Alert.AlertType.WARNING,"Selected items will be removed from Tax. Proceed?",okay,cancel);
        alert.setTitle("Remove from Tax");
        alert.setHeaderText("Remove from Tax");
        Optional<ButtonType> result = alert.showAndWait();
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    if(result.get().getButtonData()== ButtonBar.ButtonData.OK_DONE){
                        List<PurchasesEntity> sales = purchaseDataTable.getItems();
                        Transaction tx = session.beginTransaction();
                        int i = 0;
                        for(PurchasesEntity sl:sales){
                            i++;
                            if(sl.getChecked()!=null &&sl.getChecked()==true){
                                sl.setTax(Byte.valueOf("0"));
                                if(sl.getSaleInvoice() != null){
                                    SalesEntity sale = session.get(SalesEntity.class,sl.getSaleInvoice());
                                    sale.setPurchaseTax(Byte.valueOf("0"));
                                    sale.setTax(Byte.valueOf("0"));
                                    session.update(sale);
                                }
                                session.update(sl);
                            }
                            if ( i % 20 == 0 ) { //20, same as the JDBC batch size
                                //flush a batch of inserts and release memory:
                                session.flush();
                                session.clear();
                            }
                        }
                        tx.commit();
                        session.clear();
                        setpurchaseDataTable();
                        alertL.dispose();
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

}
