package org.assignment.repository.interfaces;

import org.assignment.entities.Invoice;
import org.assignment.services.InvoiceService;

import java.util.Optional;

public interface InvoiceRepository {

    Invoice save(Invoice invoice);

    Invoice update(Invoice invoice);


    Optional<Invoice> findById(Long id);

}
