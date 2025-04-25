package Presentation;

import Admin.AdminService;
import Customer.CustomerService;
import Session.SessionManager;
import User.User;

import java.sql.SQLException;
import java.util.Scanner;
public class LoginController {


    private final CustomerService customerService = new CustomerService();
    private final AdminService adminService = new AdminService();

    public void runLoginMeny() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ange e-post: ");
        String email = scanner.nextLine();
        System.out.println("Ange lösenord: ");
        String password = scanner.nextLine();


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
