package subsystem;

import common.exception.PaymentException;
import common.exception.UnrecognizedException;
import entity.payment.PaymentTransaction;
import entity.response.Response;

import java.text.ParseException;

public interface VnPayInterface {

    /**
     * Pay order, and then return the payment transaction.
     */
    public abstract String generatePayUrl(int amount, String contents) throws PaymentException, UnrecognizedException;

    public PaymentTransaction makePaymentTransaction(Response response) throws ParseException;
}
