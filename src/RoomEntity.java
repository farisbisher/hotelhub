package src;

import java.util.List;
import java.util.Scanner;

public class RoomEntity {

    private Scanner input;
    private RoomProcessor roomProcessor;

    public RoomEntity() {
        this.input = new Scanner(System.in);
        this.roomProcessor = new RoomProcessor();
    }

    public void getRoomInfo() {
        System.out.println("Enter The Room Number:");
        int RoomNo = input.nextInt();

        if (roomProcessor.roomExist(RoomNo)) {
            System.out.println("Room with RoomNo " + RoomNo + " already exists.");
            return;
        }
        System.out.println("Enter the number of rooms:");
        int NoRooms = input.nextInt();
        System.out.println("Enter the price of the room:");
        int Price = input.nextInt();
        System.out.println("Enter the floor of the room:");
        int Floor = input.nextInt();

        roomProcessor.insertRoomInfoInDB(RoomNo, NoRooms, Price, Floor);
    }

    public void viewAllRooms() {
        List<Object[]> rooms = roomProcessor.getAllRooms();
        for (Object[] room : rooms) {
            System.out.println("RoomNo: " + room[0] +
                    ", NoRooms: " + room[1] +
                    ", Price: " + room[2] +
                    ", Floor: " + room[3] +
                    ", RoomStatus: " + room[4]);
        }
    }

    public void updateRoomInfo() {
        int choice;
        System.out.println("Enter Room Number to update:");
        int RoomNo = input.nextInt();
        if (roomProcessor.roomExist(RoomNo)) {
            System.out.println("Enter new number of rooms:");
            int NoRooms = input.nextInt();
            System.out.println("Enter new price of the room:");
            int Price = input.nextInt();
            System.out.println("Enter new floor of the room:");
            int Floor = input.nextInt();
            choice = getStatusChoice();

            roomProcessor.updateRoomInfo(RoomNo, NoRooms, Price, Floor, choice);
        } else {
            System.out.println("Room with RoomNo " + RoomNo + " does not exist.");
        }
    }

    public void modifyRoomStatus() {
        System.out.println("Enter Room Number to modify status:");
        int RoomNo = input.nextInt();
        roomProcessor.modifyRoomStatus(RoomNo);
    }

    public int getStatusChoice() {
        int choice = 0;

        do {
            System.out.print("Enter the status of the room: \n 1- Empty \n 2- Full \n");
            choice = input.nextInt();
            try {
                if (choice != 1 && choice != 2) {
                    System.out.println("Invalid input. Please enter either 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (choice != 1 && choice != 2);

        return choice;
    }

}
