package Presentation;

import Admin.AdminController;
import Customer.CustomerController;
import Order.OrderController;
import Product.ProductController;
import Review.ReviewController;
import Session.SessionManager;

import java.sql.SQLException;
import java.util.Scanner;

    public class MainController {

        private LoginController loginController = new LoginController();
        private CustomerController customerController = new CustomerController();
        private ProductController productController = new ProductController();
        private OrderController orderController = new OrderController();
        private ReviewController reviewController = new ReviewController();

        private String role;


        public MainController() {

        }


//        public MainController(int userId, String role) {
//            this.loggedInUserId = userId;
//            this.role = role;
//        }
// ############################# START MENY ######################################

        public void runMainMenu() {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("=== HUVUDMENY ===");
                System.out.println("1. Logga in");
                System.out.println("2. Skapa konto");
                System.out.println("0. Avsluta");
                System.out.print("Välj ett alternativ: ");
                System.out.println();

                String select = scanner.nextLine();

                switch (select) {
                    case "1":
                        try {
                            loginController.runLoginMeny();

                            if (SessionManager.getInstance().isLoggedIn()) {
                                this.role = SessionManager.getInstance().getLoggedInUserRole();


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
                        running = false;
                        System.out.println("Programmet avslutas");
                        break;

                    default:
                        System.out.println("Ogiltigt val.");
                }
            }
        }

        public void runLogIn() throws SQLException {
            if ("admin".equalsIgnoreCase(role)) {
                mainAdminMenu();
            } else if ("customer".equalsIgnoreCase(role)) {
                mainCustomerMenu();
            }
        }


// ############################## KUNDMENY OM INLOGGAD KUND ########################################

        public void mainCustomerMenu() {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("=== HUVUDMENY KUND ===");
                System.out.println("1. Kundmeny");
                System.out.println("2. Produktmeny");
                System.out.println("3. OrderMeny");
                System.out.println("4. Recension meny");
                System.out.println("0. Logga ut");
                System.out.println("9. Avsluta programmet");
                System.out.print("Välj ett alternativ: ");
                System.out.println();

                String select = scanner.nextLine();
                try {
                switch (select) {
                    case "1":
                       customerController.runCustomerMenu();
                       break;

                    case "2":
                       productController.runMeny();
                        break;
                    case "3":
                        orderController.runCustomerMenu();
                        break;
                    case "4":
                        reviewController.runCustomerMenu();
                        break;
                    case "0":
                        SessionManager.getInstance().logout();
                        System.out.println("Du har loggats ut");
                        System.exit(0);
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


// ################################ ADMINMENY OM INLOGGAD ADMIN ###################################

        public void mainAdminMenu() {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("=== HUVUDMENY ADMIN ===");
                System.out.println("1. Kundmeny");
                System.out.println("2. Produktmeny");
                System.out.println("3. OrderMeny");
                System.out.println("0. Logga ut");
                System.out.println("9. Avsluta programmet");
                System.out.print("Välj ett alternativ: ");
                System.out.println();

                String select = scanner.nextLine();
                try {
                    switch (select) {
                        case "1":
                            customerController.runAdminMenu();
                            break;

                        case "2":
                            productController.runAdminMeny();
                            break;
                        case "3":
                            orderController.runAdminMenu();
                            break;
                        case "4":
                            // reviewController kanske ska kunna se alla reviews av alla produkter
                        case "0":
                            SessionManager.getInstance().logout();
                            System.out.println("Du har loggats ut");
                            System.exit(0);
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
