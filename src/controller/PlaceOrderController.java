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
        double tmpweight = 0;
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity(), cartMedia.getPrice());
            orderMedia.setWeight(cartMedia.getMedia().getWeight());
            //System.out.println("weight: "+ orderMedia.getWeight() + " and quanity: "+ orderMedia.getQuantity());
            tmpweight = tmpweight + orderMedia.getWeight() * orderMedia.getQuantity();
            //System.out.println("Tmp weight: " + tmpweight);
            order.setTotalweight(tmpweight);
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
    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order){
        HashMap<String, String> deliveryInfo = order.getDeliveryInfo();
        String province = "";
        //if(deliveryInfo.get("province") != null) province = deliveryInfo.get("province");
        int maxfees = 25000;
        int fees = 0;
        System.out.println(order.toString() + " Order's weight: "+ order.getTotalweight());
        if (order.getTypePayment().equals("standard order")) {
            if(order.getAmount()<100000) {
                province = String.valueOf(order.getDeliveryInfo().get("province")).toLowerCase();
                if(province.equals("hà nội")||province.equals("hồ chí minh")) {
                    if(order.getTotalweight()>3) {
                        fees += 22000*3 + (order.getTotalweight()-3)*24500;
                    } else fees = (int)(22000* order.getTotalweight());
                } else {
                    if(order.getTotalweight()>0.5) {
                        fees += 30000*0.5 + 32500 *(order.getTotalweight()-0.5);
                    } else fees = (int)(30 * order.getTotalweight());
                }
                if(fees>25000) fees = 25000;
            } else fees = 0;
        }
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
