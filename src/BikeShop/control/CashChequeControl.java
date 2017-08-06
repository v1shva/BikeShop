package BikeShop.control;

import BikeShop.Entity.SalesEntity;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Vishva on 2/28/2017.
 */



public class CashChequeControl {
    private int invoice;
    private ChangeListener listener;
    Locale locale ;
    ResourceBundle rb ;
    SalesEntity sl;
    Tab currentTab;
    Session session;

    @FXML
    TextField BikeNoIn1,BikeNoIn2,BikeModalIn,BikeColorIn;
    @FXML
    TextField chequeBank, chequeNumber, chequeAmount;
    @FXML
    ChoiceBox BikeNoDash,BikeNoProvince;
    @FXML
    DatePicker chequeDate;

    public void setSession(Session current){
        session = current;
    }

    public void setValues(int invoiceNo, String bikeNo, String bikeMoadal, String bikeColor, Double amount){
        invoice = invoiceNo;
        String bikeNos[] = bikeNo.split("\\s+");
        if(bikeNos.length==2){
            BikeNoProvince.setValue(bikeNos[0]);
            String noRest[] = bikeNos[1].split("-");
            BikeNoDash.setValue("-");
            BikeNoIn1.setText(noRest[0]);
            BikeNoIn2.setText(noRest[1]);
        }
        else if (bikeNos.length == 1){
            String SRINos[] = bikeNos[0].split("SRI");
            if(SRINos.length==1){
                String DashNos[] = bikeNos[0].split("-");
                if(DashNos.length==2){
                    BikeNoDash.setValue("-");
                    BikeNoIn1.setText(DashNos[1]);
                    BikeNoIn2.setText(DashNos[2]);
                }
            }
            else if(SRINos.length==2){
                BikeNoDash.setValue("SRI");
                BikeNoIn1.setText(SRINos[1]);
                BikeNoIn2.setText(SRINos[2]);
            }
        }
        BikeModalIn.setText(bikeMoadal);
        BikeColorIn.setText(bikeColor);
        chequeAmount.setText(""+amount);
    }

    @FXML
    public void initialize() {
        // delete entity on tab close
        /*currentTab.setOnCloseRequest(e -> {
            DeleteEntity();
        });*/

    }

    @FXML
    private void addCashCheque(){
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            SalesEntity sales = session.get(SalesEntity.class, invoice);
            assignValues(sales);
            session.update(sales);
            tx.commit();
            Scene scene = BikeNoIn1.getScene();
            TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
            tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );

        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.clear();
        }
    }

    private void assignValues(SalesEntity sales){
        Date chequeD = Date.valueOf(chequeDate.getValue());
        Date currentDate = Date.valueOf(chequeDate.getValue());
        sales.setInvoiceNo(invoice);
        sales.setChequeResolved(Byte.valueOf("1"));
        sales.setSaleDate(currentDate);
        sales.setChequeBank(chequeBank.getText());
        sales.setChequeDate(chequeD);
        sales.setChequeNumber(chequeNumber.getText());
    }


    @FXML private void CancelTab(){
        ButtonType okay = new ButtonType("Okay",  ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        // get a handle to the stage
        Alert alert = new Alert(Alert.AlertType.WARNING,"Changes will be lost. Proceed?",okay,cancel);
        alert.setTitle("Cancel");
        alert.setHeaderText("Cancel Action");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().getButtonData()== ButtonBar.ButtonData.OK_DONE){
            Scene scene = BikeNoIn1.getScene();
            TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
            tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
        }

    }


}
