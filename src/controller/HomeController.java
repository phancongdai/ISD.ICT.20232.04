package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.cart.Cart;
import entity.media.Media;
import entity.media.MediaRepository;
import entity.user.User;
import views.screen.home.MediaHandler;
public class HomeController extends BaseController{
    public List<Media> getAllMedia() throws SQLException{
        return new MediaRepository().getAllMedia();
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
