package com.nhom1.bookstore.apis;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.entity.Account;
import com.nhom1.bookstore.services.AccountService;

@RestController
public class EditPersonalAccountAPIController {
    private final AccountService accountService;
    
    public EditPersonalAccountAPIController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/api/accounts/{userid}")
    public String changeInformation(@PathVariable("userid") String userid, @RequestBody Account newAccount) {        
            Account currentAccount = accountService.getAccount(userid);
        
        if(newAccount.getHoTen() != null) {
            newAccount.setSoDienThoai(currentAccount.getSoDienThoai());
            newAccount.setDiaChi(currentAccount.getDiaChi());
            newAccount.setEmail(currentAccount.getEmail());   

        } else if(newAccount.getSoDienThoai() != null) {
            newAccount.setHoTen(currentAccount.getHoTen());
            newAccount.setDiaChi(currentAccount.getDiaChi());
            newAccount.setEmail(currentAccount.getEmail());  

        } else if(newAccount.getDiaChi() != null) {
            newAccount.setHoTen(currentAccount.getHoTen());
            newAccount.setSoDienThoai(currentAccount.getSoDienThoai());
            newAccount.setEmail(currentAccount.getEmail());  

        } else if(newAccount.getEmail() != null) {
            newAccount.setHoTen(currentAccount.getHoTen());
            newAccount.setSoDienThoai(currentAccount.getSoDienThoai());
            newAccount.setDiaChi(currentAccount.getDiaChi());
        }

        accountService.editAccount(currentAccount.getTenTaiKhoan(), newAccount);

        return "Account edited successfully";
    }
}