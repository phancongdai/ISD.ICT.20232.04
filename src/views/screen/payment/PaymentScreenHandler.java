package views.screen.payment;

import controller.PaymentController;
import entity.invoice.Invoice;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import entity.response.Response;
import java.io.IOException;

public class PaymentScreenHandler extends BaseScreenHandler {
	public Response response;
	@FXML
	private Button btnConfirmPayment;

	@FXML
	private ImageView loadingImage;

	@FXML
	private Label paymentLink;

	@FXML
	private Button btnGoToLink;

	@FXML
	private VBox vBox;

	private Invoice invoice;

	@FXML
	private ImageView back;

	private ResultScreenHandler resultScreenHandler;

//	public void setResponse(String response) {
//		this.Response  = response;
//	}
//
//	public String getResponse() {
//		return Response;
//	}
	public PaymentScreenHandler(Stage stage, String screenPath, int amount, String contents) throws IOException {
		super(stage, screenPath);
	}

	// Data Coupling
//	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
//		super(stage, screenPath);
//		this.invoice = invoice;
//		String url = "https://www.sandbox.paypal.com/checkoutnow?token=" + this.invoice.getPaypalId();
//		paymentLink.setText(url);
//
//		btnConfirmPayment.setOnMouseClicked(e -> {
//			try {
//				confirmToPayOrder();
//				//Content Coupling
//				((PaymentController) getBController()).emptyCart();
//			} catch (Exception exp) {
//				System.out.println(exp.getStackTrace());
//			}
//		});
//
//		btnGoToLink.setOnMouseClicked(e -> {
//			try {
//				Desktop.getDesktop().browse(new URI(url));
//			} catch (Exception exp) {
//				System.out.println(exp.getStackTrace());
//			}
//		});
//	}



	private void displayWebView() {
		var paymentController = new PaymentController();
		var paymentUrl = paymentController.getUrlPay(invoice.getAmount(), "Thanh toan hoa don AIMS");
		System.out.println(paymentUrl);
		WebView paymentView = new WebView();
		WebEngine webEngine = paymentView.getEngine();
		webEngine.load(paymentUrl);
		webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
			// Xử lý khi URL thay đổi
			System.out.println(newValue);
			//this.setResponse(handleUrlChanged(newValue));
			if(newValue.contains("http://127.0.0.1:50387/?")) {
				response = new Response(newValue);
                try {
					//System.out.println("check");
                    BaseScreenHandler ResultScreenHandler = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, "PAYMENT RESULT", "SUCESSFULL!");
					ResultScreenHandler.setPreviousScreen(this);
					//ResultScreenHandler.setHomeScreenHandler(adminHomeScreenHandler);
					ResultScreenHandler.setScreenTitle("Result");
					ResultScreenHandler.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
		});
		vBox.getChildren().clear();
		vBox.getChildren().add(paymentView);
	}

//	void confirmToPayOrder() throws IOException{
//		String contents = "pay order";
//		PaymentController ctrl = (PaymentController) getBController();
//		Map<String, String> response = ctrl.payOrder(invoice, contents);
//		BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, response.get("RESULT"), response.get("MESSAGE") );
//		resultScreen.setPreviousScreen(this);
//		resultScreen.setHomeScreenHandler(adminHomeScreenHandler);
//		resultScreen.setScreenTitle("Result Screen");
//		resultScreen.show();
//	}

	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		back.setOnMouseClicked(e -> {
			this.getPreviousScreen().show();
		});
		displayWebView();
		//Map<String, String> response = parseQueryParameters(this.getResponse());
		//response.forEach((key,value)->System.out.println(key + " :" +value));
	}
}