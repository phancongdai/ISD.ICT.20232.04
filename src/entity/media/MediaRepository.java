package entity.media;

import entity.db.AIMSDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MediaRepository implements IMediaRepository {
    private Connection conn = AIMSDB.getConnection();

    @Override
    public List<Media> getAllMedia() {
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM Media";
        try (PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet res = stm.executeQuery()) {
            mediaList = extractMediaListFromResultSet(res);
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
        }
        return mediaList;
    }

    @Override
    public List<Media> getMediaByType(String type) {
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM Media WHERE type = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, type);
            try (ResultSet res = stm.executeQuery()) {
                mediaList = extractMediaListFromResultSet(res);
            }
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
        }
        return mediaList;
    }

    @Override
    public void updateMediaFieldById(int id, String field, Object value) {
        String sql = "UPDATE Media SET " + field + " = ? WHERE id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, value.toString());
            stm.setInt(2, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
        }
    }

    @Override
    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) {
        String sql = "INSERT INTO Media (title, category, price, quantity, type, imageURL) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, title);
            stm.setString(2, category);
            stm.setDouble(3, price);
            stm.setInt(4, quantity);
            stm.setString(5, type);
            stm.setString(6, imgUrl);
            stm.executeUpdate();
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMediaById(int id) {
        String sql = "DELETE FROM Media WHERE id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            // Handle or log the exception
            e.printStackTrace();
        }
    }

    private List<Media> extractMediaListFromResultSet(ResultSet res) throws SQLException {
        List<Media> mediaList = new ArrayList<>();
        while (res.next()) {
            Media media = extractMediaFromResultSet(res);
            mediaList.add(media);
        }
        return mediaList;
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
}
