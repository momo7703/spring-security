package com.momo.cn.security.filter;

import com.momo.cn.entity.SysUser;
import com.momo.cn.service.SysUserService;
import com.momo.cn.util.JwtUtils;
import com.momo.cn.util.SpringBeanFactoryUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    static final String USER_ID_KEY = "USER_ID";

    private AuthenticationManager authenticationManager;

    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * 该方法在Spring Security验证前调用
     * 将用户信息从request中取出，并放入authenticationManager中
     * @author jitwxs
     * @since 2018/5/4 10:35
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            // 将用户信息放入authenticationManager
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password,
                            Collections.emptyList())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 该方法在Spring Security验证成功后调用
     * 在这个方法里生成JWT token，并返回给用户
     * @author jitwxs
     * @since 2018/5/4 10:37
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        String username = ((User)auth.getPrincipal()).getUsername();

        // 从数据库中取出用户信息
        SysUserService userService = SpringBeanFactoryUtils.getBean(SysUserService.class);
        SysUser user = userService.selectByName(username);;

        // 将用户id放入JWT token
        Map<String,Object> map = new HashMap<>();
        map.put(USER_ID_KEY, user.getId());
        String token = JwtUtils.sign(map, 3600_000);

        // 将token放入响应头中
        res.addHeader("Authorization", token);
    }
}