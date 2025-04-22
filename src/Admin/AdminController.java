package Admin;

import Customer.CustomerController;
import Order.OrderController;
import Product.ProductController;
import Session.SessionManager;
import User.UserController;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminController extends UserController {

   private AdminRepository adminRepository = new AdminRepository();


   private CustomerController customerController = new CustomerController();
   private ProductController productController = new ProductController();
   private OrderController orderController = new OrderController();


   public void runAdminMenu() throws SQLException {
      Scanner scanner = new Scanner(System.in);
      boolean running = true;

      while (running) {
         String select = adminMenu(scanner);
         switch (select) {
            case "1":
               customerController.runAdminMeny();
               break;
            case "2":
               productController.runAdminMeny();
               break;
            case "3":
               orderController.runAdminMeny();
               break;
            case "0":
               SessionManager.getInstance().logout();
               running = false;
               break;
            case "9":
               System.out.println("Avslutar programmet...");
               System.exit(0);
               break;
            default:
               System.out.println("Ogiltigt val, försök igen.");
         }
      }
   }


   private String adminMenu(Scanner scanner) {
      System.out.println("\n--- ADMINMENY ---");
      System.out.println("1. Kundhantering");
      System.out.println("2. Produkthantering");
      System.out.println("3. Orderhantering");
      System.out.println("0. Logga ut");
      System.out.println("9. Avsluta programmet");
      System.out.println("Välj ett alternativ ");
      return scanner.nextLine();
   }

}

