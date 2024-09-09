package src;

import java.time.LocalDate;
import java.util.Scanner;

public class ReservationEntity {
    Scanner input = new Scanner(System.in);
    ReservationProcessor reservationProcessor;
    LocalDate sDate;
    LocalDate eDate;

    public ReservationEntity() {
        reservationProcessor = new ReservationProcessor();
    }

    public void getReservationInfo(int employeeID) {

        System.out.println("Insert the Reservation Starting Date (YYYY-MM-DD): ");
        LocalDate sDate = reservationProcessor.parseDateFromString();

        System.out.println("Insert the Reservation Ending Date (YYYY-MM-DD): ");
        LocalDate eDate = reservationProcessor.parseDateFromString();

        System.out.println("Insert the Number of Members");
        int membersNo = input.nextInt();

        System.out.println("Insert customer Phone Number: ");
        int phoneNo = input.nextInt();

        reservationProcessor.makeReservation(sDate, eDate, membersNo, phoneNo, employeeID);

    }

    public void updateReservationInfo(int employeeID) {
        System.out.println("Insert Your National (Customer) ID: ");
        int customerID = input.nextInt();
        String serialNo = reservationProcessor.checkReservation(customerID);

        if (serialNo == null) {
            return;
        }

        System.out.println("1- Extend Room Duration. \n2- Change Room. ");
        int choice = input.nextInt();

        System.out.println("Insert the New Reservation Starting Date (YYYY-MM-DD): ");
        LocalDate sDate = reservationProcessor.parseDateFromString();

        System.out.println("Insert the New Reservation Ending Date (YYYY-MM-DD): ");
        LocalDate eDate = reservationProcessor.parseDateFromString();

        if (choice == 1) {
            reservationProcessor.extendReservation(serialNo, sDate, eDate, customerID, employeeID);
        } else if (choice == 2) {
            reservationProcessor.changeReservation(serialNo, sDate, eDate, customerID, employeeID);
        }

    }

    public void getCustomerId() {
        System.out.println("Insert the customer ID for checking the reservation");
        int customerId = input.nextInt();
        String serialNo = reservationProcessor.checkReservation(customerId);

        if (serialNo == null) {
            System.out.println("There are no reservations for this customer.");
            return;
        } else {
            reservationProcessor.retreiveReservationInfoFromDB(customerId);

        }
    }
}
