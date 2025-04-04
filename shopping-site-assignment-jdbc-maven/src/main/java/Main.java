import lombok.extern.slf4j.Slf4j;
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
import org.assignment.ui.AuthUi;
import org.assignment.ui.UI;

@Slf4j
public class Main {
    public static void main(String[] args) {
        final OrderRepository orderRepository = new OrderRepoHibernateImpl();
        final ProductRepository productRepository = new ProductRepoHibernateImpl();
        final SellerRepository sellerRepository = new SellerRepoHibernateImpl();
        final CustomerRepository customerRepository = new CustomerRepoHibernateImpl();

        final ProductService productService = new ProductServiceImplementation(productRepository);
        final CartService cartService = new CartServiceImplementation(productRepository, sellerRepository, orderRepository, customerRepository);
        final OrderService orderService = new OrderServiceImplementation(orderRepository, productRepository);
        final AdminService adminService = new AdminServiceImplementation(customerRepository);
        final CustomerService customerService = new CustomerServiceImplementation(customerRepository);
        final AuthenticationService authService = new AuthenticationServiceImplementation(customerRepository, customerService, adminService, productService, orderService, cartService);

      final UI authUi = new AuthUi(customerService, authService, productService);
        try {
           authUi.initAuthServices();
        } catch (Exception e) {
            log.error("Unable to start to application : ", e);
        }


    }
}
