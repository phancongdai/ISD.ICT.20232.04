package service;

import entity.order.Order;
import controller.PlaceOrderController;
import common.exception.MediaNotAvailableException;
import views.screen.popup.PopupScreen;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RushOrderService implements OrderService {
    private final PlaceOrderController placeOrderController;

    public RushOrderService(PlaceOrderController placeOrderController) {
        this.placeOrderController = placeOrderController;
    }

    @Override
    public Order createOrder() throws IOException, SQLException, MediaNotAvailableException {
        validateCart();
        checkRushAvailability();
        placeOrderController.checkAvailability();
        return createRushOrder();
    }

    private void validateCart() throws MediaNotAvailableException, IOException {
        if (placeOrderController.getListCartMedia().isEmpty()) {
            PopupScreen.error("You don't have anything to place");
            throw new MediaNotAvailableException("You don't have anything to place");
        }
    }

    private void checkRushAvailability() throws MediaNotAvailableException, IOException {
        List<String> unavailableList = placeOrderController.checkRushAvailability();
        if (!unavailableList.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (String title : unavailableList) {
                message.append(title).append(", ");
            }
            PopupScreen.error(message.append("not available for rush order").toString());
            throw new MediaNotAvailableException("Some of the media is not available for Rush order");
        }
    }

    private Order createRushOrder() throws SQLException {
        Order order = placeOrderController.createOrder();
        order.setTypePayment("rush order");
        return order;
    }
}
