package views.screen.home_admin;

import controller.AdminController;
import controller.InvoiceListController;
import entity.media.Media;
import entity.media.MediaRepository;
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
import views.screen.SessionManager;
import views.screen.home.HomeScreenHandler;
import views.screen.invoicelist.InvoiceListAdminHandler;
import views.screen.popup.PopupScreen;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class AdminHomeScreenHandler extends BaseScreenAdminHandler implements Initializable {

    public static Logger LOGGER = Utils.getLogger(AdminHomeScreenHandler.class.getName());
    @FXML
    private Button signOutBtn;
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

    public AdminHomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public AdminController getBController() {
        return (AdminController) super.getBController();
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
    @FXML
    public void handleSignOutButton(ActionEvent event) {
        SessionManager.setLoggedIn(false);
        HomeScreenHandler homeHandler;
        try {
            homeHandler = new HomeScreenHandler(stage, Configs.HOME_PATH);
            homeHandler.setScreenTitle("Home Screen Guest");
            homeHandler.setImage();
            homeHandler.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleAddItem(ActionEvent event) throws SQLException {
        // Title
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add New Item");
        titleDialog.setHeaderText("Enter the title for the new media item:");
        titleDialog.setContentText("Title:");

        Optional<String> titleResult = titleDialog.showAndWait();
        if (titleResult.isEmpty() || titleResult.get().isEmpty()) {
            return;
        }
        String title = titleResult.get();

        // Category
        TextInputDialog categoryDialog = new TextInputDialog();
        categoryDialog.setTitle("Add New Item");
        categoryDialog.setHeaderText("Enter the category for the new media item:");
        categoryDialog.setContentText("Category:");

        Optional<String> categoryResult = categoryDialog.showAndWait();
        if (categoryResult.isEmpty() || categoryResult.get().isEmpty()) {
            return;
        }
        String category = categoryResult.get();

        // Price
        TextInputDialog priceDialog = new TextInputDialog();
        priceDialog.setTitle("Add New Item");
        priceDialog.setHeaderText("Enter the price for the new media item:");
        priceDialog.setContentText("Price:");

        Optional<String> priceResult = priceDialog.showAndWait();
        if (priceResult.isEmpty() || priceResult.get().isEmpty()) {
            return;
        }
        int price;
        try {
            price = Integer.parseInt(priceResult.get());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for price: " + e.getMessage());
            return;
        }

        // Quantity
        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Add New Item");
        quantityDialog.setHeaderText("Enter the quantity for the new media item:");
        quantityDialog.setContentText("Quantity:");

        Optional<String> quantityResult = quantityDialog.showAndWait();
        if (quantityResult.isEmpty() || quantityResult.get().isEmpty()) {
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityResult.get());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for quantity: " + e.getMessage());
            return;
        }

        // Type of media
        TextInputDialog typeDialog = new TextInputDialog();
        typeDialog.setTitle("Add New Item");
        typeDialog.setHeaderText("Enter the type for the new media item:");
        typeDialog.setContentText("Type:");

        Optional<String> typeResult = typeDialog.showAndWait();
        if (typeResult.isEmpty() || typeResult.get().isEmpty()) {
            return;
        }
        String type = typeResult.get();

        // Call the controller
        getBController().addNewMedia(title, type, category, "assets/images/book/book2.jpg", price, quantity);

        // Reload after inserting
        refreshMediaList();
    }

    private List<MediaAdminHandler> updateMediaDisplay( List<MediaAdminHandler> items) {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        List<MediaAdminHandler> displayedItems = new ArrayList<>(items.subList(startIndex, endIndex));

        int totalPages = (int) Math.ceil((double) items.size() / itemsPerPage);
        int currentDisplayPage = currentPage + 1;
        currentPageLabel.setText("Page " + currentDisplayPage + " of " + totalPages);
        return displayedItems;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new AdminController(new MediaRepository()));
        try{
            List<Media> medium = getBController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Media media : medium) {
                MediaAdminHandler mediaAdminHandler = new MediaAdminHandler(Configs.HOME_MEDIA_ADMIN_PATH, media, this, new AdminController(new MediaRepository()));
                this.homeItems.add(mediaAdminHandler);
            }
            this.displayedItems = this.homeItems;
        }catch (SQLException | IOException e){
            LOGGER.info("Initialization errors: " + e.getMessage());
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

    public void refreshMediaList() {
//        setBController(new HomeController());
        try {
            List<Media> medium = getBController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Media media : medium) {
                MediaAdminHandler mediaAdminHandler = new MediaAdminHandler(Configs.HOME_MEDIA_ADMIN_PATH, media, this, new AdminController(new MediaRepository()));
                this.homeItems.add(mediaAdminHandler);
            }

            this.currentPage = 0;
            this.displayedItems = this.homeItems;
            List<MediaAdminHandler> displayedItems = updateMediaDisplay(this.homeItems);
            addMediaHome(displayedItems);
        } catch (SQLException | IOException e) {
            LOGGER.info("Refresh errors occurred: " + e.getMessage());
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
        if(!mediaItems.isEmpty()){
            hboxMedia.getChildren().forEach(node -> {
                int vid = hboxMedia.getChildren().indexOf(node);
                VBox vBox = (VBox) node;
                while(vBox.getChildren().size()<3 && !mediaItems.isEmpty()){
                    MediaAdminHandler media = (MediaAdminHandler) mediaItems.get(0);
                    vBox.getChildren().add(media.getContent());
                    mediaItems.remove(media);
                }
            });
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

            // filter only media with the chosen category
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

                    filteredItems.sort(Comparator.comparingDouble(
                            mediax -> mediax.getMedia().getPrice()));
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
        List<MediaAdminHandler> filteredItems = convertMediaHandlerList(filteredMedia);
        checkEmpty(filteredItems);
    }

    private void checkEmpty(List<MediaAdminHandler> filteredItems) {
        if (filteredItems.isEmpty()) {
            try {
                PopupScreen.error("No matching products.");
            } catch (IOException e) {
                System.out.println("Check empty error: " + e.getMessage());
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
        for (MediaAdminHandler mediaAdminHandler : items) {
            if (mediaAdminHandler.getMedia().getTitle().toLowerCase().contains(keyword)) {
                filteredItems.add(mediaAdminHandler);
            }
        }
        return filteredItems;
    }

    public List<MediaAdminHandler> convertMediaHandlerList(List<Media> items) throws IOException {
        List<MediaAdminHandler> mediaHandlerList = new ArrayList<>();
        for (Media media : items) {
            MediaAdminHandler m1 = new MediaAdminHandler(Configs.HOME_MEDIA_ADMIN_PATH, media, this, new AdminController(new MediaRepository()));

            mediaHandlerList.add(m1);
        }
        return mediaHandlerList;
    }
}
