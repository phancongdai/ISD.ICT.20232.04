package entity.media;


import java.util.ArrayList;
import java.util.List;

public class MediaFilterImpl implements MediaFilter {
    @Override
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
