package controller;

import entity.media.IMediaRepository;
import entity.media.Media;

import java.util.ArrayList;
import java.util.List;

public class AdminController extends BaseController {
    private final IMediaRepository mediaRepo;

    public AdminController(IMediaRepository mediaRepo) {
        this.mediaRepo = mediaRepo;
    }

    public List<Media> getAllMedia()  {
        return mediaRepo.getAllMedia();
    }

    public void changeQuantity(int id, int quantity){
        mediaRepo.updateMediaFieldById(id, "quantity", quantity);
    }

    public void changePrice(int id, int price) {
        mediaRepo.updateMediaFieldById(id, "price", price);
    }

    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) {
        mediaRepo.addNewMedia(title, type, category, imgUrl, price, quantity);
    }

    public void deleteMedia(int id) {
        mediaRepo.deleteMediaById(id);
    }

    public List<Media> filterMediaByKeyWord(String keyword, List<Media> items) {
        List<Media> filteredItems = new ArrayList<>();
        for (Media item : items) {
            if (item.getTitle().toLowerCase().contains(keyword)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }
}
