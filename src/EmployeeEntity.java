package src;

import java.util.Scanner;

public class EmployeeEntity {
    Scanner input = new Scanner(System.in);
    EmployeeProcessor employeeProcessor = new EmployeeProcessor();

    public void getEmployeeInfo() {

        int employeeID = 0;
        boolean idExists = true;

        while (idExists) {
            System.out.println("Enter employee's ID: ");
            employeeID = input.nextInt();
            input.nextLine();

            if (employeeProcessor.employeeExists(employeeID)) {
                System.out.println("Employee ID already exists. Please choose a different ID.");
            } else {
                idExists = false;
            }
        }

        System.out.println("Enter employee's first name: ");
        String firstName = input.nextLine();

        System.out.println("Enter employee's last name: ");
        String lastName = input.nextLine();

        System.out.println("Enter employee's salary: ");
        int salary = input.nextInt();

        System.out.println("Give admin status? (Y/N): ");
        String adminInput = input.next().toLowerCase();
        boolean isAdmin = adminInput.equals("y");

        employeeProcessor.insertEmployeeInDB(firstName, lastName, salary, employeeID, isAdmin);
    }

    public void updateEmployeeInfo() {
        System.out.println("Enter employee ID to edit: ");
        int employeeID = input.nextInt();

        if (employeeProcessor.employeeExists(employeeID)) {
            System.out.println("Enter new first name: ");
            String newFirstName = input.next();

            System.out.println("Enter new last name: ");
            String newLastName = input.next();

            System.out.println("Enter new salary: ");
            int newSalary = input.nextInt();

            System.out.println("Change admin status? (Y/N): ");
            String adminInput = input.next().toLowerCase();
            boolean newAdminStatus = adminInput.equals("y");

            employeeProcessor.modifyEmployeeInfoInDB(employeeID, newFirstName, newLastName, newSalary, newAdminStatus);
        } else {
            System.out.println("Employee with ID " + employeeID + " does not exist.");
        }
    }

    public void removeEmployeeInfo() {
        System.out.println("Enter employee ID to delete: ");
        int employeeID = input.nextInt();

        if (employeeProcessor.employeeExists(employeeID)) {
            employeeProcessor.deleteEmployeeInfoFromDB(employeeID);
        } else {
            System.out.println("Employee with ID " + employeeID + " does not exist.");
        }
    }
}