package org.assignment.services;


import org.assignment.entities.User;
import org.assignment.enums.ProductType;
import org.assignment.util.Response;


public interface ProductService {

    Response getAllProduct();

    Response hasProduct(User user, String productName);

    Response getProductsByType(ProductType type);

    Response addProduct(String name, long seller, int typeIndex, int currencyType, double price, int stock);
}
