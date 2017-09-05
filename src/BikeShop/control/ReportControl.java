package BikeShop.control;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public class ReportControl {
    Session session;
    @FXML
    Label bikesSold, bikesPurchased, bikesStock, totalExp, totalIncome, totalFinances, totalProfit, fromLabel, toLabel, totalArr;
    @FXML
    ChoiceBox periodSelect;
    @FXML
    DatePicker fromDate, toDate;
    public void setSession(Session current){
        session = current;
        periodSelect.getSelectionModel().selectFirst();
    }
    @FXML
    public void initialize(){

        javafx.beans.value.ChangeListener listener = new javafx.beans.value.ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(!newValue.toString().equals("Custom Period")){
                    fromDate.setVisible(false);
                    fromLabel.setVisible(false);
                    toLabel.setVisible(false);
                    toDate.setVisible(false);
                }else if(newValue.toString().equals("Custom Period")){
                    fromDate.setVisible(true);
                    toDate.setVisible(true);
                    fromLabel.setVisible(true);
                    fromLabel.setText("From :");
                    toLabel.setVisible(true);
                }
                if(newValue.toString().equals("Select a Month")){
                    fromLabel.setVisible(true);
                    fromLabel.setText("Month :");
                    fromDate.setVisible(true);
                }else if(newValue.toString().equals("Select a Year")){
                    fromLabel.setText("Year :");
                    fromLabel.setVisible(true);
                    fromDate.setVisible(true);
                }
                if(newValue.toString().equals("This Month")){
                    JFrame alertL = new Loader();
                    Task task = new Task<Void>() {
                        @Override
                        public Void call() {
                            LocalDate from = LocalDate.now().withDayOfMonth(1);
                            HashMap<String,String> data = findByDate(from, LocalDate.now());
                            Platform.runLater(() -> {
                                assignValues(data);
                                alertL.dispose();
                            });
                            return null;
                        }
                    };
                    new Thread(task).start();
                }
                else if(newValue.toString().equals("Today")){
                    JFrame alertL = new Loader();
                    Task task = new Task<Void>() {
                        @Override
                        public Void call() {
                            HashMap<String,String> data = findByDate(LocalDate.now(), LocalDate.now());
                            Platform.runLater(() -> {
                                assignValues(data);
                                alertL.dispose();
                            });
                            return null;
                        }
                    };
                    new Thread(task).start();
                }
            }
        };
        periodSelect.getSelectionModel().selectedItemProperty().addListener(listener);

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
                        Scene scene = bikesSold.getScene();
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

    @FXML private void okReport(){
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Platform.runLater(() -> {
                    Scene scene = bikesSold.getScene();
                    TabPane tabPane = (TabPane) scene.lookup("#MainTabWindow");
                    tabPane.getTabs().remove( tabPane.getSelectionModel().getSelectedIndex() );
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();


    }

    @FXML private void customPeriod(){
        JFrame alertL = new Loader();
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                LocalDate from = fromDate.getValue();
                LocalDate to = toDate.getValue();
                HashMap<String,String> data = findByDate(from, to);
                Platform.runLater(() -> {
                    assignValues(data);
                    alertL.dispose();
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private HashMap<String,String> findByDate(LocalDate from, LocalDate to){
        Query queryPurchase = null;
        try {
            queryPurchase = session.createQuery("select " +
                    "sum(arrearsValue), " +
                    "sum(leaseAmount), " +
                    "sum(otherExpenses), " +
                    "sum(totalValue)," +
                    "count(bikeNo) from PurchasesEntity WHERE DATE_FORMAT(purchaseDate, '%Y-%m-%d') BETWEEN '"+from+"' AND '"+to +"'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object empty[] = new Object[8];
        empty[0] = 0D;
        empty[1] = 0D;
        empty[2] = 0D;
        empty[3] = 0D;
        empty[4] = (long) 0;
        empty[5] = 0D;
        empty[6] = 0;
        empty[7] = 0;
        Object[] purchase;

        Object n [] = (Object[]) queryPurchase.list().get(0);
        if(n[0]!= null){
            purchase = (Object[]) queryPurchase.list().get(0);
        }
        else{
            purchase = empty;
        }

        Query querySale = null;
        try {
            querySale = session.createQuery("select " +
                    "sum(arrearsValue), " +
                    "sum(financeValue), " +
                    "sum(otherExpenses), " +
                    "sum(totalValue)," +
                    "count(bikeNo)," +
                    "sum(leasingValue) " + //hand over amount
                    "from SalesEntity WHERE DATE_FORMAT(saleDate, '%Y-%m-%d') BETWEEN '"+from+"' AND '"+to +"'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object[] sale;
        Object n1 [] = (Object[]) querySale.list().get(0);
        if(n1[0]!= null){
            sale = (Object[]) querySale.list().get(0);
        }
        else{
            sale = empty;
        }

        Query queryStock = null;
        try {
            queryStock = session.createQuery("select " +
                    "sum(arrearsValue), " +
                    "sum(leaseAmount), " +
                    "sum(otherExpenses), " +
                    "sum(totalValue)," +
                    "count(bikeNo) from PurchasesEntity WHERE sold='false' AND  DATE_FORMAT(purchaseDate, '%Y-%m-%d') BETWEEN '"+from+"' AND '"+to +"'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object[] stock;
        Object n2 [] = (Object[]) queryStock.list().get(0);
        if(n2[0]!= null){
            stock = (Object[]) queryStock.list().get(0);
        }
        else{
            stock = empty;
        }

        HashMap<String,String> data = new HashMap<String,String>();
        data.put("bikeSold", Long.toString((long)sale[4]));
        data.put("bikePurchased", Long.toString((long)purchase[4]));
        data.put("bikeStock", Long.toString((long)stock[4]));
        Double exp = (Double) purchase[2] + (Double) purchase[3];
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String expString = decimalFormat.format(exp);
        data.put("Expenses", expString);

        Double income = (Double) sale[5] - (Double)sale[2] - (Double)sale[0];
        String incomeString = decimalFormat.format(income);
        data.put("Income", incomeString );

        Double pendingFin = (Double) sale[1];
        String pendingFinString = decimalFormat.format(pendingFin );
        data.put("PendingFinance", pendingFinString);

        Double profit = (Double)sale[3] - ((Double) purchase[2] + (Double) purchase[3]);
        String profitString = decimalFormat.format(profit);
        data.put("Profit", profitString);

        Double arrears = (Double) sale[0];
        String arrearsString = decimalFormat.format(arrears);
        data.put("Arrears", arrearsString);

        Double netProfit = (Double)sale[3] - ((Double) purchase[2] + (Double) purchase[3]) - (Double) sale[0];
        String netProfitString = decimalFormat.format(netProfit);
        data.put("NetProfit", Double.toString(netProfit));
        // Show all states and data in hashtable.

        return data;
    }

    private void assignValues(HashMap<String,String> data){
        bikesSold.setText(data.get("bikeSold"));
        bikesPurchased.setText(data.get("bikePurchased"));
        bikesStock.setText(data.get("bikeStock"));
        totalExp.setText(data.get("Expenses"));
        totalIncome.setText(data.get("Income"));
        totalFinances.setText(data.get("PendingFinance"));
        totalArr.setText(data.get("Arrears"));
        totalProfit.setText(data.get("Profit"));
    }

     @FXML private void fromDateAction() {
        if(periodSelect.getValue().toString().equals(("Select a Month"))){
            JFrame alertL = new Loader();
            Task task = new Task<Void>() {
                @Override
                public Void call() {
                    LocalDate from = fromDate.getValue().withDayOfMonth(1);
                    LocalDate to = fromDate.getValue().with(lastDayOfMonth());
                    HashMap<String,String> data = findByDate(from, to);
                    Platform.runLater(() -> {
                        assignValues(data);
                        alertL.dispose();
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
        else if(periodSelect.getValue().toString().equals("Select a Year")){
            JFrame alertL = new Loader();
            Task task = new Task<Void>() {
                @Override
                public Void call() {
                    LocalDate from = fromDate.getValue().with(firstDayOfYear());
                    LocalDate to = fromDate.getValue().with(lastDayOfYear());
                    HashMap<String,String> data = findByDate(from, to);
                    Platform.runLater(() -> {
                        assignValues(data);
                        alertL.dispose();
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
        else if(periodSelect.getValue().toString().equals("Custom Period")){
            if(toDate.getValue()!= null){
                customPeriod();
            }
        }
    }
}
