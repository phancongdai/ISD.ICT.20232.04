package views.screen.cart;

import common.exception.MediaNotAvailableException;
import common.exception.PlaceOrderException;
import controller.PlaceOrderController;
import controller.ViewCartController;
import entity.cart.CartMedia;
import entity.order.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.shipping.ShippingScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class CartScreenHandler extends BaseScreenHandler {

	private static final Logger LOGGER = Utils.getLogger(CartScreenHandler.class.getName());

	@FXML
	private ImageView aimsImage;

	@FXML
	private Label pageTitle;

	@FXML
	VBox vboxCart;

	@FXML
	private Label shippingFees;

	@FXML
	private Label labelAmount;

	@FXML
	private Label labelSubtotal;

	@FXML
	private Label labelVAT;

	@FXML
	private Button btnPlaceOrder;

	@FXML
	private Button btnPlaceRushOrder;

	private final PlaceOrderController placeOrderController;

	public CartScreenHandler(Stage stage, String screenPath, PlaceOrderController placeOrderController) throws IOException {
		super(stage, screenPath);
		this.placeOrderController = placeOrderController;

		// fix relative image path caused by fxml
		File file = new File("assets/images/Logo.png");
		Image im = new Image(file.toURI().toString());
		aimsImage.setImage(im);
		setHomeScreenHandler(new HomeScreenHandler(this.stage, Configs.HOME_PATH));
		// on mouse clicked, we back to home
		aimsImage.setOnMouseClicked(e -> {
			setScreenTitle("Home Screen");
			homeScreenHandler.show();

		});
		// on mouse clicked, we start processing place order usecase
		btnPlaceOrder.setOnMouseClicked(e -> {
			LOGGER.info("Place Order button clicked");
			try {
				requestToPlaceOrder();
			} catch (SQLException | IOException exp) {
				LOGGER.severe("Cannot place the order, see the logs");
				exp.printStackTrace();
				throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
			}
		});
		btnPlaceRushOrder.setOnMouseClicked(e -> {
			LOGGER.info("Place Rush Order button clicked");
			try {
				requestToPlaceRushOrder();
			} catch (SQLException | IOException exp) {
				LOGGER.severe("Cannot place the order, see the logs");
				exp.printStackTrace();
				throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
			}
		});
	}

	public Label getLabelAmount() {
		return labelAmount;
	}

	public Label getLabelSubtotal() {
		return labelSubtotal;
	}

	public ViewCartController getBController(){
		return (ViewCartController) super.getBController();
	}

	public void requestToViewCart(BaseScreenHandler prevScreen) throws SQLException {
		setPreviousScreen(prevScreen);
		setScreenTitle("Cart Screen");
		//System.out.println(getBController().toString());
//		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
		show();
	}

	public void requestToPlaceOrder() throws SQLException, IOException {
		try {
			// create placeOrderController and process the order
			PlaceOrderController placeOrderController = new PlaceOrderController();
			if (placeOrderController.getListCartMedia().isEmpty()){
				PopupScreen.error("You don't have anything to place");
				return;
			}
			placeOrderController.checkAvailability();
			// display available media
			displayCartWithMediaAvailability();
			// create order
			Order order = placeOrderController.createOrder();
			//System.out.println(order.getID() + " " + order.getAmount() + " " +order.getlstOrderMedia());
			order.setTypePayment("standard order");
			System.out.println(order.toString());
			// display shipping form
			ShippingScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, order);
			ShippingScreenHandler.setScreenTitle("Shipping Screen");
			ShippingScreenHandler.setPreviousScreen(this);
			ShippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
			File file = new File("assets/images/Logo.png");
			Image im = new Image(file.toURI().toString());
			aimsImage.setImage(im);
			// on mouse clicked, we back to home
			aimsImage.setOnMouseClicked(e -> {
				setScreenTitle("Home screen");
                this.getPreviousScreen().show();
			});
			ShippingScreenHandler.setBController(placeOrderController);
			ShippingScreenHandler.show();
		} catch (MediaNotAvailableException e) {
			// if some media are not available then display cart and break usecase Place Order
			displayCartWithMediaAvailability();
		}

	}

	public void requestToPlaceRushOrder() throws SQLException, IOException {
		try {
			Order order = placeOrderController.createRushOrder();
			displayShippingScreen(order);
		} catch (MediaNotAvailableException e) {
			displayCartWithMediaAvailability();
		}
	}

	private void displayShippingScreen(Order order) throws IOException {
		ShippingScreenHandler shippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, order);
		shippingScreenHandler.setScreenTitle("Shipping Screen");
		shippingScreenHandler.setPreviousScreen(this);
		shippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
		File file = new File("assets/images/Logo.png");
		Image im = new Image(file.toURI().toString());
		aimsImage.setImage(im);

		aimsImage.setOnMouseClicked(e -> {
			setScreenTitle("Home screen");
			this.getPreviousScreen().show();
		});

		shippingScreenHandler.setBController(placeOrderController);
		shippingScreenHandler.show();
	}

	public void updateCart() throws SQLException{
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
	}

	void updateCartAmount(){
		// calculate subtotal and amount
		int subtotal = getBController().getCartSubtotal();
		int vat = (int)((Configs.PERCENT_VAT/100)*subtotal);
		int amount = subtotal + vat;
		LOGGER.info("amount " + amount);
		// update subtotal and amount of Cart
		labelSubtotal.setText(Utils.getCurrencyFormat(subtotal*1000));
		labelVAT.setText(Utils.getCurrencyFormat(vat*1000));
		labelAmount.setText(Utils.getCurrencyFormat(amount*1000));
	}
	
	private void displayCartWithMediaAvailability(){
		// clear all old cartMedia
		vboxCart.getChildren().clear();
		// get list media of cart after check availability
		List lstMedia = getBController().getListCartMedia();

		try {
			for (Object cm : lstMedia) {
				// display the attribute of vboxCart media
				CartMedia cartMedia = (CartMedia) cm;
				MediaHandler mediaCartScreen = new MediaHandler(Configs.CART_MEDIA_PATH, this);
				mediaCartScreen.setCartMedia(cartMedia);
				// add spinner
				vboxCart.getChildren().add(mediaCartScreen.getContent());
			}
			// calculate subtotal and amount
			updateCartAmount();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
