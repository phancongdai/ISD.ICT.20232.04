package entity.media;

import java.sql.SQLException;
import java.util.List;

public interface IMediaRepository {
    List<Media> getMediaByType(String type) throws SQLException;
    void updateMediaFieldById(int id, String field, Object value) throws SQLException;
    void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) throws SQLException;
    void deleteMediaById(int id) throws SQLException;
}

