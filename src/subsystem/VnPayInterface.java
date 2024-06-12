package subsystem;

import common.exception.PaymentException;
import common.exception.UnrecognizedException;
import entity.payment.PaymentTransaction;
import entity.response.Response;

import java.text.ParseException;

/**
 * The {@code InterbankInterface} class is used to communicate with the
 * {@link VnPaySubsystem InterbankSubsystem} to make transaction.
 *
 * @author hieud
 */
public interface VnPayInterface {

    /**
     * Pay order, and then return the payment transaction.
     */
    public abstract String generatePayUrl(int amount, String contents) throws PaymentException, UnrecognizedException;

    public PaymentTransaction makePaymentTransaction(Response response) throws ParseException;
}
