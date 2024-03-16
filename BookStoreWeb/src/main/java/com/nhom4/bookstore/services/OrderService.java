package com.nhom4.bookstore.services;

import java.util.List;

import com.nhom4.bookstore.DTO.OrderDTO;
import com.nhom4.bookstore.entity.Order;
import com.nhom4.bookstore.entity.OrderDetail;

public interface OrderService {
    List<Order> getOrderList();
    Order getOrder(String id);
    OrderDetail getOrderDetail(String id);
    void editStatusOrder(String currentID, int newStatus);
    List<Order> search(String tuKhoa);
    void createOrder(String idNguoiDat, OrderDTO newOrder);
}
