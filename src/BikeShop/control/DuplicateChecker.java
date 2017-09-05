package BikeShop.control;

import BikeShop.HibernateInit;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;

public class DuplicateChecker {
    public static void main(String[] args) {
        Session session = HibernateInit.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        /*CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PurchasesEntity> cq = cb.createQuery(BikeShop.Entity.PurchasesEntity.class);
        Root<PurchasesEntity> criteriaSale = cq.from(PurchasesEntity.class);
        cq.select(criteriaSale);
        TypedQuery<PurchasesEntity> query = session.createQuery(cq);
        List<PurchasesEntity> list = query.getResultList();
        for(PurchasesEntity item:list){
        Query queryEach = session.createQuery("select invoiceNo,bikeNo from PurchasesEntity where bikeNo='" + item.getBikeNo()+"' and sold!='false' and invoiceNo!="+item.getInvoiceNo());
            if(queryEach.list().size()>0) {
                Query querySales = session.createQuery("select invoiceNo from SalesEntity where bikeNo='" + item.getBikeNo()+"'");
                SalesEntity sl = session.get(SalesEntity.class, (int) querySales.list().get(0));
                item.setSaleInvoice(sl.getInvoiceNo());
                sl.setPurchaseInvoice(item.getInvoiceNo());
                session.update(sl);
                session.update(item);
                session.flush();
            }

            if(item.getSaleInvoice() == null){
                Query querySales = session.createQuery("select invoiceNo from SalesEntity where bikeNo='" + item.getBikeNo()+"'");
                if( querySales.list().size()==1){
                    SalesEntity sl = session.get(SalesEntity.class, (int) querySales.list().get(0));
                    sl.setPurchaseInvoice(item.getInvoiceNo());
                    item.setSaleInvoice(sl.getInvoiceNo());
                    session.update(sl);
                    session.update(item);
                    session.flush();
                    System.out.println("SL :" +sl.getInvoiceNo() +" P =>"+ item.getInvoiceNo());
                }
            };
        }
        tx.commit();
        session.clear();
        CriteriaQuery<SalesEntity> cqSale = cb.createQuery(BikeShop.Entity.SalesEntity.class);
        Root<SalesEntity> criteriaSales = cq.from(SalesEntity.class);
        cqSale.select(criteriaSales);
        TypedQuery<SalesEntity> querySale = session.createQuery(cqSale);
        List<SalesEntity> listSale = querySale.getResultList();
        for(SalesEntity item:listSale){
            Query queryEach = session.createQuery("select bikeNo from PurchasesEntity where bikeNo='" + item.getBikeNo()+"' and sold='false'");
            if(queryEach.list().size()==1) System.out.println(item.getBikeNo());
        }*/
        LocalDate from = LocalDate.now().withDayOfMonth(1);
        LocalDate to = LocalDate.now();
        HashMap<String,String> data = findByDate(LocalDate.now(), LocalDate.now(), session);
        System.out.println(data.get("bikeSold"));
        System.out.println(data.get("bikePurchased"));
       System.out.println(data.get("bikeStock"));
        System.out.println(data.get("Expenses"));
        System.out.println(data.get("Income"));
        System.out.println(data.get("PendingFinance"));
        System.out.println(data.get("Arrears"));
        System.out.println(data.get("Profit"));

    }

    private static HashMap<String,String> findByDate(LocalDate from, LocalDate to, Session session){
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
}
