package org.assignment.services;


import org.assignment.entities.Customer;
import org.assignment.enums.ProductType;
import org.assignment.util.Response;


public interface ProductService {

    Response getAllProduct();

    Response hasProduct(Customer customer, String productName);

    Response getProductsByType(ProductType type);
}
