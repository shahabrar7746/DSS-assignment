package org.assignment.services;

import org.assignment.entities.CartItems;
import org.assignment.entities.User;
import org.assignment.util.Response;

public interface CartService {
    Response intiateCart(User user, String name, int quantity);

    Response removeFromCartByProductName(final User user, String name);

    Response removeFromCartByCartItemObject(final User user, CartItems items);

    Response orderFromCart(User user, String name, int quantity, boolean edited);

    Response changeQuantity(User user, CartItems item, int newQuantity);

    Response getTotalBillFromCart(User user);

    Response findCartItemByName(User user, String productName);

}
