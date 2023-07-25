package com.typeface.notification.controller;

import com.typeface.notification.model.LoginResponse;
import com.typeface.notification.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/v1/login/user")
    public LoginResponse loginUser(@RequestHeader String token) {
        return this.loginService.authenticate(token);
    }

}
