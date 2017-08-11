package BikeShop.control;

import BikeShop.Entity.PurchasesEntity;
import BikeShop.Entity.SalesEntity;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Vishva on 2/28/2017.
 */



public class SaleControl {
    private int invoice;
    private boolean editable = false;
    private ChangeListener listener;
    private ChangeListener listenerFinanceType;
    private Session session;
    private SalesEntity saleEdit;
    Locale locale ;
    ResourceBundle rb ;
    SalesEntity sl;
    Tab currentTab;
    @FXML
    Label InvoiceNoLbl;
    @FXML
    TextField BikeNoIn1,BikeNoIn2,BikeModalIn,BikeColorIn,OwnerNameIn, OwnerAdrIn,OwnerIDIn,OwnerTPIn1,OwnerTPIn2;
    @FXML
    TextField LeasedAmntIn, TotalAmntIn,OtherExpIn, ArrearsIn, FinFNo, FinValueIn, FinTypeText;
    @FXML
    ChoiceBox FinTypeIn, BikeNoDash,BikeNoProvince;
    @FXML
    TextArea OtherInfoIn;
    @FXML
    CheckBox DelLetterIn,VICIn,CRIn,TPaperIn,LicenseIn,InsuranceIn,IDCpIn, KeysIn, financeOption, serviceCardIn, manualBookIn;
    @FXML
    DatePicker saleDateIn;

    public void setValues(int invoiceNo, String bikeNo, String bikeMoadal, String bikeColor, String ownerName, String ownerAdr, String ownerID, String ownerTP,
                          Double leasedAmnt, Double TotalAmnt, Double otherExp, Double arrears, String FinFNoVal, Double FinValue, String FinType,
                          String otherInfo, String docList, Date saleDate, boolean edit,SalesEntity saleEditCurrent){
       try{
           saleEdit = saleEditCurrent;
           invoice = invoiceNo;
           InvoiceNoLbl.setText("Invoice No. "+invoice);
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
           OwnerNameIn.setText(ownerName);
           OwnerAdrIn.setText(ownerAdr);
           OwnerIDIn.setText(ownerID);
           String ownerTPNos [] = ownerTP.split(",");
           if(ownerTPNos.length==2){
               OwnerTPIn1.setText(ownerTPNos[0]);
               OwnerTPIn2.setText(ownerTPNos[1]);
           }
           else {
               OwnerTPIn1.setText(ownerTP);
           }
           LeasedAmntIn.setText(leasedAmnt+"");
           TotalAmntIn.setText(TotalAmnt+"");
           OtherExpIn.setText(otherExp+"");
           ArrearsIn.setText(arrears+"");
           if(FinValue > 0){
               financeOption.setSelected(true);
               FinFNo.setDisable(false);
               FinValueIn.setDisable(false);
               FinTypeIn.setDisable(false);
               FinFNo.setText(FinFNoVal);
               FinValueIn.setText(FinValue+"");
               if(FinType.equals("DJ Finance")){
                   FinTypeIn.setValue(FinType);
               }
               else{
                   FinTypeIn.setDisable(true);
                   FinTypeIn.setVisible(false);
                   FinTypeText.setDisable(false);
                   FinTypeText.setVisible(true);
                   FinTypeText.setText(FinType);
               }
           }

           OtherInfoIn.setText(otherInfo);
           saleDateIn.setValue(saleDate.toLocalDate());
           editable = edit;
           List<String> docListItems  = Arrays.asList(docList.split(","));
           if(docListItems.contains("Deletion Letter")) DelLetterIn.setSelected(true);
           if(docListItems.contains("VIC")) VICIn.setSelected(true);
           if(docListItems.contains("CR")) CRIn.setSelected(true);
           if(docListItems.contains("Transfer Paper")) TPaperIn.setSelected(true);
           if(docListItems.contains("ID Copy")) LicenseIn.setSelected(true);
           if(docListItems.contains("License")) InsuranceIn.setSelected(true);
           if(docListItems.contains("Insurance")) IDCpIn.setSelected(true);
           if(docListItems.contains("Keys")) KeysIn.setSelected(true);
           if(docListItems.contains("Service Card")) serviceCardIn.setSelected(true);
           if(docListItems.contains("Manual Book")) manualBookIn.setSelected(true);
       }catch (ArrayIndexOutOfBoundsException e){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Operation Failed");
           alert.setHeaderText("This entry has invalid data. Therefore can't be edited");
           alert.showAndWait();
       }

    }

