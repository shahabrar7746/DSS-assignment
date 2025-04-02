package org.assignment.serviceimlementation;

import org.assignment.entities.Product;
import org.assignment.enums.ResponseStatus;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.services.ProductService;
import org.assignment.util.Constants;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImplementation implements ProductService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ProductRepository productRepository;

    public ProductServiceImplementation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Response getAllProduct() {
        Response response = null;
        try {
            List<Product> products = productRepository.fetchProducts();
            List<ProductWrapper> wrapperList = new ArrayList<>();
            products.forEach(p->{
                wrapperList.add(new ProductWrapper(p));
            });
            response = new Response(wrapperList);
        } catch (SQLException e) {
            log.error("Some error occured while fetching products from repository", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }
}
