package Review;

import Session.SessionManager;

import java.util.ArrayList;

public class ReviewService {

    private ReviewRepository reviewRepository = new ReviewRepository();


    public ArrayList<Review> getCustomerReviews (){
        return reviewRepository.getCustomerReviews();
    }



//    public void reviewProduct(int productId, int rating, String comment) {
//        int customerId = SessionManager.getInstance().getLoggedInUserId();
//
//        if (customerId == -1) {
//            System.out.println("Ingen kund är inloggad.");
//            return;
//        }
//
//        if (!reviewRepository.hasCustomerPurchasedProduct(customerId, productId)) {
//            System.out.println("Du kan bara lämna recensioner på produkter du har köpt.");
//            return;
//        }
//
//        reviewRepository.insertReview(customerId, productId, rating, comment);
//        System.out.println("Tack! Din recension är nu sparad.");
//    }

    public void reviewProduct(int productId, int orderId, int rating, String comment) {
        int customerId = SessionManager.getInstance().getLoggedInUserId();

        if (customerId == -1) {
            System.out.println("Ingen kund är inloggad.");
            return;
        }

        if (!reviewRepository.hasCustomerPurchasedProduct(customerId, productId)) {
            System.out.println("Du kan bara recensera produkter du har köpt.");
            return;
        }

        if (reviewRepository.isAlreadyReviewed(customerId, productId, orderId)) {
            System.out.println("Du har redan lämnat en recension för denna produkt i denna order.");
            return;
        }

        reviewRepository.insertReview(productId, orderId, rating, comment);
        System.out.println("Tack för din recension!");
    }


}
