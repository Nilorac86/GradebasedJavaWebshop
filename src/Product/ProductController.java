package Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import Order.OrderController;
import Presentation.MainController;
import Session.SessionManager;
public class ProductController {


    private OrderController orderController;
    private MainController mainController;
    private final ProductService productService = new ProductService();


    // ########################### KUNDMENY PRODUKTER ####################################
    public void runMeny() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println();
            System.out.println("=== PRODUKTER ===");
            System.out.println("1. Hämta alla produkter");
            System.out.println("2. Sök produkt efter produktnamn");
            System.out.println("3. Sök produkt efter kategori");
            System.out.println("4. Sök på produkt efter kategori och maxpris");
            System.out.println("5. Gå tillbaka till order");
            System.out.println("0. Logga ut");
            System.out.println("9. Avsluta hela programmet");
            System.out.println("Välj ett alternativ:");


            String select = scanner.nextLine();

            switch (select) {
                case "1":
                    fetchAllProducts();
                    break;
                case "2":
                    seachProductByName(scanner);
                    break;
                case "3":
                    searchProductByCategory(scanner);
                    break;
                case "4":
                    getProductsByFilter(scanner);
                    break;
                case "5":
                    if (orderController == null) {
                        orderController = new OrderController();
                    }
                    orderController.runCustomerMenu();
                    break;
                case "0":
                    SessionManager.getInstance().logout(); // Logga ut användaren
                    System.out.println("Du har loggats ut.");
                    System.exit(0);
                    break;
                case "9":
                    System.out.println("Programmet avslutas");
                    System.exit(0);
                default:
                    System.out.println("Välj ett alternativ från menyn.");
            }
        }
    }

    // ########################### ADMINMENY PRODUKTER ####################################


    public void runAdminMeny() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println();
            System.out.println("=== ADMIN PRODUKTMENY ===");
            System.out.println("1. Hämta alla produkter");
            System.out.println("2. Sök produkt efter produktnamn");
            System.out.println("3. Sök produkt efter kategori");
            System.out.println("4. Uppdatera pris på produkt");
            System.out.println("5. Lägg till en produkt");
            System.out.println("6. Sök produkt med filter");
            System.out.println("7. Tillbaka till huvudmenyn");
            System.out.println("0. Logga ut");
            System.out.println("9. Avsluta programmet");
            System.out.println("Välj ett alternativ:");


            String select = scanner.nextLine();

            switch (select) {
                case "1":
                    fetchAllProducts();
                    break;
                case "2":
                    seachProductByName(scanner);
                    break;
                case "3":
                    searchProductByCategory(scanner);
                    break;
                case "4":
                    updateProductPrice(scanner);
                    break;
                case "5":
                    insertProduct(scanner);
                    break;
                case "6":
                    getProductsByFilter(scanner);
                    break;
                case "7":
                    if(mainController == null){
                        mainController = new MainController();
                    }
                    mainController.mainAdminMenu();
                    break;
                case "0":
                    SessionManager.getInstance().logout(); // Logga ut användaren
                    System.out.println("Du har loggats ut.");
                    System.exit(0);
                    break;
                case "9":
                    System.out.println("Programmet avslutas");
                    System.exit(0);
                default:
                    System.out.println("Välj ett alternativ från menyn.");
            }
        }
    }

    // ########################### METODER PRODUKTER ####################################

    // Hämta alla produkter
    public void fetchAllProducts() {
        ArrayList<Product> products = productService.getAllProducts();

        for (Product product : products) {

            System.out.println("ProductId: " + product.getProductId());
            System.out.println("Namn: " + product.getName());
            System.out.println("Pris: " + product.getPrice());
            System.out.println("Antal i lager " + product.getStockQuantity());
            System.out.println();
        }
    }

    // Sök produkter via produktnamn
    private void seachProductByName(Scanner scanner) {
        try {
            System.out.println("Skriv in ett produktnamn:");
            String name = scanner.nextLine();
            ArrayList<Product> products = productService.getProductByName(name);

            if (products.isEmpty()) {
                System.out.println();
            } else {
                for (Product product : products) {

                    System.out.println("Produkt: " + product.getName());
                    System.out.println("Pris: " + product.getPrice());
                    System.out.println("Beskrivning: " + product.getDescription());
                    System.out.println();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // Söka produkter via kategori
    private void searchProductByCategory(Scanner scanner) {
        System.out.println("Sök produkt via kategori:");
        String categoryName = scanner.nextLine();


        ArrayList<Product> products = productService.getProductByCategory(categoryName);

        if (products.isEmpty()) {
            System.out.println();
        } else {

            System.out.println("\n====== MATCHANDE PRODUKTER ======\n");

            for (Product product : products) {
                System.out.println("Kategori: " + product.getCategoryName());
                System.out.println("Produkt: " + product.getName());
                System.out.println("Pris: " + product.getPrice());
                System.out.println("Beskrivning: " + product.getDescription());
                System.out.println("-----------------------------------");
                System.out.println();
            }
        }
    }


    // Uppdatera produkt pris
    public void updateProductPrice(Scanner scanner) {
        System.out.println("Ange ID på den produkt som ska uppdateras:");

        int productId = -1;

        while (productId == -1) {
            String idInput = scanner.nextLine();


            if (idInput.isEmpty()) {
                System.out.println("Produkt-ID får inte vara tomt.");
            } else {
                try {
                    productId = Integer.parseInt(idInput);
                    if (productId <= 0) {
                        System.out.println("Produkt-ID måste vara ett positivt heltal.");
                        productId = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ogiltigt ID. Ange ett giltigt heltal.");
                }
            }
        }

        System.out.println("Ange nytt pris:");
        double price = -1;

        while (price == -1) {
            String priceInput = scanner.nextLine().trim();


            if (priceInput.isEmpty()) {
                System.out.println("Pris får inte vara tomt. Ange nytt pris:");
            } else {
                try {
                    price = Double.parseDouble(priceInput);
                    if (price <= 0) {
                        System.out.println("Pris måste vara större än 0.");
                        price = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ogiltigt prisformat. Ange ett numeriskt värde." + e.getMessage());
                }
            }
        }

        productService.updateProductPrice(productId, price);
    }


// Lägga till produkt
    private void insertProduct(Scanner scanner) {
        System.out.println("Ange namn på produkten:");
        String name = scanner.nextLine();
        while (name.trim().isEmpty()) {
            System.out.println("Namn måste fyllas i. ");
            System.out.println("Ange ett namn på produkten:");
            name = scanner.nextLine();
        }

        System.out.println("Ange en beskrivning av produkten:");
        String description = scanner.nextLine();

        double price = -1;
        while (price <= 0) {
            System.out.println("Ange produktens pris");
            String priceInput = scanner.nextLine().replace(',', '.');
            if (priceInput.trim().isEmpty()) {
                System.out.println("Pris får inte vara tomt.");
                continue;
            }
            try {
                price = Double.parseDouble(priceInput);
                if (price <= 0) {
                    System.out.println("Pris måste vara större än 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ogiltigt format på pris. Ange ett numeriskt värde.");
            }
        }

        int stockQuantity = -1;
        while (stockQuantity < 0) {
            System.out.println("Ange lagersaldo: ");

            String stockInput = scanner.nextLine().trim();

            if (stockInput.isEmpty()) {
                System.out.println("Lagersaldo får inte vara tomt.");
                continue;
            }

            try {
                stockQuantity = Integer.parseInt(stockInput);
                if (stockQuantity < 0) {
                    System.out.println("Lagersaldo får inte vara negativt.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ogiltigt format. Ange ett heltal.");
            }
        }

        try {
            productService.insertProduct(name, description, price, stockQuantity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // Hämta produkter via filter, namn, kategori och pris
    private void getProductsByFilter(Scanner scanner) {
        System.out.println("Ange kategori, eller tryck enter");
        String category = scanner.nextLine();

        System.out.println("Ange produktnamn, eller tryck enter");
        String productName = scanner.nextLine();

        double maxPrice = getValidPrice(scanner);


        ArrayList<Product> products = productService.getProductByFilter(category, productName, maxPrice);

        if (products.isEmpty()) {
            System.out.println("Inga produkter matchar dina filter: " + category + " | " + productName + " | " + maxPrice);
            System.out.println();
        } else {
            System.out.println("\n====== MATCHANDE PRODUKTER ======\n");

            for (Product p : products) {
                System.out.println("Kategori: " + p.getCategoryName());
                System.out.println("Namn: " + p.getName());
                System.out.println("Pris: " + p.getPrice() + " kr");
                System.out.println("Beskrivning: " + p.getDescription());
                System.out.println("-----------------------------------");
            }
        }
    }

// Validerings metod.
    private double getValidPrice(Scanner scanner) {
        double maxPrice = -1;
        while (maxPrice < 0) {
            System.out.println("Ange maxpris: ");
            String priceInput = scanner.nextLine();

            try {
                maxPrice = Double.parseDouble(priceInput);
                if (maxPrice < 0) {
                    System.out.println("Pris får inte vara ett negativt värde.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ogiltigt format. Ange ett numeriskt värde.");
            }
        }
        return maxPrice;
    }

}