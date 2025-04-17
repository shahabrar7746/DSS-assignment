package org.assignment.serviceimlementation;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.assignment.entities.Invoice;
import org.assignment.entities.Order;
import org.assignment.enums.InvoicePrintStatus;
import org.assignment.repository.interfaces.InvoiceRepository;
import org.assignment.services.InvoiceService;
@RequiredArgsConstructor
public class InvoiceServiceImplementation implements InvoiceService {

    private final InvoiceRepository repository;
    @Override
    public Invoice generateInvoice(Order order) {
        Invoice invoice = Invoice.builder()
                .order(order)
                .printStatus(InvoicePrintStatus.NOT_PRINTED)
                .build();
      return repository.save(invoice);
    }
}
