package com.nhom1.bookstore.apis;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.entity.Account;
import com.nhom1.bookstore.services.AccountService;

@RestController
public class RegisterAPIController {
    private final AccountService accountService;

    public RegisterAPIController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping("/api/register")
    public String registerAccount(@RequestBody Account account) {
        if(accountService.checkUsername(account.getTenTaiKhoan())){
            accountService.registerAccount(account);
            return "Success";             
        }
        return "Fails";
    }
}
