package Review;

import Product.Product;
import Session.SessionManager;

import java.sql.*;
import java.util.ArrayList;

public class ReviewRepository {

    public static final String URL = "jdbc:sqlite:webbutiken.db";

    public ArrayList<Review> getCustomerReviews() {
        ArrayList<Review> reviews = new ArrayList<>();

        int customerId = SessionManager.getInstance().getLoggedInUserId();
        if (customerId == -1) {
            System.out.println("Ingen kund är inloggad.");
            return reviews;
        }

        String sql = "SELECT reviews.*, products.* " +
                "FROM customers " +
                "JOIN reviews ON customers.customer_id = reviews.customer_id " +
                "JOIN products ON reviews.product_id = products.product_id " +
                "JOIN orders_products ON orders_products.product_id = products.product_id " +
                "WHERE customers.customer_id = ?;";

        try (
                Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("review_id"),
                        customerId,
                        new Product(
                                rs.getInt("product_id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getDouble("price"),
                                rs.getInt("stock_quantity")
                        ),
                        rs.getInt("rating"),
                        rs.getString("comment")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Något gick fel vid hämtning ");
            e.printStackTrace();
            ;
        }

        return reviews;
    }


    public boolean hasCustomerPurchasedProduct(int customerId, int productId) {
        String sql = "SELECT 1 FROM orders " +
                "JOIN orders_products ON orders.order_id = orders_products.order_id " +
                "WHERE orders.customer_id = ? AND orders_products.product_id = ? LIMIT 1";

        try (
                Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, productId);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // true om det finns ett köp
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertReview(int customerId, int productId, int rating, String comment) {
        String sql = "INSERT INTO reviews (customer_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, rating);
            pstmt.setString(4, comment);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAlreadyReviewed(int customerId, int productId, int orderId) {
        String sql = "SELECT 1 FROM customers \n" +
                "JOIN reviews ON customers.customer_id = reviews.customer_id\n" +
                "JOIN products ON reviews.product_id = products.product_id\n" +
                "JOIN orders_products ON orders_products.product_id= orders_products.product_id WHERE " +
                "reviews.customer_id= ? AND reviews.product_id = ? AND orders_products.product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, orderId);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Kunde inte kontrollera om recension redan finns.", e);
        }
    }


}





