package com.nhom1.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nhom1.bookstore.entity.Account;
import com.nhom1.bookstore.entity.Book;
import com.nhom1.bookstore.entity.Order;
import com.nhom1.bookstore.entity.OrderDetail;
import com.nhom1.bookstore.entity.OrderDetail.BookInOrder;
import com.nhom1.bookstore.services.AccountService;
import com.nhom1.bookstore.services.BookService;
import com.nhom1.bookstore.services.ConverterCurrency;
import com.nhom1.bookstore.services.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ManageOrderDetailsController {
    private final OrderService orderService;
    private final BookService bookService;
    private final AccountService accountService;

    public ManageOrderDetailsController(OrderService orderService, BookService bookService, AccountService accountService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.accountService = accountService;
    }

    @GetMapping("/quantri/donhang/{id}")
    public String getOrderDetail(@PathVariable("id") String id, Model model, HttpSession session) {
        Object isAdmin = session.getAttribute("isAdmin");
        if(isAdmin != null && isAdmin.equals(Boolean.TRUE)) {
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
            return "admin_detailorder";
        }
        return "redirect:/dangnhap";
    }
}
