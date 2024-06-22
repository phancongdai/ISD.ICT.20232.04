package controller;

import entity.cart.Cart;
import subsystem.VnPayInterface;
import subsystem.VnPaySubsystem;

public class PaymentController extends BaseController {
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