package ui;

import entities.Admin;
import entities.Customer;
import entities.Food;
import entities.Order;
import enums.OrderStatus;
import repositories.AdminRepository;
import repositories.CustomerRepository;
import repositories.RestaurantRepository;
import service.OrderService;
import utility.ColourCodes;

import java.util.*;

public final class CustomerUi {
    static Customer currentCustomer;
//    private  final static CustomerRepository customerRepository = null;

//    CustomerUi(CustomerRepository repository) {
//        this.customerRepository = repository;
//    }

    public static void customerMenu(Scanner scanner) {

        int choice;
        while (true) {
            try {
                OperationsInfo.displayOperation(List.of("1.Registration", "2.Login", "3.Back to main menu", "Choose an option"));
                //
//                System.out.println(ColourCodes.CYAN +
//                        "\nCUSTOMER REGISTRATION\n------------------------" + ColourCodes.RESET);
//                System.out.println("1.Registration");
//                System.out.println("2.Login");
//                System.out.println("3.Back to main menu");
//                System.out.println("Choose an option");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        registerCustomer(scanner);
                        break;
                    case 2:
                        loginAsCustomer(scanner);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException exception) {
                System.out.println(ColourCodes.RED + "Enter correct input and try again" + ColourCodes.RESET);
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public static void registerCustomer(Scanner scanner) {
        System.out.println(ColourCodes.CYAN
                + "\nREGISTER NEW CUSTOMER  \n------------------------ " + ColourCodes.RESET);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println(ColourCodes.RED + "Please enter correct input. Field cannot be empty. "
                    + ColourCodes.RESET);
            return;
        }

        Optional<Admin> admin = AdminRepository.getAdmins()
                .stream()
                .filter(a -> a.getEmail().equalsIgnoreCase(email))
                .findFirst();

        admin.ifPresentOrElse(a -> {
            System.out.println("Enter a valid email address");
            registerCustomer(scanner);
        }, () -> {
            Customer customer = new Customer(email, name, password);
            customer.setLoggedIn(true);
            CustomerRepository.addCustomer(customer);
            System.out.println(ColourCodes.GREEN + "Registration successful.. Redirecting to the customer menu.."
                    + ColourCodes.RESET);
            currentCustomer = customer;
            displayCustomerMenu(scanner);
        });
    }

    private static void loginAsCustomer(Scanner scanner) {
        String email = "";
        String password = "";

        if (currentCustomer == null || !currentCustomer.isLoggedIn()) { // Objects.isNull(currentCustomer)
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
        }
        for (Customer customer : CustomerRepository.getCustomers()) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                customer.setLoggedIn(true);
                System.out.println(ColourCodes.GREEN + "Login successful, Welcome back!" + ColourCodes.RESET);
                currentCustomer = customer;
                break;
            }
        }

        if (currentCustomer == null) {
            System.out.println("Login failed. Please try again.");
        } else {
            displayCustomerMenu(scanner);
        }
    }

