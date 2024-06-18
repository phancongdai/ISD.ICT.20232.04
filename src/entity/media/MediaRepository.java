package entity.media;
import entity.db.AIMSDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MediaRepository {

    public Media getMediaById(int id) throws SQLException {
        String sql = "SELECT * FROM Media WHERE id = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet res = stm.executeQuery();
        if (res.next()) {
            return extractMediaFromResultSet(res);
        }
        return null;
    }

    public List<Media> getAllMedia() throws SQLException {
        String sql = "SELECT * FROM Media";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        ResultSet res = stm.executeQuery();
        return extractMediaListFromResultSet(res);
    }

    public List<Media> getMediaByType(String type) throws SQLException {
        String sql = "SELECT * FROM Media WHERE type = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, type);
        ResultSet res = stm.executeQuery();
        return extractMediaListFromResultSet(res);
    }

    public void updateMediaFieldById(int id, String field, Object value) throws SQLException {
        String sql = "UPDATE Media SET " + field + " = ? WHERE id = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        if (value instanceof String) {
            stm.setString(1, (String) value);
        } else if (value instanceof Integer) {
            stm.setInt(1, (Integer) value);
        } // Add other types as needed
        stm.setInt(2, id);
        stm.executeUpdate();
    }

    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) throws SQLException {
        String sql = "INSERT INTO Media (title, category, price, quantity, type, imageURL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, title);
        stm.setString(2, category);
        stm.setDouble(3, price);
        stm.setInt(4, quantity);
        stm.setString(5, type);
        stm.setString(6, imgUrl);
        stm.executeUpdate();
    }

    public void deleteMediaById(int id) throws SQLException {
        String sql = "DELETE FROM Media WHERE id = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setInt(1, id);
        stm.executeUpdate();
    }

    private Media extractMediaFromResultSet(ResultSet res) throws SQLException {
        return new Media(
                res.getInt("id"),
                res.getString("title"),
                res.getString("category"),
                res.getInt("price"),
                res.getInt("quantity"),
                res.getString("type"),
                res.getString("imageURL")
        );
    }

    private List<Media> extractMediaListFromResultSet(ResultSet res) throws SQLException {
        List<Media> mediaList = new ArrayList<>();
        while (res.next()) {
            Media media = extractMediaFromResultSet(res);
            mediaList.add(media);
        }
        return mediaList;
    }
}
