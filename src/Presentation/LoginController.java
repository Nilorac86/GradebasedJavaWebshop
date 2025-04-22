package Presentation;

import Admin.AdminService;
import Customer.CustomerService;
import Session.SessionManager;
import User.User;

import java.sql.SQLException;
import java.util.Scanner;
public class LoginController {


    private CustomerService customerService = new CustomerService();
    private AdminService adminService = new AdminService();

    public void runLoginMeny() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ange e-post: ");
        String email = scanner.nextLine();
        System.out.println("Ange lösenord: ");
        String password = scanner.nextLine();

//        User loggedInCustomer = customerService.userLogin(email, password);
//        if (loggedInCustomer != null) {
//            System.out.println("Inloggning lyckades som kund.");
//            SessionManager.getInstance().login(loggedInCustomer.getId(), "customer");
//            return;
//        }
//
//        User loggedInAdmin = adminService.userLogin(email, password);
//        if (loggedInAdmin != null) {
//            System.out.println("Inloggning lyckades som admin.");
//            SessionManager.getInstance().login(loggedInAdmin.getId(), "admin");
//            return;
//        }
//
//        System.out.println("Felaktiga inloggningsuppgifter.");
//    }
        User loggedInCustomer = customerService.userLogin(email, password);
        if (loggedInCustomer != null) {
            System.out.println("Inloggning lyckades.");
            SessionManager.getInstance().login(loggedInCustomer.getId(), "customer");
            return; // lämna metoden
        }

        User loggedInAdmin = adminService.userLogin(email, password);
        if (loggedInAdmin != null) {
            System.out.println("Inloggning lyckades som admin.");
            SessionManager.getInstance().login(loggedInAdmin.getId(), "admin");
            return;
        }

        System.out.println("Felaktiga inloggningsuppgifter. Försök igen.");
    }
}
//    public void runLoginMeny() throws SQLException {
//        Scanner scanner = new Scanner(System.in);
//        boolean running = true;
//
//
//        while (running) {
//            System.out.println("Ange e-post: ");
//            String email = scanner.nextLine();
//            System.out.println("Ange lösenord: ");
//            String password = scanner.nextLine();
//
//            // Försök logga in som Customer
//            User loggedInCustomer = customerService.userLogin(email, password);
//            if (loggedInCustomer != null) {
//                System.out.println("Inloggning lyckades.");
//                SessionManager.getInstance().loginCustomer(loggedInCustomer.getId()); // Sätt session för kund
//                new MainController(loggedInCustomer.getId(), "customer").runLogIn();
//                return; // Avsluta metoden om inloggningen lyckades
//            }
//
//            // Försök logga in som Admin
//            User loggedInAdmin = adminService.userLogin(email, password);
//            if (loggedInAdmin != null) {
//                System.out.println("Inloggning lyckades som admin.");
//                SessionManager.getInstance().loginAdmin(loggedInAdmin.getId()); // Sätt session för admin
//                new MainController(loggedInAdmin.getId(), "admin").runLogIn();
//                return; // Avsluta metoden om inloggningen lyckades
//            } else {
//                System.out.println("Felaktiga inloggningsuppgifter.");
//            }
//        }
//    }

