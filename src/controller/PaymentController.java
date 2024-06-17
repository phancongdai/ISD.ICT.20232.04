package controller;

import entity.cart.Cart;
import subsystem.VnPayInterface;
import subsystem.VnPaySubsystem;


/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 *
 * @author hieud
 *
 */
public class PaymentController extends BaseController {

	/**
	 * Represent the Interbank subsystem
	 *
	 */
	private VnPayInterface vnPayService;
	//private InterbankInterface interbank;
	public PaymentController() {
		vnPayService = new VnPaySubsystem();
	}
//	public PaymentController(InterbankInterface interbank){
//		this.interbank = interbank;
//	}

//	public Map<String, String> payOrder(Invoice invoice, String contents) {
//		Map<String, String> result = new Hashtable<String, String>();
//		result.put("RESULT", "PAYMENT FAILED!");
//		try {
//			PaymentTransaction transaction = interbank.paypalPayOrder(invoice,contents);
//
//			result.put("RESULT", "PAYMENT SUCCESSFUL!");
//			result.put("MESSAGE", "You have succesffully paid the order!");
//		} catch (PaymentException | UnrecognizedException | IOException ex) {
//			result.put("MESSAGE", ex.getMessage());
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//		return result;
//	}

//	public void refundOrder(Invoice invoice) throws IOException, SQLException {
//		try {
//			interbank.refundOrder(invoice);
//		}catch (PaymentException e){
//			throw e;
//		}
//	}
	public String getUrlPay(int amount, String content) {
		var url = vnPayService.generatePayUrl(amount, content);
		return url;
	}

	public void emptyCart(){
		//Content Coupling
		Cart.getCart().emptyCart();
	}
}