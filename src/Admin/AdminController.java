package Admin;

import Customer.CustomerController;
import Order.OrderController;
import Product.ProductController;
import Session.SessionManager;
import User.UserController;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminController extends UserController {

   private final AdminRepository adminRepository = new AdminRepository();
   private CustomerController customerController;
   private ProductController productController;
   private OrderController orderController;

}


//    public void runAdminMenu() throws SQLException {
//       Scanner scanner = new Scanner(System.in);
//       boolean running = true;
//
//
//       while (running) {
//          System.out.println("\n--- ADMINMENY ---");
//          System.out.println("1. Kundhantering");
//          System.out.println("2. Produkthantering");
//          System.out.println("3. Orderhantering");
//          System.out.println("0. Logga ut");
//          System.out.println("9. Avsluta programmet");
//          System.out.println("Välj ett alternativ ");
//          String select = scanner.nextLine();

//         switch (select) {
//            case "1":
//               if (customerController == null){
//                  customerController = new CustomerController();
//               }
//               customerController.runAdminMenu();
//               break;
//            case "2":
//               if (productController == null){
//                  productController = new ProductController();
//               }
//                       productController.runAdminMeny();
//               break;
//            case "3":
//               if (orderController == null){
//                  orderController = new OrderController();
//               }
//               orderController.runAdminMenu();
//               break;
//            case "0":
//               SessionManager.getInstance().logout();
//               running = false;
//               break;
//            case "9":
//               System.out.println("Avslutar programmet...");
//               System.exit(0);
//               break;
//            default:
//               System.out.println("Ogiltigt val, försök igen.");
//         }
//      }
//   }


