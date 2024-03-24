package com.nhom1.bookstore.apis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhom1.bookstore.services.AccountService;
import com.nhom1.bookstore.services.Encoder;


@RestController
public class LoginAPIController {
    private final AccountService accountService;

    public LoginAPIController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping("/api/login")
    public ResponseEntity<AccountResponse> authentication(@RequestParam("login-username") String username,
    @RequestParam("login-password") String password) {
        int ketqua = accountService.authentication(username, password);
        if (ketqua != 0) {
            if (ketqua >= 1) {
                String userName = Encoder.encodeString(username);
                AccountResponse response = new AccountResponse(userName, false);

                if(ketqua > 1) {
                    response.setAdmin(true);
                } else{
                    response.setAdmin(false);
                }
                
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    
}
