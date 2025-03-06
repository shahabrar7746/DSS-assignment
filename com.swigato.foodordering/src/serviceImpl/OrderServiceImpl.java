package serviceImpl;

import dao.OrderDao;
import daoImpl.OrderDaoImpl;
import entities.Order;
import enums.OrderStatus;
import service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private  OrderDao orderDao;

    public OrderServiceImpl() {

    }
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void placeOrder(Order order) {
        orderDao.addOrder(order);
        simulateOrderProcessing(order);
    }

    public Order getOrder(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    public void simulateOrderProcessing(Order order) {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                order.setOrderStatus(OrderStatus.PROCESSING);
                orderDao.updateOrder(order);

                Thread.sleep(15000);
                order.setOrderStatus(OrderStatus.COMPLETED);
                orderDao.updateOrder(order);

            } catch (InterruptedException e) {
                System.out.println("Order processing was interrupted for Order ID: " + order.getId());
                order.setOrderStatus(OrderStatus.CANCELED);
            }
        });
        thread.start();
    }

}
