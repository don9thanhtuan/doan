package com.nhom4.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nhom4.bookstore.DTO.OrderDTO;
import com.nhom4.bookstore.entity.Account;
import com.nhom4.bookstore.services.AccountService;
import com.nhom4.bookstore.services.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {
    private final AccountService accountService;
    private final OrderService orderService;

    public CheckoutController(AccountService accountService, OrderService orderService) {
        this.accountService = accountService;
        this.orderService = orderService;
    }

    @GetMapping("/giohang/thanhtoan")
    public String getCheckOut(HttpSession session, Model model) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if(loggedInUser != null) {
            Account account = accountService.getAccountNonPassword(loggedInUser.toString());
            model.addAttribute("account", account);
            if(account.getDiaChi() != null) {
                if(account.getDiaChi().isEmpty()) {
                    model.addAttribute("noAddress", false);
                }else{
                    model.addAttribute("noAddress", true);
                }
            }
            return "thanhtoan";
        }
        return "redirect:/dangnhap";
    }

    @PostMapping("/giohang/thanhtoan")
    public void checkOut(@RequestBody OrderDTO orderDTO, HttpSession session) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if(loggedInUser != null) {
            orderService.createOrder(loggedInUser.toString(), orderDTO);
        }
    }
}
