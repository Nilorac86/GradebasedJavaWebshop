package Order;
import Customer.CustomerController;
import Product.Product;
import Product.ProductController;
import Product.ProductService;
import Session.SessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


    public class OrderController {
        private ProductService productService = new ProductService();
        private ProductController productController = new ProductController();
        private CustomerController customerController;

        private Cart cart = new Cart();
        private OrderService orderService = new OrderService();

        public void runMeny() throws SQLException {

            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            while (running){

                String select = orderMenu(scanner);
                switch (select) {
                    case "1":

                        addProductToCart(scanner);
                        break;
                    case "2":
                        deleteProductFromCart(scanner);
                        break;
                    case "3":
                        updateProductQuantity(scanner);
                        break;
                    case "4":
                        createOrderFromCart();
                        break;
                    case "5":
                        cart.showCartItems();
                        break;
                    case "6":
                        getLoggedInCustomerOrders();
                        break;
                    case"7":
                        productController.runMeny();
                    case "8":
                        if (customerController == null){
                            customerController = new CustomerController();
                        }
                        customerController.runCustomerMeny();
                        break;
                    case "0":
                        SessionManager.getInstance().logout(); // Logga ut användaren
                        System.out.println("Du har loggats ut.");
                        running = false;
                        break;
                    case "9":
                        System.out.println("Programmet avslutas");
                        System.exit(0);

                    default:
                        System.out.println("Välj ett alternativ från menyn.");
                }
            }
        }

        public void runAdminMeny() throws SQLException {

            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            while (running){

                String select = orderMenu(scanner);
                switch (select) {
                    case "1":
                        fetchAllOrders();
                        break;
                    case "2":
                        getCustomerOrdersById(scanner);
                        break;
                    case "3":
                        createOrderFromCart();
                        break;
                    case "0":
                        running = false;
                        break;
                    case "9":
                        System.out.println("Programmet avslutas");
                        System.exit(0);

                    default:
                        System.out.println("Välj ett alternativ från menyn.");
                }
            }
        }


        private static String orderMenu(Scanner scanner) {
            System.out.println("Order meny");
            System.out.println("1. Lägga produkter i kundvagn");
            System.out.println("2. Ta bort produkt från kundvagn");
            System.out.println("3. Ändra antal produkter i kundvagn");
            System.out.println("4. Lägga order");
            System.out.println("5. Visa kundvagn");
            System.out.println("6. Se dina ordrar");
            System.out.println("7. Produktmeny");
            System.out.println("8. Kund meny");
            System.out.println("0. Logga ut");
            System.out.println("9. Avsluta hela programmet!");
            System.out.println("Välj ett alternativ:");
            String select = scanner.nextLine();
            return select;
        }




        private static String adminOrderMenu(Scanner scanner) {
            System.out.println("Orderhantering meny");
            System.out.println("1. Hämta alla ordrar");
            System.out.println("2. Hämta en kunds ordrar");
            System.out.println("3. Lägga en order");
            System.out.println("0. Gå tillbaka till huvudmenyn");
            System.out.println("9. Avsluta hela programmet!");
            System.out.println("Välj ett alternativ:");
            String select = scanner.nextLine();
            return select;
        }



        private void fetchAllOrders() throws SQLException {
            ArrayList<Order> orders = orderService.getAllOrders();
            for (Order order : orders) {
                System.out.println("OrderId: " + order.getOrderId() + ", order datum: " + order.getOrderDate());
            }
        }


        private void getLoggedInCustomerOrders() {
            ArrayList<Order> orders = orderService.getLoggedInCustomerOrders();

            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Order Datum: " + order.getOrderDate());
                System.out.println("Produkter i denna order:");

                double orderTotal = 0; // Här sparas totalpriset för ordern

                for (OrderItem oi : order.getItems()) {
                    double itemTotal = oi.getTotalPrice(); // eller oi.getQuantity() * oi.getUnitPrice()
                    orderTotal += itemTotal;

                    System.out.printf("Produkt: %s | Beskrivning: %s | Antal: %d | Enhetspris: %.2f | Summa: %.2f \n",
                            oi.getProduct().getName(),
                            oi.getProduct().getDescription(),
                            oi.getQuantity(),
                            oi.getUnitPrice(),
                            itemTotal);
                }

                // Skriv ut totalpriset efter att alla orderrader visats
                System.out.println("----------------------------------");
                System.out.printf("Total summa för ordern: %.2f kr\n", orderTotal);
                System.out.println("----------------------------------------------------------------------");
            }
        }



        private void getCustomerOrdersById(Scanner scanner) {
            try {
                System.out.println("Ange kund id: ");
                int customerId = scanner.nextInt();
                scanner.nextLine();
                ArrayList<Order> orders = orderService.getCustomerOrders(customerId);


                for (Order order : orders) {
                    System.out.println("Order ID: " + order.getOrderId());
                    System.out.println("Order Datum: " + order.getOrderDate());
                    System.out.println("Produkter i denna order:");


                    for (OrderItem oi : order.getItems()) {

                        System.out.printf("Produkt: %s | Beskrivning: %s | Antal: %d | Enhetspris: %.2f\n",
                                oi.getProduct().getName(),
                                oi.getProduct().getDescription(),
                                oi.getQuantity(),
                                oi.getUnitPrice());
                    }
                    System.out.println("----------------------------------");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Kunde inte hämta kundens ordrar", e);
            }
        }

        private void createOrderFromCart() throws SQLException {
            int customerId = SessionManager.getInstance().getLoggedInUserId();

            if (customerId == -1) {
                System.out.println("Ingen kund är inloggad.");
                return;
            }

            ArrayList<OrderItem> orderItems = new ArrayList<>();
            for (CartItem ci : cart.getCartItems().values()) {
                orderItems.add(new OrderItem(ci.getProduct(), ci.getQuantity()));
            }

            if (orderItems.isEmpty()) {
                System.out.println("Din kundvagn är tom.");
                return;
            }

            double totalPrice = cart.totalPriceCart();

            Order order = new Order(customerId, new java.util.Date(), orderItems);
            int orderId = orderService.createOrder(order); // returnerar genererat ID

            if (orderId != -1) {
                for (OrderItem item : orderItems) {
                    orderService.addOrderItem(orderId, item);

                    Product product = item.getProduct();
                    int newQuantity = product.getStockQuantity() - item.getQuantity();
                    product.setStockQuantity(newQuantity);
                    productService.updateStockQuantity(product.getProductId(), newQuantity);

                }
                System.out.println("Ordern är lagd! Order ID: " + orderId);
                System.out.println("Total summa: " + totalPrice);
                cart.clearCart();
            } else {
                System.out.println("Kunde inte skapa order.");
            }
        }


        private void addProductToCart(Scanner scanner) {
            System.out.print("Ange produktens ID att lägga till: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Ange antal: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            try {
                Product product = productService.getProductById(productId);
                if (product != null) {

                    if (product.getStockQuantity() >= quantity) {
                        cart.addProduct(product, quantity); // <-- korrekt här
                        System.out.println("Produkten har lagts till i kundvagnen.");

                    } else {
                        System.out.println("Det finns inte tillräckligt med lager för den begärda kvantiteten. " +
                                "Lagerstatus: " + product.getStockQuantity());
                    }
                }else {
                    System.out.println("Produkten hittades inte.");
                }
            } catch (SQLException e) {
                System.out.println("Fel vid hämtning av produkt: " + e.getMessage());
            }
        }

        private void deleteProductFromCart(Scanner scanner){
            System.out.println("Ange ID på den produkt du vill ta bort från kundvagnen");
            int productId = scanner.nextInt();
            scanner.nextLine();

            if (cart.getCartItems().containsKey(productId)) {
                cart.removeProduct(productId);
                System.out.println("Produkten har tagits bort från kundvagnen.");
            } else {
                System.out.println("Produkten finns inte i kundvagnen.");
            }
        }

        private void updateProductQuantity(Scanner scanner){
            System.out.println("Ange ID på den produkt du vill ändra antalet på i kundvagnen");
            int productId = scanner.nextInt();
            scanner.nextLine();

            if (cart.getCartItems().containsKey(productId)){
                System.out.println("Ange nytt antal");
                int newQuantity = scanner.nextInt();
                scanner.nextLine();

                if(newQuantity > 0 ){
                    cart.getCartItems().get(productId).setQuantity(newQuantity);
                    System.out.println("Antalet har uppdaterats");
                } else {
                    cart.removeProduct(productId);
                    System.out.println("Produkten är nu borttagen eftersom antalet var för lågt");
                }
            }
        }
    }
