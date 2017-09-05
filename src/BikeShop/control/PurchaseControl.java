package BikeShop.control;

import BikeShop.Entity.PurchasesEntity;
import BikeShop.Entity.SalesEntity;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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



public class PurchaseControl {
    private int invoice;
    private boolean editable = false;
    private Session session;
    Locale locale ;
    ResourceBundle rb ;
    PurchasesEntity pe;
    Byte taxUser;
    //Cheque Details


    @FXML
    Label InvoiceNoLb2, engineChLabel, bikeNoLabel;
    @FXML
    TextField BikeNoIn1,BikeNoIn2,BikeModalIn,BikeColorIn,OwnerNameIn, OwnerAdrIn,OwnerIDIn,OwnerTPIn1,OwnerTPIn2, engineChIn;
    @FXML
    TextField  TotalAmntIn,OtherExpIn, ArrearsIn, FinFNo,FinFNo1, FinValueIn;//FinFNo==dno,FinFNo1=name
    @FXML
    ChoiceBox  BikeNoDash, BikeNoProvince, regStatus;
    @FXML
    TextArea OtherInfoIn;
    @FXML
    CheckBox DelLetterIn,VICIn,CRIn,TPaperIn,LicenseIn,InsuranceIn,IDCpIn, KeysIn, serviceCardIn, manualBookIn;
    @FXML
    DatePicker purchaseDateIn;


