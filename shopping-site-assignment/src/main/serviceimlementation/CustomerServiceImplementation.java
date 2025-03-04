package main.serviceimlementation;

import main.entities.Customer;
import main.entities.Order;
import main.entities.Product;

import main.entities.Seller;
import main.enums.Currency;
import main.enums.OrderStatus;
import main.enums.ResponseStatus;
import main.exceptions.CustomerNotFoundException;
import main.exceptions.EmptyCartException;
import main.exceptions.NoProductFoundException;
import main.exceptions.OrderNotFoundException;

import main.repository.interfaces.OrderRepository;
import main.repository.interfaces.ProductRepository;
import main.repository.interfaces.SellerRepository;
import main.repositoryjdbcimpl.OrderJDBCRepository;
import main.repositoryjdbcimpl.ProductJDBCRepository;
import main.repositoryjdbcimpl.SellerJDBCRepository;
import main.services.CustomerService;




import main.util.ColorCodes;
import main.util.Response;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CustomerServiceImplementation implements CustomerService {
    private  Scanner sc;
    private  List<Product> cart;
private  OrderRepository orderRepository;
private  ProductRepository productRepository;
private  SellerRepository sellerRepository;
private static CustomerServiceImplementation service;
public  static  CustomerServiceImplementation getInstance(){
    if(service == null){
        service = new CustomerServiceImplementation();
    }
    return  service;
}
private CustomerServiceImplementation() {
        init();
    }
    private void init(){
        this.sellerRepository = new SellerJDBCRepository();
        this.productRepository = new ProductJDBCRepository();
        this.cart = new ArrayList<>();
        this.orderRepository = new OrderJDBCRepository();
        this.sc = new Scanner(System.in);
    }


    public void browse(final Customer customer) throws SQLException {
        System.out.println(ColorCodes.GREEN + "******BROWSE*******" + ColorCodes.RESET);
        String operation = "";
        while (!operation.equalsIgnoreCase("back")) {
            Response response = null;
            try {
                System.out.println(ColorCodes.BLUE + "Products : " + productRepository.fetchProducts() + ColorCodes.RESET);
            }catch (NoProductFoundException e){
                System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
            }
            System.out.println("PRESS 1 FOR TO START ORDERING");
            System.out.println("PRESS 2 TO VIEW ORDERS");
            System.out.println("PRESS 3 TO CANCEL ORDER");
            System.out.println("PRESS 4 TO ORDER ITEMS FROM CART");
            System.out.println("TYPE 'BACK' TO GO BACK");
            System.out.print("Operation : ");
            operation = sc.nextLine();

                switch (operation) {
                    case "1":
                      response =  intiateCart(customer, false);
                        break;
                    case "2":
                        response = getAllOrders(customer);
                        break;
                    case "3":
                       response = cancelOrder(customer);
                        break;
                    case "4":
                       response = proceedToOrder(customer, getTotal());
                        break;
                    case "back", "BACK":
                        response = new Response("Going back");
                        break;
                    default:
                       response = new Response(null, "Invalid operation");
                }
            if(response.getStatus() == ResponseStatus.ERROR){
                System.out.println(ColorCodes.RED + "ERROR : " + response.getData() + ColorCodes.RESET);
            }else if(response.getStatus() == ResponseStatus.SUCCESSFUL) {
                System.out.println(ColorCodes.RED + response  + response.getData() + ColorCodes.RESET);
            }

        }
    }

    /**
     *
     * @param customer whose orders need to be searched.
     * @return List of orders of provided objects.
     * @throws OrderNotFoundException if no order found for the customer.
     * @see #bookOrder(Customer, Product)
     * @see #cancelOrder(Customer)
     */
    private Response getAllOrders(Customer customer) throws SQLException {
        System.out.println(ColorCodes.GREEN + "******YOUR*ORDERS******" + ColorCodes.RESET);
        List<Order> orders = null;
        try{
            orders = orderRepository.getOrderByCustomerId(customer.getId());
        } catch (CustomerNotFoundException | NoProductFoundException e) {
            return new Response(e.getLocalizedMessage());
        }
        return orders.isEmpty() ? new Response(null, "No order found") :  new Response(orders);
    }

    /**
     *
     * injects Product object inside order list of customer.
     * @param customer for whom we want to place order.
     * @param product which product we need to order.
     * @see #cancelOrder(Customer) 
     */
    private Response bookOrder(Customer customer, Product product) throws SQLException {
Optional<Seller> optionalSeller = sellerRepository.fetchById(1L);
if(optionalSeller.isEmpty()){
    return new Response(null, "No seller found for this product");
}

        Order order = new Order(customer, product, optionalSeller.get(), OrderStatus.ORDERED, Currency.INR, LocalDateTime.now(), product.getPrice());
        try {
            orderRepository.addOrder(order);
        } catch (Exception e) {
            return new Response(null, e.getLocalizedMessage());
        }
        return new Response("Order booked");
    }

    /**
     *
     * @param customer whose order need to be cancelled.
     * @throws OrderNotFoundException if no orders found for the customer or order list is empty of the provided customer.
     * @see #bookOrder(Customer, Product)
     */

    private Response cancelOrder(Customer customer) throws SQLException {
        System.out.println(ColorCodes.GREEN + "*****CANCEL*ORDER*****" + ColorCodes.RESET);
        List<Order> ordersByCustomer = null;
        try {
            ordersByCustomer = orderRepository.getOrderByCustomerId(customer.getId());
        } catch (CustomerNotFoundException | NoProductFoundException e) {
            return new Response(null, e.getLocalizedMessage());
        }
        System.out.println(ColorCodes.BLUE + "Your orders : " + ordersByCustomer + ColorCodes.RESET);
        if (ordersByCustomer.isEmpty()) {
            return new Response(null, "No orders are there to be cancelled");
        }
        Optional<List<Order>> order = Optional.empty();
        while (!order.isPresent()) {
            System.out.print("Please provide the product name whose order you want to cancel : ");
            String productName = sc.nextLine().toUpperCase();
            List<Order> orders = null;
            try {
                orders = orderRepository.fetchOrderByProductName(productName);
            } catch (CustomerNotFoundException  | NoProductFoundException e) {
                return new Response(null, e.getLocalizedMessage());
            }
            if(!orders.isEmpty()){
                order = Optional.of(orders);
            }
           if(order.isPresent()){
               helperForCancelOrder(order.get(),customer);
           }
            if(order.isPresent()){
                return new Response("Order cancelled..");
            }

        }
        return new Response(null, "Incorrect product name.....");
    }
    private void helperForCancelOrder(List<Order> l,Customer customer) throws SQLException {
        System.out.println(ColorCodes.BLUE + "Your orders : " + l + ColorCodes.RESET);
        if (l.size() > 1) {
            System.out.println("and your quantity of order : " + l.size());
            System.out.print("Please provide the quantity for cancellation : ");
            long quantity = sc.nextLong();
            while (quantity > l.size()) {
                System.out.println(ColorCodes.RED + "Quantity must be lesser or equal to number of orders\nplease try again" + ColorCodes.RESET);
                System.out.print("Quantity : ");
                quantity = sc.nextLong();
            }
            List<Order> removedOrderList = l.stream().limit(quantity).toList();
            for (Order o : removedOrderList){
                orderRepository.cancelOrder(o);
            }
            System.out.println("Orders removed");
        } else if(l.size() == 1) {
            Order removedOrder = l.get(0);
            orderRepository.cancelOrder(removedOrder);
        }
    }


    /**
     *
     * Calculates total price of products from cart.
     * @return AtomicReference<Double> containing total amount of cart.
     * @see #getTotalFromCart()
     */
    private AtomicReference<Double> getTotal() {
        return new AtomicReference<>(getTotalFromCart());
    }

    /**
     * Intiates cart for user.
     * @param customer used for creating cart against provided customer object.
     * @param cartIntiated used to print banner.
     */
    private Response intiateCart(Customer customer, boolean cartIntiated) {
        if (cartIntiated) {
            System.out.println(ColorCodes.GREEN + "******CART*******" + ColorCodes.RESET);
        } else {
            System.out.println(ColorCodes.GREEN + "******BROWSE*******" + ColorCodes.RESET);
        }
        boolean exitCart = false;
        AtomicReference<Double> totalPrice = new AtomicReference<>(getTotalFromCart());//re-calculates cart total.
        while (!exitCart) {
            try {
                System.out.println(ColorCodes.BLUE + "Products : " + productRepository.fetchProducts() + ColorCodes.RESET);
            } catch (Exception e){
                return new Response(null, e.getLocalizedMessage());
            }
            System.out.println("PRESS 0 TO REMOVE PRODUCT FROM CART");
            System.out.println("PRESS -1 TO EXIT CART");
            System.out.println(ColorCodes.BLUE + "Cart : " + cart + ColorCodes.RESET);
            System.out.print("product name : ");
            String name = sc.nextLine().toUpperCase();
            if (name.equalsIgnoreCase("-1")) {
                exitCart = true;//breaks the loop, by making exit condtion as true
            } else if (name.equalsIgnoreCase("0")) {
                try {
                    removeFromCart(cart, totalPrice);
                } catch (Exception e) {
                    return new Response(null, e.getLocalizedMessage());
                }

            } else {
                Optional<Product> product = null;
                try {
                    product = productRepository.fetchProductByName(name);
                } catch (Exception e){
                    return new Response(null, e.getLocalizedMessage());
                }
                if(product.isEmpty()){
                    return new Response(null, "Invalid product name please try again.");
                }
                final  Product p = product.get();
                cart.add(p);

                totalPrice.updateAndGet(price -> price + p.getPrice());
                System.out.println("Your total price is : " + totalPrice.get());

            }
        }
        if (!cart.isEmpty()) {
            System.out.println("Proceeding to order the products in cart");
            proceedToOrder(customer, totalPrice);
        }
        return new Response(null, "cannot order from empty cart");
    }

    /**
     * Used to calculate total price of cart.
     * @return Double containing total price of products present inside cart.
     * @see #getTotal()
     */
    private double getTotalFromCart() {
        double total = 0;
        for (Product p : cart) {
            total += p.getPrice();
        }
        return total;
    }

    /**
     * Called to place order for products from cart. if not placed. products in cart will exist until they are removed or ordered.
     * @param customer for whom the orders are being placed.
     * @param totalPrice indicates total price of cart or produces inside it.
     * @throws EmptyCartException if cart is empty.
     */
    private Response proceedToOrder(Customer customer, AtomicReference<Double> totalPrice) {
        if (cart.isEmpty()) {
            return new Response(null, "No products found in cart");
        }
        System.out.println(ColorCodes.GREEN + "******PROCEED*TO*ORDER*******" + ColorCodes.RESET);
        System.out.println(ColorCodes.BLUE + "Cart : " + cart);
        System.out.println("Your total amount is : " + totalPrice.get() + ColorCodes.RESET);
        System.out.print("Press 'y' to proceed further or 'n' to go back to cart : ");
        String operation = sc.nextLine();
        if (operation.equalsIgnoreCase("y")) {
            try {
                cart.forEach(p ->
                        {
                            try {
                                bookOrder(customer, p);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            } catch (Exception e) {
                return new Response(null, e.getLocalizedMessage());
            }
            cart.clear();
        } else if (operation.equalsIgnoreCase("n")) {
            try {
                return intiateCart(customer, true);
            } catch (Exception e) {
                return new Response(null, e.getLocalizedMessage());
            }
        }
        return new Response(null, "Invalid inputs");
    }

    /**
     * Removes products from cart.
     * @param cart contains product which need to removed.
     * @param total represents total price of cart or products from cart.
     * @throws EmptyCartException if the cart is empty, indicating removal operation is not possible for empty cart.
     */
    private void removeFromCart(List<Product> cart, AtomicReference<Double> total) throws EmptyCartException, SQLException, NoProductFoundException {
        if (cart.isEmpty()) {
            throw new EmptyCartException("No products found in cart");
        }
        System.out.println(ColorCodes.GREEN + "******REMOVE*FROM*CART*******" + ColorCodes.RESET);
        System.out.println(ColorCodes.BLUE + "Cart : " + cart + ColorCodes.RESET);
        System.out.print("Enter product name to be removed : ");
        String name = sc.nextLine();
        Optional<Product> product = productRepository.fetchProductByName(name);
        product.ifPresentOrElse(p -> {
                    cart.remove(p);
                    total.getAndUpdate(price -> price - p.getPrice());
                    System.out.println("Product removed");
                }
                , () -> System.out.println(ColorCodes.RED + "Could find the product" + ColorCodes.RESET));
    }
}