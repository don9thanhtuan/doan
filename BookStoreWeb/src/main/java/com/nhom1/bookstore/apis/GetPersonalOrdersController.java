package com.nhom1.bookstore.apis;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.entity.Order;
import com.nhom1.bookstore.services.BookService;
import com.nhom1.bookstore.services.OrderService;

@RestController
public class GetPersonalOrdersController {
    private final OrderService orderService;
    private final BookService bookService;

    public GetPersonalOrdersController(OrderService orderService, BookService bookService) {
        this.orderService = orderService;
        this.bookService = bookService;
    }

    @GetMapping("/api/accounts/{userid}/orders")
    public ResponseEntity<List<Order>> getPersonalOrders(@PathVariable("userid") String userid) {
        List<Order> orderList = orderService.search(userid);
        for (Order order : orderList) {
            Book book = bookService.getBook(order.getIdSachDau());
            order.setCuonSachDau(book);
            order.setSoSanPham(order.getSoSanPham() - 1);
        }
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }
}
