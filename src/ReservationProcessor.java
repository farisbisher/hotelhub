package src;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ReservationProcessor {

    Scanner input = new Scanner(System.in);
    private Connection connection;

    public ReservationProcessor() {
        this.connection = DatabaseConnection.connect();
    }

    public int retreiveCustomerIDFromDB(int phoneNo) {
        int customerID = 0;
        try {
            String retrievalQuery = "SELECT customerID from customer where customer.phoneNo = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(retrievalQuery);
            preparedStatement.setInt(1, phoneNo);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customerID = resultSet.getInt("CustomerID");
            }

            resultSet.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (customerID != 0) {
            return customerID;
        } else {
            System.out.println("There is no customer with this phone number");
        }
        return customerID;

    }

    public List<Integer> retreiveAllEmptyRoomNoFromDB(int membersNo) {
        List<Integer> emptyRooms = new ArrayList<Integer>();
        try {
            String retrievalQuery = "SELECT room.RoomNo FROM room WHERE room.NoRooms = ? AND room.RoomStatus = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(retrievalQuery);
            preparedStatement.setInt(1, membersNo);
            preparedStatement.setString(2, "Empty");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int roomNo = resultSet.getInt("RoomNo");
                emptyRooms.add(roomNo);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (emptyRooms.isEmpty()) {
            System.out.println("There is no room that handles the number of members.");
        }
        return emptyRooms;
    }

    public int retreiveOldRoomNoInfoFromDB(String serialNo) {
        int roomNo = 0;
        try {

            String retrievalQuery = "select roomNo from reservation where serialNo = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(retrievalQuery);
            preparedStatement.setString(1, serialNo);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                roomNo = resultSet.getInt("RoomNo");
            }

            resultSet.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roomNo != 0) {
            return roomNo;
        } else {
            System.out.println("There is no room reserved under this serialNo.");
        }
        return roomNo;

    }

    public int retreiveRoomPriceInfoFromDB(int roomNo) {
        int dailyPrice = 0;
        try {

            String retrievalQuery = "select price from room where roomNo = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(retrievalQuery);
            preparedStatement.setInt(1, roomNo);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                dailyPrice = resultSet.getInt("price");
            }

            resultSet.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dailyPrice;

    }

    public List<Object[]> retreiveReservationInfoFromDB(int customerId) {
        List<Object[]> reservations = new LinkedList<>();

        try {
            String query = "SELECT reservation.serialNo, reservation.sDate, reservation.eDate, reservation.totalPrice, customer.customerID, room.roomNo "
                    +
                    "FROM reservation " +
                    "INNER JOIN customer ON reservation.customerID = customer.customerID " +
                    "INNER JOIN room ON reservation.roomNo = room.roomNo " +
                    "WHERE customer.customerID = ? AND room.roomStatus = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, customerId);
                preparedStatement.setString(2, "Full");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Object[] reservation = {
                                resultSet.getString("serialNo"),
                                resultSet.getDate("sDate").toLocalDate(),
                                resultSet.getDate("eDate").toLocalDate(),
                                resultSet.getLong("totalPrice"),
                                resultSet.getInt("customerID"),
                                resultSet.getInt("roomNo")
                        };
                        reservations.add(reservation);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Object[] reservation : reservations) {
            System.out.println("ReservationID: " + reservation[0] +
                    ", StartDate: " + reservation[1] +
                    ", EndDate: " + reservation[2] +
                    ", TotalPrice: " + reservation[3] +
                    ", CustomerID: " + reservation[4] +
                    ", RoomNo: " + reservation[5]);
        }
        return reservations;
    }

    public void changeRoomStatusToFullInDB(int roomNo) {

        int roomNumber = 0;
        try {

            String retrievalQuery = "SELECT roomNo from reservation where roomNo = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(retrievalQuery);
            preparedStatement.setInt(1, roomNo);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                roomNumber = resultSet.getInt("RoomNo");
            }

            resultSet.close();
            preparedStatement.close();

            String insertionQuery = "update room set roomStatus = ? where RoomNo = ?";

            PreparedStatement preparedStatement2 = connection.prepareStatement(insertionQuery);
            preparedStatement2.setString(1, "Full");
            preparedStatement2.setInt(2, roomNumber);

            int rowsInserted = preparedStatement2.executeUpdate();

            preparedStatement2.close();

            if (rowsInserted > 0) {
                System.out.println("Room " + roomNo + " Status Changed to Full Successfully");
            } else {
                System.out.println("The Room Status didn't Change Because Something is Wrong");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeRoomStatusToEmptyInDB(int roomNo) {

        int roomNumber = 0;
        try {
            String retrievalQuery = "SELECT roomNo from reservation where roomNo = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(retrievalQuery);
            preparedStatement.setInt(1, roomNo);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                roomNumber = resultSet.getInt("RoomNo");
            }

            resultSet.close();
            preparedStatement.close();

            String insertionQuery = "update room set roomStatus = ? where RoomNo = ?";

            PreparedStatement preparedStatement2 = connection.prepareStatement(insertionQuery);
            preparedStatement2.setString(1, "Empty");
            preparedStatement2.setInt(2, roomNumber);

            int rowsInserted = preparedStatement2.executeUpdate();

            preparedStatement2.close();

            if (rowsInserted > 0) {
                System.out.println("Room " + roomNo + " Status Changed to Empty Successfully");
            } else {
                System.out.println("The Room Status didn't Change Because Something is Wrong");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertReservationInDB(LocalDate sDate, LocalDate eDate, long totalPrice, int roomNo, int customerID,
            String serialNo, int membersNo, int employeeID, boolean isPaid) {
        try {

            String insertionQuery = "INSERT INTO reservation (sDate, eDate, totalPrice, roomNo, customerId, serialNo, membersNo, employeeID, isPaid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertionQuery);
            preparedStatement.setDate(1, Date.valueOf(sDate));
            preparedStatement.setDate(2, Date.valueOf(eDate));
            preparedStatement.setLong(3, totalPrice);
            preparedStatement.setInt(4, roomNo);
            preparedStatement.setInt(5, customerID);
            preparedStatement.setString(6, serialNo);
            preparedStatement.setInt(7, membersNo);
            preparedStatement.setInt(8, employeeID);
            preparedStatement.setBoolean(9, isPaid);

            int rowsInserted = preparedStatement.executeUpdate();

            preparedStatement.close();

            if (rowsInserted > 0) {
                System.out.println("Reservation Done Successfully.");
            } else {
                System.out.println("Reservation Failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyReservationInDB(LocalDate sDate, LocalDate eDate, long totalPrice, int roomNo, int customerID,
            String serialNo, int employeeID, boolean isPaid) {
        try {

            String updateQuery = "UPDATE reservation SET sDate = ?, eDate = ?, totalPrice = ?, roomNo = ?, customerId = ?, employeeID = ?, isPaid = ? WHERE serialNo = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDate(1, Date.valueOf(sDate));
            preparedStatement.setDate(2, Date.valueOf(eDate));
            preparedStatement.setLong(3, totalPrice);
            preparedStatement.setInt(4, roomNo);
            preparedStatement.setInt(5, customerID);
            preparedStatement.setInt(6, employeeID);
            preparedStatement.setBoolean(7, isPaid);
            preparedStatement.setString(8, serialNo);

            int rowsInserted = preparedStatement.executeUpdate();

            preparedStatement.close();

            if (rowsInserted > 0) {
                System.out.println("Reservation has been updated successfully.");
            } else {
                System.out.println("Reservation Failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public long calculateReservationPrice(int dailyPrice, long daysNo) {
        return dailyPrice * daysNo;
    }

    public LocalDate parseDateFromString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();

        while (true) {
            String dateString = input.nextLine().trim();
            if (dateString.isEmpty()) {
                System.out.println("Date cannot be empty.");
                continue;
            }

            try {
                LocalDate parsedDate = LocalDate.parse(dateString, formatter);
                if (parsedDate.isBefore(currentDate)) {
                    System.out.println("Date should be after or equal to the current date.");
                } else {
                    return parsedDate;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    public static long calculateDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public boolean paymentCheck() {
        boolean isPaid = false;
        System.out.println("Does the customer pay the price of the reservation : 1- Yes 2- No");
        int response = input.nextInt();
        if (response == 1) {
            isPaid = true;
        } else {
            isPaid = false;
        }
        return isPaid;
    }

    public String generateSerialNo(Boolean isPaid) {
        UUID uuid = UUID.randomUUID();
        String uniqueSerialNo = "";
        if (isPaid) {
            uniqueSerialNo = uuid.toString();
        } else {
            System.out.println("You should pay first in order to generate the serial number");
        }
        return uniqueSerialNo;

    }

    public void makeReservation(LocalDate sDate, LocalDate eDate, int membersNo, int phoneNo, int employeeID) {

        int customerID = retreiveCustomerIDFromDB(phoneNo);
        int roomNo = 0;
        if (customerID == 0) {
            return;
        }

        long daysNo = calculateDaysBetweenDates(sDate, eDate);
        List<Integer> emptyRooms = retreiveAllEmptyRoomNoFromDB(membersNo);
        if (emptyRooms.size() == 0) {
            return;
        }
        System.out.println("Write the number of the choosen available room:" + emptyRooms);
        int choice = input.nextInt();
        for (int i = 0; i < emptyRooms.size(); i++) {
            if (choice == emptyRooms.get(i)) {
                roomNo = choice;
                break;
            }
        }
        long totalPrice = calculateReservationPrice(
                retreiveRoomPriceInfoFromDB(roomNo), daysNo);
        if (totalPrice == 0) {
            return;
        }
        System.out.println("The Total Price for the Reservation is: " + totalPrice);
        boolean isPaid = paymentCheck();
        String serialNo = generateSerialNo(isPaid);

        System.out.println("Your serial Number is: " + serialNo);

        if (isPaid) {
            insertReservationInDB(sDate, eDate, totalPrice, roomNo, customerID, serialNo, membersNo, employeeID,
                    isPaid);
            changeRoomStatusToFullInDB(roomNo);
        }

    }

    public String checkReservation(int customerID) {
        String serialNo = null;
        try {

            String retrievalQuery = "select serialNo from reservation where customerID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(retrievalQuery);
            preparedStatement.setInt(1, customerID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                serialNo = resultSet.getString("serialNo");
            }

            resultSet.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (serialNo != null) {
            return serialNo;
        } else {
            System.out.println("There is no reservation under this national ID");
        }
        return serialNo;
    }

    public void extendReservation(String serialNo, LocalDate sDate, LocalDate eDate,
            int customerID, int employeeID) {
        long daysNo = calculateDaysBetweenDates(sDate, eDate);
        int oldRoomNo = retreiveOldRoomNoInfoFromDB(serialNo);
        long totalPrice = calculateReservationPrice(
                retreiveRoomPriceInfoFromDB(oldRoomNo), daysNo);
        if (totalPrice == 0) {
            return;
        }
        System.out.println("The Total Price for the Reservation is: " + totalPrice);
        boolean isPaid = paymentCheck();
        if (isPaid) {
            modifyReservationInDB(sDate, eDate, totalPrice, oldRoomNo, customerID, serialNo, employeeID, isPaid);
        }

    }

    public void changeReservation(String serialNo, LocalDate sDate, LocalDate eDate,
            int customerID, int employeeID) {
        long daysNo = calculateDaysBetweenDates(sDate, eDate);
        int roomNo = 0;
        System.out.println("Insert the Number of Members");
        int membersNo = input.nextInt();
        List<Integer> emptyRooms = retreiveAllEmptyRoomNoFromDB(membersNo);
        if (emptyRooms.size() == 0) {
            return;
        }
        System.out.println("Write the number of the choosen available room:" + emptyRooms);
        int choice = input.nextInt();
        for (int i = 0; i < emptyRooms.size(); i++) {
            if (choice == emptyRooms.get(i)) {
                roomNo = choice;
                break;
            }
        }
        long totalPrice = calculateReservationPrice(
                retreiveRoomPriceInfoFromDB(roomNo), daysNo);
        if (totalPrice == 0) {
            return;
        }

        System.out.println("The Total Price for the Reservation is: " + totalPrice);
        boolean isPaid = paymentCheck();
        String updatedSerialNo = generateSerialNo(isPaid);
        if (isPaid) {
            insertReservationInDB(sDate, eDate, totalPrice, roomNo, customerID, updatedSerialNo, membersNo, employeeID,
                    isPaid);
            changeRoomStatusToFullInDB(roomNo);
        }

    }

}
