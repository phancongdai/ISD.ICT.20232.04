package views.screen.payment;

import controller.PaymentController;
import entity.db.AIMSDB;
import entity.invoice.Invoice;
import entity.media.Media;
import entity.response.Response;
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
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentScreenHandler extends BaseScreenHandler {
	public Response response;

	@FXML
	private VBox vBox;

	private Invoice invoice;

	@FXML
	private ImageView back;

	private ResultScreenHandler resultScreenHandler;
	public PaymentScreenHandler(Stage stage, String screenPath, int amount, String contents) throws IOException {
		super(stage, screenPath);
	}

	private void displayWebView() {
		var paymentController = new PaymentController();
		var paymentUrl = paymentController.getUrlPay(invoice.getAmount(), "Thanh toan hoa don AIMS");
		System.out.println(paymentUrl);
		WebView paymentView = new WebView();
		WebEngine webEngine = paymentView.getEngine();
		System.out.println(paymentUrl);
		webEngine.load(paymentUrl);
		webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
			// Xử lý khi URL thay đổi
			System.out.println(newValue);
			//this.setResponse(handleUrlChanged(newValue));
			if(newValue.contains("http://127.0.0.1:50387/?")) {
				response = new Response(newValue);
				//System.out.println(invoice.getAmount());
				//System.out.println("Amount VNPay: "+ response.getVnp_Amount());
                if(response.getVnp_ResponseCode().equals("00")){
					try {
						paymentController.emptyCart();
						System.out.println("Successful Payment");
						BaseScreenHandler ResultScreenHandler = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, "PAYMENT RESULT", "SUCESSFULL!", String.valueOf(invoice.getId()), response.getVnp_BankCode(), response.getVnp_BankTranNo(), String.valueOf(invoice.getAmount()), response.getVnp_TransactionStatus(), response.getVnp_PayDate());
						ResultScreenHandler.setPreviousScreen(this);
						ResultScreenHandler.setScreenTitle("Result");
						ResultScreenHandler.show();
						this.invoice.setPaypalId(response.getVnp_BankTranNo());
						this.invoice.updateStatus("PAYMENT COMPLETED");
						UpdateStock();
					} catch (IOException e) {
						throw new RuntimeException(e);
					} catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
				if(!response.getVnp_ResponseCode().equals("00")){
					try {
						PopupScreen.error("VNPay response code: "+response.getVnp_ResponseCode()+"\nLet's check again!", 3);
						BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
						InvoiceScreenHandler.setScreenTitle("Invoice Screen");
						InvoiceScreenHandler.show();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}

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
//		resultScreen.setHomeScreenHandler(homeScreenHandler);
//		resultScreen.setScreenTitle("Result Screen");
//		resultScreen.show();
//	}

	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		back.setOnMouseClicked(e -> {
			setScreenTitle("Invoice screen");
			this.getPreviousScreen().show();
		});
		displayWebView();
	}

	public void UpdateStock() throws SQLException {
		int invoiceId = invoice.getId();
		Connection connection = AIMSDB.getConnection();
		//String sql0 = "Select orderId From Invoice where id = ?";
		int orderid;
		String sql0 = "SELECT orderId FROM Invoice WHERE id = ?";
		try (PreparedStatement preInvoice = connection.prepareStatement(sql0)) {
			preInvoice.setInt(1, invoiceId);
			try (ResultSet res0 = preInvoice.executeQuery()) {
				if (res0.next()) {
					orderid = res0.getInt(1);
				} else {
					throw new SQLException("Invoice with ID " + invoiceId + " not found.");
				}
			}
		} catch (SQLException e) {
			return;
		}

		String sql1 = "SELECT mediaID, quantity FROM OrderMedia WHERE orderID = ?";
		try (PreparedStatement preOrder = connection.prepareStatement(sql1)) {
			preOrder.setInt(1, orderid);
			try (ResultSet res1 = preOrder.executeQuery()) {
				while (res1.next()) {
					int mediaid = res1.getInt("mediaID");
					int quantity = res1.getInt("quantity");

					String sql2 = "UPDATE Media SET quantity = quantity - ? WHERE id = ?";
					try (PreparedStatement preMedia = connection.prepareStatement(sql2)) {
						preMedia.setInt(1, quantity);
						preMedia.setInt(2, mediaid);
						int rowAffected = preMedia.executeUpdate();
						if (rowAffected > 0) {
							System.out.println("Media with ID " + mediaid + " updated successfully");
						} else {
							System.out.println("No media with ID " + mediaid + " exists");
						}
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}


	}
}