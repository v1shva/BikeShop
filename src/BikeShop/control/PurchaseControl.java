package BikeShop.control;

import BikeShop.Entity.PurchasesEntity;
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
import java.time.LocalDate;
import java.util.*;



public class PurchaseControl {
    private int invoice;
    private boolean editable = false;
    private ChangeListener listener;
    Locale locale ;
    ResourceBundle rb ;
    PurchasesEntity pe;
    Tab currentTab;

    @FXML
    Label InvoiceNoLb2;
    @FXML
    TextField BikeNoIn1,BikeNoIn2,BikeModalIn,BikeColorIn,OwnerNameIn, OwnerAdrIn,OwnerIDIn,OwnerTPIn1,OwnerTPIn2;
    @FXML
    TextField  TotalAmntIn,OtherExpIn, ArrearsIn, FinFNo,FinFNo1, FinValueIn;//FinFNo==dno,FinFNo1=name
    @FXML
    ChoiceBox  BikeNoDash, BikeNoProvince;
    @FXML
    TextArea OtherInfoIn;
    @FXML
    CheckBox DelLetterIn,VICIn,CRIn,TPaperIn,LicenseIn,InsuranceIn,IDCpIn, KeysIn;
    @FXML
    DatePicker purchaseDateIn;


    public void setValues(int invoiceNo, String bikeNo, String bikeMoadal, String bikeColor, String ownerName, String ownerAdr, String ownerID, String ownerTP,
                          Double leasedAmnt, Double TotalAmnt, Double otherExp, Double arrears, String FinFNoVal, String FinType,
                          String otherInfo, String docList, Date saleDate, boolean edit){
        invoice = invoiceNo;
        InvoiceNoLb2.setText("Invoice No. "+invoice);
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
            OwnerTPIn1.setText(ownerTPNos[1]);
        }
        else {
            OwnerTPIn1.setText(ownerTP);
        }
        TotalAmntIn.setText(TotalAmnt+"");
        OtherExpIn.setText(otherExp+"");
        ArrearsIn.setText(arrears+"");
        FinFNo.setText(FinFNoVal);
        FinValueIn.setText(leasedAmnt+"");
        FinFNo1.setText(FinType);
        OtherInfoIn.setText(otherInfo);
        purchaseDateIn.setValue(saleDate.toLocalDate());
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

    }

    public void postInitialize(){
        purchaseDateIn.setValue(LocalDate.now());
        InvoiceNoLb2.setText("Invoice No. "+ InvoiceNo());
    }
    @FXML
    public void initialize() {
        //date initialize
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

    }

    @FXML
    private void addPurchase(){
        Session session = HibernateInit.getSessionFactory().openSession();
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
            if(!editable){
                Query query = session.createQuery("from PurchasesEntity where bikeNo='"+bikeNo+"'" + "AND sold='false'");
                if(query.list().size()!=1){
                    assignValues(purchases,bikeNo);
                    session.update(purchases);
                    tx.commit();
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
                tx.commit();
                Scene scene = InvoiceNoLb2.getScene();
                TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
            }


        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    private void assignValues(PurchasesEntity purchase,String bikeNo ){
        Date currentDate = Date.valueOf(purchaseDateIn.getValue());
        purchase.setSold("false");
        purchase.setInvoiceNo(invoice);
        purchase.setBikeNo(bikeNo);
        purchase.setBikeModal(BikeModalIn.getText());
        purchase.setBikeColor(BikeColorIn.getText());
        purchase.setOwnerName(OwnerNameIn.getText());
        purchase.setOwnerAddress(OwnerAdrIn.getText());
        purchase.setOwnerNic(OwnerIDIn.getText());
        purchase.setOwnerTpNo(OwnerTPIn1.getText()+","+OwnerTPIn2.getText());
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

    }
    private void DeleteEntity(){
        Session session = HibernateInit.getSessionFactory().openSession();
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
            session.close();
        }
    }
    private int InvoiceNo(){
        Session session = HibernateInit.getSessionFactory().openSession();
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
