package src;

import java.util.Scanner;

public class UserInterface {
    public LoginPanel loginPanel;
    public EmployeeEntity employeeEntity;
    public CustomerEntity customerEntity;
    public ReservationEntity reservationEntity;
    public RoomEntity roomEntity;
    public EmployeeProcessor employeeProcessor;
    public CheckoutEntity checkoutEntity;
    public int employeeID;

    Scanner input;

    public UserInterface() {
        this.employeeEntity = new EmployeeEntity();
        this.customerEntity = new CustomerEntity();
        this.reservationEntity = new ReservationEntity();
        this.roomEntity = new RoomEntity();
        this.employeeProcessor = new EmployeeProcessor();
        this.checkoutEntity = new CheckoutEntity();

        this.input = new Scanner(System.in);
    }

    public void systemInterface(boolean isAdmin, int employeeID) {

        System.out.println("=========================");
        if (isAdmin) {
            System.out.println("Welcome, Admin!");
            this.employeeID = employeeID;
            displayAdminDashboard();
        } else {
            System.out.println("Welcome, Employee!");
            this.employeeID = employeeID;
            displayEmployeeDashboard();
        }
        System.out.println("=========================");

    }

    public void displayAdminDashboard() {
        int choice = 0;
        System.out.println("### Welcome to the Hotel Management System Admin ###");
        System.out.println(
                " 1. Manage Employees \n 2. Manage Customers \n 3. Manage Rooms \n 4. Manage Room Reservations \n 5. Logout from the Admin Dashboard");
        choice = input.nextInt();
        manageAdminDashboard(choice);
    }

    public void displayEmployeeDashboard() {
        int choice = 0;
        System.out.println("### Welcome to the Hotel Management System Employee ###");
        System.out.println(
                " 1. Manage Customers \n 2. Manage Rooms \n 3. Manage Room Reservations \n 4. Logout from the Employee Dashboard");
        choice = input.nextInt();
        manageEmployeeDashboard(choice);
    }

    public void manageAdminDashboard(int choice) {

        switch (choice) {

            case 1:

                manageEmployees();
                break;

            case 2:
                manageCustomers();
                break;

            case 3:
                manageRooms();
                break;

            case 4:
                manageRoomReservation(employeeID);
                break;
            case 5:
                System.out.println("Loging Out...............");
                System.out.println("Logout Complete");
                System.exit(0);

            default:
                System.out.println("Invalid choice");
        }
    }

    public void manageEmployeeDashboard(int choice) {

        switch (choice) {

            case 1:

                manageCustomers();
                break;

            case 2:
                manageRooms();
                break;

            case 3:
                manageRoomReservation(employeeID);
                break;

            case 4:
                System.out.println("Loging Out...............");
                System.out.println("Logout Complete");
                System.exit(0);

            default:
                System.out.println("Invalid choice");
        }
    }

    public void manageEmployees() {
        int adminChoice = 0;

        while (adminChoice != 4) {

            System.out.println(
                    " 1. Add a New Employee \n 2. Update Employee Information \n 3. Remove an Employee \n 4. Back To The Main Dashboard");
            adminChoice = input.nextInt();

            switch (adminChoice) {
                case 1:

                    employeeEntity.getEmployeeInfo();
                    break;

                case 2:
                    employeeEntity.updateEmployeeInfo();
                    break;

                case 3:
                    employeeEntity.removeEmployeeInfo();
                    break;

                case 4:
                    displayAdminDashboard();
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void manageCustomers() {
        int choice = 0;

        while (choice != 4) {
            System.out.println(
                    " 1. Add a New Customer \n 2. Update Customer Information \n 3. Remove a Customer (Admin Only) \n 4. Back To The Main Dashboard");
            choice = input.nextInt();

            switch (choice) {
                case 1:

                    customerEntity.getCustomerInfo();
                    break;

                case 2:
                    customerEntity.updateCustomerInfo();
                    break;

                case 3:
                    if (employeeProcessor.isEmployeeAdmin(employeeID)) {
                        customerEntity.removeCustomerInfo();
                    } else {
                        System.out.println("You Have To Be An Admin In Order To Remove a Customer");
                    }
                    break;

                case 4:
                    if (employeeProcessor.isEmployeeAdmin(employeeID)) {
                        displayAdminDashboard();
                    } else {
                        displayEmployeeDashboard();
                    }
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void manageRooms() {
        int choice = 0;
        while (choice != 4) {

            System.out.println(
                    " 1. Add a New Room \n 2. Update Room Information \n 3. View All Rooms \n 4. Back To The Main Dashboard");
            choice = input.nextInt();

            switch (choice) {
                case 1:

                    roomEntity.getRoomInfo();
                    break;

                case 2:
                    roomEntity.viewAllRooms();
                    roomEntity.updateRoomInfo();
                    break;

                case 3:
                    roomEntity.viewAllRooms();
                    break;

                case 4:
                    if (employeeProcessor.isEmployeeAdmin(employeeID)) {
                        displayAdminDashboard();
                    } else {
                        displayEmployeeDashboard();
                    }
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void manageRoomReservation(int employeeID) {
        int choice = 0;

        while (choice != 3) {

            System.out.println(
                    " 1. Reserve a Room \n 2. Update Reservation Information \n 3. Checkout a Room \n 4. Back To The Main Dashboard");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    roomEntity.viewAllRooms();
                    reservationEntity.getReservationInfo(employeeID);
                    break;

                case 2:
                    reservationEntity.updateReservationInfo(employeeID);
                    break;

                case 3:
                    reservationEntity.getCustomerId();
                    checkoutEntity.getCheckoutInfo();
                case 4:
                    if (employeeProcessor.isEmployeeAdmin(employeeID)) {
                        displayAdminDashboard();
                    } else {
                        displayEmployeeDashboard();
                    }
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
