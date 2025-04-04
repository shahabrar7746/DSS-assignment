package org.assignment.services;

import org.assignment.entities.Customer;
import org.assignment.util.Response;

public interface CartService {

     Response intiateCart(Customer customer, String name, int quantity);

     Response removeFromCart(final Customer customer, String name);
     Response orderFromCart(Customer customer, String name, int quantity, boolean edited);

     Response incrementQuantity(Customer customer, String productName, int newQuantity);

     Response decrementQuantity(Customer customer, String productName, int newQuantity);
     Response getTotalBillFromCart(Customer customer);
}
