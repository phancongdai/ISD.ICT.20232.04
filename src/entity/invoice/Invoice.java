package entity.invoice;

import entity.order.Order;

import java.sql.SQLException;
import java.util.List;

public class Invoice {

    private int id;
    private Order order;
    private int amount;
    private String paypalId;
    private String status = "CREATED";
    private InvoiceRepository repository;

    // Dependency injection via constructor
    public Invoice(Order order, InvoiceRepository repository) {
        this.order = order;
        this.amount = order.getAmount() + order.getShippingFees();
        this.repository = repository;
    }

    public Invoice(Order order, String paypalId, InvoiceRepository repository) {
        this(order, repository);
        this.paypalId = paypalId;
    }


    public Invoice(Order order) {
        this.order = order;
    }

    // Default constructor for database retrieval usage
    public Invoice() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Order getOrder() { return order; }
    public int getOrderId() { return this.order.getID(); }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getPaypalId() { return paypalId; }
    public void setPaypalId(String paypalId) { this.paypalId = paypalId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Business logic for saving the invoice
    public void save() throws SQLException {
        repository.save(this);
    }

    // Business logic for updating the status
    public void updateStatus(String status) throws SQLException {
        this.status = status;
        repository.updateStatus(this.id, status);
    }

    // Static method to get all invoices
    public static List<Invoice> getAllInvoices(InvoiceRepository repository) throws SQLException {
        return repository.getAll();
    }

    // Static method to find invoice by ID
    public static Invoice findById(int id, InvoiceRepository repository) throws SQLException {
        return repository.findById(id);
    }
}
