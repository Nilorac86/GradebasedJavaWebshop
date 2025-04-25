package Product;
import java.sql.*;
import java.util.ArrayList;

// URL är adressen till databasen. Den ligger i samma mapp och vi behöver därför endast skriva
public class ProductRepository {


    public static final String URL = "jdbc:sqlite:webbutiken.db";

    // En metod som skickar en SQL till databasen och tar emot ett svar. Sparar detta svar i ett ResultSet.
    // Metoden returnerar till sist en Arraylist av produkter

    public ArrayList<Product> getAll() {

        // Initierar en Arraylist.
        ArrayList<Product> products = new ArrayList<>();

        // Upprättar en koppling till databasen, definierar en query och skickar, sparar svaret i ett ResultSet
        //System.out.println("Detta är metoden för att hämta alla produkter getAll()");
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {


            // Loopar igenom ResultSet och skapar product.objekt för varje rad.
            while (rs.next()) {
                // En rad är lika med ett objekt.

                // Vi tar tag i de kolumner vi vill och sätter dessa värden på våra attribut i Produkt-objektet.

                products.add(new Product(rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity")));
                // Sparar objekten i en samling (Arraylist) som heter products.
            }
        } catch (SQLException e) {
            System.out.println("Fel vid hämtning av produkter: " + e.getMessage());
        }
        //Returnerar en Arraylist.
        return products;

    }

    // Query Hämta produkter via namn
    public ArrayList<Product> getProductsByName(String name) {

        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products WHERE name LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return null;
            }
            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity")));
            }
        } catch (SQLException e) {
            System.out.println("Fel vid sökning av produkter via namn: " + e.getMessage());
        }

        return products;
    }

    // Query hämta produkter via kategori
    public ArrayList<Product> getProductsByCategory(String categoryName) {
        ArrayList<Product> products = new ArrayList<>();

        String sql = """
                SELECT categories.name AS categoryName, products.*\s
                FROM products \s
                JOIN products_categories ON products.product_id = products_categories.product_id
                JOIN categories ON products_categories.category_id = categories.category_id WHERE categories.name LIKE ?""";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + categoryName + "%");

            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("categoryName")));
            }
        } catch (SQLException e) {
            System.out.println("Fel vid sökning av produkter via kategori: " + e.getMessage());
        }

        return products;
    }

    // Uppdatera pris på produkt
    public boolean updateProductPrice(int product_id, double price) {
        String sql = "UPDATE products SET price = ? WHERE product_id = ? ";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, price);
            pstmt.setInt(2, product_id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Fel vid uppdatering av produktens pris: " + e.getMessage());
            return false;
        }
    }

// Hämta produkt via id
    public Product getProductById(int productId) {

        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return (new Product(rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity")));
            }
        } catch (SQLException e) {
                System.out.println("Fel vid hämtning av produkt med ID " + productId + ": " + e.getMessage());
        }
                return null;
    }

// Uppdaterar lagersaldo
    public void updateStockQuantity(int product_id, int stock_quantity) {
        String sql = "UPDATE products SET stock_quantity = ? WHERE product_id = ? ";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stock_quantity);
            pstmt.setInt(2, product_id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fel vid uppdatering av lagerkvantitet för produkt med ID " + product_id + ": " + e.getMessage());
        }
    }

// Lägger till produkt
    public void insertProduct(String name, String description, double price, int stockQuantity) {
        String sql = "INSERT INTO products(name, description, price, stock_quantity) VALUES (?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, stockQuantity);


            pstmt.execute();

        } catch (SQLException e) {
        System.out.println("Fel när produkt skulle läggas till: " + e.getMessage());
    }
    }

    // Sök produkter via filter
    public ArrayList<Product> getProductsByFilter(String category, String productName, double maxPrice) {
        ArrayList<Product> products = new ArrayList<>();

        String sql = """
                SELECT categories.name AS categoryName, products.*
                                FROM products\s
                                JOIN products_categories ON products.product_id = products_categories.product_id
                                JOIN categories ON products_categories.category_id = categories.category_id WHERE \
                                categories.name LIKE ? OR products.name LIKE ? OR price <= ?  ;""";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, "%" + category + "%");
            pstmt.setString(2, "%" + productName + "%");
            pstmt.setDouble(3, maxPrice);


            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("categoryName")));


            }
        } catch (SQLException e) {
            System.out.println("Fel vid sökning av produkter via filter: " + e.getMessage());
        }
        return products;
    }
}