    public void setSession(Session current){
        session = current;
    }

    public void postInitialize(){
        saleDateIn.setValue(LocalDate.now());
        InvoiceNoLbl.setText("Invoice No. "+ InvoiceNo());
    }

    /*public void attachCancelAction(){
        Scene scene = InvoiceNoLbl.getScene();
        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        currentTab.setOnCloseRequest(new EventHandler<Event>()
        {
            @Override
            public void handle(Event arg0)
            {
                CancelTab();
            }
        });
    }*/
    @FXML
    public void initialize() {
        // delete entity on tab close
        /*currentTab.setOnCloseRequest(e -> {
            DeleteEntity();
        });*/

        locale = new Locale("sin");
        rb = ResourceBundle.getBundle("BikeShop/Localization/language", locale);
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
                            "SRI", "-" )
                    );
                    BikeNoDash.getSelectionModel().selectFirst();
                } else  {
                    BikeNoDash.setItems(FXCollections.observableArrayList(
                             "-" )
                    );
                    BikeNoDash.getSelectionModel().selectFirst();
                }

                System.out.println(BikeNoProvince.getValue());
            }
        };
        BikeNoProvince.getSelectionModel().selectedItemProperty().addListener(listener);
        listenerFinanceType = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue.toString().equals("Other")){
                    FinTypeIn.setDisable(true);
                    FinTypeIn.setVisible(false);
                    FinTypeText.setDisable(false);
                    FinTypeText.setVisible(true);
                }
            }
        };
        FinTypeIn.getSelectionModel().selectedItemProperty().addListener(listenerFinanceType);
        FinTypeText.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(FinTypeText.getText().equals("")) {
                        FinTypeIn.setDisable(false);
                        FinTypeIn.setVisible(true);
                        FinTypeText.setDisable(true);
                        FinTypeText.setVisible(false);
                    }
                }

            }
        });


    }

    @FXML
    private void addSale(){
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    Transaction tx = null;

                    try{
                        tx = session.beginTransaction();
                        SalesEntity sales = new SalesEntity();
                        if(!FinFNo.isDisabled()){
                            sales.setChequeResolved(Byte.valueOf("0"));
                        }
                        String bikeNo = "";
                        if(BikeNoProvince.getValue().toString().equals("")){
                            bikeNo = BikeNoIn1.getText()+BikeNoDash.getValue().toString()+BikeNoIn2.getText();
                        }
                        else{
                            bikeNo = BikeNoProvince.getValue().toString().toUpperCase()+" " + BikeNoIn1.getText()+BikeNoDash.getValue().toString()+BikeNoIn2.getText();
                        }
                        if(!editable){
                            Query query = session.createQuery("from PurchasesEntity where bikeNo='"+bikeNo+"'" + "AND sold='false'");
                            if(query.list().size()==1){
                                List<PurchasesEntity> purchaseL = query.list();
                                PurchasesEntity purchase = purchaseL.get(0);
                                Date currentDate = Date.valueOf(saleDateIn.getValue());
                                purchase.setSold(currentDate.toString());
                                session.update(purchase);
                                assignValues(sales,bikeNo);
                                session.update(sales);
                                Scene scene = InvoiceNoLbl.getScene();
                                TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                                tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
                            }
                            else{
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Bike is not in Stocks");
                                alert.setHeaderText("Bike you're trying to sell is not availabe in our stocks");
                                alert.showAndWait();
                            }
                        }else{
                            assignValues(sales,bikeNo);
                            sales.setChequeNumber(saleEdit.getChequeNumber());
                            sales.setChequeDate(saleEdit.getChequeDate());
                            sales.setChequeBank(saleEdit.getChequeBank());
                            sales.setChequeResolved(saleEdit.getChequeResolved());
                            sales.setChequeAmount(saleEdit.getChequeAmount());
                            session.update(sales);
                            Scene scene = InvoiceNoLbl.getScene();
                            TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                            tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
                        }


                    }catch (HibernateException e) {
                        if (tx!=null) tx.rollback();
                        e.printStackTrace();
                    }finally {
                        tx.commit();
                        session.clear();
                    }
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();


    }

    private void assignValues(SalesEntity sales,String bikeNo ){
        Date currentDate = Date.valueOf(saleDateIn.getValue());
        sales.setInvoiceNo(invoice);
        sales.setBikeNo(bikeNo);
        sales.setBikeModal(BikeModalIn.getText());
        sales.setBikeColor(BikeColorIn.getText());
        sales.setOwnerName(OwnerNameIn.getText());
        sales.setOwnerAddress(OwnerAdrIn.getText());
        sales.setOwnerNic(OwnerIDIn.getText());
        sales.setOwnerTpNo(OwnerTPIn1.getText()+","+OwnerTPIn2.getText());
        sales.setFinanceFNo(FinFNo.getText());
        if(!FinTypeIn.isDisabled()){
            sales.setFinanceType(FinTypeIn.getValue().toString());
        }
        else{
            sales.setFinanceType(FinTypeText.getText());
        }
        System.out.println(FinValueIn.getText());
        Double amountDbl = FinValueIn.getText().length()>0 ? Double.parseDouble(FinValueIn.getText()) : 0;
        sales.setFinanceValue(amountDbl);


        sales.setSaleDate(currentDate);
        String docList = "";
        if(DelLetterIn.isSelected()){
            docList = "Deletion Letter" + ",";
        }
        if(TPaperIn.isSelected()){
            docList += "Transfer Paper" + ",";
        }
        if(IDCpIn.isSelected()){
            docList += "ID Copy" + ",";
        }
        if(VICIn.isSelected()){
            docList += "VIC" + ",";
        }
        if(LicenseIn.isSelected()){
            docList += "License" + ",";
        }
        if(KeysIn.isSelected()){
            docList += "Keys" +",";
        }
        if(CRIn.isSelected()){
            docList += "CR" + ",";
        }
        if(InsuranceIn.isSelected()){
            docList += "Insurance" + ",";
        }
        if(serviceCardIn.isSelected()){
            docList += "Service Card" + ",";
        }
        if(manualBookIn.isSelected()){
            docList += "Manual Book" + ",";
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
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    ButtonType okay = new ButtonType("Okay",  ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    // get a handle to the stage
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Changes will be lost. Proceed?",okay,cancel);
                    alert.setTitle("Cancel");
                    alert.setHeaderText("Cancel Action");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get().getButtonData()== ButtonBar.ButtonData.OK_DONE){
                        if(!editable){
                            DeleteEntity();
                        }
                        Scene scene = InvoiceNoLbl.getScene();
                        TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                        tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
                    }
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();


    }

    private void DeleteEntity(){
        Transaction tx = null;
        try{
            session.delete(sl);
            tx = session.beginTransaction();
            tx.commit();;
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.clear();
        }
    }
    private int InvoiceNo(){
        Transaction tx = null;
        invoice = 0;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("select max(invoiceNo) from SalesEntity ");
            if(query.list().get(0)!=null)
            {
                invoice = (int)query.list().get(0) + 1;
                sl = new SalesEntity();
                sl.setInvoiceNo(invoice);
                session.save(sl);
                tx.commit();
            }
            else{
                invoice =1000;
                sl = new SalesEntity();
                sl.setInvoiceNo(invoice);
                session.save(sl);
                tx.commit();
            }
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.clear();
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
            scene.getStylesheets().add(this.getClass().getResource("/BikeShop/css/sale.css").toExternalForm());
            errorTip.getStyleClass().add("errorTip");
            field.setTooltip(errorTip);
        }
        else if(!field.getUserData().toString().equals("errorInvalid") && !field.getText().equals("")){
            ValidField(field);
        }
    }



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
        BikeNoIn1.setText(BikeNoIn1.getText().toUpperCase());
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
        scene.getStylesheets().add(this.getClass().getResource("/BikeShop/css/sale.css").toExternalForm());
        errorTip.getStyleClass().add("errorTip");
        field.setTooltip(errorTip);
    }

}
