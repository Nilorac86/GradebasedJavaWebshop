package Order;

import Product.Product;


public class OrderItem {

        private Product product;
        private int quantity;
        private double unitPrice;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice(){
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return "ProductId: " + product.getProductId() +
                "Product: " + product.getName() +
                "Antal: " + quantity +
                "Enhetspris: " + unitPrice +
                "Total summa: " + getTotalPrice();
    }
}
