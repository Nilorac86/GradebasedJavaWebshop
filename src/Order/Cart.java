package Order;

import Product.Product;

import java.util.HashMap;


    public class Cart {
        private HashMap<Integer, CartItem> cartItems; // Nyckel: produktens ID, Värde: CartItem (produkt och kvantitet)


        public Cart() {
            cartItems = new HashMap<>();
        }

        public void addProduct(Product product, int quantity) {
            if (cartItems.containsKey(product.getProductId())) {
                CartItem item = cartItems.get(product.getProductId());
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                cartItems.put(product.getProductId(), new CartItem(product, quantity));
            }
        }

        public void removeProduct(int productId) {
            cartItems.remove(productId);
        }

        // Beräkna totalpriset för kundvagnen
        public double totalPriceCart() {
            double totalPrice = 0;
            for (CartItem item : cartItems.values()) {
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
            return totalPrice; // Rätt variabel returneras nu
        }

        // Visa alla produkter i kundvagnen
        public void showCartItems() {
            for (CartItem item : cartItems.values()) {
                System.out.println("Produkt: " + item.getProduct().getName() +
                        ", Kvantitet: " + item.getQuantity() +
                        ", Pris: " + item.getProduct().getPrice() +
                        ", Total summa: " + totalPriceCart());
            }
        }

        public HashMap<Integer, CartItem> getCartItems() {
            return cartItems;
        }

        public void clearCart() {
            cartItems.clear();
        }

    }

