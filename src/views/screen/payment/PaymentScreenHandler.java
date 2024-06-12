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
import views.screen.BaseScreenHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PaymentScreenHandler extends BaseScreenHandler {
	String Response;
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

	public void setResponse(String response) {
		this.Response  = response;
	}

	public String getResponse() {
		return Response;
	}
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
			//System.out.println(newValue);
			this.setResponse(handleUrlChanged(newValue));
		});
		vBox.getChildren().clear();
		vBox.getChildren().add(paymentView);
	}
	private String handleUrlChanged(String newValue) {
		//http://127.0.0.1:50387/?vnp_Amount=6000000&vnp_BankCode=NCB&vnp_BankTranNo=VNP14455363&vnp_CardType=ATM&vnp_OrderInfo=Thanh+toan+hoa+don+AIMS&vnp_PayDate=20240612111510&vnp_ResponseCode=00&vnp_TmnCode=TXOOZNX4&vnp_TransactionNo=14455363&vnp_TransactionStatus=00&vnp_TxnRef=37159084&vnp_SecureHash=1453893fe9acaf82ddcd8b5d3b0fa67882f3b97cbdfb4d2ba65da6017d883aef5f2878218876642182ddc397320d69204dec08b692dbe0515655592a1c1f4340

		if (newValue.contains("http://127.0.0.1:50387/?")) {
			try {
				System.out.println(newValue);
				return newValue;
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		return "Errors emerge";
	}



//	void confirmToPayOrder() throws IOException{
//		String contents = "pay order";
//		PaymentController ctrl = (PaymentController) getBController();
//		Map<String, String> response = ctrl.payOrder(invoice, contents);
//		BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, response.get("RESULT"), response.get("MESSAGE") );
//		resultScreen.setPreviousScreen(this);
//		resultScreen.setHomeScreenHandler(homeScreenHandler);
//		resultScreen.setScreenTitle("Result Screen");
//		resultScreen.show();
//	}
	public Map<String, String> parseQueryParameters(String query) {
		Map<String, String> paramMap = new HashMap<>();

		if (query != null) {
			String[] pairs = query.split("&");
			for (String pair : pairs) {
				String[] keyValue = pair.split("=");
				if (keyValue.length == 2) {
					String key = keyValue[0];
					String value = keyValue[1];
					paramMap.put(key, value);
				}
			}
		}
		return paramMap;
	}

	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		back.setOnMouseClicked(e -> {
			this.getPreviousScreen().show();
		});
		displayWebView();
		Map<String, String> response = parseQueryParameters(this.getResponse());
		response.forEach((key,value)->System.out.println(key + " :" +value));
	}
}