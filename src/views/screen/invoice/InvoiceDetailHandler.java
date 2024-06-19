package views.screen.invoice;

import common.exception.ProcessInvoiceException;
import entity.db.AIMSDB;
import entity.invoice.Invoice;
import entity.media.Media;
import entity.media.MediaRepository;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.invoicelist.InvoiceListHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailHandler extends BaseScreenHandler {
    private HomeScreenHandler home;
    private Invoice invoice;
    @FXML
    private Label id;
    @FXML
    private Label orderId;
    @FXML
    private Label amount;
    @FXML
    private Label paypalId;
    @FXML
    private Label status;
    @FXML
    private ImageView aimsImage;
    @FXML
    private VBox info;
    public InvoiceDetailHandler(Stage stage, Invoice invoice, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);
        setPreviousScreen(new InvoiceListHandler(this.stage, Configs.INVOICE_LIST_PATH));
        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        this.aimsImage.setImage(im);
        //System.out.println(home._instance);
        this.aimsImage.setOnMouseClicked(e -> {
            setScreenTitle("Invoice List Screen");
            getPreviousScreen().show();
        });

        this.invoice = invoice;
        id.setText(String.valueOf(invoice.getId()));
//        orderId.setText(String.valueOf(invoice.getOrder().getID()));
        amount.setText(String.valueOf(invoice.getAmount()));
        paypalId.setText(invoice.getPaypalId());
        status.setText(invoice.getStatus());
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        int fid = this.invoice.getId();
        int orderid;
        ResultSet resultSet = null;
        String sql = "SELECT orderId FROM invoice WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, fid);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                orderid = resultSet.getInt(1);
            } else {
                throw new SQLException("Invoice with ID " + fid + " not found.");
            }
        } finally {
            // Close resources in reverse order of creation
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // Handle closing resultSet exception (optional)
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    // Handle closing preparedStatement exception (optional)
                }
            }
        }
        System.out.println(orderid);
        List lst = getOrderMedia(orderid);
        if(lst==null) {
            PopupScreen.error("This invoice contains a deleted media!");
        }
        System.out.println(lst);
        lst.forEach(orderMedia -> {
            try {
                MediaInvoiceScreenHandler mis = new MediaInvoiceScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                mis.setOrderMedia((OrderMedia) orderMedia);
                if (info != null) {
                    info.getChildren().add(mis.getContent());
                } else {
                    // Handle the case where info is null (e.g., print a message)
                    System.err.println("Error: info VBox is null");
                }
            } catch (IOException | SQLException e) {
                System.err.println("errors: " + e.getMessage());
                throw new ProcessInvoiceException(e.getMessage());
            }
        });
    }
    public void requestToDetail(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);

        setScreenTitle("Invoice Detail Screen");
        show();
    }
    public List getOrderMedia(int ordid) {
        List ordermedialist = new ArrayList<>();
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;
        try {
            String sql = "SELECT * FROM OrderMedia where orderID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ordid);
            resultset = preparedStatement.executeQuery();
            while (resultset.next()) {
                int quantity = resultset.getInt("quantity");
                int mediaid = resultset.getInt("mediaID");
                Media media = getMediaById(mediaid);
                if(media==null) {
                    //PopupScreen.error("Invoice contains a deleted media!\nLet's see another invoice");
                    return null;
                }
                OrderMedia orderMedia = new OrderMedia(media, quantity, media.getPrice());
                ordermedialist.add(orderMedia);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    // Handle closing preparedStatement exception
                }
            }
            if (resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException e) {
                    // Handle closing resultset exception
                }
            }
        }
        return ordermedialist;
    }
    public Media getMediaById(int id) throws SQLException{

        String sql1 = "SELECT * FROM Media where id = ?";
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        preparedStatement1.setInt(1, id);
        ResultSet res = preparedStatement1.executeQuery();
        if(res.next()) {
            Media newMedia = new Media();
            newMedia.setId(res.getInt("id"));
            newMedia.setTitle(res.getString("title"));
            newMedia.setQuantity(res.getInt("quantity"));
            newMedia.setCategory(res.getString("category"));
            newMedia.setImageURL(res.getString("imageUrl"));
            newMedia.setPrice(res.getInt("price"));
            newMedia.setType(res.getString("type"));
            return newMedia;
        }
        return null;
    }

}
