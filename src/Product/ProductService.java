package Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

// En klass vars enda ansvar är att skicka queris till databasen och hantera svaren.
public class ProductService {

    ProductRepository productRepository = new ProductRepository();

    public ArrayList<Product> getAllProducts () throws SQLException{
        return productRepository.getAll();
    }

    public ArrayList<Product> getProductByName (String name) throws SQLException{
        return productRepository.getProductsByName(name);

    }

    public ArrayList<Product> getProductByCategory(String categoryName) throws SQLException{
        return productRepository.getProductsByCategory(categoryName);
    }


    public boolean updateProductPrice(int productId, double price) throws SQLException {
        if (price <= 0) {
            System.out.println("Pris måste vara större än 0");
            System.out.println();
            return false;
        }

        Product product = productRepository.getProductById(productId);

        if (product == null) {
            System.out.println("Produkt med ID " + productId + " hittades ej.");
            System.out.println();
            return false;
        }

        boolean success = productRepository.updateProductPrice(productId, price);

        if (success) {
            System.out.println("Priset på : " + product.getName() +
                    " har uppdaterats. Nytt pris: " + price + " kr");
            System.out.println();
        } else {
            System.out.println("Uppdateringen misslyckades.");
            System.out.println();

        }

        return success;
    }

    public void updateStockQuantity(int productId, int stockQuantity)throws SQLException{
        productRepository.updateStockQuantity(productId, stockQuantity);
    }

    public void insertProduct(String name, String description, double price, int stockQuantity) throws SQLException{

        if(name == null || name.trim().isEmpty()){
            System.out.println("Namnet får inte vara tomt");
            return;
        }

        if (price <= 0){
            System.out.println("Priset måste vara större än 0");
            return;
        }

        if ( stockQuantity < 0) {
            System.out.println("Lagersaldo kan inte vara negativt");
            return;
        }
        productRepository.insertProduct(name, description, price, stockQuantity);
        System.out.println("Produkten: " + name + " har lagts till!");
        System.out.println();
    }

    public Product getProductById(int productId) throws SQLException{
        return productRepository.getProductById(productId);
    }

    public ArrayList<Product> getProductByFilter(String category, double maxPrice) {

        ArrayList<Product> products = productRepository.getProductsByFilter(category, maxPrice);


        return (ArrayList<Product>) products.stream()
                .filter(p -> p.getCategoryName().toLowerCase().contains(category.toLowerCase()))
                .filter(p -> p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
}

