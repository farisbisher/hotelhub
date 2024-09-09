package src;

import java.util.Scanner;

public class CheckoutEntity {

    DatabaseConnection databaseConnection = new DatabaseConnection();
    CheckoutProcessor checkoutProcessor = new CheckoutProcessor();

    Scanner input = new Scanner(System.in);

    public void getCheckoutInfo() {
        System.out.println("Enter The SerialNo To Checkout:");
        String SerialNo = input.next();
        checkoutProcessor.checkoutRoom(SerialNo);
    }

}
