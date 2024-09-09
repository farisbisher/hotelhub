package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CheckoutProcessor {

    private Connection connection;

    public CheckoutProcessor() {
        this.connection = DatabaseConnection.connect();
    }

    public boolean isRoomFull(int roomNo) {
        String query = "SELECT RoomStatus FROM room WHERE RoomNo = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            {
                preparedStatement.setInt(1, roomNo);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String roomStatus = resultSet.getString("RoomStatus");
                        return "Full".equalsIgnoreCase(roomStatus);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean serialNoExist(String serialNo) {
        String query = "SELECT COUNT(*) FROM checkout WHERE SerialNo = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            {

                preparedStatement.setString(1, serialNo);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void checkoutRoom(String serialNo) {
        int roomNo;
        int employeeId;
        int customerId;

        if (serialNoExist(serialNo)) {
            System.out.println("SerialNo already used for checkout. Check out not possible.");
            return;
        }
        String querySelect = "SELECT RoomNo, EmployeeID, CustomerID, SerialNo FROM Reservation WHERE SerialNo = ?;";

        try {
            try (PreparedStatement preparedStatementSelect = connection.prepareStatement(querySelect)) {
                preparedStatementSelect.setString(1, serialNo);

                try (ResultSet resultSet = preparedStatementSelect.executeQuery()) {
                    if (resultSet.next()) {
                        roomNo = resultSet.getInt("RoomNo");
                        employeeId = resultSet.getInt("EmployeeID");
                        customerId = resultSet.getInt("CustomerID");
                    } else {
                        System.out.println("Reservation information not found for SerialNo: " + serialNo);
                        return;
                    }
                }
            }
            if (!isRoomFull(roomNo)) {
                System.out.println("The room is Empty. Check out not possible.");
                return;
            }
            String queryInsert = "INSERT INTO checkout (SerialNo, RoomNo, EmployeeID, CustomerID) VALUES (?, ?, ?, ?);";
            try (PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert)) {
                preparedStatementInsert.setString(1, serialNo);
                preparedStatementInsert.setInt(2, roomNo);
                preparedStatementInsert.setInt(3, employeeId);
                preparedStatementInsert.setInt(4, customerId);
                preparedStatementInsert.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String queryUpdate = "UPDATE room SET RoomStatus = 'Empty' WHERE RoomNo = ? AND RoomStatus = 'Full';";
            try (PreparedStatement preparedStatementUpdate = connection.prepareStatement(queryUpdate)) {
                preparedStatementUpdate.setInt(1, roomNo);
                int rowsUpdated = preparedStatementUpdate.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Check out successful. Room status updated to 'Empty'.");
                } else {
                    System.out.println("Check out failed. The room is not currently full.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
