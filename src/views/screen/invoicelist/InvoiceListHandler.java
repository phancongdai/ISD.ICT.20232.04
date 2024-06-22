package views.screen.invoicelist;

import controller.InvoiceListController;
import entity.db.AIMSDB;
import entity.invoice.Invoice;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.invoice.InvoiceDetailHandler;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceListHandler extends BaseScreenHandler {

    private ArrayList<Invoice> dataInvoice;

    private HomeScreenHandler home;

    @FXML
    private ImageView aimsImage;

    @FXML
    private TableView<Invoice> tableView;

    @FXML
    private TableColumn<Invoice, Integer> sttColumn;

    @FXML
    private TableColumn<Invoice, String> idColumn;

    @FXML
    private TableColumn<Invoice, String> amountColumn;

    @FXML
    private TableColumn<Invoice, String> statusColumn;

    @FXML
    private TableColumn<Invoice, Button> invoice_custom;

    @FXML
    private TableColumn<Invoice, Button> invoice_detail;

    public InvoiceListHandler(Stage stage, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);
        CleanInvoiceList();
        loadData();
        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);
        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            setScreenTitle("Home Screen");
            homeScreenHandler.show();
        });
    }

    private void initializeTableColumns() throws SQLException {
        // Map TableColumns to corresponding fields in Invoice class
        sttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaypalId()));
        amountColumn.setCellValueFactory(cellData -> {
            //int multipliedValue = cellData.getValue().getAmount() * 1000;
            return new SimpleStringProperty(String.format( "%.3f",cellData.getValue().getAmount()) + " đ");
        });

        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        invoice_detail.setCellValueFactory(cellData -> {
            Button detailButton = createDetailButton(cellData.getValue());
            //System.out.println(cellData.getValue());
            return new SimpleObjectProperty<>(detailButton);
        });
        invoice_detail.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button button, boolean empty) {
                super.updateItem(button, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
    }

    private Button createDetailButton(Invoice invoice) {
        Button detailButton = new Button();
        detailButton.setText("Detail");
//        String status = invoice.getStatus();

        detailButton.setOnMouseClicked(e -> {
            InvoiceDetailHandler invoiceDetailHandler;
            try {
                //System.out.println(invoice.getId());
                invoiceDetailHandler = new InvoiceDetailHandler(this.stage, invoice, Configs.INVOICE_DETAIL_PATH);
                invoiceDetailHandler.requestToDetail(this);
                invoiceDetailHandler.setHomeScreenHandler(this.homeScreenHandler);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        return detailButton;
    }

    private void populateTable() {
        //Convert ArrayList to ObservableList and set it to the table
        ObservableList<Invoice> observableData = FXCollections.observableArrayList(dataInvoice);
        tableView.setItems(observableData);
    }

    public InvoiceListController getBController(){
        return (InvoiceListController) super.getBController();
    }

    public void requestToInvoiceList(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Invoice List Screen");
        show();
    }

    private void loadData() throws SQLException {
        dataInvoice = Invoice.getListInvoice();
        initializeTableColumns();
        populateTable();
        //tableView.lookup()
    }
    public void CleanInvoiceList() throws SQLException {
        String sql = "DELETE FROM Invoice WHERE VNPayId = ?;";
        Connection connection = AIMSDB.getConnection();
        PreparedStatement preparedStatement = null;
        int res;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "");
            //preparedStatement.setString(2, "CREATED");
            res = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql1 = "DELETE FROM Invoice WHERE status = ?;";
        PreparedStatement preparedStatement1 = null;
        int res1;
        try {
            preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setString(1, "CREATED");
            res1 = preparedStatement1.executeUpdate();
            preparedStatement1.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
