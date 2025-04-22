//package Presentation;
//
//import java.sql.SQLException;
//import java.util.Scanner;
//
//import Admin.AdminController;
//import Customer.CustomerController;
//import Session.SessionManager;
//
//public class MainController {
//
//    AdminController adminController = new AdminController();
//    CustomerController customerController = new CustomerController();
//    LoginController loginController = new LoginController();
//
//    private int loggedInUserId;
//    private String userRole;
//
//    public MainController (){
//    }
//
//    public MainController(int loggedInUserId, String userRole) {
//        this.loggedInUserId = loggedInUserId;
//        this.userRole = userRole;
//    }
//
//
//    public void runMainMenu() {
//        Scanner scanner = new Scanner(System.in);
//        boolean running = true;
//
//        while (running) {
//            System.out.println("=== HUVUDMENY ===");
//            System.out.println("1. Logga in");
//            System.out.println("2. Skapa konto");
//            System.out.println("0. Avsluta");
//            System.out.println("Välj ett av alternativen: ");
//
//            String select = scanner.nextLine();
//
//            switch (select) {
//                case "1":
//                    try {
//                        loginController.runLoginMeny();
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try {
//                        adminController.runAdminMeny();
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
//                    //running = false; // Pausa huvudmenyn medan användaren är inloggad
//                    break;
//                case "2":
//                    try {
//                        customerController.runCustomerMeny();
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
//                    break;
//                case "0":
//                    System.out.println("Programmet avslutas.");
//                    running = false;
//                    break;
//                default:
//                    System.out.println("Ogiltigt val, försök igen.");
//            }
//        }
//    }
//
//    public void runLogIn() {
//        Scanner scanner = new Scanner(System.in);
//        boolean running = true; // Menyn fortsätter så länge running är true
//
//        while (running) {
//            System.out.println("Välkommen!!" );
//            if ("admin".equalsIgnoreCase(userRole)) {
//                runAdminMenu();
//            } else if ("customer".equalsIgnoreCase(userRole)) {
//                runCustomerMenu();
//            } else {
//                System.out.println("Felaktig användarroll.");
//                break;
//            }
//
//            System.out.print("Välj ett alternativ: ");
//            String select = scanner.nextLine();
//            running = handleChoice(select); // Sätter running till false om användaren loggar ut
//        }
//    }
//
//    private void runAdminMenu() {
//        System.out.println("Admin meny:");
//        System.out.println("1. Hantera kunder");
//        System.out.println("2. Hantera produkter");
//        System.out.println("0. Logga ut");
//    }
//
//    private void runCustomerMenu() {
//        System.out.println("Kund meny:");
//        System.out.println("1. Visa produkter");
//        System.out.println("2. Lägg till produkter i kundvagn");
//        System.out.println("3. Gör en beställning");
//        System.out.println("0. Logga ut");
//    }
//
//    private boolean handleChoice(String choice) {
//        if (choice.equals("0")) {
//            if ("admin".equalsIgnoreCase(userRole)) {
//                SessionManager.getInstance().logoutAdmin();
//                System.out.println("Admin har loggat ut.");
//            } else if ("customer".equalsIgnoreCase(userRole)) {
//                SessionManager.getInstance().logoutCustomer();
//                System.out.println("Kund har loggat ut.");
//            }
//            return false; // Avslutar loopen när användaren loggar ut
//        }
//
//        if ("admin".equalsIgnoreCase(userRole)) {
//            // Admin-specifik logik
//            switch (choice) {
//                case "1":
//
//                    break;
//                case "2":
//                    // Hantera produkter
//                    break;
//                default:
//                    System.out.println("Ogiltigt val.");
//            }
//        } else if ("customer".equalsIgnoreCase(userRole)) {
//            // Customer-specifik logik
//            switch (choice) {
//                case "1":
//                    // Visa produkter
//                    break;
//                case "2":
//                    // Lägg till produkter i kundvagn
//                    break;
//                case "3":
//                    // Gör en beställning
//                    break;
//                default:
//                    System.out.println("Ogiltigt val.");
//            }
//        }
//
//        return true; // Fortsätt loopen om inget avslut valts
//    }
//
//    public int getLoggedInUserId() {
//        return loggedInUserId;
//    }
//
//
//}

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
