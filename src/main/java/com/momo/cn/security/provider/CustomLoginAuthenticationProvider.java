package com.momo.cn.security.provider;

import com.momo.cn.security.CustomUserDetails;
import com.momo.cn.security.CustomUserDetailsService;
import com.momo.cn.security.token.CustomLoginAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomLoginAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        CustomLoginAuthenticationToken cAuthentication=(CustomLoginAuthenticationToken) authentication;
        String username=cAuthentication.getName();
        System.out.println("username:"+username);
        String password=cAuthentication.getCredentials().toString();
        System.out.println("password:"+password);

        CustomUserDetails userDetails=userDetailsService.loadUserByUsername(username);
        if(userDetails==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //从前端获取用户名，密码再与   authentication与service中获取的用户名密码进行对比
        HttpServletRequest request= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String userName=request.getParameter("username");
        String passWord=request.getParameter("password");
        if(!passWord.equals(userDetails.getPassword())){
            throw new BadCredentialsException("密码错误");
        }
        CustomLoginAuthenticationToken c=new CustomLoginAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        return c;
    }

    @Override
    public boolean supports(Class<?> aClass){
        return CustomLoginAuthenticationToken.class.isAssignableFrom(aClass);
    }

//    public CustomUserDetailsService getUserDetailsService(){
//        return userDetailsService;
//    }
//
//    public void setUserDetailsService(CustomUserDetailsService userDetailsService){
//        this.userDetailsService=userDetailsService;
//    }



}
