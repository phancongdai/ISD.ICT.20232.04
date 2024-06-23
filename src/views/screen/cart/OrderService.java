package views.screen.cart;


import common.exception.MediaNotAvailableException;
import entity.order.Order;
import controller.PlaceOrderController;
import views.screen.popup.PopupScreen;

import java.io.IOException;
import java.sql.SQLException;

public class OrderService {

    private final PlaceOrderController placeOrderController;

    public OrderService(PlaceOrderController placeOrderController) {
        this.placeOrderController = placeOrderController;
    }

    public Order createOrder(String orderType) throws SQLException, MediaNotAvailableException, IOException {
        if (placeOrderController.getListCartMedia().isEmpty()) {
            PopupScreen.error("You don't have anything to place");
            throw new MediaNotAvailableException("You don't have anything to place");
        }

        if (!placeOrderController.checkRushAvailability().isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (String title : placeOrderController.checkRushAvailability()){
                message.append(title).append(", ");
            }
            PopupScreen.error(message.append("not available for rush order").toString());
            throw new MediaNotAvailableException("Some of the media is not available for Rush order");
        }

        placeOrderController.checkAvailability();

        Order order = placeOrderController.createOrder();
        order.setTypePayment(orderType);
        return order;
    }
}