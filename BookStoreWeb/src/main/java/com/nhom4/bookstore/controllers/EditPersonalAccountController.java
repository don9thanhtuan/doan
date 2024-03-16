package com.nhom4.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhom4.bookstore.entity.Account;
import com.nhom4.bookstore.services.AccountService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EditPersonalAccountController {
    private final AccountService accountService;
    
    public EditPersonalAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/taikhoan/thongtin")
    public String changeInformation(HttpSession session,
    @RequestParam("hoten") String hoten, 
    @RequestParam("sdt") String sdt, 
    @RequestParam("diachi") String diachi) {
        Account account = new Account();
        account.setHoTen(hoten);
        account.setSoDienThoai(sdt);
        account.setDiaChi(diachi);

        Object loggedInUser = session.getAttribute("loggedInUser");
        accountService.editAccount(loggedInUser.toString(), account);
        return "redirect:/taikhoan/thongtin";
    }
}
