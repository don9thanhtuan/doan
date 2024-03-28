package com.nhom1.bookstore.apis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.services.AccountService;


@RestController
public class LoginAPIController {
    private final AccountService accountService;

    public LoginAPIController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping("/api/login")
    public ResponseEntity<AccountResponse> authentication(@RequestParam("username") String username, @RequestParam("password") String password) {
        int ketqua = accountService.authentication(username, password);
        AccountResponse response = new AccountResponse(null, false);
        if (ketqua != 0) {
            if (ketqua >= 1) {
                response.setUserID(username);
                if(ketqua > 1) {
                    response.setAdmin(true);
                } else{
                    response.setAdmin(false);
                }
            }
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
}