    public void setValues(boolean edit, PurchasesEntity currentPurchase){
        try{
            invoice = currentPurchase.getInvoiceNo();
            InvoiceNoLb2.setText("Invoice No. "+invoice);
            if(currentPurchase.getUnregistered() == Byte.valueOf("1")){
                regStatus.getSelectionModel().selectLast();
            }
            if(currentPurchase.getUnregistered() == Byte.valueOf("1")){
                regStatus.getSelectionModel().selectLast();
                engineChIn.setText(currentPurchase.getBikeNo());
            }
            else{
                String bikeNos[] = currentPurchase.getBikeNo().split("\\s+");
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
                            BikeNoIn1.setText(DashNos[0]);
                            BikeNoIn2.setText(DashNos[1]);
                        }
                    }
                    else if(SRINos.length==2){
                        BikeNoDash.setValue("SRI");
                        BikeNoIn1.setText(SRINos[0]);
                        BikeNoIn2.setText(SRINos[1]);
                    }
                }
            }

            BikeModalIn.setText(currentPurchase.getBikeModal());
            BikeColorIn.setText(currentPurchase.getBikeColor());
            OwnerNameIn.setText(currentPurchase.getOwnerName());
            OwnerAdrIn.setText(currentPurchase.getOwnerAddress());
            OwnerIDIn.setText(currentPurchase.getOwnerNic());
            String ownerTPNos [] = currentPurchase.getOwnerTpNo().split(",");
            if(ownerTPNos.length==2){
                OwnerTPIn1.setText(ownerTPNos[0]);
                OwnerTPIn2.setText(ownerTPNos[1]);
            }
            else {
                OwnerTPIn1.setText(currentPurchase.getOwnerTpNo());
            }
            TotalAmntIn.setText(currentPurchase.getTotalValue()+"");
            OtherExpIn.setText(currentPurchase.getOtherExpenses()+"");
            ArrearsIn.setText(currentPurchase.getArrearsValue()+"");
            FinFNo.setText(currentPurchase.getLeaseDNo());
            FinValueIn.setText(currentPurchase.getLeaseAmount()+"");
            FinFNo1.setText(currentPurchase.getLeasersName());
            OtherInfoIn.setText(currentPurchase.getOtherInfo());
            purchaseDateIn.setValue(currentPurchase.getPurchaseDate().toLocalDate());
            editable = edit;
            List<String> docListItems  = Arrays.asList(currentPurchase.getDocList().split(","));
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

        }

    }

    public void setSession(Session current, Tab currentTab){
        session = current;
        currentTab.setOnCloseRequest(e -> {
            e.consume();
            CancelTab();
        });
    }

    public void postInitialize(Byte taxValue){
        purchaseDateIn.setValue(LocalDate.now());
        InvoiceNoLb2.setText("Invoice No. "+ InvoiceNo());
        taxUser= taxValue;
        Scene scene = InvoiceNoLb2.getScene();

    }

    private void toggleRegStatus(){
        if (regStatus.getValue().toString().equals("Registered")){
            engineChLabel.setVisible(false);
            engineChIn.setVisible(false);
            bikeNoLabel.setVisible(true);
            BikeNoIn1.setVisible(true);
            BikeNoIn2.setVisible(true);
            BikeNoDash.setVisible(true);
            BikeNoProvince.setVisible(true);
        }
        else {
            engineChLabel.setVisible(true);
            engineChIn.setVisible(true);
            bikeNoLabel.setVisible(false);
            BikeNoIn1.setVisible(false);
            BikeNoIn2.setVisible(false);
            BikeNoDash.setVisible(false);
            BikeNoProvince.setVisible(false);
        }
    }
    @FXML
    public void initialize() {
        //date initialize
        ChangeListener listener, regStatusListener;
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

        listener = (observable, oldValue, newValue) -> {
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
        };
        BikeNoProvince.getSelectionModel().selectedItemProperty().addListener(listener);
        regStatusListener = (observable, oldValue, newValue) -> {
           toggleRegStatus();
        };
        regStatus.getSelectionModel().selectedItemProperty().addListener(regStatusListener);
        regStatus.getSelectionModel().selectFirst();

    }

    @FXML
    private void addPurchase(){
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    Transaction tx = null;
                    try{
                        tx = session.beginTransaction();
                        PurchasesEntity purchases = new PurchasesEntity();
                        String bikeNo = "";
                        if(BikeNoProvince.getValue().toString().equals("")){
                            bikeNo = BikeNoIn1.getText()+BikeNoDash.getValue().toString()+BikeNoIn2.getText();
                        }
                        else{
                            bikeNo = BikeNoProvince.getValue().toString().toUpperCase()+" " + BikeNoIn1.getText()+BikeNoDash.getValue().toString()+BikeNoIn2.getText();
                        }
                        if (!regStatus.getValue().toString().equals("Registered")){
                            bikeNo = engineChIn.getText();
                            purchases.setUnregistered(Byte.valueOf("1"));
                        }
                        if(!editable){
                            Query query = session.createQuery("from PurchasesEntity where bikeNo='"+bikeNo+"'" + "AND sold='false'");
                            if(query.list().size()!=1){
                                assignValues(purchases,bikeNo);
                                session.update(purchases);
                                Scene scene = InvoiceNoLb2.getScene();
                                TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                                tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
                            }
                            else{
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Bike is in Stocks");
                                alert.setHeaderText("Bike you're trying to sell is already in our stocks");
                                alert.showAndWait();
                            }
                        }
                        else {
                            assignValues(purchases,bikeNo);
                            session.update(purchases);
                            Scene scene = InvoiceNoLb2.getScene();
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

    private void assignValues(PurchasesEntity purchase,String bikeNo ){
        Date currentDate = Date.valueOf(purchaseDateIn.getValue());
        if(taxUser == Byte.valueOf("1")){
            purchase.setTax(Byte.valueOf("1"));
            if(purchase.getSaleInvoice() != null){
                SalesEntity sale = session.get(SalesEntity.class,purchase.getSaleInvoice());
                if(sale != null){
                    sale.setPurchaseTax(Byte.valueOf("1"));
                    session.update(sale);
                }
            }
        }
        purchase.setSold("false");
        purchase.setInvoiceNo(invoice);
        purchase.setBikeNo(bikeNo);
        purchase.setBikeModal(BikeModalIn.getText());
        purchase.setBikeColor(BikeColorIn.getText());
        purchase.setOwnerName(OwnerNameIn.getText());
        purchase.setOwnerAddress(OwnerAdrIn.getText());
        purchase.setOwnerNic(OwnerIDIn.getText());
        if(OwnerTPIn2.getText().equals("")){
            purchase.setOwnerTpNo(OwnerTPIn1.getText());
        }
        else{
            purchase.setOwnerTpNo(OwnerTPIn1.getText()+","+OwnerTPIn2.getText());
        }
        purchase.setLeaseDNo(FinFNo.getText());
        purchase.setLeasersName(FinFNo1.getText());
        Double amountDbl = FinValueIn.getText().length()>0 ? Double.parseDouble(FinValueIn.getText()) : 0;
        purchase.setLeaseAmount(amountDbl);
        purchase.setPurchaseDate(currentDate);
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
        purchase.setDocList(docList);
        purchase.setOtherInfo(OtherInfoIn.getText());
        amountDbl = TotalAmntIn.getText().length()>0 ? Double.parseDouble(TotalAmntIn.getText()) : 0;
        purchase.setTotalValue(amountDbl);
        amountDbl = OtherExpIn.getText().length()>0 ? Double.parseDouble(OtherExpIn.getText()) : 0;
        purchase.setOtherExpenses(amountDbl);
        amountDbl = ArrearsIn.getText().length()>0 ? Double.parseDouble(ArrearsIn.getText()) : 0;
        purchase.setArrearsValue(amountDbl);

    }

    @FXML
    private void ToggleFinance(){
        if(FinFNo.isDisabled()){
            FinFNo.setDisable(false);
            FinFNo1.setDisable(false);
            FinValueIn.setDisable(false);
        }
        else{
            FinFNo.setDisable(true);
            FinFNo1.setDisable(true);
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
                        Scene scene = InvoiceNoLb2.getScene();
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
        System.out.println("here");
        try{
            session.delete(pe);
            tx = session.beginTransaction();
            tx.commit();
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
            Query query = session.createQuery("select max(invoiceNo) from PurchasesEntity ");
            if(query.list().get(0)!=null)
            {
                invoice = (int)query.list().get(0) + 1;
                pe = new PurchasesEntity();
                pe.setInvoiceNo(invoice);
                session.save(pe);
                tx.commit();
            }
            else{
                invoice =1000;
                pe = new PurchasesEntity();
                pe.setInvoiceNo(invoice);
                session.save(pe);
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
