package service;

import common.exception.MediaNotAvailableException;
import entity.order.Order;

import java.io.IOException;
import java.sql.SQLException;

public interface OrderService {
    Order createOrder() throws IOException, SQLException, MediaNotAvailableException;
}