package Review;

import Session.SessionManager;
import java.util.ArrayList;

public class ReviewService {

    private final ReviewRepository reviewRepository = new ReviewRepository();

    // hämtar kunds recensioner
    public ArrayList<Review> getCustomerReviews (){
        try {
            return reviewRepository.getCustomerReviews();
        } catch (Exception e) {
            System.out.println("Fel vid hämtning av recensioner: " + e.getMessage());
            return new ArrayList<>();
        }
    }

// recensera produkter
    public void reviewProduct(int productId, int orderId, int rating, String comment) {
        int customerId = SessionManager.getInstance().getLoggedInUserId();

        if (customerId == -1) {
            System.out.println("Ingen kund är inloggad.");
            return;
        }


            if (!reviewRepository.hasCustomerPurchasedProduct(customerId, productId)) {
                System.out.println("Du kan bara recensera produkter du har köpt.");
                System.out.println();
                return;
            }

            if (reviewRepository.isAlreadyReviewed(customerId, productId, orderId)) {
                System.out.println("Du har redan lämnat en recension för denna produkt i denna order.");
                System.out.println();
                return;
            }

            reviewRepository.insertReview(customerId, productId, rating, comment);
            System.out.println("Tack för din recension!");
            System.out.println();
    }

    // Hämta recenserade produkter
    public ArrayList<ProductRating> getProductsWithRatings() {
        try {
            return reviewRepository.getProductsAverageRatings();
        } catch (Exception e) {
            System.out.println("Fel vid hämtning av produkter med betyg: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
