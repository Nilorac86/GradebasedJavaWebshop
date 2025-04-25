package Customer;
import Session.SessionManager;
import java.sql.SQLException;
import java.util.ArrayList;

import User.User;
import User.UserService;

public class CustomerService extends UserService {

   private final CustomerMapper customerMapper = new CustomerMapper();

    private final CustomerRepository customerRepository = new CustomerRepository();

    @Override
    public User userLogin( String loginValue, String password) {
        return super.userLogin("customers", "email", loginValue, password, customerMapper); // Skicka med loginField
    }



    public ArrayList<Customer> getAllCustomers() throws SQLException {
        return customerRepository.getAll();
    }


    public void insertUser(String name, String email, String password, String address, String phone) {

        if (name.isEmpty()) {
            System.out.println("Namn måste fyllas i.");
            return;
        }

        if ( email.isEmpty()) {
            System.out.println("E-mail måste fyllas i.");
            return;
        }
        if ( password.isEmpty() || !isPasswordValid(password) ) {
            System.out.println("Lösenord måste fyllas i. Lösenordet måste innehålla minst 8 tecken, " +
                    "en storbokstav, en liten bokstav samt en siffra");
            return;
        }

        if (isEmailTaken(email)) {
            System.out.println("Den e-mail du angav finns redan registrerad");
            return;
        }

        if (!isEmailValid(email)) {
            System.out.println("Felaktigt format på e-mail.");
            return;
        }

        customerRepository.insertCustomer(name, email, password, address, phone);
        System.out.println("Kund har lagts till!");
    }

    // Uppdatera användarens e-postadress
    public boolean updateEmail(int loggedInCustomerId, String email)  {

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

    public Customer getCustomerById(int customerId){
        return customerRepository.getCustomerById(customerId);
    }


    // Radera användare (endast om inloggad)
    public boolean deleteCustomer() {
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


    private boolean isEmailTaken(String email) {
        return customerRepository.isEmailTaken(email);
    }

    public boolean deleteCustomerById(int idToDelete) throws SQLException {
        return customerRepository.deleteCustomer(idToDelete);
    }
}

