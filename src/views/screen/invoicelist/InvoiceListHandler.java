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
    private TableColumn<Invoice, Integer> amountColumn;

    @FXML
    private TableColumn<Invoice, String> statusColumn;

    @FXML
    private TableColumn<Invoice, Button> invoice_custom;

    @FXML
    private TableColumn<Invoice, Button> invoice_detail;

    private InvoiceListController invoiceListController;

    public InvoiceListHandler(Stage stage, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);

        // Initialize the controller
        this.invoiceListController = new InvoiceListController();

        CleanInvoiceList();
        loadData();
        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        aimsImage.setOnMouseClicked(e -> {
            setScreenTitle("Home Screen");
            homeScreenHandler.show();
        });
    }

    private void initializeTableColumns() throws SQLException {
        sttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaypalId()));
        amountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        invoice_detail.setCellValueFactory(cellData -> {
            Button detailButton = createDetailButton(cellData.getValue());
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

        detailButton.setOnMouseClicked(e -> {
            InvoiceDetailHandler invoiceDetailHandler;
            try {
                invoiceDetailHandler = new InvoiceDetailHandler(this.stage, invoice, Configs.INVOICE_DETAIL_PATH);
                invoiceDetailHandler.requestToDetail(this);
                invoiceDetailHandler.setHomeScreenHandler(this.homeScreenHandler);
            } catch (IOException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        return detailButton;
    }

    private void populateTable() {
        ObservableList<Invoice> observableData = FXCollections.observableArrayList(dataInvoice);
        tableView.setItems(observableData);
    }

    public InvoiceListController getBController(){
        return invoiceListController; // Return the initialized controller
    }

    public void requestToInvoiceList(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Invoice List Screen");
        show();
    }

    private void loadData() throws SQLException {
        // Fetch invoices using the controller
        dataInvoice = getBController().getListInvoice();
        initializeTableColumns();
        populateTable();
    }

    public void CleanInvoiceList() throws SQLException {
        String sql = "DELETE FROM Invoice WHERE VNPayId = ?;";
        try (Connection connection = AIMSDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "");
            preparedStatement.executeUpdate();
        }

        String sql1 = "DELETE FROM Invoice WHERE status = ?;";
        try (Connection connection = AIMSDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql1)) {
            preparedStatement.setString(1, "CREATED");
            preparedStatement.executeUpdate();
        }
    }
}
