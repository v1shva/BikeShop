package BikeShop.control;



/**
 * Created by Geeth on 5/12/2017.
 */

import BikeShop.Entity.UsersEntity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.*;
import java.util.List;

public class ManageUserControl {
    Session session;
    @FXML
    TextField searchInput;
    @FXML
    private TableView<BikeShop.Entity.UsersEntity> saleDataTable;

    public void setSession(Session current){
        session = current;
        setTableData();
    }

   private void setTableData(){
       ObservableList<UsersEntity> masterData = LoadTable();
       FilteredList<UsersEntity> filteredData = new FilteredList<>(masterData, p -> true);
       searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
           filteredData.setPredicate(userItem -> {
               // If filter text is empty, display all persons.
               if (newValue == null || newValue.isEmpty()) {
                   return true;
               }
               String lowerCaseFilter = newValue.toLowerCase();
               if(userItem.getName()!=null && userItem.getName().toLowerCase().contains(lowerCaseFilter)){
                   return true;
               }
               else if(userItem.getNic()!=null && userItem.getNic().toLowerCase().contains(lowerCaseFilter)){
                   return true;
               }
               else if(userItem.getUsername()!=null && userItem.getUsername().toLowerCase().contains(lowerCaseFilter)){
                   return true;
               }
               return false; // Does not match.
           });
       });
       SortedList<UsersEntity> sortedData = new SortedList<>(filteredData);

       // 4. Bind the SortedList comparator to the TableView comparator.
       sortedData.comparatorProperty().bind(saleDataTable.comparatorProperty());

       // 5. Add sorted (and filtered) data to the table.
       saleDataTable.setItems(sortedData);
   }
    private ObservableList<UsersEntity> LoadTable() {
        Transaction tx = null;
        ObservableList<UsersEntity> data = null;
        try {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<UsersEntity> cq = cb.createQuery(UsersEntity.class);
            Root<UsersEntity> criteriaSale = cq.from(UsersEntity.class);
            cq.select(criteriaSale);
            TypedQuery<UsersEntity> query = session.createQuery(cq);
            List<UsersEntity> list = query.getResultList();
            data = FXCollections.observableList(list);
            saleDataTable.setItems(data);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.clear();
        }
        return data;
    }

    @FXML private void LoadItems(){
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                setTableData();
                Platform.runLater(() -> {
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();

    }


}
