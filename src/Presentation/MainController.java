package Presentation;

import Admin.AdminController;
import Customer.CustomerController;
import Session.SessionManager;

import java.sql.SQLException;
import java.util.Scanner;

    public class MainController {

        private final LoginController loginController = new LoginController();
        private final CustomerController customerController = new CustomerController();
        private final AdminController adminController = new AdminController();

        private int loggedInUserId;
        private String role;

        public MainController() {

        }

        public MainController(int userId, String role) {
            this.loggedInUserId = userId;
            this.role = role;
        }

        public void runMainMenu() {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("=== HUVUDMENY ===");
                System.out.println("1. Logga in");
                System.out.println("2. Skapa konto");
                System.out.println("0. Avsluta");
                System.out.print("Välj ett alternativ: ");

                String select = scanner.nextLine();

                switch (select) {
                    case "1":
                        try {
                            loginController.runLoginMeny();

                            if (SessionManager.getInstance().isLoggedIn()) {
                                this.role = SessionManager.getInstance().getLoggedInUserRole();
                                this.loggedInUserId = SessionManager.getInstance().getLoggedInUserId();

                                runLogIn(); // nu körs rätt meny (admin/kund)
                            }

                        } catch (SQLException e) {
                            System.out.println("Fel vid inloggning: " + e.getMessage());
                        }
                        break;

                    case "2":
                        try {
                            customerController.createCustomer(scanner);
                        } catch (SQLException e) {
                            System.out.println("Fel vid skapande av nytt konto: " + e.getMessage());
                        }
                        break;

                    case "0":
                        System.out.println("Programmet avslutas.");
                        running = false;
                        break;

                    default:
                        System.out.println("Ogiltigt val.");
                }
            }
        }

        public void runLogIn() throws SQLException {
            if ("admin".equalsIgnoreCase(role)) {
                adminController.runAdminMenu();
            } else if ("customer".equalsIgnoreCase(role)) {
                customerController.runCustomerMeny();
            }
        }
    }
