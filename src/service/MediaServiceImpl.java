package service;

import entity.media.Media;
import repository.IMediaRepository;

import java.util.List;

// MediaServiceImpl.java
public class MediaServiceImpl implements MediaService {
    private final IMediaRepository mediaRepo;

    public MediaServiceImpl(IMediaRepository mediaRepo) {
        this.mediaRepo = mediaRepo;
    }

    @Override
    public List<Media> getAllMedia() {
        return mediaRepo.getAllMedia();
    }

    @Override
    public void changeQuantity(int id, int quantity) {
        mediaRepo.updateMediaFieldById(id, "quantity", quantity);
    }

    @Override
    public void changePrice(int id, int price) {
        mediaRepo.updateMediaFieldById(id, "price", price);
    }

    @Override
    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) {
        mediaRepo.addNewMedia(title, type, category, imgUrl, price, quantity);
    }

    @Override
    public void deleteMedia(int id) {
        mediaRepo.deleteMediaById(id);
    }
}
