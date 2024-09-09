package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeProcessor {

    private Connection connection;

    public EmployeeProcessor() {
        this.connection = DatabaseConnection.connect();
    }

    public void insertEmployeeInDB(String firstName, String lastName, int salary, int employeeID, boolean isAdmin) {
        try {
            String query = "INSERT INTO Employee (Fname, Lname, salary, EmployeeID, isAdmin) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, salary);
            statement.setInt(4, employeeID);
            statement.setBoolean(5, isAdmin);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee added successfully!");
            } else {
                System.out.println("Failed to add employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean employeeExists(int employeeID) {
        try {
            String query = "SELECT * FROM Employee WHERE EmployeeID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, employeeID);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void modifyEmployeeInfoInDB(int oldEmployeeID, String newFirstName, String newLastName, int newSalary,
            boolean newAdminStatus) {
        try {
            if (hasAssociatedRecords("Reservation", "EmployeeID", oldEmployeeID) ||
                    hasAssociatedRecords("Checkout", "EmployeeID", oldEmployeeID)) {
                System.out.println(
                        "Cannot update employee information. Employee has associated reservations or checkouts.");
                return;
            }

            String updateQuery = "UPDATE Employee SET Fname = ?, Lname = ?, salary = ?, isAdmin = ? WHERE EmployeeID = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setString(1, newFirstName);
                statement.setString(2, newLastName);
                statement.setInt(3, newSalary);
                statement.setBoolean(4, newAdminStatus);
                statement.setInt(5, oldEmployeeID);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Employee information updated successfully!");
                } else {
                    System.out.println("No employee found with that ID or failed to update.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployeeInfoFromDB(int employeeID) {
        try {
            if (hasAssociatedRecords("Reservation", "EmployeeID", employeeID)
                    || hasAssociatedRecords("Checkout", "EmployeeID", employeeID)) {
                System.out.println("Cannot delete employee. Employee has associated reservations or checkouts.");
                return;
            }
            String query = "DELETE FROM Employee WHERE EmployeeID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, employeeID);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Employee deleted successfully!");
                } else {
                    System.out.println("No employee found with that ID.");
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

    public boolean isEmployeeAdmin(int employeeID) {
        try {
            String query = "SELECT isAdmin FROM Employee WHERE EmployeeID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, employeeID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("isAdmin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isValidUser(String firstName, int employeeID) {
        try {
            String query = "SELECT * FROM Employee WHERE Fname = ? AND EmployeeID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setInt(2, employeeID);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
