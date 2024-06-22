package entity.invoice;

import entity.db.AIMSDB;
import entity.order.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private int id;
    private Order order;
    private double amount;
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

    public Invoice() {

    }

    public Order getOrder() {
        return order;
    }
    public int getOrderId() {
        return this.order.getID();
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setId(int id){this.id = id;}
    public int getId(){return this.id;}

    public double getAmount() {
        return amount;
    }

    public String getPaypalId(){ return this.paypalId;}

    public void setPaypalId(String paypalId){ this.paypalId = paypalId;}

    public String getStatus(){return status;}

    public void setStatus(String status){this.status=status;}

    public void updateStatus(String status) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("SELECT * from Invoice");
        ArrayList<Invoice> invoices = new ArrayList<>();
        while (res.next()) {
            Invoice invoice = new Invoice();
            invoice.setId(res.getInt("id"));
            invoice.setAmount(res.getDouble("amount"));
            invoice.setPaypalId(res.getString("VNPayId"));
            invoice.setStatus(res.getString("status"));
            invoices.add(invoice);
        }
    }
}
