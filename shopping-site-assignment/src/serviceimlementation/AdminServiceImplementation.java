package serviceimlementation;

import entities.Customer;
import entities.Order;
import entities.Product;

import enums.OrderStatus;
import enums.ProductType;
import enums.Roles;

import exceptions.*;

import repository.CustomerCollectionRepository;
import repository.jdbc.CustomerJDBCRepository;
import repository.OrderCollectionRepository;
import repository.ProductCollectionRepository;
import repository.interfaces.OrderRepository;
import repository.interfaces.ProductRepository;
import repository.jdbc.OrderJDBCRepository;
import repository.jdbc.ProductJDBCRepository;
import services.AdminService;

import repository.interfaces.CustomerRepository;


import util.ColorCodes;

import javax.naming.OperationNotSupportedException;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminServiceImplementation implements AdminService {
    private static final String SUPER_ADMIN_PASSWORD = "1234567890";
    private final Scanner sc;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
private final OrderRepository orderRepository;
    private AdminServiceImplementation() throws SQLException {
        this.productRepository = new ProductJDBCRepository();
        this.customerRepository = new CustomerJDBCRepository();
        this.sc = new Scanner(System.in);
        this.orderRepository = new OrderJDBCRepository();
    }

    private static AdminServiceImplementation service;

    public static AdminServiceImplementation getInstance() throws SQLException {
        if (service == null) {
            service = new AdminServiceImplementation();
        }
        return service;
    }


    @Override
    public List<Product> getProductsByType() {
        List<Product> products = new ArrayList<>();
        String operation;
        boolean isFinished = false;
        while (!isFinished) {
            System.out.print("Press : \n 1 for PHONE \n 2 for FURNITURE \n 3 for APPLIANCES \n 4 for MAKEUP \n 5 for CLOTHING \n operation : ");
            operation = sc.nextLine();
            switch (operation) {
                case "1":
                    products = helperForGetProductsByType(ProductType.PHONE);
                    isFinished = true;
                    break;
                case "2":
                    products = helperForGetProductsByType(ProductType.FURNITURE);
                    isFinished = true;
                    break;
                case "3":
                    products = helperForGetProductsByType(ProductType.APPLIANCES);
                    isFinished = true;
                    break;
                case "4":
                    products = helperForGetProductsByType(ProductType.MAKEUP);
                    isFinished = true;
                    break;
                case "5":
                    products = helperForGetProductsByType(ProductType.CLOTHING);
                    isFinished = true;
                    break;
                default:
                    System.out.println(ColorCodes.RED + "Wrong operation choosen" + ColorCodes.RESET);//executed if the type is of the defined enums.
            }
        }
        return products;//returns list of product of same type.
    }

    @Override
    public Customer getCustomerById() {
        if (getAllCustomer().isEmpty()) {
            throw new CustomerNotFoundException("No customers are there");//thrown if the customer list is empty indicating no customer id s found to be deleted.
        }
        System.out.print("Please provide the id of Customer : ");
        Long id = sc.nextLong();
        Optional<Customer> customer = customerRepository.fetchById(id);
       if(customer.get().getRole() != Roles.CUSTOMER){
           throw new CustomerNotFoundException("No Customer object found");
       }
        customer.orElseThrow(() -> {
            throw new CustomerNotFoundException("No Customer object found");//executed if no customer object is found
        });
        return customer.get();// returns the found customer.
    }

    @Override
    public List<Customer> getAllCustomer() {
        List<Customer> allCustomer = customerRepository.getCustomers().stream().filter(c -> c.getRole() == Roles.CUSTOMER).toList();
        if (allCustomer.isEmpty()) {
            throw new CustomerNotFoundException("Customer repository is empty");//executed if no customer object is found
        }
        return allCustomer;// returns all the customer.
    }



    @Override
    public List<Product> getAllProdcuts() {
        List<Product> allProducts = productRepository.fetchProducts();
        if (allProducts.isEmpty()) {
            throw new NoProductFoundException("Product repository is empty");//executed if no product object is found
        }
        return allProducts;// returns all the product.
    }

    /**
     *
     * @param type to search for product of same type.
     * @return List of Product of same type as provided by caller.
     * @throws NoProductFoundException if no product is found of same type.
     * @see #getAllProdcuts()
     */

    private List<Product> helperForGetProductsByType(ProductType type) {
        List<Product> products = productRepository.fetchProducts();// fetches List of product to be searched in.
        Map<ProductType, List<Product>> map = products.stream().collect(Collectors.groupingByConcurrent(Product::getType));
        if (!map.containsKey(type)) {
            throw new NoProductFoundException("No product found for this category");// throws exception if no product is found for the provided type.
        }
        return map.get(type);
    }

    @Override
    public List<Order> getAllDeliveredOrders() {

        return orderRepository.getAllDeliveredOrders();
    }



    @Override
    public void grantAccess(boolean isAuthorized) throws OperationNotSupportedException {
        if (!isAuthorized) {//checks if the operation performed by superadmin or not.
            throw new OperationNotSupportedException("Your are not authorized to access this service");//if not, throws this exception.
        }
        System.out.println(ColorCodes.BLUE + "Admins : " + getAllCustomer() + ColorCodes.RESET);
        System.out.print("Enter Customer id to whom you want to grant access to : ");
        String cid = sc.nextLine();
        int count = 3;
        Optional<Customer> customer = customerRepository.fetchById(Long.valueOf(cid));
        while (customer.isEmpty() && count-- >= 0) {
            System.out.println("Wrong customer id, enter correct customer id");
            System.out.print(ColorCodes.BLUE + "Enter Customer id to whom you want to grant access to : " + ColorCodes.RESET);
            cid = sc.nextLine();
            customer = customerRepository.fetchById(Long.valueOf(cid));//fetches customer by id.
        }
        if (count < 0) {
            throw new TrialLimitExceedException("You have exceeded the try limit");// thrown if the try 'count' <= 0.
        }
        if (customer.get().getRole() == Roles.ADMIN) {
            System.err.println("Already a Admin");//indicates if the customer is already a admin.
            return;
        }
        if (customer.get().getRole() == Roles.SUPER_ADMIN) {//executed if super admins authority is tried to overridden.
            System.err.println("action not allowed");
            return;
        }
        if (authenticateSuperAdmin()) {//if 'true' lets you perform critical operation.
            customer.get().setRole(Roles.ADMIN);//critical operation.
            customerRepository.updateCustomer(customer.get());
            System.out.println(ColorCodes.RESET + "Access granted !!" + ColorCodes.RESET);
        }

    }


    @Override
    public void revokeAccess(boolean isAuthorized) throws OperationNotSupportedException {
        if (!isAuthorized) {//checks if the operation performed by superadmin or not.
            throw new UnauthorizedOperationException("Your are not authorized to access this service");//if not, throws this exception.
        }
        System.out.println(ColorCodes.BLUE + "Customers : " + fetchAllAdmins() + ColorCodes.RESET);
        System.out.print("Enter Customer id to whom you want to revoke access to : ");
        String cid = sc.nextLine();
        Optional<Customer> customer = customerRepository.fetchAdminById(Long.valueOf(cid));//fetches admins based on id.
        int count = 3;//sets try limit.
        while (customer.isEmpty() && count-- >= 0) {
            System.out.println("Wrong customer id, enter correct customer id");
            System.out.print("Enter Customer id to whom you want to revoke access to : ");
            cid = sc.nextLine();//keeps on requesting id if no object is found for the previous id, keeps on going until count is not equals to 0 or less than 0
            customer = customerRepository.fetchAdminById(Long.valueOf(cid));
        }
        if (customer.isEmpty()) {
            throw new NoAdminFoundException("No admin found");//throws exception, if no object is found for last operation.
        }
        if (count < 0) {
            throw new TrialLimitExceedException("You have exceeded the try limit");// thrown if the try 'count' <= 0.
        }
        if (customer.isPresent() && customer.get().getRole() == Roles.SUPER_ADMIN) {
            throw new OperationNotSupportedException("Cannot perform action");// thrown, if the id corresponds to Super admin.
        }
        if (customer.isPresent() && customer.get().getRole() == Roles.CUSTOMER) {
            throw new OperationNotSupportedException("Customer does not have Admin access");// throws exception, if the customer object does not have admin access.
        }
        if (customer.isPresent() && authenticateSuperAdmin()) {//checks performing critical section.
            customer.get().setRole(Roles.CUSTOMER);
            customerRepository.updateCustomer(customer.get());  //critical section.
            System.out.println("Access revoked !!");
        }
    }


    @Override
    public void cancelOrder(boolean isAuthorized) {
        if (!isAuthorized) {//checks if the operation performed by superadmin or not.
            throw new UnauthorizedOperationException("Your are not authorized to access this service");//if not, throws this exception.
        }
        List<Order> orders = orderRepository.getAllOrders();//gets all orders present in the repository.
        if (orders.isEmpty()) {
            throw new OrderNotFoundException(orders.size() + " orders found to be cancelled");// throws exception if no orders are present in the OrderRepository.
        }
        System.out.println(ColorCodes.BLUE + "Orders : " + orders + ColorCodes.RESET);
        System.out.print("Enter order id to be cancelled : ");
        Optional<Order> order = orderRepository.fetchOrderById(sc.nextLong());//fetches Order object using the provided id.
        order.ifPresentOrElse(o -> {
                    if (authenticateSuperAdmin()) {//executed if Order object is found for the id.
                        orderRepository.cancelOrder(o);
                        System.out.println("Order cancelled successfully.");
                    }
                },
                () -> {
                    throw new OrderNotFoundException("The order id is incorrect");
                });// thrown if no Order is found.
    }

    /**
     * Below method requests for super admin password for variable 'count' times which is default set to 6.
     * If number of tries exceeds the default set count then 'TrialLimitExceedException' exception is thrown.
     * @throws WrongPasswordException if the password is incorrect even on last try.
     * @throws TrialLimitExceedException if the try limit exceeds.
     * @return true if successfully authenticated.
     */
    private boolean authenticateSuperAdmin() {
        int count = 6;
        String password = "";

        while (!password.equals(SUPER_ADMIN_PASSWORD) && count-- > 0) {
            if (count < 5) {
                System.err.println("Try again");
            }
            System.out.println("Provide Super Admin password to procced further\nPassword : ");
            password = sc.nextLine();
        }
        if (count <= 0) {
            throw new TrialLimitExceedException("you have tried too many times, please try again later");
        }
        if (!password.equals(SUPER_ADMIN_PASSWORD)) {
            throw new WrongPasswordException("you have entered the wrong password");
        }
        return true;
    }

    @Override
    public void deleteCustomer(boolean isAuthorized)  {
        if (!isAuthorized) {//checks if the operation performed by superadmin or not.
            throw new UnauthorizedOperationException("Your are not authorized to access this service");//if not, throws this exception.
        }
        List<Customer> allCustomer = getAllCustomer();
        if (allCustomer.isEmpty()) {
            throw new CustomerNotFoundException("No customers are present");//thrown if no customer is present in customer repository or in db.
        }
        System.out.println(ColorCodes.BLUE + "Customers : " + allCustomer + ColorCodes.RESET);
        System.out.print("Provide customer id : ");
        Long cid = sc.nextLong();
        Optional<Customer> customer = customerRepository.fetchById(cid);//fetch customer by provided id.
        if(customer.get().getRole() != Roles.CUSTOMER){
            throw new CustomerNotFoundException("No Customer object found");
        }
        customer.orElseThrow(() -> {
            throw new CustomerNotFoundException("incorrect customer id or deleted exception");//thrown if no customer is found,to associated id.
        });
        if (authenticateSuperAdmin()) { //autheticates befores performing critical section. on any negative scenario a exception is thrown. or else true.

            customerRepository.removeCustomer(customer.get());//critical section.
        }
    }

    @Override
    public List<Customer> fetchAllAdmins() {//fetches all admins excluding super admin and customer.
        List<Customer> adminList = customerRepository.getCustomers().stream().filter(c -> c.getRole() == Roles.ADMIN).toList();
        if (adminList.isEmpty()) {
            throw new NoAdminFoundException("No admin found");//throws NoAdminFoundException if no admin found CustomerRepository.
        }
        return adminList;//returns list of admins if any found.
    }
}
