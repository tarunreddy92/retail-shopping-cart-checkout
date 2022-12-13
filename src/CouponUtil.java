import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CouponUtil {

    //  applies coupons to a shopping cart
    public static void applyCoupons(List<Items> itemsList) throws IOException, ParseException {
        List<Coupons> couponsList = getCoupons();

        List<Long> couponItemSkuList = new ArrayList<>();
        for (Coupons coupon: couponsList) {
            couponItemSkuList.add(coupon.getAppliedSku());
        }

        List<Long> itemCartSkuList = new ArrayList<>();
        for (Items item: itemsList) {
            long cartItemSku = item.getSku();
            if (couponItemSkuList.contains(cartItemSku)) {
                Coupons couponObj = new Coupons();
//                couponObj.setDiscountPrice(couponsList.indexOf());
                List<Coupons> couponObjList;
//                couponObjList.add()
                System.out.println("Coupon found for " + item.getItemName());
                System.out.println("Coupon Discount price " + couponObj.getDiscountPrice());
//                coupon.getDiscountPrice();
                item.setPrice(couponObj.getDiscountPrice());
            }
        }

//        for (Coupons coupon: ShoppingCart.couponList) {
//            long couponItemSku = coupon.getAppliedSku();
//            if (itemCartSkuList.contains(couponItemSku)) {
//                System.out.println("Coupon found for " + coupon.getCouponName());
//                coupon.getDiscountPrice();
//                itemsList.indexOf(1);
//            }
//        }

//        List<BigDecimal> cartSkuList = ShopCartUtil.getCartSkus();
    }

    public static List<Coupons> getCoupons() throws IOException, ParseException {
        JSONArray couponArray = getCouponArray();

        try {
            for (Object obj : couponArray) {
                Coupons coupon = new Coupons();
                JSONObject jsonObject = (JSONObject) obj;
                coupon.setCouponName(jsonObject.get("couponName").toString());
                coupon.setDiscountPrice(BigDecimal.valueOf((Double) jsonObject.get("discountPrice")));
                coupon.setAppliedSku((Long) jsonObject.get("appliedSku"));

                ShoppingCart.couponList.add(coupon);
            }
        } catch (Exception e) {
            System.out.println("Coupons do not exist.");
        }
        return ShoppingCart.couponList;
    }

    public static JSONArray getCouponArray() throws IOException, ParseException {
        String couponFile = "/Users/tarunreddynukala/Repos/Shopping_Cart_HEB/src/Resources/coupons.json";
        JSONObject couponObj = (JSONObject) ShopCartUtil.jsonParser.parse(new FileReader(couponFile));
        return (JSONArray) couponObj.get("coupons");
    }
}
