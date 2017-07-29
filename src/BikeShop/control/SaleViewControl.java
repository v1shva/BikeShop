package BikeShop.control;

/**
 * Created by Vishva on 3/27/2017.
 */

import BikeShop.Entity.SalesEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class SaleViewControl {
    @FXML
    TextField searchInput;
    @FXML
    private TableView<BikeShop.Entity.SalesEntity> saleDataTable;

    @FXML
    public void initialize() {
        ObservableList<SalesEntity> masterData = LoadTable();
        System.out.println(masterData);
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
                return false; // Does not match.
            });
        });
        SortedList<SalesEntity> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(saleDataTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        saleDataTable.setItems(sortedData);


    }

    private ObservableList<SalesEntity> LoadTable() {
        SessionFactory factory;
        try {
            factory = new Configuration().configure("bikeDB.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
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
        List<SalesEntity> sales = saleDataTable.getItems();
        for(SalesEntity sl:sales){
            if(sl.getChecked()==true){
                System.out.println(sl.getInvoiceNo());
            }
        }
    }

    @FXML private void LoadItems(){
        LoadTable();
    }


}
