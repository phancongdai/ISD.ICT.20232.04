package entity.media;

import java.util.List;

public interface MediaFilter {
    List<Media> filterMediaByKeyWord(String keyword, List<Media> items);
}
