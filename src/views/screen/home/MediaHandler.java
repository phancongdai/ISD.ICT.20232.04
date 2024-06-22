package views.screen.home;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import common.exception.MediaNotAvailableException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Utils;
import views.screen.FXMLScreenHandler;
import views.screen.popup.PopupScreen;

public class MediaHandler extends FXMLScreenHandler{

    @FXML
    protected ImageView mediaImage;

    @FXML
    protected Label mediaTitle;

    @FXML
    protected Label mediaPrice;

    @FXML
    protected Label mediaAvail;

   // @FXML
    //protected Spinner<Integer> spinnerChangeNumber;

    @FXML
    protected Button addToCartBtn;

    @FXML
    protected Button btnDetail;

    private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());
    private Media media;
    private HomeScreenHandler home;

    public MediaHandler(String screenPath, Media media, HomeScreenHandler home) throws SQLException, IOException{
        super(screenPath);
        this.media = media;
        this.home = home;
        // Control coupling
        addToCartBtn.setOnMouseClicked(event -> {
            Cart cart = Cart.getCart();
            CartMedia mediaInCart = home.getBController().checkMediaInCart(media);
            if (mediaInCart == null) {
                cart.getListMedia().add(new CartMedia(media, 1, media.getPrice()));
                try {
                    PopupScreen.success(media.getTitle() + " is added to cart!", 3);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    PopupScreen.alter(media.getTitle()+ " exists in cart!", 3);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            home.getNumMediaCartLabel().setText(String.valueOf(cart.getTotalMedia() + " media"));

//            try {
//                System.out.println(spinnerChangeNumber.getValue() + " and "+ media.getQuantity());
//                if (spinnerChangeNumber.getValue() > media.getQuantity()) throw new MediaNotAvailableException("Media is not enough!");
//                Cart cart = Cart.getCart();
//                CartMedia mediaInCart = home.getBController().checkMediaInCart(media);
//                if (mediaInCart != null) {
//                    mediaInCart.setQuantity(mediaInCart.getQuantity() + 1);
//                }else{
//                    CartMedia cartMedia = new CartMedia(media, cart, spinnerChangeNumber.getValue(), media.getPrice());
//                    cart.getListMedia().add(cartMedia);
//                    LOGGER.info("Added " + cartMedia.getQuantity() + " " + media.getTitle() + " to cart");
//                }
//
//                // subtract the quantity and redisplay
//                media.setQuantity(media.getQuantity() - spinnerChangeNumber.getValue());
//                mediaAvail.setText(String.valueOf(media.getQuantity()));
//                // Content coupling
//                home.getNumMediaCartLabel().setText(String.valueOf(cart.getTotalMedia() + " media"));
//                PopupScreen.success("The media " + media.getTitle() + " added to Cart");
//            } catch (MediaNotAvailableException exp) {
//                try {
//                    String message = "Not enough media:\nRequired: " + spinnerChangeNumber.getValue() + "\nAvail: " + media.getQuantity();
//                    LOGGER.severe(message);
//                    PopupScreen.error(message);
//                } catch (Exception e) {
//                    LOGGER.severe("Cannot add media to cart: ");
//                }
//
//            } catch (Exception exp) {
//                LOGGER.severe("Cannot add media to cart: ");
//                exp.printStackTrace();
//            }
        });

        btnDetail.setOnMouseClicked(e -> {
            home.handleClickDetail(this.media);
        });

        setMediaInfo();
    }

    public Media getMedia(){
        return media;
    }

    private void setMediaInfo() throws SQLException {
        // set the cover image of media
        File file = new File(media.getImageURL());
        Image image = new Image(file.toURI().toString());
        mediaImage.setFitHeight(160);
        mediaImage.setFitWidth(152);
        mediaImage.setImage(image);

        mediaTitle.setText(media.getTitle());
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()*1000));
        mediaAvail.setText(Integer.toString(media.getQuantity()));
        setImage(mediaImage, media.getImageURL());
    }

}
