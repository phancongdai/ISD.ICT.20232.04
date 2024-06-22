package controller;

import entity.invoice.Invoice;
import entity.invoice.InvoiceRepository;
import entity.invoice.SQLInvoiceRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceListController extends BaseController {

    // Declare the InvoiceRepository
    private InvoiceRepository invoiceRepository;

    // Constructor to initialize the repository
    public InvoiceListController() {
        this.invoiceRepository = new SQLInvoiceRepository(); // This can be injected for better flexibility
    }
    public ArrayList<Invoice> getListInvoice() throws SQLException {
        // Fetch invoices from the repository
        return new ArrayList<>(invoiceRepository.getAll());
    }

    // Method to update the status of an invoice
    public void updateInvoiceStatus(int id, Boolean approved) throws SQLException {
        String status = approved ? "APPROVED" : "REJECTED";
        // Update the invoice status using the repository
        invoiceRepository.updateStatus(id, status);
    }
}
