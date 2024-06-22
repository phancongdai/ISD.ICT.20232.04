package controller;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.invoice.InvoiceRepository;
import entity.invoice.SQLInvoiceRepository;
import entity.order.Order;
import entity.order.OrderMedia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class PlaceOrderController extends BaseController{

    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    // Declare the InvoiceRepository
    private InvoiceRepository invoiceRepository;

    // Constructor to initialize the repository
    public PlaceOrderController() {
        this.invoiceRepository = new SQLInvoiceRepository(); // Use dependency injection or initialize here
    }

    public void placeOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    public Order createOrder() throws SQLException {
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                    cartMedia.getQuantity(),
                    cartMedia.getPrice());
            order.addOrderMedia(orderMedia);
        }
        return order;
    }

    public Invoice createInvoice(Order order) throws SQLException {
        Invoice invoice = new Invoice(order);
        // Save the invoice using the repository
        invoiceRepository.save(invoice);
        return invoice;
    }

    public void processDeliveryInfo(HashMap<String, String> info) throws InterruptedException {
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        // Validate delivery info using DeliveryValidator
        DeliveryValidator.validateDeliveryInfo(info);
    }

    public int calculateShippingFee(Order order){
        HashMap<String, String> deliveryInfo = order.getDeliveryInfo();
        String province = "";
        if(deliveryInfo.get("province") != null) province = deliveryInfo.get("province");
        int fees = 25;
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
