package Review;

import Presentation.MainController;
import Session.SessionManager;
import Order.Order;
import Order.OrderService;
import Order.OrderItem;
import Product.Product;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReviewController {

    private final OrderService orderService = new OrderService();
   private final ReviewService reviewService = new ReviewService();
   private MainController mainController;

    public void runCustomerMenu() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println();
            System.out.println("=== RECENSIONER ===");
            System.out.println("1. Se dina recensioner");
            System.out.println("2. Lämna en recension");
            System.out.println("3. Se alla produkters betyg");
            System.out.println("3. Tillbaka till huvudmenyn");
            System.out.println("0. Logga ut");
            System.out.println("9. Avsluta programmet");
            System.out.println("Välj ett alternativ:");


            String select = scanner.nextLine();

            switch (select) {
                case "1":
                    getCustomersReviews();
                    break;
                case "2":
                    reviewProduct(scanner);
                    break;
                case "3" :
                    showProductRatings();
                    break;
                case "4":
                    if ( mainController == null){
                        mainController = new MainController();
                    }
                    mainController.mainCustomerMenu();
                    break;
                case "0":
                    SessionManager.getInstance().logout();
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



    private void getCustomersReviews () {
        ArrayList<Review> reviews = reviewService.getCustomerReviews();

        if (reviews.isEmpty()) {
            System.out.println("Du har inte lämnat någon recension än.");
            System.out.println();
            return;
        }

        System.out.println("=== DINA RECENSIONER ===");
        for (Review review : reviews) {
            System.out.println("----------------------------------");
            System.out.println("Produkt: " + review.getProduct().getName());
            System.out.println("Beskrivning: " + review.getProduct().getDescription());
            System.out.println("Betyg: " + review.getRating() + "/5");
            System.out.println("Kommentar: " + review.getComment());
        }
        System.out.println("----------------------------------");

    }

    private void reviewProduct(Scanner scanner) {
        int customerId = SessionManager.getInstance().getLoggedInUserId();
        if (customerId == -1) {
            System.out.println("Ingen kund är inloggad.");
            System.out.println();
            return;
        }

        ArrayList<Order> orders = orderService.getLoggedInCustomerOrders();
        if (orders.isEmpty()) {
            System.out.println("Du har inga ordrar.");
            System.out.println();
            return;
        }

        System.out.println("Dina ordrar:");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderId() + " | Datum: " + order.getOrderDate());
            System.out.println();
        }

        System.out.print("Ange Order ID för produkten du vill recensera: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order selectedOrder = null;
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                selectedOrder = order;
                break;
            }
        }

        if (selectedOrder == null) {
            System.out.println("Ogiltigt order-ID. Skriv in ditt order-ID");
            return;
        }

        System.out.println("Produkter i ordern:");
        for (OrderItem item : selectedOrder.getItems()) {
            Product p = item.getProduct();
            System.out.printf("Produkt ID: %d | Namn: %s\n", p.getProductId(), p.getName());
        }

        System.out.print("Ange produkt-ID att recensera: ");
        int productId = scanner.nextInt();
        scanner.nextLine();


        System.out.print("Ange betyg (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Skriv din kommentar: ");
        String comment = scanner.nextLine();

        reviewService.reviewProduct(productId, orderId, rating, comment);
    }

    public void showProductRatings() {
        ArrayList<ProductRating> productRatings = reviewService.getProductsWithRatings();

        if (productRatings.isEmpty()) {
            System.out.println("Inga produkter hittades.");
            return;
        }

        System.out.println("=== PRODUKTER OCH BETYG ===");
        for (ProductRating pr : productRatings) {
            Product p = pr.getProduct();
            System.out.println("----------------------------------");
            System.out.println("Produkt ID: " + p.getProductId());
            System.out.println("Namn: " + p.getName());
            System.out.println("Pris: " + p.getPrice() + " kr");

            if (pr.getRatingCount() > 0) {
                System.out.printf("Genomsnittligt betyg: %.1f/5 (%d omdömen)%n",
                        pr.getAverageRating(), pr.getRatingCount());
            } else {
                System.out.println("Genomsnittligt betyg: Inga omdömen än");
            }
        }
        System.out.println("----------------------------------");
    }
}
