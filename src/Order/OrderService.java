package Order;

import Session.SessionManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderService {

    OrderRepository orderRepository = new OrderRepository();

    public ArrayList<Order> getAllOrders () throws SQLException {
        return orderRepository.getAll();

    }

    public ArrayList<Order> getLoggedInCustomerOrders(){
        return orderRepository.getCustomerOrders(SessionManager.getInstance().getLoggedInUserId());
    }

    public ArrayList<Order> getCustomerOrders(int customerId)throws SQLException{
        return orderRepository.getCustomerOrders(customerId);
    }

    public int createOrder(Order order)throws SQLException{
        return orderRepository.createOrder(order);
    }

    public void addOrderItem(int orderId, OrderItem item) throws SQLException {
        orderRepository.addOrderItem(orderId, item);
    }

}

