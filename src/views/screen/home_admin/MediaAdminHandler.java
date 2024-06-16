package views.screen.home_admin;

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

    private void handleDeleteItem() throws SQLException {
        // Hiển thị hộp thoại xác nhận
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Confirm Deletion",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // Kiểm tra xem người dùng có chọn "No" hoặc đóng hộp thoại bằng dấu "X" không
        if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) {
            return;
        }

        // Nếu người dùng chọn "Yes", tiến hành ẩn item_form và xóa item khỏi cơ sở dữ liệu
        item_form.setVisible(false);

        // Remove the media item from the database
        //Viết câu lệnh xóa Item trong Database
        connect = AIMSDB.getConnection();
        String deleteData = "DELETE FROM Media WHERE id = ? ";
        prepare = connect.prepareStatement(deleteData);
        prepare.setInt(1,media.getId());

        //dùng hàm bên AdminHomeScreenHandler để load lại màn hình nhé !
        home.refreshMediaList();
    }

    private void handleUpdateItem() throws SQLException {
        TextInputDialog priceDialog = new TextInputDialog(mediaPrice.getText());
        priceDialog.setTitle("Update Media Price");
        priceDialog.setHeaderText("Enter new price for the media:");
        priceDialog.setContentText("Price:");

        Optional<String> priceResult = priceDialog.showAndWait();
        if (!priceResult.isPresent() || priceResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }

        // Hiển thị hộp thoại để nhập giá trị mới cho mediaAvail
        TextInputDialog availDialog = new TextInputDialog(mediaAvail.getText());
        availDialog.setTitle("Update Media Availability");
        availDialog.setHeaderText("Enter new availability for the media:");
        availDialog.setContentText("Availability:");

        Optional<String> availResult = availDialog.showAndWait();
        if (!availResult.isPresent() || availResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }

        // Hiển thị hộp thoại xác nhận
        int response = JOptionPane.showConfirmDialog(null, "Do you want to update the media details?", "Confirm Update",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            //Xóa 2 dòng dưới nếu cập nhật thành công trong database nhé
            mediaPrice.setText(priceResult.get() );
            mediaAvail.setText(availResult.get());
//            connect = AIMSDB.getConnection();
//            String sql = "UPDATE media SET price = ?, availability = ? WHERE id = ?";
//            // Cập nhật giá trị mới
//            prepare = connect.prepareStatement(sql);
//            prepare.setString(1,mediaPrice.getText());
//            prepare.setString(2,mediaAvail.getText());
//            prepare.setInt(3,media.getId());
//            prepare.executeUpdate();
//            home.refreshMediaList();
        }
        else{
            return;
        }
    }
}
