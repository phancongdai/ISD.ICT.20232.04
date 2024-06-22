package controller;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        double tmpweight = 0;
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity(), cartMedia.getPrice());
            orderMedia.setWeight(cartMedia.getMedia().getWeight());
            System.out.println("weight: "+ orderMedia.getWeight() + " and quanity: "+ orderMedia.getQuantity());
            tmpweight = tmpweight + orderMedia.getWeight() * orderMedia.getQuantity();
            System.out.println("Tmp weight: " + tmpweight);
            order.setTotalweight(tmpweight);
            order.addOrderMedia(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) throws SQLException {
        //this.interbankInterface = new InterbankSubsystem();
        //String id = this.interbankInterface.getUrlPayOrder(order.getAmount() + calculateShippingFee(order));
        Invoice invoice = new Invoice(order);
        invoice.saveInvoice();
        return invoice;
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     */
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
    public double calculateShippingFee(Order order){
        HashMap<String, String> deliveryInfo = order.getDeliveryInfo();
        String province = "";
        //if(deliveryInfo.get("province") != null) province = deliveryInfo.get("province");
        double maxfees = 25;
        double fees = 0;
        System.out.println(order.toString() + " Order's weight: "+ order.getTotalweight());
        if (order.getTypePayment().equals("standard order")) {
            if(order.getAmount()<100) {
                province = String.valueOf(order.getDeliveryInfo().get("province")).toLowerCase();
                if(province.equals("hà nội")||province.equals("hồ chí minh")) {
                    if(order.getTotalweight()>3) {
                        fees += 22*3 + (order.getTotalweight()-3)*24.5;
                    } else fees = (22* order.getTotalweight());
                } else {
                    if(order.getTotalweight()>0.5) {
                        fees += 30*0.5 + 32.5 *(order.getTotalweight()-0.5);
                    } else fees = 30 * order.getTotalweight();
                }
                if(fees>25) fees = 25;
            } else fees = 0;
        }
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
