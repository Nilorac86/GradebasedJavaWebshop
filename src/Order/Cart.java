package Order;

import Product.Product;

import java.util.HashMap;


    public class Cart {
        private final HashMap<Integer, CartItem> cartItems; // Nyckel med produktens ID, och värde CartItem, produkt och kvantitet.


        public Cart() {
            cartItems = new HashMap<>();
        } // constructor för kundvagn

        // Lägger till produkt i kundvagnen, hämtar produkt via id, skapar nytt objekt med produktId och kvantitet.
        public void addProduct(Product product, int quantity) {
            if (cartItems.containsKey(product.getProductId())) {
                CartItem item = cartItems.get(product.getProductId());
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                cartItems.put(product.getProductId(), new CartItem(product, quantity));
            }
        }

        // Tar bort en produkt från kundvagn via id
        public void removeProduct(int productId) {
            cartItems.remove(productId);
        }

        // Beräkna totalpriset för kundvagnen
        public double totalPriceCart() {
            double totalPrice = 0;
            for (CartItem item : cartItems.values()) {
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
            return totalPrice;
        }

        // Visar alla produkter i kundvagnen
        public void showCartItems() {

            // För varje produkt i kundvagnen, räknas summa av produkten, skriver ut en för varje rad/produkt.
            for (CartItem item : cartItems.values()) {
                double itemTotal = item.getProduct().getPrice() * item.getQuantity();

                System.out.println("Produkt: " + item.getProduct().getName() +
                        ", Kvantitet: " + item.getQuantity() +
                        ", Pris: " + item.getProduct().getPrice() +
                        ", Summa: " + itemTotal);
                System.out.println(2);
            }
            System.out.println("Summa kundvagn : " + totalPriceCart() + "Kr"); // totalpris kundvagn
            System.out.println("-----------------------------------------");
            System.out.println();
        }


        public HashMap<Integer, CartItem> getCartItems() {
            return cartItems;
        } // En getter för kundvagnen.

        public void clearCart() {
            cartItems.clear();
        } // Tömmer kundvagnen

    }

