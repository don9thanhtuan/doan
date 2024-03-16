package com.nhom4.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhom4.bookstore.entity.Account;
import com.nhom4.bookstore.entity.Book;
import com.nhom4.bookstore.entity.Order;
import com.nhom4.bookstore.entity.OrderDetail;
import com.nhom4.bookstore.entity.OrderDetail.BookInOrder;
import com.nhom4.bookstore.services.AccountService;
import com.nhom4.bookstore.services.BookService;
import com.nhom4.bookstore.services.ConverterCurrency;
import com.nhom4.bookstore.services.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewPersonalOrderDetailsController {
    private final OrderService orderService;
    private final BookService bookService;
        private final AccountService accountService;

    public ViewPersonalOrderDetailsController(OrderService orderService, BookService bookService, AccountService accountService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.accountService = accountService;
    }

    @GetMapping("/taikhoan/donhang/{id}")
    public String viewPersonalOrder(@PathVariable("id") String id, Model model, HttpSession session) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if(loggedInUser != null) {
            Order order = orderService.getOrder(id);
            OrderDetail orderDetail = orderService.getOrderDetail(id);
            Account account = accountService.getAccountNonPassword(order.getIdNguoiDat());
            
            for (BookInOrder bookInOrder : orderDetail.getBookList()) {
                Book book = bookService.getBook(bookInOrder.getIdSach());
                bookInOrder.setSach(book);
            }
            int tongTienInt = ConverterCurrency.currencyToNumber(order.getThanhTien());
            String tongTien = ConverterCurrency.numberToCurrency(tongTienInt + 25000);
            model.addAttribute("account", account);
            model.addAttribute("order", order);
            model.addAttribute("bookList", orderDetail.getBookList());
            model.addAttribute("orderDetail", orderDetail);
            model.addAttribute("totalValue", tongTien);
            return "user_chitietdonhang";
        }
        return "redirect:/trangchu";
    }
}
