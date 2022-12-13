import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

public class Coupons {

    JSONObject coupon = (JSONObject) ShopCartUtil.jsonParser.parse(new FileReader("./Resources/coupons.json"));
    private String couponName;
    private long appliedSku;
    private BigDecimal discountPrice;

    public Coupons() throws IOException, ParseException {
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public long getAppliedSku() {
        return appliedSku;
    }

    public void setAppliedSku(long appliedSku) {
        this.appliedSku = appliedSku;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public boolean isEligible(Items inventoryItemName) {
        return false;
    }

//    public BigDecimal getDiscountedPrice(double itemSku) {
//        return coupon.getString(String.valueOf(itemSku));
//    }
}