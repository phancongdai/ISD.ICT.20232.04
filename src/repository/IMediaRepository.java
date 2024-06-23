package repository;

import entity.media.Media;

import java.sql.SQLException;
import java.util.List;

public interface IMediaRepository {
    List<Media> getMediaByType(String type);
    List<Media> getAllMedia();
    void updateMediaFieldById(int id, String field, Object value);
    void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity);
    void deleteMediaById(int id);
}

