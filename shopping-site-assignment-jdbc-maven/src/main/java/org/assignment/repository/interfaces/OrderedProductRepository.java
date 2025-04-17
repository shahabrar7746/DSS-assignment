package org.assignment.repository.interfaces;

import org.assignment.entities.OrderedProduct;

public interface OrderedProductRepository {


    OrderedProduct save(OrderedProduct product);
    OrderedProduct update(OrderedProduct product);
}
