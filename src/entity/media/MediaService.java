package entity.media;

import java.util.List;

// MediaService.java
public interface MediaService {
    List<Media> getAllMedia();
    void changeQuantity(int id, int quantity);
    void changePrice(int id, int price);
    void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity);
    void deleteMedia(int id);
}