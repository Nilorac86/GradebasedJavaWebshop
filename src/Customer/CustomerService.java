package Customer;
import Session.SessionManager;
import java.sql.SQLException;
import java.util.ArrayList;

import User.User;
import User.UserService;

public class CustomerService extends UserService {

   private CustomerMapper customerMapper = new CustomerMapper();

    private CustomerRepository customerRepository = new CustomerRepository();

    @Override
    public User userLogin( String loginValue, String password) {
        return super.userLogin("customers", "email", loginValue, password, customerMapper); // Skicka med loginField
    }



    public ArrayList<Customer> getAllCustomers() throws SQLException {
        return customerRepository.getAll();
    }


    public void insertUser(String name, String email, String phone, String address, String password)
            throws SQLException {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Namn, email och lösenord måste fyllas i.");
            return;
        }

        if (isEmailTaken(email)) {
            System.out.println("Den e-mailen du angav finns redan registrerad");
            return;
        }

        if (!isEmailValid(email)) {
            System.out.println("Felaktigt format på e-mail.");
            return;
        }

        if (!isPasswordValid(password)) {
            System.out.println("Lösenordet måste innehålla minst 8 tecken, en storbokstav, en liten bokstav samt en siffra");
            return;
        }

        customerRepository.insertCustomer(name, email, phone, address, password);
        System.out.println("Kund har lagts till!");
    }

    // Uppdatera användarens e-postadress
    public boolean updateEmail(int loggedInCustomerId, String email) throws SQLException {

        if (isEmailTaken(email)) {
            System.out.println("Den e-mail du angav finns redan");
            return false;
        }

        if (!isEmailValid(email)) {
            System.out.println("Felaktigt format på email.");
            return false;
        }

        int customerId = SessionManager.getInstance().getLoggedInUserId(); // Hämta inloggad kunds ID från sessionen
        if (customerId == -1) {
            System.out.println("Ingen användare är inloggad.");
            return false;
        }

        return customerRepository.updateEmail(customerId, email);
    }

    // Radera användare (endast om inloggad)
    public boolean deleteCustomer() throws SQLException {
        int customerId = SessionManager.getInstance().getLoggedInUserId();
        if (customerId == -1) {
            System.out.println("Ingen användare är inloggad.");
            return false;
        }
        return customerRepository.deleteCustomer(customerId);
    }


    // Kontrollera om en e-postadress är korrekt
    private boolean isEmailValid(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    // Kontrollera om ett lösenord är korrekt
    private boolean isPasswordValid(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(regex);
    }


    private boolean isEmailTaken(String email) throws SQLException{
        return customerRepository.isEmailTaken(email);
    }

    public boolean deleteCustomerById(int idToDelete) throws SQLException {
        return customerRepository.deleteCustomer(idToDelete);
    }
}

