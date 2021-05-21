package com.momo.cn.security;


import com.momo.cn.filter.JwtAuthenticationFilter;
import com.momo.cn.filter.JwtLoginFilter;
import com.momo.cn.security.config.CustomWebSecurityConfig;
import com.momo.cn.security.filter.CustomLoginAuthenticationFilter;
import com.momo.cn.security.handler.FailureHandler;
import com.momo.cn.security.handler.SuccessHandler;
import com.momo.cn.security.provider.CustomLoginAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomLoginAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private SuccessHandler successHandler;

    @Autowired
    private FailureHandler failureHandler;

    @Autowired
    private CustomUserDetailsService userDetailsService;

//    JwtLoginFilter jwtLoginFilter = new JwtLoginFilter();
//    JwtAuthenticationFilter jwtAuthenticationFilter=new JwtAuthenticationFilter();

//    @Autowired
//    private CustomWebSecurityConfig customWebSecurityConfig;
    @Bean
    CustomLoginAuthenticationFilter filter() throws Exception {
        CustomLoginAuthenticationFilter authenticationFilter = new CustomLoginAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();

        http
//                .apply(customWebSecurityConfig).and()
                .authorizeRequests()
                .antMatchers("/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/myLogin").permitAll()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
//                .defaultSuccessUrl("/success")
//                .failureUrl("/errorPage")
                .permitAll()
                .and()
                .userDetailsService(userDetailsService)
                .addFilterAt(filter(), UsernamePasswordAuthenticationFilter.class)
//        .addFilter(new JwtLoginFilter(authenticationManager()))
//        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        ;

        //JWT拦截器配置
//        http
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //禁用session
//                .and()
//                .addFilterAt(new JwtLoginFilter(authenticationManager()),UsernamePasswordAuthenticationFilter.class)
//                .addFilterAfter(new JwtAuthenticationFilter(authenticationManager()),JwtLoginFilter.class)
//        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(customAuthenticationProvider);
    }

}
