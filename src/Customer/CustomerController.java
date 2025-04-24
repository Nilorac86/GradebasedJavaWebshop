package Customer;

import Presentation.MainController;
import Session.SessionManager;
import User.UserController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerController extends UserController {

    private CustomerRepository customerRepository = new CustomerRepository();
    private CustomerService customerService = new CustomerService();
    private MainController mainController;


    public CustomerController() {
        this.mainController = mainController;
    }

// ############################ KUNDMENY ###############################################
    public void runCustomerMenu() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("=== KUNDMENY INSTÄLLNINGAR ===");
            System.out.println("1.Uppdatera din email ");
            System.out.println("2. Radera konto");
            System.out.println("3. Gå tillbaka till Huvudmeny");
            System.out.println("0. Logga ut");
            System.out.println("9. Avsluta programmet");
            System.out.println("Välj ett alternativ:");


            String select = scanner.nextLine();

            switch (select) {
                case "1":
                    updateCustomer(scanner);
                    break;
                case "2":
                    deleteCustomer();
                    break;
                case "3":
                    if ( mainController == null){
                        mainController = new MainController();
                    }
                    mainController.mainCustomerMenu();
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
                    System.out.println("Välj ett alternativ från menyn.");
            }
        }
    }

// ############################# ADMINMENY ############################################

    public void runAdminMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("=== ADMIN KUNDMENY ===");
            System.out.println("1. Hämta alla kunder");
            System.out.println("2. Hämta en kund efter id");
            System.out.println("3. Lägg till en ny kund");
            System.out.println("4. Uppdatera kunds email");
            System.out.println("5. Radera användare");
            System.out.println("0. Logga ut");
            System.out.println("9. Avsluta hela programmet");
            System.out.println("Välj ett alternativ:");


            String select = scanner.nextLine();

            try {
            switch (select) {
                case "1":
                    fetchAllCustomers();
                    break;
                case "2":
                    getCustomerById(scanner);
                    break;
                case "3":
                     createCustomer(scanner);
                     break;
                case "4":
                    updateCustomerEmail(scanner);
                    break;
                case "5":
                    deleteCustomerById(scanner);
                case "6":
                    mainController.mainAdminMenu();
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
                    System.out.println("Välj ett alternativ från menyn.");
            }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


// ####################### METODER ###############################################

    public void createCustomer(Scanner scanner) throws SQLException {
        System.out.println("Ange ett namn:");
        String name = scanner.nextLine();
        System.out.println("Ange din email:");
        String email = scanner.nextLine();
        System.out.println("Ange ett lösenord:");      // Flytta upp lösenordet hit
        String password = scanner.nextLine();
        System.out.println("Ange ett telefonnummer:");
        String phone = scanner.nextLine();
        System.out.println("Ange din adress:");
        String address = scanner.nextLine();

        customerService.insertUser(name, email, password, phone, address);
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
            System.out.println("Lösenord : " + customer.getPassword());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Adress: " + customer.getAddress());
            System.out.println("Telefonnummer: " + customer.getPhone());

            System.out.println();


        }

    }

    private boolean updateCustomer(Scanner scanner) {

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

    private boolean updateCustomerEmail(Scanner scanner){
        System.out.println("Ange id på den kund som ska uppdateras");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ange din nya e-postadress:");
        String email = scanner.nextLine();
        boolean success = false;

        try {
            success = customerService.updateEmail(customerId,email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(success ? "Email har uppdaterats." : "Kund hittades ej.");
        return success;
    }


    public void getCustomerById(Scanner scanner) {
        System.out.println("Ange id:");
        int id = scanner.nextInt();
        scanner.nextLine();
        Customer customer = null;

        try {
            customer = customerRepository.getCustomerById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("KundId: " + customer.getId());
        System.out.println("Namn: " + customer.getName());
        System.out.println("Lösenord : " + customer.getPassword());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Adress: " + customer.getAddress());
        System.out.println("Telefonnummer: " + customer.getPhone());

        System.out.println();;
    }


    private boolean deleteCustomer() {
        boolean deleteSuccess = false;
        try {
            deleteSuccess = customerService.deleteCustomer();
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

