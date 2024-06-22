package views.screen.invoicelist;

import controller.InvoiceListController;
import entity.invoice.Invoice;
import javafx.beans.property.DoubleProperty;
import entity.invoice.InvoiceRepository;
import entity.invoice.SQLInvoiceRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import views.screen.BaseScreenAdminHandler;
import views.screen.home_admin.AdminHomeScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceListAdminHandler extends BaseScreenAdminHandler {

    private ArrayList<Invoice> dataInvoice;
    private AdminHomeScreenHandler home;

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
    private TableColumn<Invoice, Button> statusColumn;

    @FXML
    private TableColumn<Invoice, String> invoice_custom;

    // Use the repository interface for database operations
    private InvoiceRepository invoiceRepository;

    public InvoiceListAdminHandler(Stage stage, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);

        // Initialize the repository with the appropriate implementation
        this.invoiceRepository = new SQLInvoiceRepository();

        loadData();

        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        aimsImage.setOnMouseClicked(e -> adminHomeScreenHandler.show());
    }

    public InvoiceListController getBController() {
        return (InvoiceListController) super.getBController();
    }

    private void loadData() throws SQLException {
        // Load invoices using the repository
        dataInvoice = new ArrayList<>(invoiceRepository.getAll());
        initializeTableColumns();
        populateTable();
    }

    private void initializeTableColumns() {
        sttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaypalId()));
        //amountColumn.setCellValueFactory(cellData -> new DoubleProperty(cellData.getValue().getAmount()).asObject());
        amountColumn.setCellValueFactory(cellData -> {
            //int multipliedValue = cellData.getValue().getAmount() * 1000;
            return new SimpleStringProperty(cellData.getValue().getAmount() + " đ");
        });

        statusColumn.setCellValueFactory(cellData -> {
            Button statusButton = createStatusButton(cellData.getValue());
            return new SimpleObjectProperty<>(statusButton);
        });

        statusColumn.setCellFactory(col -> new TableCell<>() {
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

        invoice_custom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
    }

    private Button createStatusButton(Invoice invoice) {
        Button statusButton = new Button(invoice.getStatus());

        if ("PAYMENT COMPLETED".equals(invoice.getStatus())) {
            statusButton.setOnAction(event -> {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem approveItem = new MenuItem("APPROVE");
                MenuItem rejectItem = new MenuItem("REJECT");

                approveItem.setOnAction(e -> {
                    updateInvoiceStatus(invoice, "APPROVED");
                    statusButton.setText("APPROVED");
                    refreshTable();
                });

                rejectItem.setOnAction(e -> {
                    updateInvoiceStatus(invoice, "REFUND");
                    statusButton.setText("REFUND");
                    refreshTable();
                });

                contextMenu.getItems().addAll(approveItem, rejectItem);
                contextMenu.show(statusButton, Side.RIGHT, 0, 0);
            });
        }

        return statusButton;
    }

    private void updateInvoiceStatus(Invoice invoice, String status) {
        try {
            // Use the repository to update the invoice status
            invoice.setStatus(status);
            invoiceRepository.updateStatus(invoice.getId(), status);
        } catch (SQLException e) {
            try {
                PopupScreen.error(e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void refreshTable() {
        tableView.refresh();
    }

    public void requestToInvoiceList(BaseScreenAdminHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Invoice List Screen");
        show();
    }

    private void populateTable() {
        ObservableList<Invoice> observableData = FXCollections.observableArrayList(dataInvoice);
        tableView.setItems(observableData);
    }
}
