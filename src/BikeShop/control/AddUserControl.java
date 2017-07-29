package BikeShop.control;


import BikeShop.Entity.UsersEntity;
import BikeShop.HibernateInit;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
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
    private TextField NameIn;
    @FXML
    private TextField NICIn;
    @FXML
    private TextField passwordIn;
    @FXML
    private TextField confirmPasswordIn;
    @FXML private void addUser() throws IOException {
        String username = userNameIn.getText();
        String password = passwordIn.getText();
        String name = NameIn.getText();
        String nic = NICIn.getText();
        Session session = HibernateInit.getSessionFactory().openSession();
        String hashedPass = "";

        try {
            hashedPass = PasswordStorage.createHash(password);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            UsersEntity user = new UsersEntity();
            user.setName(name);
            user.setUsername(username);
            user.setPassword(hashedPass);
            user.setNic(nic);
            session.save(user);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
            Scene scene = userNameIn.getScene();
            TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
            tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
        }
    }
    @FXML private void CancelTab(){
        Scene scene = userNameIn.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
    }

}


// JAVA Password Hash
