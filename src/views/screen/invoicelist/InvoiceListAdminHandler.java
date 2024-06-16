package views.screen.invoicelist;

import controller.InvoiceListController;
import entity.invoice.Invoice;
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
    private TableColumn<Invoice, Integer> amountColumn;

    @FXML
    private TableColumn<Invoice, Button> statusColumn;

    @FXML
    private TableColumn<Invoice, String> invoice_custom;

    public InvoiceListAdminHandler(Stage stage, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);

        loadData();

        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);
        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> adminHomeScreenHandler.show());
    }

    private void initializeTableColumns() {
        // Map TableColumns to corresponding fields in Invoice class
        sttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaypalId()));
        amountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject());
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

        if ("CREATED".equals(invoice.getStatus())) {
            statusButton.setOnAction(event -> {
                // Create a context menu with options
                ContextMenu contextMenu = new ContextMenu();
                MenuItem approveItem = new MenuItem("APPROVE");
                MenuItem rejectItem = new MenuItem("REJECT");

                approveItem.setOnAction(e -> {
                    invoice.setStatus("PAYMENT COMPLETED");
                    updateInvoiceStatus(invoice, "PAYMENT COMPLETED");
                    statusButton.setText("PAYMENT COMPLETED");
                    refreshTable();
                });

                rejectItem.setOnAction(e -> {
                    invoice.setStatus("REFUND");
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
            // Update the status in the database
            Invoice.updateStatus(invoice.getId(), status);
        } catch (SQLException e) {
            try {
                PopupScreen.error(e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void refreshTable() {
        ObservableList<Invoice> observableData = FXCollections.observableArrayList(dataInvoice);
        tableView.setItems(observableData);
        tableView.refresh();
    }

    public InvoiceListController getBController() {
        return (InvoiceListController) super.getBController();
    }

    public void requestToInvoiceList(BaseScreenAdminHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Invoice List Screen");
        show();
    }

    private void loadData() throws SQLException {
        dataInvoice = Invoice.getListInvoice();
        initializeTableColumns();
        populateTable();
    }

    private void populateTable() {
        ObservableList<Invoice> observableData = FXCollections.observableArrayList(dataInvoice);
        tableView.setItems(observableData);
    }
}
