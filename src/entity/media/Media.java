package entity.media;

import entity.db.AIMSDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * The general media class, for another media it can be done by inheriting this class
 * @author nguyendai
 * */
public class Media {
    private int id;
    private String title;
    private String category;
    private int price; // the price which will be displayed on browser (eg: 500)
    private int quantity;
    private String type;
    private String imageURL;

    private double weight;

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String sql = "Select weight From Media where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getDouble(1);
                } else {
                    throw new SQLException("Weight of Media with ID " + this.id + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isSupportedPlaceRushOrder = new Random().nextBoolean();

    public Media() {}

    public Media(int id, String title, String category, int price, int quantity, String type, String imageURL) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.imageURL = imageURL;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isSupportedPlaceRushOrder() {
        return isSupportedPlaceRushOrder;
    }

    public void setSupportedPlaceRushOrder(boolean supportedPlaceRushOrder) {
        isSupportedPlaceRushOrder = supportedPlaceRushOrder;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", type='" + type + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", isSupportedPlaceRushOrder=" + isSupportedPlaceRushOrder +
                '}';
    }
}
