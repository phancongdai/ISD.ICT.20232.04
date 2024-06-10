package views.screen.invoice;

import common.exception.ProcessInvoiceException;
import entity.db.AIMSDB;
import entity.invoice.Invoice;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
    private VBox Info;
    public InvoiceDetailHandler(Stage stage, Invoice invoice, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);
        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        this.aimsImage.setImage(im);
        //System.out.println(home._instance);
        this.aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        this.invoice = invoice;
        id.setText(String.valueOf(invoice.getId()));
//        orderId.setText(String.valueOf(invoice.getOrder().getID()));
        amount.setText(String.valueOf(invoice.getAmount()));
        paypalId.setText(invoice.getPaypalId());
        status.setText(invoice.getStatus());
//        Connection connection = AIMSDB.getConnection();
//        PreparedStatement preparedStatement = null;
//        int fid = invoice.getId();
//        int orderid;
//        ResultSet resultSet = null;
//        String sql = "SELECT orderId FROM invoice WHERE id = ?";
//        try {
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1, fid);
//            resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                orderid = resultSet.getInt(1);
//            } else {
//                throw new SQLException("Invoice with ID " + fid + " not found.");
//            }
//        } finally {
//            // Close resources in reverse order of creation
//            if (resultSet != null) {
//                try {
//                    resultSet.close();
//                } catch (SQLException e) {
//                    // Handle closing resultSet exception (optional)
//                }
//            }
//            if (preparedStatement != null) {
//                try {
//                    preparedStatement.close();
//                } catch (SQLException e) {
//                    // Handle closing preparedStatement exception (optional)
//                }
//            }
//        }
//        System.out.println(orderid);
//        sql = "SELECT mediaID FROM OrderMedia WHERE orderID = ?";
//        resultSet = null;
//        try {
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1, orderid);
//            resultSet = preparedStatement.executeQuery();
//
//        } finally {
//            if (preparedStatement != null) {
//                try {
//                    preparedStatement.close();
//                } catch (SQLException e) {
//                    // Handle closing preparedStatement exception (optional)
//                }
//        }
        //res = new
        //System.out.println(order);
        /*invoice.getOrder().getlstOrderMedia().forEach(orderMedia -> {
            try {
                MediaInvoiceScreenHandler mis = new MediaInvoiceScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                mis.setOrderMedia((OrderMedia) orderMedia);
                Info.getChildren().add(mis.getContent());
            } catch (IOException | SQLException e) {
                System.err.println("errors: " + e.getMessage());
                throw new ProcessInvoiceException(e.getMessage());
            }
        });*/
    }
    public void requestToDetail(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Invoice Detail Screen");
        show();
    }
}
