import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShoppingCart {
    static Scanner scan = new Scanner(System.in);
    static List<Items> itemCart = new ArrayList<>();
    static List<Coupons> couponList = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Welcome to HEB!");
        int mainMenuChoice;

        do {
            System.out.print(
                    "\nMAIN MENU \nChoose from the following-- \n  " +
                            "[1]. Add items to cart \n  " +
                            "[2]. Checkout \n> "
            );
            mainMenuChoice = scan.nextInt();
            if (mainMenuChoice == 1) {
                ShopCartUtil.addToCartRunner();
            } else if (mainMenuChoice == 2) {
                // Checkout flow
                System.out.println("\nProceeding to Checkout...");
                break;
            } else System.out.println("Invalid choice. Try again!"); //TODO: stop from exiting out of the loop.
        } while (mainMenuChoice == 1);

        if (itemCart.size() != 0) {
            for (Items i : itemCart) {
                System.out.println("Item Name: " + i.getItemName());
                System.out.println("Item Price: $" + i.getPrice());
            }
            ShopCartUtil.checkoutHelper(itemCart);
        } else {
            System.out.println("Cart is empty. Nothing to checkout. Good Bye!");
        }
    }
}
