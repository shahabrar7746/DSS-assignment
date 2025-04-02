import org.assignment.Driver;
import org.assignment.repository.interfaces.CustomerRepository;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repository.interfaces.SellerRepository;
import org.assignment.repositoryhibernateimpl.CustomerRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.OrderRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.SellerRepoHibernateImpl;
import org.assignment.serviceimlementation.*;
import org.assignment.services.*;

public class Main {
    public static void main(String[] args) {
       final OrderRepository orderRepository = new OrderRepoHibernateImpl();
       final ProductRepository productRepository  = new ProductRepoHibernateImpl();
       final SellerRepository sellerRepository = new SellerRepoHibernateImpl();
       final CustomerRepository customerRepository = new CustomerRepoHibernateImpl();
  final OrderService orderService = new OrderServiceImplementation(orderRepository);
       final AdminService adminService = new AdminServiceImplementation(productRepository, customerRepository, orderRepository);
       final CustomerService customerService = new CustomerServiceImplementation(customerRepository, orderRepository, productRepository, sellerRepository);
       final AuthenticationService authService = new AuthenticationServiceImplementation(orderService, adminService, customerService, customerRepository);
       final ProductService productService = new ProductServiceImplementation(productRepository);

       final Driver driver = new Driver(productService, authService, customerService);
      try {
          driver.start();
      }catch (Exception e)
      {
          System.out.println(e.getLocalizedMessage());
      }
    }
}
