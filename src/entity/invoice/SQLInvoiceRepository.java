package entity.invoice;

import entity.db.AIMSDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLInvoiceRepository implements InvoiceRepository {

    @Override
    public void save(Invoice invoice) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO `invoice` (orderId, amount, VNPayId, status) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, invoice.getOrderId());
            preparedStatement.setDouble(2, invoice.getAmount());
            preparedStatement.setString(3, invoice.getPaypalId());
            preparedStatement.setString(4, invoice.getStatus());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                invoice.setId(generatedKeys.getInt(1));
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    @Override
    public void updateStatus(int invoiceId, String status) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "UPDATE invoice SET status = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, invoiceId);
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    @Override
    public List<Invoice> getAll() throws SQLException {
        Connection connection = AIMSDB.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Invoice");
            while (resultSet.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(resultSet.getInt("id"));
                invoice.setAmount(resultSet.getInt("amount"));
                invoice.setPaypalId(resultSet.getString("VNPayId"));
                invoice.setStatus(resultSet.getString("status"));
                invoices.add(invoice);
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return invoices;
    }

    @Override
    public Invoice findById(int id) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Invoice invoice = null;
        try {
            String sql = "SELECT * FROM invoice WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                invoice = new Invoice();
                invoice.setId(resultSet.getInt("id"));
                invoice.setAmount(resultSet.getInt("amount"));
                invoice.setPaypalId(resultSet.getString("VNPayId"));
                invoice.setStatus(resultSet.getString("status"));
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return invoice;
    }
}
