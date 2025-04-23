package Presentation;

import Admin.AdminController;
import Customer.CustomerController;
import Order.OrderController;
import Product.ProductController;
import Session.SessionManager;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

    public class MainController {

        private final LoginController loginController = new LoginController();
        private final CustomerController customerController = new CustomerController();
        private final AdminController adminController = new AdminController();
        private final ProductController productController = new ProductController();
        private final OrderController orderController = new OrderController();

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
                        System.exit(0);
                        System.out.println("Programmet avslutas");
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
                mainCustomerMenu();
            }
        }

        public void mainCustomerMenu() {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("=== HUVUDMENY KUND ===");
                System.out.println("1. Kund meny");
                System.out.println("2. Produkt meny");
                System.out.println("3. Order Meny");
                System.out.println("0. Logga ut");
                System.out.println("9. Avsluta programmet");
                System.out.print("Välj ett alternativ: ");

                String select = scanner.nextLine();
                try {
                switch (select) {
                    case "1":
                       customerController.runCustomerMeny();
                       break;

                    case "2":
                       productController.runMeny();
                        break;
                    case "3":
                        orderController.runMeny();
                        break;
                    case "0":
                        SessionManager.getInstance().logout();
                        running = false;
                        break;
                    case "9":
                        System.exit(0);
                        System.out.println("Programmet avslutas");
                        break;

                    default:
                        System.out.println("Ogiltigt val.");

                }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        public void mainAdminMenu() {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("=== HUVUDMENY KUND ===");
                System.out.println("1. Kund meny");
                System.out.println("2. Produkt meny");
                System.out.println("3. Order Meny");
                System.out.println("0. Logga ut");
                System.out.println("9. Avsluta programmet");
                System.out.print("Välj ett alternativ: ");

                String select = scanner.nextLine();
                try {
                    switch (select) {
                        case "1":
                            customerController.runAdminMeny();
                            break;

                        case "2":
                            productController.runAdminMeny();
                            break;
                        case "3":
                            orderController.runAdminMeny();
                            break;
                        case "0":
                            SessionManager.getInstance().logout();
                            running = false;
                            break;
                        case "9":
                            System.exit(0);
                            System.out.println("Programmet avslutas");
                            break;

                        default:
                            System.out.println("Ogiltigt val.");

                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
