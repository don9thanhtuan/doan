package com.nhom4.bookstore.repositories;

import java.util.List;

import com.nhom4.bookstore.entity.Order;
import com.nhom4.bookstore.entity.OrderDetail;

public interface OrderDAOController {
    List<Order> getOrderList();
    Order getOrder(String id);
    OrderDetail getOrderDetail(String id);
    void editStatusOrder(String currentID, int newStatus);
    List<Order> search(String tuKhoa);
    void createOrder(Order newOrder);
    void createOrderDetail(OrderDetail newOrderDetail);
}
