package BikeShop.Entity;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "sales", schema = "bikedb", catalog = "")
public class SalesEntity {
    private int invoiceNo;
    private Double arrearsValue;
    private String bikeColor;
    private String bikeModal;
    private String bikeNo;
    private String docList;
    private String financeFNo;
    private String financeType;
    private Double financeValue;
    private Double otherExpenses;
    private String otherInfo;
    private String ownerAddress;
    private String ownerName;
    private String ownerNic;
    private String ownerTpNo;
    private Double totalValue;
    private Boolean checked;
    private boolean editLock;
    private Double leasingValue;
    private Date saleDate;
    private Byte chequeResolved;
    private String chequeBank;
    private String chequeNumber;
    private Date chequeDate;
    private Double chequeAmount;
    private String tax;



    @Id
    @Column(name = "invoiceNo", nullable = false)
    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    @Basic
    @Column(name = "arrearsValue", nullable = true, precision = 0)
    public Double getArrearsValue() {
        return arrearsValue;
    }

    public void setArrearsValue(Double arrearsValue) {
        this.arrearsValue = arrearsValue;
    }

    @Basic
    @Column(name = "bikeColor", nullable = true, length = 255)
    public String getBikeColor() {
        return bikeColor;
    }

    public void setBikeColor(String bikeColor) {
        this.bikeColor = bikeColor;
    }

    @Basic
    @Column(name = "bikeModal", nullable = true, length = 255)
    public String getBikeModal() {
        return bikeModal;
    }

    public void setBikeModal(String bikeModal) {
        this.bikeModal = bikeModal;
    }

    @Basic
    @Column(name = "bikeNo", nullable = true, length = 255)
    public String getBikeNo() {
        return bikeNo;
    }

    public void setBikeNo(String bikeNo) {
        this.bikeNo = bikeNo;
    }

    @Basic
    @Column(name = "docList", nullable = true, length = 255)
    public String getDocList() {
        return docList;
    }

    public void setDocList(String docList) {
        this.docList = docList;
    }

    @Basic
    @Column(name = "financeFNo", nullable = true, length = 255)
    public String getFinanceFNo() {
        return financeFNo;
    }

    public void setFinanceFNo(String financeFNo) {
        this.financeFNo = financeFNo;
    }

    @Basic
    @Column(name = "financeType", nullable = true, length = 255)
    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    @Basic
    @Column(name = "financeValue", nullable = true, precision = 0)
    public Double getFinanceValue() {
        return financeValue;
    }

    public void setFinanceValue(Double financeValue) {
        this.financeValue = financeValue;
    }

    @Basic
    @Column(name = "otherExpenses", nullable = true, precision = 0)
    public Double getOtherExpenses() {
        return otherExpenses;
    }

    public void setOtherExpenses(Double otherExpenses) {
        this.otherExpenses = otherExpenses;
    }

    @Basic
    @Column(name = "otherInfo", nullable = true, length = 255)
    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    @Basic
    @Column(name = "ownerAddress", nullable = true, length = 255)
    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    @Basic
    @Column(name = "ownerName", nullable = true, length = 255)
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Basic
    @Column(name = "ownerNic", nullable = true, length = 255)
    public String getOwnerNic() {
        return ownerNic;
    }

    public void setOwnerNic(String ownerNic) {
        this.ownerNic = ownerNic;
    }

    @Basic
    @Column(name = "ownerTpNo", nullable = true, length = 255)
    public String getOwnerTpNo() {
        return ownerTpNo;
    }

    public void setOwnerTpNo(String ownerTpNo) {
        this.ownerTpNo = ownerTpNo;
    }

    @Basic
    @Column(name = "totalValue", nullable = true, precision = 0)
    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    @Basic
    @Column(name = "checked", nullable = true)
    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Basic
    @Column(name = "editLock", nullable = false)
    public boolean isEditLock() {
        return editLock;
    }

    public void setEditLock(boolean editLock) {
        this.editLock = editLock;
    }

    @Basic
    @Column(name = "leasingValue", nullable = true, precision = 0)
    public Double getLeasingValue() {
        return leasingValue;
    }

    public void setLeasingValue(Double leasingValue) {
        this.leasingValue = leasingValue;
    }

    @Basic
    @Column(name = "saleDate", nullable = true)
    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    @Basic
    @Column(name = "chequeResolved", nullable = true)
    public Byte getChequeResolved() {
        return chequeResolved;
    }

    public void setChequeResolved(Byte chequeResolved) {
        this.chequeResolved = chequeResolved;
    }

    @Basic
    @Column(name = "chequeBank", nullable = true, length = 15)
    public String getChequeBank() {
        return chequeBank;
    }

    public void setChequeBank(String chequeBank) {
        this.chequeBank = chequeBank;
    }

    @Basic
    @Column(name = "chequeNumber", nullable = true, length = 30)
    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    @Basic
    @Column(name = "chequeDate", nullable = true)
    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalesEntity that = (SalesEntity) o;

