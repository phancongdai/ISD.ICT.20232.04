package controller;

import entity.media.Media;
import entity.media.MediaRepository;

import java.sql.SQLException;
import java.util.List;

public class AdminCRUDController extends BaseController {
    MediaRepository mediaRepo = new MediaRepository();

    public AdminCRUDController() throws SQLException {}

    public List<Media> getAllBooks() throws SQLException {
        return mediaRepo.getMediaByType("book");
    }

    public List<Media> getAllCDs() throws SQLException {
        return mediaRepo.getMediaByType("cd");
    }

    public List<Media> getAllDvds() throws SQLException {
        return mediaRepo.getMediaByType("dvd");
    }

    public void changeQuantity(int id, int quantity) throws SQLException {
        mediaRepo.updateMediaFieldById(id, "quantity", quantity);
    }

    public void changePrice(int id, int price) throws SQLException {
        mediaRepo.updateMediaFieldById(id, "price", price);
    }

    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) throws SQLException {
        mediaRepo.addNewMedia(title, type, category, imgUrl, price, quantity);
    }

    public void deleteMedia(int id) throws SQLException {
        mediaRepo.deleteMediaById(id);
    }

    public void approveInvoice(Boolean approved) throws SQLException {
    }
}
