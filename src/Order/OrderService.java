package Order;

import Session.SessionManager;
import java.util.ArrayList;

public class OrderService {

    OrderRepository orderRepository = new OrderRepository();

    // Hämta alla ordrar
    public ArrayList<Order> getAllOrders ()  {
        return orderRepository.getAll();

    }

    // Hämta inloggad kund order
    public ArrayList<Order> getLoggedInCustomerOrders(){
        return orderRepository.getCustomerOrders(SessionManager.getInstance().getLoggedInUserId());
    }

    // Hämta kunds order via id
    public ArrayList<Order> getCustomerOrders(int customerId){
        return orderRepository.getCustomerOrders(customerId);
    }

    // Skapa order
    public int createOrder(Order order){
        return orderRepository.createOrder(order);
    }

    // Lägga till produkter
    public void addOrderItem(int orderId, OrderItem item) {
        orderRepository.addOrderItem(orderId, item);
    }

}

