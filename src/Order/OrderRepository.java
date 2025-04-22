package Order;

import Product.Product;

import java.sql.*;
import java.util.ArrayList;

public class OrderRepository {


    public static final String URL = "jdbc:sqlite:webbutiken.db";


    public ArrayList<Order> getAll() throws SQLException {


        ArrayList<Order> orders = new ArrayList<>();


        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {


            while (rs.next()) {

                orders.add(new Order(rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getDate("order_date")));
            }

        } catch (SQLException e) {
            // Logga eller kasta vidare exception
            System.err.println("SQL Exception occurred while fetching orders: " + e.getMessage());
            throw e;  // Rethrow or handle in an appropriate way
        }

        return orders;

    }


    public ArrayList<Order> getCustomerOrders(int customerId) {
        ArrayList<Order> customerOrders = new ArrayList<>();
        String sql = "SELECT orders.order_id, customers.customer_id, orders.order_date, products.*, orders_products.quantity, orders_products.unit_price \n" +
                "                FROM customers\n" +
                "                JOIN orders ON customers.customer_id = orders.customer_id \n" +
                "                JOIN \n" +
                "                orders_products ON orders.order_id = orders_products.order_id \n" +
                "                JOIN products ON products.product_id = orders_products.product_id\n" +
                "                WHERE customers.customer_id = ?;";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Skapa en Product-objekt
                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity")
                    );

                    // Skapa ett OrderItem-objekt
                    OrderItem item = new OrderItem(product, rs.getInt("quantity"));

                    // Kontrollera om en Order redan finns med samma order_id
                    boolean orderExists = false;
                    for (Order order : customerOrders) {
                        if (order.getOrderId() == rs.getInt("order_id")) {
                            // Lägg till OrderItem i den befintliga ordern
                            order.getItems().add(item);
                            orderExists = true;
                            break;
                        }
                    }

                    // Om ordern inte finns, skapa en ny Order och lägg till det första OrderItem
                    if (!orderExists) {
                        Order newOrder = new Order(
                                rs.getInt("order_id"),
                                rs.getInt("customer_id"),
                                rs.getDate("order_date"),
                                new ArrayList<>());


                        newOrder.getItems().add(item); // Lägg till första item till ny order
                        customerOrders.add(newOrder);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerOrders;
    }

    public int createOrder (Order order)throws SQLException{
        String sql = "INSERT INTO orders (customer_id, order_date) VALUES (?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, order.customerId);
            pstmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();

            if ( keys.next()){
                return keys.getInt(1);
            }

        }
        return -1;
    }

    public void addOrderItem(int orderId, OrderItem item) throws SQLException {
        String sql = "INSERT INTO orders_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            pstmt.setInt(2, item.getProduct().getProductId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getUnitPrice());
            pstmt.executeUpdate();
        }
    }

}



