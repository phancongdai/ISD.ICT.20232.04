package controller.admin;

import controller.BaseController;
import entity.media.IMediaRepository;
import entity.media.Media;
import entity.media.MediaFilter;
import entity.media.MediaService;

import java.util.ArrayList;
import java.util.List;
// AdminController.java
public class AdminController extends BaseController {
    private final MediaService mediaService;
    private final MediaFilter mediaFilter;

    public AdminController(MediaService mediaService, MediaFilter mediaFilter) {
        this.mediaService = mediaService;
        this.mediaFilter = mediaFilter;
    }

    public List<Media> getAllMedia() {
        return mediaService.getAllMedia();
    }

    public void changeQuantity(int id, int quantity) {
        mediaService.changeQuantity(id, quantity);
    }

    public void changePrice(int id, int price) {
        mediaService.changePrice(id, price);
    }

    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) {
        mediaService.addNewMedia(title, type, category, imgUrl, price, quantity);
    }

    public void deleteMedia(int id) {
        mediaService.deleteMedia(id);
    }

    public List<Media> filterMediaByKeyWord(String keyword, List<Media> items) {
        return mediaFilter.filterMediaByKeyWord(keyword, items);
    }
}
