package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerProcessor {

    private Connection connection;

    public CustomerProcessor() {
        this.connection = DatabaseConnection.connect();

    }

    public void insertCustomerInfoInDB(String Fname, String Lname, int PohneNO, int CustomerID) {
        try {

            String insertQuery = "INSERT INTO customer (Fname, Lname, PhoneNO, CustomerID) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, Fname);
            preparedStatement.setString(2, Lname);
            preparedStatement.setInt(3, PohneNO);
            preparedStatement.setInt(4, CustomerID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("customer Inserted Successfully");
            } else {
                System.out.println("Failed to insert customer");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public void modifyCustomerInfoInDB(int oldCustomerID, String newFirstName, String newLastName, int newPhoneNO) {
        try {
            if (hasAssociatedRecords("Reservation", "CustomerID", oldCustomerID)
                    || hasAssociatedRecords("Checkout", "CustomerID", oldCustomerID)) {
                System.out.println(
                        "Cannot update customer information. Customer has associated reservations or checkouts.");
                return;
            }

            String updateQuery = "UPDATE Customer SET Fname = ?, Lname = ?, PhoneNO = ? WHERE CustomerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setString(1, newFirstName);
                statement.setString(2, newLastName);
                statement.setInt(3, newPhoneNO);
                statement.setInt(4, oldCustomerID);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Customer information updated successfully!");
                } else {
                    System.out.println("No customer found with that ID or failed to update.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomerInfoFromDB(int customerID) {
        try {
            if (hasAssociatedRecords("Reservation", "CustomerID", customerID)
                    || hasAssociatedRecords("Checkout", "CustomerID", customerID)) {
                System.out.println("Cannot delete customer. Customer has associated reservations or checkouts.");
                return;
            }

            String query = "DELETE FROM customer WHERE CustomerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, customerID);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Customer deleted successfully!");
                } else {
                    System.out.println("No customer found with that ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean hasAssociatedRecords(String tableName, String columnName, int value) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, value);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }

    public boolean customerExists(int CustomerID) {
        try {
            String query = "SELECT * FROM customer WHERE CustomerID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, CustomerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
