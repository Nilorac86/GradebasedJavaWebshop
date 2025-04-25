package Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

// En klass vars enda ansvar är att skicka queries till databasen och hantera svaren.
public class ProductService {

    ProductRepository productRepository = new ProductRepository();

    public ArrayList<Product> getAllProducts() {
        return productRepository.getAll();
    }

    public ArrayList<Product> getProductByName(String name) {
        ArrayList<Product> products = productRepository.getProductsByName(name);

        if (products == null || products.isEmpty()) {
            products = new ArrayList<>();

            System.out.println("Ingen produkt hittades med namnet: " + name);
            System.out.println();
        }

        return products;
    }


    public ArrayList<Product> getProductByCategory(String categoryName)  {
        ArrayList<Product> products = productRepository.getProductsByCategory(categoryName);

        if (products == null || products.isEmpty()) {
            products = new ArrayList<>();

            System.out.println("Ingen produkt hittades med kategorinamnet: " + categoryName);
            System.out.println();
        }
        return products;
    }


    public void updateProductPrice(int productId, double price) {
        if (price <= 0) {
            System.out.println("Pris måste vara större än 0");
            System.out.println();
            return;
        }

        Product product = productRepository.getProductById(productId);

        if (product == null) {
            System.out.println("Produkt med ID " + productId + " hittades ej.");
            System.out.println();
            return;
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

    }

    public void updateStockQuantity(int productId, int stockQuantity) {
        productRepository.updateStockQuantity(productId, stockQuantity);
    }

    public void insertProduct(String name, String description, double price, int stockQuantity) throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Namnet får inte vara tomt");
            System.out.println();
            return;
        }

        if (price <= 0) {
            System.out.println("Priset måste vara större än 0");
            System.out.println();
            return;
        }

        if (stockQuantity < 0) {
            System.out.println("Lagersaldo kan inte vara negativt");
            System.out.println();
            return;
        }
        productRepository.insertProduct(name, description, price, stockQuantity);
        System.out.println("Produkten: " + name + " har lagts till!");
        System.out.println();
    }

    public Product getProductById(int productId) throws SQLException {
        return productRepository.getProductById(productId);
    }

    public ArrayList<Product> getProductByFilter(String category, String productName, double maxPrice ) {

        ArrayList<Product> products = productRepository.getProductsByFilter(category, productName, maxPrice);

        if (maxPrice <= 0){
            System.out.println("Ogiltigt pris. Maxpris måste vara ett positivt tal.");
            return new ArrayList<>();
        }

            return (ArrayList<Product>) products.stream()
                    .filter(p -> p.getCategoryName().toLowerCase().contains(category.toLowerCase()))
                    .filter(p -> p.getName().toLowerCase().contains(productName.toLowerCase()))
                    .filter(p -> p.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

}