        if (invoiceNo != that.invoiceNo) return false;
        if (editLock != that.editLock) return false;
        if (arrearsValue != null ? !arrearsValue.equals(that.arrearsValue) : that.arrearsValue != null) return false;
        if (bikeColor != null ? !bikeColor.equals(that.bikeColor) : that.bikeColor != null) return false;
        if (bikeModal != null ? !bikeModal.equals(that.bikeModal) : that.bikeModal != null) return false;
        if (bikeNo != null ? !bikeNo.equals(that.bikeNo) : that.bikeNo != null) return false;
        if (docList != null ? !docList.equals(that.docList) : that.docList != null) return false;
        if (financeFNo != null ? !financeFNo.equals(that.financeFNo) : that.financeFNo != null) return false;
        if (financeType != null ? !financeType.equals(that.financeType) : that.financeType != null) return false;
        if (financeValue != null ? !financeValue.equals(that.financeValue) : that.financeValue != null) return false;
        if (otherExpenses != null ? !otherExpenses.equals(that.otherExpenses) : that.otherExpenses != null)
            return false;
        if (otherInfo != null ? !otherInfo.equals(that.otherInfo) : that.otherInfo != null) return false;
        if (ownerAddress != null ? !ownerAddress.equals(that.ownerAddress) : that.ownerAddress != null) return false;
        if (ownerName != null ? !ownerName.equals(that.ownerName) : that.ownerName != null) return false;
        if (ownerNic != null ? !ownerNic.equals(that.ownerNic) : that.ownerNic != null) return false;
        if (ownerTpNo != null ? !ownerTpNo.equals(that.ownerTpNo) : that.ownerTpNo != null) return false;
        if (totalValue != null ? !totalValue.equals(that.totalValue) : that.totalValue != null) return false;
        if (checked != null ? !checked.equals(that.checked) : that.checked != null) return false;
        if (leasingValue != null ? !leasingValue.equals(that.leasingValue) : that.leasingValue != null) return false;
        if (saleDate != null ? !saleDate.equals(that.saleDate) : that.saleDate != null) return false;
        if (chequeResolved != null ? !chequeResolved.equals(that.chequeResolved) : that.chequeResolved != null)
            return false;
        if (chequeBank != null ? !chequeBank.equals(that.chequeBank) : that.chequeBank != null) return false;
        if (chequeNumber != null ? !chequeNumber.equals(that.chequeNumber) : that.chequeNumber != null) return false;
        if (chequeDate != null ? !chequeDate.equals(that.chequeDate) : that.chequeDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = invoiceNo;
        result = 31 * result + (arrearsValue != null ? arrearsValue.hashCode() : 0);
        result = 31 * result + (bikeColor != null ? bikeColor.hashCode() : 0);
        result = 31 * result + (bikeModal != null ? bikeModal.hashCode() : 0);
        result = 31 * result + (bikeNo != null ? bikeNo.hashCode() : 0);
        result = 31 * result + (docList != null ? docList.hashCode() : 0);
        result = 31 * result + (financeFNo != null ? financeFNo.hashCode() : 0);
        result = 31 * result + (financeType != null ? financeType.hashCode() : 0);
        result = 31 * result + (financeValue != null ? financeValue.hashCode() : 0);
        result = 31 * result + (otherExpenses != null ? otherExpenses.hashCode() : 0);
        result = 31 * result + (otherInfo != null ? otherInfo.hashCode() : 0);
        result = 31 * result + (ownerAddress != null ? ownerAddress.hashCode() : 0);
        result = 31 * result + (ownerName != null ? ownerName.hashCode() : 0);
        result = 31 * result + (ownerNic != null ? ownerNic.hashCode() : 0);
        result = 31 * result + (ownerTpNo != null ? ownerTpNo.hashCode() : 0);
        result = 31 * result + (totalValue != null ? totalValue.hashCode() : 0);
        result = 31 * result + (checked != null ? checked.hashCode() : 0);
        result = 31 * result + (editLock ? 1 : 0);
        result = 31 * result + (leasingValue != null ? leasingValue.hashCode() : 0);
        result = 31 * result + (saleDate != null ? saleDate.hashCode() : 0);
        result = 31 * result + (chequeResolved != null ? chequeResolved.hashCode() : 0);
        result = 31 * result + (chequeBank != null ? chequeBank.hashCode() : 0);
        result = 31 * result + (chequeNumber != null ? chequeNumber.hashCode() : 0);
        result = 31 * result + (chequeDate != null ? chequeDate.hashCode() : 0);
        return result;
    }


    @Basic
    @Column(name = "chequeAmount", nullable = true, precision = 0)
    public Double getChequeAmount() {
        return chequeAmount;
    }

    public void setChequeAmount(Double chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    @Basic
    @Column(name = "tax", nullable = true, length = 10)
    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