    public static void displayCustomerMenu(Scanner scanner) {
        if (!currentCustomer.isLoggedIn()) {
            System.out.println("Customer login required.");
        }
        int choice;
        while (true) {
            try {
                System.out.println(ColourCodes.CYAN
                        + "\nCUSTOMER MENU: \n------------------------ " + ColourCodes.RESET);
                System.out.println("1.View menu");
                System.out.println("2.View Order/ Cart");
                System.out.println("3.Track order status");
                System.out.println("4.View order history");
                System.out.println("5.Back to main menu");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewMenu(scanner);
                        break;
                    case 2:
                        viewOrder(scanner);
                        break;
                    case 3:
                        trackOrderStatus(scanner);
                        break;
                    case 4:
                        viewOrderHistory();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println(ColourCodes.RED
                                + "Invalid option. Please try again later. " + ColourCodes.RESET);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void viewMenu(Scanner scanner) {
        List<Food> foodList = RestaurantRepository.getRestaurant().getFoodItemList();

        while (true) {
            try {
                System.out.println("\nAvailable Food Items:");
                foodList.forEach(System.out::println);

                System.out.println(ColourCodes.GREEN
                        + "\nEnter the food name you want to add to your cart:\n " + ColourCodes.RESET + ColourCodes.RED
                        + "Or type 'checkout' to proceed to checkout:" + ColourCodes.RESET + ColourCodes.RED
                        + "\n Or type 'exit' to back to menu:  " + ColourCodes.RESET);

                String foodItem = scanner.nextLine();
                if (foodItem.equalsIgnoreCase("exit")) {
                    return;

                } else if (foodItem.equalsIgnoreCase("checkout")) {
                    checkout(scanner);
                    break;

                } else {
                    Optional<Food> food = foodList.stream().
                            filter(f -> f.getName().equalsIgnoreCase(foodItem))
                            .findFirst();

                    if (food.isPresent()) {
                        System.out.println("Enter the quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        if (quantity > 0) {
                            Food food1 = food.get();
                            food1.setQuantity(quantity);

                            currentCustomer.addToCart(food1.getName(), food1);
                            System.out.println(ColourCodes.BLUE + "> " + foodItem + " x " + quantity + " added to your cart." + ColourCodes.RESET);
                        } else {
                            System.out.println(ColourCodes.RED + "Quantity must be greater than 0." + ColourCodes.RESET);
                        }
                    }
                    if (food.isEmpty()) {
                        System.out.println("Food item is not present.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println(ColourCodes.RED + "Enter correct input." + ColourCodes.RESET);
                scanner.nextLine();
            }
        }
    }

    public static void checkout(Scanner scanner) {
        if (currentCustomer.getCart().isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Your cart contains:");
//        currentCustomer.getCart().forEach((food, quantity) ->
//                System.out.println(ColourCodes.BLUE + ">" + food.getName() + " x " + quantity +
//                        " Price: " + (food.getPrice() * quantity) + ".Rs" + ColourCodes.RESET)

        currentCustomer.getCart().forEach((foodName, food) ->
                System.out.println(food.getName() + " Quantity: " + food.getQuantity()
                        + " Price: " + (food.getPrice() * food.getQuantity())));


        double total = getTotal();
        System.out.println("Your total price is: " + total + ".Rs");

        System.out.println(ColourCodes.GREEN + "Do you want to place the order? (y/n)" + ColourCodes.RESET);
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            Order order = new Order(currentCustomer, OrderStatus.ORDERED);
            OrderService.placeOrder(order);
            currentCustomer.clearCart();
            System.out.print(ColourCodes.BLUE + "Order placed successfully. Your order ID is: " + order.getId());
            System.out.println(" and your total price is " + total + ".RS " + ColourCodes.RESET);
        } else {
            System.out.println("Order not placed.");
        }
    }

    public static void viewOrder(Scanner scanner) {
        if (currentCustomer.getCart().isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Your cart contains:");
        boolean isExit = true;
        while (isExit) {
            try {
                currentCustomer.getCart().forEach((foodName, food) -> System.out.println("Name: " + foodName +
                        " Quantity: x" + food.getQuantity() + " Price " + food.getPrice()* food.getQuantity()));


                System.out.println("Press 1 to delete from cart | press 2 to update the cart quantity |"
                        + ColourCodes.RED + " press 3 to exit cart" + ColourCodes.RESET);
                int confirm = scanner.nextInt();
                scanner.nextLine();

                switch (confirm) {
                    case 1:
                        //handlingDeleteFromCart(scanner);
                        break;
                    case 2:
                        handlingUpdateFromCart(scanner);
                        break;
                    case 3:
                        isExit = false;
                        break;
                    default:
                        System.out.println("Enter a valid option");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(ColourCodes.RED + "Invalid input."
                        + "Please try selecting 1 | 2 or 3 to exit " + ColourCodes.RESET);
                scanner.nextLine();
            }
        }
    }

    private static void handlingUpdateFromCart(Scanner scanner) {
        System.out.println("Enter the food item you want to update: ");
        String foodItem = scanner.nextLine();

//        Optional<Food> itemToUpdate = currentCustomer.getCart().values().stream()
//                .filter(food -> food.getName().equalsIgnoreCase(foodItem))
//                .findFirst();

        Food i = currentCustomer.getCart().get(foodItem);

        if (i != null) {
            System.out.println("Enter the new quantity: ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine();

            if (newQuantity > 0) {
//                currentCustomer.updateCart(foodItem, newQuantity);
            i.setQuantity(newQuantity);
                    System.out.println("cart updated successfully ");
                }
            }
            else {
                System.out.println("Quantity must be grater than 0 ");
            }
        }
        else {
            System.out.println("Food item not present");
        }
    }
//        if (i.isPresent()) {
//            System.out.println("Enter the new quantity: ");
//            int newQuantity = scanner.nextInt();
//            scanner.nextLine();
//
//            if (newQuantity > 0) {
//                currentCustomer.getCart().forEach((integer, food) -> {
//                    if (food.getQuantity() <= 0) {
//
//                    } else {
//                        food.setQuantity(newQuantity);
//                    }
//                });

//                currentCustomer.updateQuantity(itemToUpdate.get(), newQuantity);
//        System.out.println("Item updated successfully.");
//     else {
//        System.out.println("Quantity must be greater than 0.");
//    }
//} else {
//        System.out.println("Food item not found.");
//        }
//
//double total = getTotal();
//        System.out.println("Updated Total Price: " + total + ".Rs");
//    }

//    private static void handlingDeleteFromCart(Scanner scanner) {
//        System.out.println("Enter the food item you want to remove: ");
//        String foodItem = scanner.nextLine();
//
//        Food foodToRemove = null;
//        for (Food food : currentCustomer.getCart().values()) {
//            if (food.getName().equalsIgnoreCase(foodItem)) {
//                foodToRemove = food.getQuantity();
//                break;
//            }
//        }
//
//        if (foodToRemove != null) {
//            if (currentCustomer.getCart().get(foodToRemove) > 1) {
//                System.out.println("Enter the quantity to remove: ");
//                int quantity = scanner.nextInt();
//                scanner.nextLine();
//
//                if (quantity > 0) {
//                    currentCustomer.removeFromCart(foodToRemove, quantity);
//                    System.out.println("Quantity updated successfully.");
//                } else {
//                    System.out.println("Quantity must be greater than 0.");
//                }
//            } else {
//                currentCustomer.getCart().remove(foodToRemove);
//                System.out.println("Food item removed successfully.");
//            }
//        } else {
//            System.out.println("Food item not found in the cart.");
//        }
//
//        double total = getTotal();
//        System.out.println("Updated Total Price: " + total + ".Rs");
//    }

private static double getTotal() {
    double total = 0;
    for (Map.Entry<String, Food> entry : currentCustomer.getCart().entrySet()) {
        total += (entry.getValue().getPrice() * entry.getValue().getQuantity());
    }
    return total;
}

public static void trackOrderStatus(Scanner scanner) {
    try {
        System.out.println(ColourCodes.BLUE + "Enter your order ID: " + ColourCodes.RESET);
        int orderId = scanner.nextInt();

        Order order = OrderService.getOrder(orderId);
        if (order != null) {
            System.out.println("Order Status: " + order.getOrderStatus());
        } else {
            System.out.println("Order not found.");
        }
    } catch (InputMismatchException e) {
        System.out.println(ColourCodes.RED
                + "Please enter correct input:.. Redirecting to customer menu.. " + ColourCodes.RESET);
        scanner.nextLine();
        displayCustomerMenu(scanner);
    }
}

public static void viewOrderHistory() {
    Map<Integer, Order> orders = OrderService.getAllOrders();

    if (orders.isEmpty()) {
        System.out.println("You have no past orders.");
    } else {
        orders.values().forEach(System.out::println);
    }}

}