package com.ecommerce.demo.controller;

import com.ecommerce.demo.dto.ResponseDto;
import com.ecommerce.demo.dto.user.SigninDto;
import com.ecommerce.demo.dto.user.SigninResponseDto;
import com.ecommerce.demo.dto.user.SignupDto;
import com.ecommerce.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    UserService userService;
    //two apis
    //1. signup
    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupDto signupDto) {
        return userService.signUp(signupDto);
    }

    //2. signin
    @PostMapping("/signin")
    public SigninResponseDto signIn(@RequestBody SigninDto signinDto) {
        return userService.signIn(signinDto);
    }

}
