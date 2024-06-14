package controller;

import entity.media.Media;

import java.sql.SQLException;
import java.util.List;

public class AdminCRUDController extends BaseController {
    Media media = new Media();

    public AdminCRUDController() throws SQLException {}

    public List getAllBooks() throws SQLException {
        return media.getMediaByType("book");
    }

    public List getAllCDs() throws SQLException {
        return media.getMediaByType("cd");
    }

    public List getAllDvds() throws SQLException {
        return media.getMediaByType("dvd");
    }

    public void changeQuantity(int id, int quantity) throws SQLException {
        media.updateMediaFieldById("Media", id, "quantity", quantity);
    }
}
