package Customer;

import User.UserRepository;

import java.sql.*;
import java.util.ArrayList;

public class CustomerRepository extends UserRepository {

   public static final String URL = "jdbc:sqlite:webbutiken.db";

    public ArrayList<Customer> getAll() {

        ArrayList<Customer> customers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address")));

            }

        } catch (SQLException e) {
            System.out.println("Fel vid hämtning av alla kunder: " + e.getMessage());
            return null;
        }
        return customers;
    }


    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            ResultSet rs = pstmt.executeQuery();


            if (rs.next()) {
                return new Customer(customerId, rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"));
            }

        } catch (SQLException e) {
            System.out.println("Fel vid hämtning av kund med ID " + customerId + ": " + e.getMessage());

        }
        return null;
    }


    public void insertCustomer(String name, String email, String password, String phone, String address)  {
        String sql = "INSERT INTO customers(name, email, password, phone, address) VALUES (?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);

            pstmt.execute();
        } catch (SQLException e) {
            System.out.println("Något gick fel vid skapande av kund: " + e.getMessage());
        }
    }

    public boolean updateEmail(int customer_id, String email) {
        String sql = "UPDATE customers SET email = ? WHERE customer_id = ? ";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setInt(2, customer_id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Något gick fel vid uppdatering av e-mail" + e.getMessage());
        }
        return false;
    }

    public boolean deleteCustomer(int customerId)  {
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Något gick när kund skulle raderas: " +e.getMessage());
        }
        return false;
    }

    public boolean isEmailTaken(String email)  {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            // Om antalet rader är större än 0, betyder det att e-posten är tagen
            return rs.getInt(1) > 0;
        } catch(SQLException e){
            System.out.println("Något gick fel när email uppdaterades: " +e.getMessage());
        }
        return false;
    }



}

