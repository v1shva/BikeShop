package BikeShop.control;


import BikeShop.HibernateInit;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddUserControl {

    @FXML
    private ChoiceBox languageChoice;
    private ChangeListener listener;
    private Locale locale;
    private ResourceBundle rb;
    private static String languageChoiceVal;




    @FXML
    private TextField userNameIn;
    @FXML
    private TextField passwordIn;
    @FXML
    private TextField confirmPasswordIn;
    @FXML private void addUser() throws IOException {
        String username = userNameIn.getText();
        String password = confirmPasswordIn.getText();
        Session session = HibernateInit.getSessionFactory().openSession();
        Transaction tx = null;
        /*try{
            tx = session.beginTransaction();

            PurchasesEntity sl = new PurchasesEntity();
            sl.setInvoiceNo(invoice);
            sl.setLeaseDNo("");
            System.out.println(invoice);
            session.save(sl);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }*/
    }


}