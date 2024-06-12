package subsystem;

//import entity.payment.CreditCard;

import entity.payment.PaymentTransaction;
import entity.response.Response;
import subsystem.vnPay.VnPaySubsystemController;

import java.io.IOException;
import java.text.ParseException;

public class VnPaySubsystem implements VnPayInterface {
    private VnPaySubsystemController ctrl;

    public VnPaySubsystem() {
        this.ctrl = new VnPaySubsystemController();
    }

    public String generatePayUrl(int amount, String contents) {

        try {
            return ctrl.generatePayOrderUrl(amount, contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentTransaction makePaymentTransaction(Response response) throws ParseException {
            return ctrl.makePaymentTransaction(response);
    }
}
