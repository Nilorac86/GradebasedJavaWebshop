package Review;

import Product.Product;

public class ProductRating {
    private Product product;
    private double averageRating;
    private int ratingCount;

    public ProductRating(Product product, double averageRating, int ratingCount) {
        this.product = product;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    public Product getProduct() {
        return product;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }
}