package serviceimlementation;

import entities.Customer;
import entities.Order;
import entities.Product;

import entities.Seller;
import enums.Currency;
import enums.OrderStatus;
import exceptions.EmptyCartException;
import exceptions.OrderNotFoundException;

import repository.OrderCollectionRepository;
import repository.ProductCollectionRepository;
import repository.interfaces.OrderRepository;
import repository.interfaces.ProductRepository;
import repository.interfaces.SellerRepository;
import repository.jdbc.OrderJDBCRepository;
import repository.jdbc.ProductJDBCRepository;
import repository.jdbc.SellerJDBCRepository;
import services.CustomerService;




import util.ColorCodes;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CustomerServiceImplementation implements CustomerService {
    private final Scanner sc;
    private final List<Product> cart;
private final OrderRepository orderRepository;
private final ProductRepository productRepository;
private final SellerRepository sellerRepository;
    public CustomerServiceImplementation() {
        this.sellerRepository = new SellerJDBCRepository();
        this.productRepository = new ProductJDBCRepository();
        this.cart = new ArrayList<>();
        this.orderRepository = new OrderJDBCRepository();
        this.sc = new Scanner(System.in);
    }


    public void browse(final Customer customer) {
        System.out.println(ColorCodes.GREEN + "******BROWSE*******" + ColorCodes.RESET);
        String operation = "";
        while (!operation.equalsIgnoreCase("back")) {
            System.out.println(ColorCodes.BLUE + "Products : " + productRepository.fetchProducts() + ColorCodes.RESET);
            System.out.println("PRESS 1 FOR TO START ORDERING");
            System.out.println("PRESS 2 TO VIEW ORDERS");
            System.out.println("PRESS 3 TO CANCEL ORDER");
            System.out.println("PRESS 4 TO ORDER ITEMS FROM CART");
            System.out.println("TYPE 'BACK' TO GO BACK");
            System.out.print("Operation : ");
            operation = sc.nextLine();
            try {
                switch (operation) {
                    case "1":
                        intiateCart(customer, false);
                        break;
                    case "2":
                        System.out.println(ColorCodes.BLUE + "My Order : " + getAllOrders(customer) + ColorCodes.RESET);
                        break;
                    case "3":
                        cancelOrder(customer);
                        break;
                    case "4":
                        proceedToOrder(customer, getTotal());
                        break;
                    case "back", "BACK":
                        System.out.println("Going back");
                        break;
                    default:
                        System.out.println(ColorCodes.RED + "Invalid operation" + ColorCodes.RESET);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println(ColorCodes.RED + e.getLocalizedMessage() + ColorCodes.RESET);
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
    private List<Order> getAllOrders(Customer customer) {
        System.out.println(ColorCodes.GREEN + "******YOUR*ORDERS******" + ColorCodes.RESET);
        List<Order> orders = orderRepository.getOrderByCustomerId(customer.getId());
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No order found");
        }
        return orders;
    }

    /**
     *
     * injects Product object inside order list of customer.
     * @param customer for whom we want to place order.
     * @param product which product we need to order.
     * @see #cancelOrder(Customer) 
     */
    private void bookOrder(Customer customer, Product product) {
Optional<Seller> optionalSeller = sellerRepository.fetchById(1L);
optionalSeller.ifPresentOrElse(s->
        {

            Order order = new Order(customer, product, s, OrderStatus.ORDERED, Currency.INR, LocalDateTime.now(), product.getPrice());
          //to be asked to shubham.

            orderRepository.addOrder(order);
            System.out.println("******ORDER*BOOKED*******");
        }
        , ()-> System.out.println(ColorCodes.RED + "No seller found for this product" + ColorCodes.RESET));




    }

    /**
     *
     * @param customer whose order need to be cancelled.
     * @throws OrderNotFoundException if no orders found for the customer or order list is empty of the provided customer.
     * @see #bookOrder(Customer, Product)
     */

    private void cancelOrder(Customer customer) {
        System.out.println(ColorCodes.GREEN + "*****CANCEL*ORDER*****" + ColorCodes.RESET);
        List<Order> ordersByCustomer = orderRepository.getOrderByCustomerId(customer.getId());
        System.out.println(ColorCodes.BLUE + "Your orders : " + ordersByCustomer + ColorCodes.RESET);
        if (ordersByCustomer.isEmpty()) {
            throw new OrderNotFoundException("No orders are there to be cancelled");
        }
        Optional<List<Order>> order = Optional.empty();
        while (!order.isPresent()) {
            System.out.print("Please provide the product name whose order you want to cancel : ");
            String productName = sc.nextLine().toUpperCase();
            List<Order> orders = orderRepository.fetchOrderByProductName(productName);
            if(!orders.isEmpty()){
                order = Optional.of(orders);
            }
            order.ifPresentOrElse(l -> {
                helperForCancelOrder(l,customer);
            }, () -> System.out.println(ColorCodes.RED + "Incorrect product name....." + ColorCodes.RESET));
        }
    }
    private void helperForCancelOrder(List<Order> l,Customer customer){
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
            removedOrderList.forEach(o -> {
                orderRepository.cancelOrder(o);
            });
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
    private void intiateCart(Customer customer, boolean cartIntiated) {
        if (cartIntiated) {
            System.out.println(ColorCodes.GREEN + "******CART*******" + ColorCodes.RESET);
        } else {
            System.out.println(ColorCodes.GREEN + "******BROWSE*******" + ColorCodes.RESET);
        }
        boolean exitCart = false;
        AtomicReference<Double> totalPrice = new AtomicReference<>(getTotalFromCart());//re-calculates cart total.
        while (!exitCart) {
            System.out.println(ColorCodes.BLUE + "Products : " + productRepository.fetchProducts() + ColorCodes.RESET);
            System.out.println("PRESS 0 TO REMOVE PRODUCT FROM CART");
            System.out.println("PRESS -1 TO EXIT CART");
            System.out.println(ColorCodes.BLUE + "Cart : " + cart + ColorCodes.RESET);
            System.out.print("product name : ");
            String name = sc.nextLine().toUpperCase();
            if (name.equalsIgnoreCase("-1")) {
                exitCart = true;//breaks the loop, by making exit condtion as true
            } else if (name.equalsIgnoreCase("0")) {
                removeFromCart(cart, totalPrice);
            } else {
                Optional<Product> product = productRepository.fetchProductByName(name);
                product.ifPresentOrElse(p -> {
                            cart.add(p);

                            totalPrice.updateAndGet(price -> price + p.getPrice());
                            System.out.println("Your total price is : " + totalPrice.get());
                        },
                        () -> System.err.println("Invalid product name please try again."));
            }
        }
        if (!cart.isEmpty()) {
            System.out.println("Proceeding to order the products in cart");
            proceedToOrder(customer, totalPrice);
        }
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
    private void proceedToOrder(Customer customer, AtomicReference<Double> totalPrice) {
        if (cart.isEmpty()) {
            throw new EmptyCartException("No products found in cart");
        }
        System.out.println(ColorCodes.GREEN + "******PROCEED*TO*ORDER*******" + ColorCodes.RESET);
        System.out.println(ColorCodes.BLUE + "Cart : " + cart);
        System.out.println("Your total amount is : " + totalPrice.get() + ColorCodes.RESET);
        System.out.print("Press 'y' to proceed further or 'n' to go back to cart : ");
        String operation = sc.nextLine();
        if (operation.equalsIgnoreCase("y")) {
            cart.forEach(p ->
                    bookOrder(customer, p)
            );
            cart.clear();
        } else if (operation.equalsIgnoreCase("n")) {
            intiateCart(customer, true);
        }
    }

    /**
     * Removes products from cart.
     * @param cart contains product which need to removed.
     * @param total represents total price of cart or products from cart.
     * @throws EmptyCartException if the cart is empty, indicating removal operation is not possible for empty cart.
     */
    private void removeFromCart(List<Product> cart, AtomicReference<Double> total) {
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