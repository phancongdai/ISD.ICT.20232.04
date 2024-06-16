package views.screen.home_admin;

import controller.AdminCRUDController;
import controller.HomeController;
import controller.InvoiceListController;
import entity.db.AIMSDB;
import entity.media.Media;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenAdminHandler;
import views.screen.invoicelist.InvoiceListAdminHandler;
import views.screen.popup.PopupScreen;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class AdminHomeScreenHandler extends BaseScreenAdminHandler implements Initializable {

    public static Logger LOGGER = Utils.getLogger(AdminHomeScreenHandler.class.getName());

    @FXML
    private ImageView aimsImage;

    @FXML
    private Label currentPageLabel;

    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private HBox hboxMedia;

    @FXML
    private TextField searchField;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;
    @FXML
    private Button manageInvoiceBtn;
    @FXML
    private Button addItemBtn;

    private List homeItems;

    private List displayedItems;

    public static AdminHomeScreenHandler _instance;
    private Connection connect;
    private PreparedStatement prepare;

    public AdminHomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    private int currentPage = 0;
    private final int itemsPerPage = 12;

    @FXML
    private void showNextMedia(MouseEvent event) {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, displayedItems.size());

        if (endIndex < displayedItems.size()) {
            currentPage++;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.displayedItems);
            addMediaHome(displayedItems);
        }
    }

    @FXML
    private void showPreviousMedia(MouseEvent event) {
        if (currentPage > 0) {
            currentPage--;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.displayedItems);
            addMediaHome(displayedItems);
        }
    }

    //Hàm này được dùng để thêm item
    @FXML
    private void handleAddItem(ActionEvent event) throws SQLException {
        // Logic xử lý khi nút addItemBtn được nhấn
        // Nhập tiêu đề (title)
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add New Item");
        titleDialog.setHeaderText("Enter the title for the new media item:");
        titleDialog.setContentText("Title:");

        Optional<String> titleResult = titleDialog.showAndWait();
        if (!titleResult.isPresent() || titleResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        String title = titleResult.get();

        // Nhập danh mục (category)
        TextInputDialog categoryDialog = new TextInputDialog();
        categoryDialog.setTitle("Add New Item");
        categoryDialog.setHeaderText("Enter the category for the new media item:");
        categoryDialog.setContentText("Category:");

        Optional<String> categoryResult = categoryDialog.showAndWait();
        if (!categoryResult.isPresent() || categoryResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        String category = categoryResult.get();

        // Nhập giá trị (value)
        TextInputDialog valueDialog = new TextInputDialog();
        valueDialog.setTitle("Add New Item");
        valueDialog.setHeaderText("Enter the value for the new media item:");
        valueDialog.setContentText("Value:");

        Optional<String> valueResult = valueDialog.showAndWait();
        if (!valueResult.isPresent() || valueResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        int value;
        try {
            value = Integer.parseInt(valueResult.get());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for value: " + e.getMessage());
            return;
        }

        // Nhập giá (price)
        TextInputDialog priceDialog = new TextInputDialog();
        priceDialog.setTitle("Add New Item");
        priceDialog.setHeaderText("Enter the price for the new media item:");
        priceDialog.setContentText("Price:");

        Optional<String> priceResult = priceDialog.showAndWait();
        if (!priceResult.isPresent() || priceResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        int price;
        try {
            price = Integer.parseInt(priceResult.get());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for price: " + e.getMessage());
            return;
        }

        // Nhập số lượng (quantity)
        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Add New Item");
        quantityDialog.setHeaderText("Enter the quantity for the new media item:");
        quantityDialog.setContentText("Quantity:");

        Optional<String> quantityResult = quantityDialog.showAndWait();
        if (!quantityResult.isPresent() || quantityResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityResult.get());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for quantity: " + e.getMessage());
            return;
        }

        // Nhập loại (type)
        TextInputDialog typeDialog = new TextInputDialog();
        typeDialog.setTitle("Add New Item");
        typeDialog.setHeaderText("Enter the type for the new media item:");
        typeDialog.setContentText("Type:");

        Optional<String> typeResult = typeDialog.showAndWait();
        if (!typeResult.isPresent() || typeResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        String type = typeResult.get();

        AdminCRUDController controller = new AdminCRUDController();
        controller.addNewMedia(title, type, category, "assets/images/book/book2.jpg", price, quantity);

        refreshMediaList();
    }

    private List<MediaAdminHandler> updateMediaDisplay( List Items) {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, Items.size());
        List<MediaAdminHandler> displayedItems = new ArrayList<>(Items.subList(startIndex, endIndex));

        int totalPages = (int) Math.ceil((double) Items.size() / itemsPerPage);
        int currentDisplayPage = currentPage + 1;
        currentPageLabel.setText("Page " + currentDisplayPage + " of " + totalPages);
        return displayedItems;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new HomeController());
        try{
            List medium = getBController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media)object;
                MediaAdminHandler m1 = new MediaAdminHandler(Configs.HOME_MEDIA_ADMIN_PATH, media, this);
                this.homeItems.add(m1);
            }
            this.displayedItems = this.homeItems;
        }catch (SQLException | IOException e){
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }

        // Home button
        aimsImage.setOnMouseClicked(e -> {
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.homeItems);
            addMediaHome(displayedItems);
        });

        manageInvoiceBtn.setOnMouseClicked(e -> {
            InvoiceListAdminHandler invoiceListAdminHandler;
            try {
                invoiceListAdminHandler = new InvoiceListAdminHandler(this.stage, Configs.INVOICE_LIST_ADMIN_PATH);
                invoiceListAdminHandler.setHomeScreenHandler(this);
                invoiceListAdminHandler.setBController(new InvoiceListController());
                invoiceListAdminHandler.requestToInvoiceList(this);
            } catch (IOException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        addMediaHome(this.homeItems);
        addMenuItem(0, "Book", splitMenuBtnSearch);
        addMenuItem(1, "DVD", splitMenuBtnSearch);
        addMenuItem(2, "CD", splitMenuBtnSearch);
    }

    //hàm này dùng để load lại giao diện
    public void refreshMediaList() {
        setBController(new HomeController());
        try {
            List medium = getBController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media) object;
                MediaAdminHandler m1 = new MediaAdminHandler(Configs.HOME_MEDIA_ADMIN_PATH, media, this);
                this.homeItems.add(m1);
            }

            this.currentPage = 0;
            this.displayedItems = this.homeItems;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.homeItems);
            addMediaHome(displayedItems);
        } catch (SQLException | IOException e) {
            LOGGER.info("Errors occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setImage(){
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);
    }

    public void addMediaHome(List items){
        ArrayList mediaItems = (ArrayList)((ArrayList) items).clone();
        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });
        while(!mediaItems.isEmpty()){
            hboxMedia.getChildren().forEach(node -> {
                int vid = hboxMedia.getChildren().indexOf(node);
                VBox vBox = (VBox) node;
                while(vBox.getChildren().size()<3 && !mediaItems.isEmpty()){
                    MediaAdminHandler media = (MediaAdminHandler) mediaItems.get(0);
                    vBox.getChildren().add(media.getContent());
                    mediaItems.remove(media);
                }
            });
            return;
        }
    }

    public void addMenuItem(int position, String text, MenuButton menuButton){
        // Create a menu item
        MenuItem menuItem = new MenuItem();
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(31));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);

        // Set action
        menuItem.setOnAction(e -> {
            // empty home media
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                vBox.getChildren().clear();
            });

            // filter only media with the choosen category
            List<MediaAdminHandler> filteredItems = new ArrayList<>();
            homeItems.forEach(me -> {
                MediaAdminHandler media = (MediaAdminHandler) me;
                if (media.getMedia().getTitle().toLowerCase().startsWith(text.toLowerCase())){
                    filteredItems.add(media);
                }else{
                    if (text.equals("<20đ")) {
                        if (media.getMedia().getPrice() < 20) {
                            filteredItems.add(media);
                        }

                    } else if (text.equals("20đ-50đ")) {
                        if (media.getMedia().getPrice() >= 20 && media.getMedia().getPrice() < 50) {
                            filteredItems.add(media);
                        }
                    } else if (text.equals("50đ-100đ")) {
                        if (media.getMedia().getPrice() >= 50 && media.getMedia().getPrice() <= 100) {
                            filteredItems.add(media);
                        }
                    }
                    else if (text.equals(">100đ")) {
                        if (media.getMedia().getPrice() > 100) {
                            filteredItems.add(media);
                        }
                    }

                    Collections.sort(filteredItems, Comparator.comparingDouble(
                            mediax -> ((MediaAdminHandler) mediax).getMedia().getPrice()));
                }

            });
            checkEmpty(filteredItems);
        });

        // Add to button
        menuButton.getItems().add(position, menuItem);
    }

    @FXML
    void searchButtonClicked(MouseEvent event) throws SQLException, IOException {
        String searchText = searchField.getText().toLowerCase().trim();
        List<Media> medium = getBController().getAllMedia();
        List<Media> filteredMedia = getBController().filterMediaByKeyWord(searchText,medium);
//        List<MediaAdminHandler> filteredItems = filterMediaByKeyWord(searchText, homeItems);
        List<MediaAdminHandler> filteredItems = convertMediaHandlerList(filteredMedia);
        checkEmpty(filteredItems);
    }

    private void checkEmpty(List<MediaAdminHandler> filteredItems) {
        if (filteredItems.isEmpty()) {
            try {
                PopupScreen.error("No matching products.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            currentPage = 0;
            this.displayedItems = filteredItems;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(filteredItems);
            addMediaHome(displayedItems);
        }
    }

    public List<MediaAdminHandler> filterMediaByKeyWord(String keyword, List<MediaAdminHandler> items) {
        List<MediaAdminHandler> filteredItems = new ArrayList<>();
        for (Object item : items) {
            MediaAdminHandler media = (MediaAdminHandler) item;
            if (media.getMedia().getTitle().toLowerCase().contains(keyword)) {
                filteredItems.add(media);
            }
        }
        return filteredItems;
    }

    public List<MediaAdminHandler> convertMediaHandlerList(List<Media> items) throws SQLException, IOException {
        List<MediaAdminHandler> mediaHandlerList = new ArrayList<>();
        for (Object item : items) {
            Media media = (Media) item ;
            MediaAdminHandler m1 = new MediaAdminHandler(Configs.HOME_MEDIA_ADMIN_PATH, media, this);
            mediaHandlerList.add(m1);
        }
        return mediaHandlerList;
    }
}
