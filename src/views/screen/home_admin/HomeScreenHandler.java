package views.screen.home_admin;

import controller.HomeController;
import entity.cart.Cart;
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
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class HomeScreenHandler extends BaseScreenHandler implements Initializable {

    public static Logger LOGGER = Utils.getLogger(HomeScreenHandler.class.getName());

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

    public static HomeScreenHandler _instance;

    public HomeScreenHandler(Stage stage, String screenPath) throws IOException {
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
            this.displayedItems = this.homeItems;
            addMediaHome(this.homeItems);
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
