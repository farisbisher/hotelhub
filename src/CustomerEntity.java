package src;

import java.util.Scanner;

public class CustomerEntity {

    static Scanner input = new Scanner(System.in);
    DatabaseConnection databaseConnection = new DatabaseConnection();
    CustomerProcessor customerProcessor = new CustomerProcessor();

    public void getCustomerInfo() {
        int customerID = 0;
        boolean idExists = true;

        while (idExists) {
            System.out.println("Enter customer's ID: ");
            customerID = input.nextInt();
            input.nextLine();

            if (customerProcessor.customerExists(customerID)) {
                System.out.println("customer ID already exists. Please choose a different ID.");
            } else {
                idExists = false;
            }
        }

        System.out.println("Please enter the first name: ");
        String first_name = input.next();

        System.out.println("Please enter the last name: ");
        String last_name = input.next();

        System.out.println("Please enter the phone: ");
        int phone = input.nextInt();

        customerProcessor.insertCustomerInfoInDB(first_name, last_name, phone, customerID);

    }

    public void updateCustomerInfo() {

        System.out.println("Enter the customer ID to edit: ");
        int CustomerID = input.nextInt();

        if (customerProcessor.customerExists(CustomerID)) {
            System.out.println("Enter the new first name: ");
            String newFirstName = input.next();

            System.out.println("Enter the new last name: ");
            String newLastName = input.next();

            System.out.println("Enter the new phone: ");
            int newPhone = input.nextInt();

            customerProcessor.modifyCustomerInfoInDB(CustomerID, newFirstName, newLastName, newPhone);
        } else {
            System.out.println("Customer with ID " + CustomerID + " does not exist.");
        }
    }

    public void removeCustomerInfo() {
        System.out.println("Enter customer ID to delete: ");
        int customerID = input.nextInt();

        if (customerProcessor.customerExists(customerID)) {
            customerProcessor.deleteCustomerInfoFromDB(customerID);
        } else {
            System.out.println("Customer with ID " + customerID + " does not exist.");
        }
    }
}
