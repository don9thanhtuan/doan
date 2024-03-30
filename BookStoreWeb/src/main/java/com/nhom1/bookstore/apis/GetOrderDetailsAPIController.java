package com.nhom1.bookstore.apis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.entity.OrderDetail;
import com.nhom1.bookstore.entity.OrderDetail.OrderItem;
import com.nhom1.bookstore.services.BookService;
import com.nhom1.bookstore.services.OrderService;

@RestController
public class GetOrderDetailsAPIController {
    private final OrderService orderService;
    private final BookService bookService;

    public GetOrderDetailsAPIController(OrderService orderService, BookService bookService) {
        this.orderService = orderService;
        this.bookService = bookService;
    }

    @GetMapping("/api/orders/{orderID}")
    public ResponseEntity<OrderDetail> getOrderDetail(@PathVariable("orderID") String id) {
        OrderDetail orderDetail = orderService.getOrderDetail(id);

        for (OrderItem bookInOrder : orderDetail.getOrderItemList()) {
            Book book = bookService.getBook(bookInOrder.getBookID());
            bookInOrder.setBook(book);
        }
        
        return new ResponseEntity<>(orderDetail, HttpStatus.OK);
    }


}
