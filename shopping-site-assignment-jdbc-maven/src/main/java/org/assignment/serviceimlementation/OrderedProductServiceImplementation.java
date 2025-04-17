package org.assignment.serviceimlementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.assignment.entities.OrderedProduct;
import org.assignment.services.OrderedProductService;

import java.util.List;

@RequiredArgsConstructor
public class OrderedProductServiceImplementation implements OrderedProductService {
    private final EntityManager manager;
    private final EntityTransaction transaction;
    @Override
    public void save(List<OrderedProduct> orderedProducts) {
        transaction.begin();
        orderedProducts.forEach(manager::persist);
        transaction.commit();

    }
}
