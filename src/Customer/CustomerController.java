package Customer;
import Order.OrderController;
import Presentation.MainController;
import Review.ReviewController;
import Session.SessionManager;
import User.UserController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerController extends UserController {

    private CustomerRepository customerRepository = new CustomerRepository();
    private CustomerService customerService = new CustomerService();
    private OrderController orderController = new OrderController();
    private MainController mainController;
    private ReviewController reviewController = new ReviewController();



    public void runAdminMeny() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {

            String select = adminMeny(scanner);

            switch (select) {
                case "1":
                    fetchAllCustomers();
                    break;
                case "2":
                    System.out.println("Ange id:");
                    int id = scanner.nextInt();
                    fetchCustomerById(scanner);
                    Customer customer = null;
                    try {
                        customer = customerRepository.getCustomerById(id);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(customer.getName());
                    break;
                case "3":
                    try {
                        createCustomer(scanner);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "4":
                    updateCustomer(scanner);
                    break;
                case "5":
                    deleteCustomerById(scanner);
                case "0":
                    SessionManager.getInstance().logout();
                    running = false;
                    break;
                case "9":
                    System.exit(0);
                    System.out.println("Programmet avslutas");
                    break;
                default:
                    System.out.println("Välj ett alternativ från menyn.");
            }
        }
    }

    public void fetchCustomerById(Scanner scanner) {
        scanner.nextLine();
    }


    public void runCustomerMeny() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {

            String select = customerMeny(scanner);

            switch (select) {
                case "1":
                    createCustomer(scanner);
                    break;
                case "2":
                    updateCustomer(scanner);
                    break;
                case "3":
                    deleteCustomer();
                    break;
                case "4":
                    orderController.runMeny();
                case "5":
                    reviewController.runMenu();
                case "0":
                    SessionManager.getInstance().logout();
                    running = false;
                    break;
                case "9":
                    System.exit(0);
                    System.out.println("Programmet avslutas");
                    break;
                default:
                    System.out.println("Välj ett alternativ från menyn.");
            }
        }
    }

    private static String adminMeny(Scanner scanner) {
        System.out.println("Kundhanterings meny");
        System.out.println("1. Hämta alla kunder");
        System.out.println("2. Hämta en kund efter id");
        System.out.println("3. Lägg till en ny kund");
        System.out.println("4. Uppdatera din email");
        System.out.println("5. Radera användare");
        System.out.println("0. Logga ut");
        System.out.println("9. Avsluta hela programmet");
        System.out.println("Välj ett alternativ:");
        return scanner.nextLine();
    }

    private static String customerMeny(Scanner scanner) {
        System.out.println("Kund meny");
        System.out.println("1. Skapa ett konto");
        System.out.println("2. Uppdatera din email");
        System.out.println("3. Radera konto");
        System.out.println("4. Hantera order");
        System.out.println("5. Reviews meny");
        System.out.println("0. Logga ut");
        System.out.println("9. Avsluta hela programmet");
        System.out.println("Välj ett alternativ:");
        return scanner.nextLine();
    }


    public void createCustomer(Scanner scanner) throws SQLException {
        System.out.println("Ange ett namn:");
        String name = scanner.nextLine();
        System.out.println("Ange din email:");
        String email = scanner.nextLine();
        System.out.println("Ange ett telefonnummer:");
        String phone = scanner.nextLine();
        System.out.println("Ange din adress:");
        String address = scanner.nextLine();
        System.out.println("Ange ett lösenord:");
        String password = scanner.nextLine();

        customerService.insertUser(name, email, phone, address, password);
    }

    private void fetchAllCustomers() {
        ArrayList<Customer> customers = null;
        try {
            customers = customerService.getAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Customer customer : customers) {
            System.out.println("KundId: " + customer.getId());
            System.out.println("Namn: " + customer.getName());
            System.out.println("Email: " + customer.getEmail());
            System.out.println();
        }
    }

    private boolean updateCustomer(Scanner scanner) {
        if (!SessionManager.getInstance().isCustomerLoggedIn()) {
            System.out.println("Du måste vara inloggad för att uppdatera din e-post.");
            return false;
        }

        System.out.println("Ange din nya e-postadress:");
        String email = scanner.nextLine();
        boolean success = false;

        try {
            success = customerService.updateEmail(SessionManager.getInstance().getLoggedInUserId(),email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(success ? "Email har uppdaterats." : "Kund hittades ej.");
        return success;
    }

    private boolean deleteCustomer() {
        boolean deleteSuccess = false;
        try {
            deleteSuccess = customerService.deleteCustomer(); // Tar bort inloggad kund
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(deleteSuccess ? "Kund raderad " : "Kund hittades ej");
        SessionManager.getInstance().logout();
        if (mainController == null) {
            mainController = new MainController();
        }
        mainController.runMainMenu();
        return deleteSuccess;
    }


    private boolean deleteCustomerById(Scanner scanner) {
        System.out.println("Ange id på den kund som ska raderas.");
        int idToDelete = scanner.nextInt();
        scanner.nextLine();
        boolean deleteSuccess = false;
        try {
            deleteSuccess = customerService.deleteCustomerById(idToDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(deleteSuccess ? "Kund raderad" : "Kund hittades ej");

        return deleteSuccess;
    }
}

