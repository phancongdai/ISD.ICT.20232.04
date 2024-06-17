package controller;

import entity.media.Media;

import java.sql.SQLException;
import java.util.List;

public class AdminCRUDController extends BaseController {
    Media media = new Media();

    public AdminCRUDController() throws SQLException {}

    public List<Media> getAllBooks() throws SQLException {
        return media.getMediaByType("book");
    }

    public List<Media> getAllCDs() throws SQLException {
        return media.getMediaByType("cd");
    }

    public List<Media> getAllDvds() throws SQLException {
        return media.getMediaByType("dvd");
    }

    public void changeQuantity(int id, int quantity) throws SQLException {
        media.updateMediaFieldById("Media", id, "quantity", quantity);
    }

    public void changePrice(int id, int price) throws SQLException {
        media.updateMediaFieldById("Media", id, "price", price);
    }

    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) throws SQLException {
        media.addNewMedia(title, type, category, imgUrl, price, quantity);
    }

    public void deleteMedia(int id) throws SQLException {
        media.deleteMediaById(id);
    }

    public void approveInvoice(Boolean approved) throws SQLException {
    }
}
