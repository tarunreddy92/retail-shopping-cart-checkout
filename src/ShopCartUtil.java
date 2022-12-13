import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ShopCartUtil {
    // tax rate in decimal form (e.g. 0.0825 for 8.25%)
    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.0825);
    static JSONParser jsonParser = new JSONParser();

    // list of coupons
    private final List<Coupons> coupons;

    // constructor that takes a list of coupons
    public ShopCartUtil(List<Coupons> coupons) {
        this.coupons = coupons;
    }

    public static void checkoutHelper(List<Items> itemCart) throws IOException, ParseException {
        int checkoutInput;
        do {
            System.out.println("\nCHECKOUT & PAY \n" +
                    "Please choose from the following options: \n" +
                    "[1] Get cart total \n" +
                    "[2] Get cart grand total with tax for all items \n" +
                    "[3] Get cart grand total with tax for taxable items \n" +
                    "[4] Apply coupons and get grand total for taxable items \n" +
                    "[5] Go back and add more items \n" +
                    "[0] Exit");

            System.out.print("Choose a checkout option: ");

            checkoutInput = ShoppingCart.scan.nextInt();

            if (checkoutInput == 0) {
                System.out.println("Thank you for shopping. Good Bye!");
                break;
            }

            switch (checkoutInput) {
                case 1:
                    cartTotalWithNoTax(itemCart);
                    break;
                case 2:
                    cartGrandTotalWithTaxForAllItems(itemCart);
                    break;
                case 3:
                    cartGrandTotalWithTaxForTaxableItems(itemCart);
                    break;
                case 4:
                    System.out.println("This calculates total with coupons applied. \n" +
                            "Yet to be implemented!! \n");
                    CouponUtil.applyCoupons(itemCart);
                    break;
                case 5:
                    addToCartRunner();
                    break;
                default:
                    System.out.println("Invalid checkout option. Try again!");
            }
        } while (checkoutInput >= 1 && checkoutInput <= 5);
    }

    public static void cartTotalWithNoTax(List<Items> itemCart) {
        System.out.println("\n**** Cart total with no tax added ****");
        BigDecimal costBeforeTax = calculateTotal(itemCart);
        System.out.println("Cart total before tax: $" + costBeforeTax + "\n");
    }

    public static void cartGrandTotalWithTaxForAllItems(List<Items> itemCart) {
        System.out.println("\n**** Cart totals with tax added for ALL items ****");
        BigDecimal costBeforeTax = calculateTotal(itemCart);
        System.out.println("Cart Subtotal before tax: $" + costBeforeTax);

        BigDecimal totalTaxForAllItems = costBeforeTax.multiply(ShopCartUtil.TAX_RATE);
        System.out.println("Total taxes at the rate of " +
                TAX_RATE.multiply(BigDecimal.valueOf(100)) + "%: $" + totalTaxForAllItems);

        BigDecimal costWithTax = costBeforeTax.add(totalTaxForAllItems);
        System.out.println("Grand total: $" + costWithTax + "\n");
    }

    public static void cartGrandTotalWithTaxForTaxableItems(List<Items> itemCart) {
        System.out.println("\n**** Cart totals with tax added for ONLY taxable items ****");
        BigDecimal costBeforeTax = calculateTotal(itemCart);
        System.out.println("Cart Subtotal before tax: $" + costBeforeTax);

        BigDecimal totalTaxes = calculateTaxTotal(itemCart);
        System.out.println("Total taxes at the rate of " +
                TAX_RATE.multiply(BigDecimal.valueOf(100)) + "% for taxable items: $" + totalTaxes);

        BigDecimal costWithTax = costBeforeTax.add(totalTaxes);
        System.out.println("Grand total: $" + costWithTax + "\n");
    }

    static void addToCartRunner() throws IOException, ParseException {
        String cartInput;

        System.out.println("Hello! Please add items to your shopping cart.");

        do {
            System.out.print("Enter the SKU of the item or 'pay' to checkout: ");
            cartInput = ShoppingCart.scan.next().trim();

            if (cartInput.equalsIgnoreCase("pay")) break;
            if (isValidSku(cartInput)) {
                addItemToCart(Long.parseLong(cartInput));
            } else {
                System.out.println("Invalid SKU entry! \n");
            }
        } while (!cartInput.equalsIgnoreCase("pay"));

    }

    // calculates the total of a shopping cart
    public static BigDecimal calculateTotal(List<Items> cart) {
        // initialize the total to 0
        BigDecimal total = BigDecimal.valueOf(0);

        // iterate over the items in the cart
        for (Items item : cart) {
            // add the item's price to the total
            total = total.add(item.getPrice());
        }

        // return the total
        return total;
    }

    private static BigDecimal calculateTaxTotal(List<Items> cart) {
        BigDecimal taxTotal = BigDecimal.valueOf(0);

        // iterate over the items in the cart
        for (Items item : cart) {
            // check if the item is taxable
            if (item.isTaxable()) {
                // add the item's price to the tax total
                taxTotal = taxTotal.add(item.getPrice());
            }
        }

        // apply the tax rate to the tax total
        taxTotal = taxTotal.multiply(ShopCartUtil.TAX_RATE);

        // return the tax total
        return taxTotal;
    }

    public static boolean isValidSku(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void addItemToCart(long sku) throws IOException, ParseException {
        JSONObject itemObj = getItem(sku);

        // Assuming only one sku is entered once
        if (itemObj != null) {
            Items item = new Items();

            item.setItemName(itemObj.get("itemName").toString());
            item.setPrice(BigDecimal.valueOf((Double) itemObj.get("price")));
            item.setSku((Long) itemObj.get("sku"));
            item.setTaxable((Boolean) itemObj.get("isTaxable"));
            item.setOwnBrand((Boolean) itemObj.get("ownBrand"));

            ShoppingCart.itemCart.add(item);
            System.out.println("Item added to cart. \nSKU: " + sku + " | " +
                    "Item Name: " + item.getItemName());
            System.out.println("No. of items in cart: " + ShoppingCart.itemCart.size());
        } else {
            System.out.println("Invalid SKU. Please try again!");
        }
    }

    public static JSONObject getItem(long itemSku) throws IOException, ParseException {
        JSONObject item = null;
        JSONArray inventoryArray = getInventoryArray();

        try {
            for (Object obj : inventoryArray) {
                JSONObject itemObj = (JSONObject) obj;
                if (itemSku == (Long) itemObj.get("sku")) {
                    item = itemObj;
                }
            }
        } catch (Exception e) {
            System.out.println("Item with the SKU: " + itemSku + " does not exist.");
        }
        return item;
    }

    public static JSONArray getInventoryArray() throws IOException, ParseException {
        String inventoryFile = "./Resources/inventory.json";
        JSONObject inventoryObj = (JSONObject) jsonParser.parse(new FileReader(inventoryFile));
        return (JSONArray) inventoryObj.get("inventory");
    }
}