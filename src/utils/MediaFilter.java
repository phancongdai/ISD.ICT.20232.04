package utils;

import entity.media.Media;

import java.util.List;

public interface MediaFilter {
    List<Media> filterMediaByKeyWord(String keyword, List<Media> items);
}
