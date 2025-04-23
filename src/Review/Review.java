package Review;

import Product.Product;

public class Review {

    private int reviewId;
    private int customerId;
    private Product product;
    private int rating;
    private String comment;

    public Review(int reviewId, int customerId, Product product, int rating, String comment) {
        this.reviewId = reviewId;
        this.customerId = customerId;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
    }



    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
