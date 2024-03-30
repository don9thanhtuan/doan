package com.nhom1.bookstore.apis;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.entity.Order;
import com.nhom1.bookstore.services.BookService;
import com.nhom1.bookstore.services.OrderService;

@RestController
public class ManageDeleteBookAPIController {
    private final BookService bookService;
        private final OrderService orderService;


    public ManageDeleteBookAPIController(BookService bookService, OrderService orderService) {
        this.bookService = bookService;
        this.orderService = orderService;
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") String id) {
        List<Order> orderList = orderService.search(id);
        if(orderList.size() != 0) {
            boolean isCompleted = true;
            for(Order order : orderList) {
                if(order.getTrangThaiInt() <= 2) {
                    isCompleted = false;
                    break;
                }
            }
            
            if(isCompleted) {
                for(Order order : orderList) {
                    orderService.deleteOrder(order.getOrderID());
                }
            } else{
                return new ResponseEntity<>("Book deleted fails", HttpStatus.BAD_REQUEST);
            }
        }

        bookService.deleteBook(id);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
    }
}
