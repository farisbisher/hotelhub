package src;

import java.util.Scanner;

public class LoginPanel {
    EmployeeProcessor employeeProcessor = new EmployeeProcessor();
    UserInterface userInterface = new UserInterface();
    Scanner input = new Scanner(System.in);

    public void login() {
        System.out.println("======== Login System ========");
        System.out.println();

        System.out.println("Enter first name: ");
        String Fname = input.nextLine();

        System.out.println("Enter employee ID: ");
        int employeeID = input.nextInt();

        System.out.println();

        boolean isValidUser = employeeProcessor.isValidUser(Fname, employeeID);

        System.out.println("=========================");
        if (isValidUser) {
            boolean isAdmin = employeeProcessor.isEmployeeAdmin(employeeID);
            userInterface.systemInterface(isAdmin, employeeID);
        } else {
            System.out.println("Invalid user information. Please try again.");
        }
        System.out.println("=========================");

    }
}
