package com.momo.cn.security.config;

import com.momo.cn.security.CustomUserDetailsService;
import com.momo.cn.security.filter.CustomLoginAuthenticationFilter;
import com.momo.cn.security.handler.FailureHandler;
import com.momo.cn.security.handler.SuccessHandler;
import com.momo.cn.security.provider.CustomLoginAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CustomWebSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private SuccessHandler successHandler;

    @Autowired
    private FailureHandler failureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CustomLoginAuthenticationFilter customFilter=new CustomLoginAuthenticationFilter();
        customFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        customFilter.setAuthenticationSuccessHandler(successHandler);
        customFilter.setAuthenticationFailureHandler(failureHandler);

        CustomLoginAuthenticationProvider customProvider=new CustomLoginAuthenticationProvider();
//        customProvider.setUserDetailsService(userDetailsService);

        http.authenticationProvider(customProvider)
                .addFilterAfter(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
