import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assignment.repository.interfaces.InvoiceRepository;
import org.assignment.repository.interfaces.UserRepository;
import org.assignment.repository.interfaces.OrderRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repositoryhibernateimpl.InvoiceHibernateImpl;
import org.assignment.repositoryhibernateimpl.UserRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.OrderRepoHibernateImpl;
import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;
import org.assignment.serviceimlementation.*;
import org.assignment.services.*;
import org.assignment.ui.AuthUi;
import org.assignment.ui.UI;
import org.assignment.util.ConnectionUtility;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try(EntityManager manager = ConnectionUtility.getEntityManager()) {
        final OrderRepository orderRepository = new OrderRepoHibernateImpl(manager, manager.getTransaction());
        final ProductRepository productRepository = new ProductRepoHibernateImpl(manager, manager.getTransaction());
        final UserRepository userRepository = new UserRepoHibernateImpl(manager, manager.getTransaction());
       final InvoiceRepository invoiceRepository = new InvoiceHibernateImpl(manager, manager.getTransaction());

       final InvoiceService invoiceService = new InvoiceServiceImplementation(invoiceRepository);
        final ProductService productService = new ProductServiceImplementation(productRepository, userRepository);
        final UserService userService = new UserServiceImplementation(userRepository);
        final CartService cartService = new CartServiceImplementation(productRepository, orderRepository, userRepository);
        final OrderService orderService = new OrderServiceImplementation(orderRepository, productRepository,
                userService,invoiceRepository,invoiceService);
        final AdminService adminService = new AdminServiceImplementation(userRepository);

        final AuthenticationService authService = new AuthenticationServiceImplementation(userRepository, userService, adminService, productService, orderService, cartService);

      final UI authUi = new AuthUi(authService, productService);


           authUi.initAuthServices();
        } catch (Exception e) {
            log.error("Unable to start to application : ", e);
            System.out.println("Enable to start application");
        }


    }
}
