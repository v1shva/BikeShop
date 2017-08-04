package BikeShop.control;



/**
 * Created by Geeth on 5/12/2017.
 */

import BikeShop.Entity.PurchasesEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class StockViewControl {
    @FXML
    TextField searchInput;
    @FXML
    private TableView<PurchasesEntity> saleDataTable;
    @FXML
    private TableColumn purchaseColumn;

    @FXML
    public void initialize() {
        setTableData();

    }

    private void setTableData(){
        ObservableList<PurchasesEntity> masterData = LoadTable();
        FilteredList<PurchasesEntity> filteredData = new FilteredList<>(masterData, p -> true);
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
        sortedData.comparatorProperty().bind(saleDataTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        saleDataTable.setItems(sortedData);
        purchaseColumn.setComparator(purchaseColumn.getComparator().reversed());
        saleDataTable.getSortOrder().add(purchaseColumn);
    }

    private ObservableList<PurchasesEntity> LoadTable() {
        SessionFactory factory;
        try {
            factory = new Configuration().configure("bikeDB.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        ObservableList<PurchasesEntity> data = null;
        try {
            tx = session.beginTransaction();
            List<PurchasesEntity> list  = (List<PurchasesEntity>) session.createQuery("from PurchasesEntity  where sold='false'").list();
            data = FXCollections.observableList(list);
            saleDataTable.setItems(data);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return data;
    }
    @FXML private void EditItems(){

    }

    @FXML private void LoadItems(){
        setTableData();
    }


}
