package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RoomProcessor {

    private Connection connection;

    public RoomProcessor() {
        this.connection = DatabaseConnection.connect();
    }

    public void insertRoomInfoInDB(int RoomNo, int NoRooms, int Price, int Floor) {

        String query = "INSERT INTO room (RoomNo, NoRooms, Price, Floor, RoomStatus) VALUES (?, ?, ?, ?, ?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            {
                preparedStatement.setInt(1, RoomNo);
                preparedStatement.setInt(2, NoRooms);
                preparedStatement.setInt(3, Price);
                preparedStatement.setInt(4, Floor);
                preparedStatement.setString(5, "Empty");

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new room has been added successfully");
                } else {
                    System.out.println("Failed to add the new room");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object[]> getAllRooms() {
        List<Object[]> rooms = new LinkedList<>();
        try {
            String query = "SELECT * FROM room";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] room = {
                            resultSet.getInt("RoomNo"),
                            resultSet.getInt("NoRooms"),
                            resultSet.getInt("Price"),
                            resultSet.getInt("Floor"),
                            resultSet.getString("RoomStatus")
                    };
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public void updateRoomInfo(int roomNo, int noRooms, int price, int floor, int choice) {

        int userChoice = choice;
        String roomStatus = (userChoice == 1) ? "Empty" : "Full";

        try {
            String query = "UPDATE room SET NoRooms=?, Price=?, Floor=?, RoomStatus=? WHERE RoomNo=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, noRooms);
                preparedStatement.setInt(2, price);
                preparedStatement.setInt(3, floor);
                preparedStatement.setString(4, roomStatus);
                preparedStatement.setInt(5, roomNo);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("The room " + roomNo + " has been updated successfully");
                } else {
                    System.out.println("Failed to add the new room");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean roomExist(int roomNo) {
        String query = "SELECT COUNT(*) FROM room WHERE RoomNo = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, roomNo);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void modifyRoomStatus(int roomNo) {
        if (!roomExist(roomNo)) {
            System.out.println("No room found with RoomNo: " + roomNo);
            return;
        }

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE room SET RoomStatus = 'Under Service' WHERE RoomNo = ?");
            preparedStatement.setInt(1, roomNo);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Room status updated to 'Under Service'.");
            } else {
                System.out.println("No room found with RoomNo: " + roomNo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
