package org.assignment.services;

import org.assignment.entities.Invoice;
import org.assignment.entities.Order;

public interface InvoiceService {
    Invoice generateInvoice(Order order);
}
