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
	public PaymentController() {
		vnPayService = new VnPaySubsystem();
	}
	public String getUrlPay(int amount, String content) {
		var url = vnPayService.generatePayUrl(amount, content);
		return url;
	}

	public void emptyCart(){
		//Content Coupling
		Cart.getCart().emptyCart();
	}
}