package org.assignment.repositoryhibernateimpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.assignment.entities.Invoice;
import org.assignment.repository.interfaces.InvoiceRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class InvoiceHibernateImpl implements InvoiceRepository {

    private final EntityManager manager;
    private final EntityTransaction transaction;
    @Override
    public Invoice save(Invoice invoice) {
        transaction.begin();
        manager.persist(invoice);
        transaction.commit();
        return invoice;
    }

    @Override
    public Invoice update(Invoice invoice) {
        transaction.begin();
        manager.merge(invoice);
        transaction.commit();
        return invoice;
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return Optional.ofNullable(manager.find(Invoice.class, id));
    }
}
