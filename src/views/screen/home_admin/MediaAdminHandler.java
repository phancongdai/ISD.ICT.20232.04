package views.screen.home_admin;

import controller.AdminCRUDController;
import entity.db.AIMSDB;
import entity.media.Media;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import utils.Utils;
import views.screen.FXMLScreenHandler;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

public class MediaAdminHandler extends FXMLScreenHandler {
    @FXML
    private Button deleteItemBtn;

    @FXML
    private AnchorPane item_form;

    @FXML
    private Label mediaAvail;

    @FXML
    private ImageView mediaImage;

    @FXML
    private Label mediaPrice;

    @FXML
    private Label mediaTitle;

    @FXML
    private Button updateItemBtn;

    private Media media;
    private AdminHomeScreenHandler home;
    private Connection connect;
    private PreparedStatement prepare;

    private AdminCRUDController adminCRUDController;

    private static Logger LOGGER = Utils.getLogger(MediaAdminHandler.class.getName());

    public MediaAdminHandler(String screenPath, Media media, AdminHomeScreenHandler home) throws SQLException, IOException {
        super(screenPath);
        this.media = media;
        this.home = home;

        setMediaInfo();

        deleteItemBtn.setOnAction(e -> {
            try {
                handleDeleteItem();
            } catch (SQLException ex) {
                LOGGER.severe("Error deleting media: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        updateItemBtn.setOnAction(e -> {
            try {
                handleUpdateItem();
            } catch (SQLException ex) {
                LOGGER.severe("Error update media: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
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
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
        mediaAvail.setText(Integer.toString(media.getQuantity()));

        setImage(mediaImage, media.getImageURL());
    }

    private void handleAddNewItem() throws SQLException {
        //TODO: Cần các thông tin để truyền vào hàm
        String title = "Test";
        String type = "Book";
        String category = "Romance";
        String imgUrl = "img url";
        double price = 100;
        int quantity = 10;
        adminCRUDController.addNewMedia(title, type, category, imgUrl, price, quantity);
    }

    private void handleDeleteItem() throws SQLException {
        if (adminCRUDController == null) adminCRUDController = new AdminCRUDController();
        // Confirm box
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Confirm Deletion",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // If user cancel
        if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) {
            return;
        }

        // Remove item and reload
        item_form.setVisible(false);
        adminCRUDController.deleteMedia(media.getId());
        home.refreshMediaList();
    }

    private void handleUpdateItem() throws SQLException {
        if (adminCRUDController == null) adminCRUDController = new AdminCRUDController();

        // New price
        TextInputDialog priceDialog = new TextInputDialog(mediaPrice.getText());
        priceDialog.setTitle("Update Media Price");
        priceDialog.setHeaderText("Enter new price for the media:");
        priceDialog.setContentText("Price:");

        Optional<String> priceResult = priceDialog.showAndWait();
        if (priceResult.isEmpty() || priceResult.get().isEmpty()) {
            return;
        }

        // New quantity
        TextInputDialog availDialog = new TextInputDialog(mediaAvail.getText());
        availDialog.setTitle("Update Media Availability");
        availDialog.setHeaderText("Enter new availability for the media:");
        availDialog.setContentText("Availability:");

        Optional<String> availResult = availDialog.showAndWait();
        if (availResult.isEmpty() || availResult.get().isEmpty()) {
            return;
        }

        // Confirm box
        int response = JOptionPane.showConfirmDialog(null, "Do you want to update the media details?", "Confirm Update",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            adminCRUDController.changeQuantity(media.getId(), Integer.parseInt(priceResult.get()));
            adminCRUDController.changePrice(media.getId(), Integer.parseInt(priceResult.get()));
        }

        home.refreshMediaList();
    }
}
