package controller;

import entity.Invoice;
import service.InvoiceService;
import constants.PaymentStatus;

import java.util.List;

public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public int generateInvoice(Invoice invoice) {
        int invoiceId = invoiceService.generateInvoice(invoice);  // Get generated ID
        return invoiceId;
    }

    public Invoice getInvoiceByBookingId(int bookingId) {
        return invoiceService.getInvoiceByBookingId(bookingId);
    }

    public void updatePaymentStatus(int invoiceId, PaymentStatus status) {
        invoiceService.updatePaymentStatus(invoiceId, status);
        System.out.println("Payment status updated!");
    }

    public Invoice getInvoiceById(int invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }

    public List<Invoice> getInvoiceByUserId(int userID) {
        return invoiceService.getInvoiceByUserId(userID);
    }
}
