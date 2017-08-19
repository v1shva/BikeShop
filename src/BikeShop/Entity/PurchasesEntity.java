package BikeShop.Entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "purchases", schema = "bikedb", catalog = "")
public class PurchasesEntity {
    private int invoiceNo;
    private Double arrearsValue;
    private String bikeColor;
    private String bikeModal;
    private String bikeNo;
    private Boolean checked;
    private String docList;
    private String leaseDNo;
    private Double leaseAmount;
    private String leasersName;
    private Double otherExpenses;
    private String otherInfo;
    private String ownerAddress;
    private String ownerName;
    private String ownerNic;
    private String ownerTpNo;
    private Double totalValue;
    private Date purchaseDate;
    private String sold;
    private Byte tax;
    private Byte unregistered;
    private Integer saleInvoice;

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
    @Column(name = "checked", nullable = true)
    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
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
    @Column(name = "LeaseDNo", nullable = true, length = 255)
    public String getLeaseDNo() {
        return leaseDNo;
    }

    public void setLeaseDNo(String leaseDNo) {
        this.leaseDNo = leaseDNo;
    }

    @Basic
    @Column(name = "LeaseAmount", nullable = true, precision = 0)
    public Double getLeaseAmount() {
        return leaseAmount;
    }

    public void setLeaseAmount(Double leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    @Basic
    @Column(name = "LeasersName", nullable = true, length = 255)
    public String getLeasersName() {
        return leasersName;
    }

    public void setLeasersName(String leasersName) {
        this.leasersName = leasersName;
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
    @Column(name = "purchaseDate", nullable = true)
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Basic
    @Column(name = "sold", nullable = true, length = 255)
    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    @Basic
    @Column(name = "tax", nullable = true)
    public Byte getTax() {
        return tax;
    }

    public void setTax(Byte tax) {
        this.tax = tax;
    }

    @Basic
    @Column(name = "unregistered", nullable = true)
    public Byte getUnregistered() {
        return unregistered;
    }

    public void setUnregistered(Byte unregistered) {
        this.unregistered = unregistered;
    }

    @Basic
    @Column(name = "saleInvoice", nullable = true)
    public Integer getSaleInvoice() {
        return saleInvoice;
    }

    public void setSaleInvoice(Integer saleInvoice) {
        this.saleInvoice = saleInvoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchasesEntity that = (PurchasesEntity) o;

        if (invoiceNo != that.invoiceNo) return false;
        if (arrearsValue != null ? !arrearsValue.equals(that.arrearsValue) : that.arrearsValue != null) return false;
        if (bikeColor != null ? !bikeColor.equals(that.bikeColor) : that.bikeColor != null) return false;
        if (bikeModal != null ? !bikeModal.equals(that.bikeModal) : that.bikeModal != null) return false;
        if (bikeNo != null ? !bikeNo.equals(that.bikeNo) : that.bikeNo != null) return false;
        if (checked != null ? !checked.equals(that.checked) : that.checked != null) return false;
        if (docList != null ? !docList.equals(that.docList) : that.docList != null) return false;
        if (leaseDNo != null ? !leaseDNo.equals(that.leaseDNo) : that.leaseDNo != null) return false;
        if (leaseAmount != null ? !leaseAmount.equals(that.leaseAmount) : that.leaseAmount != null) return false;
        if (leasersName != null ? !leasersName.equals(that.leasersName) : that.leasersName != null) return false;
        if (otherExpenses != null ? !otherExpenses.equals(that.otherExpenses) : that.otherExpenses != null)
            return false;
        if (otherInfo != null ? !otherInfo.equals(that.otherInfo) : that.otherInfo != null) return false;
        if (ownerAddress != null ? !ownerAddress.equals(that.ownerAddress) : that.ownerAddress != null) return false;
        if (ownerName != null ? !ownerName.equals(that.ownerName) : that.ownerName != null) return false;
        if (ownerNic != null ? !ownerNic.equals(that.ownerNic) : that.ownerNic != null) return false;
        if (ownerTpNo != null ? !ownerTpNo.equals(that.ownerTpNo) : that.ownerTpNo != null) return false;
        if (totalValue != null ? !totalValue.equals(that.totalValue) : that.totalValue != null) return false;
        if (purchaseDate != null ? !purchaseDate.equals(that.purchaseDate) : that.purchaseDate != null) return false;
        if (sold != null ? !sold.equals(that.sold) : that.sold != null) return false;
        if (tax != null ? !tax.equals(that.tax) : that.tax != null) return false;
        if (unregistered != null ? !unregistered.equals(that.unregistered) : that.unregistered != null) return false;
        if (saleInvoice != null ? !saleInvoice.equals(that.saleInvoice) : that.saleInvoice != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = invoiceNo;
        result = 31 * result + (arrearsValue != null ? arrearsValue.hashCode() : 0);
        result = 31 * result + (bikeColor != null ? bikeColor.hashCode() : 0);
        result = 31 * result + (bikeModal != null ? bikeModal.hashCode() : 0);
        result = 31 * result + (bikeNo != null ? bikeNo.hashCode() : 0);
        result = 31 * result + (checked != null ? checked.hashCode() : 0);
        result = 31 * result + (docList != null ? docList.hashCode() : 0);
        result = 31 * result + (leaseDNo != null ? leaseDNo.hashCode() : 0);
        result = 31 * result + (leaseAmount != null ? leaseAmount.hashCode() : 0);
        result = 31 * result + (leasersName != null ? leasersName.hashCode() : 0);
        result = 31 * result + (otherExpenses != null ? otherExpenses.hashCode() : 0);
        result = 31 * result + (otherInfo != null ? otherInfo.hashCode() : 0);
        result = 31 * result + (ownerAddress != null ? ownerAddress.hashCode() : 0);
        result = 31 * result + (ownerName != null ? ownerName.hashCode() : 0);
        result = 31 * result + (ownerNic != null ? ownerNic.hashCode() : 0);
        result = 31 * result + (ownerTpNo != null ? ownerTpNo.hashCode() : 0);
        result = 31 * result + (totalValue != null ? totalValue.hashCode() : 0);
        result = 31 * result + (purchaseDate != null ? purchaseDate.hashCode() : 0);
        result = 31 * result + (sold != null ? sold.hashCode() : 0);
        result = 31 * result + (tax != null ? tax.hashCode() : 0);
        result = 31 * result + (unregistered != null ? unregistered.hashCode() : 0);
        result = 31 * result + (saleInvoice != null ? saleInvoice.hashCode() : 0);
        return result;
    }
}
