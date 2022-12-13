import java.math.BigDecimal;

public class Items {

    private String itemName;
    private long sku;
    private BigDecimal price;
    private boolean isTaxable;
    private boolean ownBrand;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getSku() {
        return sku;
    }

    public void setSku(long sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isTaxable() {
        return isTaxable;
    }

    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
    }

    public boolean isOwnBrand() {
        return ownBrand;
    }

    public void setOwnBrand(boolean ownBrand) {
        this.ownBrand = ownBrand;
    }
}
