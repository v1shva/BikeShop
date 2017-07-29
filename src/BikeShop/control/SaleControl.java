package BikeShop.control;

import BikeShop.Entity.SalesEntity;
import BikeShop.HibernateInit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Vishva on 2/28/2017.
 */



public class SaleControl {
    private int invoice;
    private ChangeListener listener;
    Locale locale ;
    ResourceBundle rb ;
    SalesEntity sl;
    Tab currentTab;
    @FXML
    Label InvoiceNoLbl;
    @FXML
    TextField BikeNoIn1,BikeNoIn2,BikeModalIn,BikeColorIn,OwnerNameIn, OwnerAdrIn,OwnerIDIn,OwnerTPIn1,OwnerTPIn2;
    @FXML
    TextField LeasedAmntIn, TotalAmntIn,OtherExpIn, ArrearsIn, FinFNo, FinValueIn;
    @FXML
    ChoiceBox FinTypeIn, BikeNoDash;
    @FXML
    TextArea OtherInfoIn;
    @FXML
    CheckBox DelLetterIn,VICIn,CRIn,TPaperIn,LicenseIn,InsuranceIn,IDCpIn, KeysIn;
    @FXML
    DatePicker saleDateIn;


    @FXML
    public void initialize() {
        // delete entity on tab close
        /*currentTab.setOnCloseRequest(e -> {
            DeleteEntity();
        });*/
        System.out.println(currentTab);


        InvoiceNoLbl.setText("Invoice No. "+ InvoiceNo());
        locale = new Locale("sin");
        rb = ResourceBundle.getBundle("BikeShop/language", locale);
        ArrayList<TextField> txArray = new ArrayList<TextField>();
        txArray.addAll(Arrays.asList(BikeNoIn1,BikeNoIn2,BikeColorIn,BikeModalIn,OwnerNameIn,OwnerIDIn,TotalAmntIn));
        for(TextField currentField:txArray){
            currentField.focusedProperty().addListener((ov, oldV, newV) -> {
                if (!newV) {
                    ValidateRequired(currentField);
                }
                else{

                }
            });
        }

        listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (BikeNoProvince.getValue().equals("")) {
                    BikeNoDash.setItems(FXCollections.observableArrayList(
                            "SRI", "—" )
                    );
                    BikeNoDash.getSelectionModel().selectFirst();
                } else  {
                    BikeNoDash.setItems(FXCollections.observableArrayList(
                             "—" )
                    );
                    BikeNoDash.getSelectionModel().selectFirst();
                }

                System.out.println(BikeNoProvince.getValue());
            }
        };
        BikeNoProvince.getSelectionModel().selectedItemProperty().addListener(listener);

    }

    @FXML
    private void addSale(){
        Session session = HibernateInit.getSessionFactory().openSession();
        Transaction tx = null;
        Double amountDbl;
        try{
            tx = session.beginTransaction();
            SalesEntity sales = new SalesEntity();

            //Create sales object from form fields.

            sales.setInvoiceNo(invoice);
            sales.setBikeNo(BikeNoProvince.getValue().toString()+" " + BikeNoIn1.getText()+BikeNoDash.getValue().toString()+BikeNoIn2.getText());
            sales.setBikeModal(BikeModalIn.getText());
            sales.setBikeColor(BikeColorIn.getText());
            sales.setOwnerName(OwnerNameIn.getText());
            sales.setOwnerAddress(OwnerAdrIn.getText());
            sales.setOwnerNic(OwnerIDIn.getText());
            sales.setOwnerTpNo(OwnerTPIn1.getText()+","+OwnerTPIn2.getText());
            sales.setFinanceFNo(FinFNo.getText());
            sales.setFinanceType(FinTypeIn.getValue().toString());
            System.out.println(FinValueIn.getText());
            amountDbl = FinValueIn.getText().length()>0 ? Double.parseDouble(FinValueIn.getText()) : 0;
            sales.setFinanceValue(amountDbl);
            System.out.println(saleDateIn.getValue());

            Date currentDate = Date.valueOf(saleDateIn.getValue());

            sales.setSaleDate(currentDate);
            String docList = "";
            if(DelLetterIn.isSelected()){
                docList = "delLetter" + ",";
            }
            if(TPaperIn.isSelected()){
                docList += "transferPaper" + ",";
            }
            if(IDCpIn.isSelected()){
                docList += "IDCopy" + ",";
            }
            if(VICIn.isSelected()){
                docList += "VIC" + ",";
            }
            if(LicenseIn.isSelected()){
                docList += "License" + ",";
            }
            if(KeysIn.isSelected()){
                docList += "keys" +",";
            }
            if(CRIn.isSelected()){
                docList += "CR" + ",";
            }
            if(InsuranceIn.isSelected()){
                docList += "insurance" + ",";
            }
            sales.setDocList(docList);
            sales.setOtherInfo(OtherInfoIn.getText());
            amountDbl =LeasedAmntIn.getText().length()>0 ? Double.parseDouble(LeasedAmntIn.getText()) : 0;
            sales.setLeasingValue(amountDbl);
            amountDbl = TotalAmntIn.getText().length()>0 ? Double.parseDouble(TotalAmntIn.getText()) : 0;
            sales.setTotalValue(amountDbl);
            amountDbl = OtherExpIn.getText().length()>0 ? Double.parseDouble(OtherExpIn.getText()) : 0;
            sales.setOtherExpenses(amountDbl);
            amountDbl = ArrearsIn.getText().length()>0 ? Double.parseDouble(ArrearsIn.getText()) : 0;
            sales.setArrearsValue(amountDbl);
            session.update(sales);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        System.out.println("invoice");
    }

    @FXML
    private void ToggleFinance(){
        if(FinFNo.isDisabled()){
            FinFNo.setDisable(false);
            FinTypeIn.setDisable(false);
            FinValueIn.setDisable(false);
        }
        else{
            FinFNo.setDisable(true);
            FinTypeIn.setDisable(true);
            FinValueIn.setDisable(true);
        }
    }

    @FXML private void CancelTab(){
        Scene scene = InvoiceNoLbl.getScene();
        DeleteEntity();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
    }

    private void DeleteEntity(){
        Session session = HibernateInit.getSessionFactory().openSession();
        Transaction tx = null;
        System.out.println("here");
        try{
            session.delete(sl);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
    private int InvoiceNo(){
        Session session = HibernateInit.getSessionFactory().openSession();
        Transaction tx = null;
        invoice = 0;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("select max(invoiceNo) from SalesEntity ");
            java.util.List list = query.list();
            invoice = (int)query.list().get(0) + 1;
            sl = new SalesEntity();
            sl.setInvoiceNo(invoice);
            session.save(sl);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return invoice;
    }

    private void ValidateRequired(TextField field){
        Tooltip errorTip = null;
        if((field.getText().equals("") && field.getUserData()==null) ||
                (field.getText().equals("") &&!(field.getUserData().toString().equals("error")))){

            field.setUserData("error");
            System.out.println(field.getUserData().toString());
            field.getStyleClass().add("errorField");
            errorTip = new Tooltip(rb.getString("thisIsRequired"));
            errorTip.getStyleClass().removeAll();
            Scene scene = field.getScene();
            scene.getStylesheets().add(this.getClass().getResource("../css/sale.css").toExternalForm());
            errorTip.getStyleClass().add("errorTip");
            field.setTooltip(errorTip);
        }
        else if(!field.getUserData().toString().equals("errorInvalid") && !field.getText().equals("")){
            ValidField(field);
        }
    }

    @FXML
    private void saveData(){
        Session session = HibernateInit.getSessionFactory().openSession();
        Transaction tx = null;
        invoice = 0;
        try{
            tx = session.beginTransaction();
            sl.setInvoiceNo(invoice);
            sl.setBikeNo(BikeNoIn1.getText());
            sl.setBikeModal(BikeModalIn.getText());
            sl.setBikeColor(BikeColorIn.getText());
            sl.setOwnerName(OwnerNameIn.getText());
            sl.setOwnerAddress(OwnerAdrIn.getText());
            sl.setOwnerNic(OwnerIDIn.getText());
            sl.setOwnerTpNo(OwnerTPIn1.getText()+","+OwnerTPIn2.getText());
            sl.setFinanceType(FinTypeIn.getValue().toString());
            sl.setFinanceFNo(FinFNo.getText());
            session.save(sl);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }

    }
    @FXML
    ChoiceBox BikeNoProvince;
    @FXML
    private void ValidateBikeNo1(){

        String BikeNo1 = BikeNoIn1.getText();
        if(BikeNo1.length()==2 || BikeNo1.length()==3) {
            if (BikeNoProvince.getValue().toString().equals("")) {
                if (BikeNoIn1.getUserData() == null && (!BikeNo1.matches("[0-9]+"))) {
                    InvalidField(BikeNoIn1);
                } else if (!(BikeNoIn1.getUserData().toString().equals("errorInvalid")) && (!BikeNo1.matches("[0-9]+"))) {
                    InvalidField(BikeNoIn1);
                } else if (BikeNo1.matches("[0-9]+")) {
                    ValidField(BikeNoIn1);
                }
            } else {
                if (BikeNoIn1.getUserData() == null && (BikeNo1.matches("[0-9]+"))) {
                    InvalidField(BikeNoIn1);
                } else if (!(BikeNoIn1.getUserData().toString().equals("errorInvalid")) && (BikeNo1.matches("[0-9]+"))) {
                    InvalidField(BikeNoIn1);
                } else if (!BikeNo1.matches("[0-9]+")) {
                    ValidField(BikeNoIn1);
                }

            }
        }
        else{
            InvalidField(BikeNoIn1);
        }
    }
    @FXML
    private void ValidateBikeNo2(){
        String BikeNo2 = BikeNoIn2.getText();
        if(BikeNo2.length()==4){
            if (BikeNoIn2.getUserData() == null && (!BikeNo2.matches("[0-9]+"))) {
                InvalidField(BikeNoIn2);
            } else if (!(BikeNoIn2.getUserData().toString().equals("errorInvalid")) && (!BikeNo2.matches("[0-9]+"))) {
                InvalidField(BikeNoIn2);
            } else if (BikeNo2.matches("[0-9]+")) {
                ValidField(BikeNoIn2);
            }
        }
        else{
            InvalidField(BikeNoIn2);
        }
    }
    @FXML
    private void ValidateForLetters(javafx.scene.input.KeyEvent event){
        TextField trigrdField = (TextField) event.getSource();
        if((trigrdField.getText().matches("[0-9]+") && trigrdField.getUserData()==null) ||
                (trigrdField.getText().matches("[0-9]+") && !(trigrdField.getUserData().toString().equals("errorInvalid")))){
            InvalidField(trigrdField);
        }
        else{
            ValidField(trigrdField);
        }
    }

    public void setCurrentTab(Tab tab){
        currentTab = tab;
    }

    @FXML
    private void ValidateForNumbers(javafx.scene.input.KeyEvent event){
        TextField trigrdField = (TextField) event.getSource();
        if((!trigrdField.getText().matches("[0-9]+") && trigrdField.getUserData()==null) ||
                (!trigrdField.getText().matches("[0-9]+") && !(trigrdField.getUserData().toString().equals("errorInvalid")))){
            InvalidField(trigrdField);
            System.out.println("here");
        }
        else{
            ValidField(trigrdField);
            System.out.println("valid");
        }
    }
    
    private void ValidField(TextField field){
        field.getStyleClass().clear();
        field.getStyleClass().addAll("text-field", "text-input");
        field.setTooltip(null);
        field.setUserData("");
    }
    
    private void InvalidField(TextField field){
        field.setUserData("errorInvalid");
        field.getStyleClass().add("errorField");
        Tooltip errorTip = new Tooltip(rb.getString("thisIsInvalid"));
        errorTip.getStyleClass().removeAll();
        Scene scene = field.getScene();
        scene.getStylesheets().add(this.getClass().getResource("../css/sale.css").toExternalForm());
        errorTip.getStyleClass().add("errorTip");
        field.setTooltip(errorTip);
    }

}
