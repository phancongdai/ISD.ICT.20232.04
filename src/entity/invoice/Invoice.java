package entity.invoice;

import entity.db.AIMSDB;
import entity.order.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    public Invoice() {

    }

    public Order getOrder() {
        return order;
    }
    public int getOrderId() {
        return this.order.getID();
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(int id){this.id = id;}
    public int getId(){return this.id;}

    public int getAmount() {
        return amount;
    }

    public String getPaypalId(){ return this.paypalId;}

    public void setPaypalId(String paypalId){ this.paypalId = paypalId;}

    public String getStatus(){return status;}

    public void setStatus(String status){this.status=status;}

    public void updateInvoice(int id, String status, String VNPayId, int amount) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String sql = "UPDATE Invoice SET status = ?, VNPayId = ?, amount = ? WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, VNPayId);
            preparedStatement.setInt(3, amount);
            preparedStatement.setInt(4, id);
            int res = preparedStatement.executeUpdate();
            if(res>0) {
                System.out.println("Update an invoice with ID "+id+" successfully!");
            } else {
                System.out.println("Can not update an invoice with ID "+ id+ "!");
            }
        }
    }



//    public void updateStatus(int invoiceid, String status) throws SQLException {
//        Connection connection = AIMSDB.getConnection();
//        String sql = "Update Invoice set VNPayId = ?, status = ?, amount =? where id = ?";
//        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, this.getPaypalId());
//            preparedStatement.setString(2, status);
//            preparedStatement.setInt(3, this.getAmount());
//            preparedStatement.setInt(3, invoiceid);
//
//            int res = preparedStatement.executeUpdate();
//            if(res>0) {
//                System.out.println("Update an invoice with ID "+ this.getId() +" successfully!");
//            } else {
//                System.out.println("Can not update an invoice with ID "+ this.getId());
//            }
//        }
//    }

//    public void saveInvoice() throws SQLException {
//        Connection connection = AIMSDB.getConnection();
//        String sql ="Insert Into Invoice (orderId, amount, status) VALUES (?, ?, ?)";
//        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
//            preparedStatement.setInt(1, order.getID());
//            preparedStatement.setInt(2, this.getAmount());
//            preparedStatement.setString(3, this.getStatus());
//            int res = preparedStatement.executeUpdate();
//            if(res>0) {
//                System.out.println("Add a new invoice with ID" + this.getId()+ "successfully!");
//            } else {
//                System.out.println("Can not add a new invoice" +this.getId() +"!");
//            }
//
//        }
//    }
}
