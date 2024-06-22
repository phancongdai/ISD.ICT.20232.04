package entity.order;

import entity.db.AIMSDB;
import utils.Configs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order {
    private double totalweight;
    private int ID;
    private double shippingFees;

    public double getTotalweight() {
        return totalweight;
    }

    public void setTotalweight(double weight) {
        this.totalweight = weight;
    }

    private List lstOrderMedia;
    private HashMap<String, String> deliveryInfo;

    public Order(){
        this.lstOrderMedia = new ArrayList<>();
    }

    public Order(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void addOrderMedia(OrderMedia om){
        this.lstOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om){
        this.lstOrderMedia.remove(om);
    }

    public List getlstOrderMedia() {
        return this.lstOrderMedia;
    }

    public void setlstOrderMedia(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void setShippingFees(double shippingFees) {
        this.shippingFees = shippingFees;
    }

    public double getShippingFees() {
        return shippingFees;
    }

    public HashMap getDeliveryInfo() {
        return deliveryInfo;
    }

    public int getID() {
        return ID;
    }

    public void setDeliveryInfo(HashMap deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public String toString() {
        return this.getID() + " " + this.getlstOrderMedia() + " " +this.getAmount();
    }

    public String toStringWithDeliveryInfo(){
        return this.toString() + " " + this.getDeliveryInfo();
    }

    private String typePayment;

    public void setTypePayment(String typePayment) {
        this.typePayment = typePayment;
    }

    public String getTypePayment() {
        return typePayment;
    }

    public int getAmount(){
        double amount = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getPrice()* om.getQuantity();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }


    public void saveOrder() throws SQLException {
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO `Order` (name, phone , address, shipping_fee) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.deliveryInfo.get("name"));
            preparedStatement.setString(2, deliveryInfo.get("phone"));
            preparedStatement.setString(3, deliveryInfo.get("address"));
            preparedStatement.setDouble(4, getShippingFees());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.ID = generatedKeys.getInt(1); // Lấy giá trị ID từ cột đầu tiên của kết quả
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

        for (Object object : lstOrderMedia){
            OrderMedia orderMedia = (OrderMedia) object;
            orderMedia.saveOrderMedia(this.ID);
        }
    }

}
