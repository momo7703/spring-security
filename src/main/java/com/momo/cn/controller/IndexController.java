package com.momo.cn.controller;

import com.momo.cn.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class IndexController {
    @Autowired
    CustomUserDetailsService userDetailsService;

    @RequestMapping("/myLogin")
    public String showLogin() {
        return "myLogin";
    }

    @RequestMapping("/success")
    public String success(Authentication authentication) {
        log.info("principal:"+authentication.getPrincipal().toString());
        log.info("name:"+authentication.getName());
        return "success";
    }

    @RequestMapping("/errorPage")
    public String fail() {
        return "errorPage";
    }

    @RequestMapping("/test")
    @ResponseBody
    @PreAuthorize("hasAuthority('page_u')")
    public void testUser(){
        log.info("roleList:"+userDetailsService.loadUserByUsername("user").getRoleList());
        log.info("authorities:"+userDetailsService.loadUserByUsername("user").getAuthorities());
    }
    
}
