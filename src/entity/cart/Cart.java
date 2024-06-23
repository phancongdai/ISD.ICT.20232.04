package entity.cart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.media.Media;
import views.screen.popup.PopupScreen;

public class Cart {
    
    private List<CartMedia> listCartMedia;
    private static Cart cartInstance;

    public static Cart getCart(){
        if(cartInstance == null) cartInstance = new Cart();
        return cartInstance;
    }

    private Cart(){
        listCartMedia = new ArrayList<>();
    }

    public void addCartMedia(CartMedia cm){
        listCartMedia.add(cm);
    }

    public void removeCartMedia(CartMedia cm){
        listCartMedia.remove(cm);
    }

    public List getListMedia(){
        return listCartMedia;
    }

    public void emptyCart(){
        listCartMedia.clear();
    }

    public int getTotalMedia(){
        int total = 0;
        for (CartMedia cartMedia : listCartMedia) {
            total += cartMedia.getQuantity();
        }
        return total;
    }

    public int calSubtotal(){
        int total = 0;
        for (CartMedia cartMedia : listCartMedia) {
            total += cartMedia.getPrice()*cartMedia.getQuantity();
        }
        return total;
    }

    public void checkAvailabilityOfProduct() {
        boolean allAvail = true;
        for (CartMedia cartMedia : listCartMedia) {
            int requiredQuantity = cartMedia.getQuantity();
            int availQuantity = cartMedia.getMedia().getQuantity();
            if (requiredQuantity > availQuantity) {
                System.out.println(requiredQuantity + " > " + availQuantity);
                System.out.println(cartMedia.getMedia().getTitle() + " " + cartMedia.getMedia().getQuantity());
                allAvail = false;
            }
        }
        if (!allAvail) {
            try {
                PopupScreen.error("Some media is not available");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("checkAvailabilityOfProduct error: " + e.getMessage());
            }
        }
    }

    public List<String> checkRushAvailability() {
        ArrayList<String> unavailableMedia = new ArrayList<>();
        for (CartMedia cartMedia : listCartMedia) {
            boolean rushAvailable = cartMedia.getMedia().isSupportedPlaceRushOrder();
            if (!rushAvailable) {
                unavailableMedia.add(cartMedia.getMedia().getTitle());
            }
        }
        return unavailableMedia;
    }

    public CartMedia checkMediaInCart(Media media){
        for (CartMedia cartMedia : listCartMedia) {
            if (cartMedia.getMedia().getId() == media.getId()) return cartMedia;
        }
        return null;
    }

}
