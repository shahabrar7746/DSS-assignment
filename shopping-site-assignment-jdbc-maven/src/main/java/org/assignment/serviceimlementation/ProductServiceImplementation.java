package org.assignment.serviceimlementation;

import lombok.AllArgsConstructor;
import org.assignment.entities.Customer;
import org.assignment.entities.Product;
import org.assignment.enums.ProductType;
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
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductServiceImplementation implements ProductService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ProductRepository productRepository;

    @Override
    public Response getAllProduct() {
        Response response;
        try {
            List<Product> products = productRepository.fetchProducts();
            List<ProductWrapper> wrapperList = new ArrayList<>();
            products.forEach(p ->
                wrapperList.add(new ProductWrapper(p))
            );
            response = new Response(wrapperList);
        } catch (SQLException e) {
            log.error("Some error occured while fetching products from repository", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

    @Override
    public Response hasProduct(Customer customer, String productName) {
        boolean exists = customer
                .getCart()
                .stream()
                .anyMatch(item -> item
                        .getProduct()
                        .getName()
                        .equalsIgnoreCase(productName));
        return new Response(ResponseStatus.SUCCESSFUL, exists, null);
    }

    @Override
    public Response getProductsByType(ProductType type) {
        List<Product> products;// fetches List of product to be searched in.
        try {
            products = productRepository.fetchProducts();
        } catch (SQLException e) {
            log.error("Error occured while retrieving product basen on types : ", e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if (products.isEmpty()) {
            return new Response(ResponseStatus.ERROR, null, "Could not find any product based on provided type");
        }
        Map<ProductType, List<Product>> map = products
                .stream()
                .collect(Collectors.groupingBy(Product::getType));

        if (!map.containsKey(type)) {
            return new Response(null, "No product found for this category"); // throws exception if no product is found for the provided type.
        }
        return new Response(map.get(type));
    }
}
