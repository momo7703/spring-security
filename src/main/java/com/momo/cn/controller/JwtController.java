package com.momo.cn.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JwtController {

    private final String USER_ID_KEY="USER_ID";

    @GetMapping("/api/admin")
    @ResponseBody
    @PreAuthorize("hasAuthority('page_r')")
    public Object helloAdmin(@RequestAttribute(value = USER_ID_KEY) Integer userId){
        return "Welcome Admin"+userId;
    }


    @GetMapping("/api/user")
    @ResponseBody
    @PreAuthorize("hasAuthority('page_d')")
    public Object helloUser(@RequestAttribute(value = USER_ID_KEY) Integer userId){
        return "Welcome User"+userId;
    }
}
