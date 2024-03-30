package com.nhom1.bookstore.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nhom1.bookstore.DTO.OrderDTO;
import com.nhom1.bookstore.entity.Order;
import com.nhom1.bookstore.entity.OrderDetail;
import com.nhom1.bookstore.repositories.OrderDAOController;

@Service
public class OrderSeviceImp implements OrderService {
    private final OrderDAOController orderDAOController;

    public OrderSeviceImp(OrderDAOController orderDAOController) {
        this.orderDAOController = orderDAOController;
    }

    @Override
    public List<Order> getOrderList() {
        List<Order> orderList = orderDAOController.getOrderList();
        sortByThoiGianDat(orderList);
        return orderList;
    }

    private void sortByThoiGianDat(List<Order> orderList) {
        Collections.sort(orderList, new Comparator<Order>() {
            @Override
            public int compare(Order order1, Order order2) {
                return order2.getThoiGianDat().compareTo(order1.getThoiGianDat());
            }
        });
    }

    @Override
    public Order getOrder(String id) {
        return orderDAOController.getOrder(id);
    }

    @Override
    public void editStatusOrder(String currentID, int newStatus) {
        orderDAOController.editStatusOrder(currentID, newStatus);
    }

    @Override
    public List<Order> search(String tuKhoa) {
        List<Order> orderList = orderDAOController.search(tuKhoa);
        sortByThoiGianDat(orderList);
        return orderList;
    }

    @Override
    public OrderDetail getOrderDetail(String id) {
        return orderDAOController.getOrderDetail(id);
    }

    @Override
    public void createOrder(String idNguoiDat, OrderDTO newOrder) {
        String maDonHang = IDGenerator.IDOrder();
        Order order = new Order();
        order.setMaDonHang(maDonHang);
        order.setIdNguoiDat(idNguoiDat);
        order.setThoiGianDat(new Date());
        int trangThai = 1;
        if(newOrder.getPaymentMethod().equals("cod")) {
            trangThai = 0;
        }
        order.setTrangThai(trangThai);
        order.setThanhTien(newOrder.getPrice());
        order.setIdSachDau(newOrder.getBookList().get(0));
        order.setSoSanPham(newOrder.getBookList().size());
        order.setSoDienThoai(newOrder.getPhone());
        order.setDiaChi(newOrder.getAddress());
        orderDAOController.createOrder(order);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setMaDonHang(maDonHang);

        orderDetail.setBookList(new ArrayList<>());
        
        for(int i = 0; i < newOrder.getBookList().size(); i++){
            String idSach = newOrder.getBookList().get(i);
            String soLuongRaw = newOrder.getQuantityList().get(i);
            int soLuong = Integer.parseInt(soLuongRaw);

            OrderDetail.BookInOrder bookInOrder = orderDetail.new BookInOrder(idSach, soLuong);
            orderDetail.getBookList().add(bookInOrder);
        }

        orderDAOController.createOrderDetail(orderDetail);
    }

    @Override
    public void deleteOrder(String id) {
        orderDAOController.deleteOrder(id);
    }
}
