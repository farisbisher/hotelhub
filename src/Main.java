package src;

public class Main {
    public static void main(String[] args) {

        LoginPanel loginPanel = new LoginPanel();
        loginPanel.login();
        DatabaseConnection.closeConnection();
    }

}
