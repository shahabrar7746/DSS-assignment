package entities;

import enums.OrderStatus;
import utility.ColourCodes;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Order {
    private final int id;
    private OrderStatus orderStatus;
    private final Customer customer;
    //private Map<Food, Integer> customerCart;
//
//    private List<Food> foodList;
    private final Date orderOn;

    public Order(Customer customer, OrderStatus orderStatus) {
        this.customer = customer;
        // this.customerCart = customerCart;

        this.orderStatus = orderStatus;
        this.id = new Random().nextInt(0, 2000);
        this.orderOn = new Date();
    }
//    public List<Food> getFoodList() {
//        return foodList;
//    }
//
//    public void setFoodList(List<Food> foodList) {
//        this.foodList = foodList;
//    }
    public int getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", id=" + id +
                ", orderStatus=" + orderStatus +
                ", orderOn=" + orderOn +
                '}';
    }


//    public String toString() {
//        StringBuilder orderDetails = new StringBuilder();
//        orderDetails.append(ColourCodes.PURPLE + "+----------------+----------------------------------+\n" + ColourCodes.RESET);
//        orderDetails.append(ColourCodes.BLUE + "| Order ID       | " + ColourCodes.RESET).append(id).append("\n");
//        orderDetails.append(ColourCodes.PURPLE + "+----------------+----------------------------------+\n" + ColourCodes.RESET);
//        orderDetails.append(ColourCodes.BLUE + "| Customer       | " + ColourCodes.RESET).append(customer.getName()).append("\n");
//        orderDetails.append(ColourCodes.PURPLE + "+----------------+----------------------------------+\n" + ColourCodes.RESET);
//        orderDetails.append(ColourCodes.BLUE + "| Status         | " + ColourCodes.RESET).append(orderStatus).append("\n");
//        orderDetails.append(ColourCodes.PURPLE + "+----------------+----------------------------------+\n" + ColourCodes.RESET);
//        orderDetails.append(ColourCodes.BLUE + "| Order on       | " + ColourCodes.RESET).append(orderOn).append("\n");
//        orderDetails.append(ColourCodes.PURPLE + "+----------------+----------------------------------+\n" + ColourCodes.RESET);
//        orderDetails.append(ColourCodes.BLUE + "| Items          | Quantity\n" + ColourCodes.RESET);
//        orderDetails.append(ColourCodes.PURPLE + "+----------------+----------------------------------+\n" + ColourCodes.RESET);
//        for (Map.Entry<Food, Integer> entry : customerCart.entrySet()) {
//            orderDetails.append(ColourCodes.BLUE).append("| ").append(String.format("%-14s", entry.getKey().getName()))
//                    .append(" | ").append(ColourCodes.RESET).append(entry.getValue()).append("\n");
//        }
//        orderDetails.append(ColourCodes.PURPLE + "+----------------+----------------------------------+\n" + ColourCodes.RESET);
//
//        return orderDetails.toString();
//    }
}