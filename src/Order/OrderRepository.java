package Order;

import Product.Product;

import java.sql.*;
import java.util.ArrayList;

public class OrderRepository {


    public static final String URL = "jdbc:sqlite:webbutiken.db";


    // Hämtar alla ordrar
    public ArrayList<Order> getAll() {
        ArrayList<Order> orders = new ArrayList<>();

        String sql = """
            SELECT orders.order_id, customers.customer_id, orders.order_date,
                   products.product_id, products.name, products.description,
                   products.price, products.stock_quantity,
                   orders_products.quantity, orders_products.unit_price
            FROM customers
            JOIN orders ON customers.customer_id = orders.customer_id
            JOIN orders_products ON orders.order_id = orders_products.order_id
            JOIN products ON products.product_id = orders_products.product_id;
            """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Skapa Product-objekt
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity")
                );

                // Skapa OrderItem
                OrderItem item = new OrderItem(product, rs.getInt("quantity"));

                int orderId = rs.getInt("order_id");
                boolean orderExists = false;

                for (Order order : orders) {
                    if (order.getOrderId() == orderId) {
                        order.getItems().add(item);
                        orderExists = true;
                        break;
                    }
                }

                if (!orderExists) {
                    Order newOrder = new Order(
                            orderId,
                            rs.getInt("customer_id"),
                            rs.getDate("order_date"),
                            new ArrayList<>());

                    newOrder.getItems().add(item);
                    orders.add(newOrder);
                }
            }

        } catch (SQLException e) {
            System.out.println("Något gick fel vid hämtning av alla ordrar: " + e.getMessage());
        }

        return orders;
    }

// Hämtar en kunds order via kund id
    public ArrayList<Order> getCustomerOrders(int customerId) {
        ArrayList<Order> customerOrders = new ArrayList<>();
        String sql = """
                SELECT orders.order_id, customers.customer_id, orders.order_date, products.*, orders_products.quantity, orders_products.unit_price\s
                                FROM customers
                                JOIN orders ON customers.customer_id = orders.customer_id\s
                                JOIN\s
                                orders_products ON orders.order_id = orders_products.order_id\s
                                JOIN products ON products.product_id = orders_products.product_id
                                WHERE customers.customer_id = ?;""";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity")
                    );

                    OrderItem item = new OrderItem(product, rs.getInt("quantity"));

                    boolean orderExists = false;
                    for (Order order : customerOrders) {
                        if (order.getOrderId() == rs.getInt("order_id")) {
                            order.getItems().add(item);
                            orderExists = true;
                            break;
                        }
                    }


                    if (!orderExists) {
                        Order newOrder = new Order(
                                rs.getInt("order_id"),
                                rs.getInt("customer_id"),
                                rs.getDate("order_date"),
                                new ArrayList<>());


                        newOrder.getItems().add(item); // Lägg till första produkten i ny order
                        customerOrders.add(newOrder);
                    }
                }
            }
        }catch (SQLException e) {
                System.out.println("Något gick fel när order skulle hämtas." + e.getMessage());
            }

        return customerOrders;
    }


    // Skapa en order genererar ett nyckel för order id
    public int createOrder (Order order){
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

        } catch (SQLException e){
            System.out.println("Något gick fel när order skulle läggas." + e.getMessage());
        }
        return -1;
    }

    // Lägga till produkter till ordern
    public void addOrderItem(int orderId, OrderItem item) {
        String sql = "INSERT INTO orders_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            pstmt.setInt(2, item.getProduct().getProductId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getUnitPrice());
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("Något gick när produkt skulle läggas till." + e.getMessage());
        }
    }

}



