package com.yomic.drive.config;

import com.yomic.drive.constant.HttpJsonStatus;
import com.yomic.drive.domain.User;
import com.yomic.drive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    RestAuthHandler handler = new RestAuthHandler();


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",//swagger api json
                "/swagger-resources/configuration/ui",//用来获取支持的动作
                "/swagger-resources",//用来获取api-docs的URI
                "/webjars/springfox-swagger-ui/**",//用来获取api-docs的URI
                "/swagger-resources/configuration/security",//安全选项
                "/swagger-ui.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().permitAll() // TODO 暂时放开权限控制
            .and().formLogin()
                .successHandler(handler)
                .failureHandler(handler)
            .and().logout()
                .logoutSuccessHandler(handler)
                .invalidateHttpSession(true)
            .and().exceptionHandling()
                .accessDeniedHandler(handler)
                .authenticationEntryPoint(handler)
            .and().csrf().disable()
            .cors();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return username -> {
            com.yomic.drive.domain.User user = userService.getUserByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户名未找到");
            }
            String password = user.getPassword();
            return user;
        };
    }
}



class RestAuthHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler, AccessDeniedHandler, AuthenticationEntryPoint {
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        this.send(res, HttpJsonStatus.ERROR.getCode(), e.getMessage());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        this.send(res, HttpJsonStatus.SUCCESS.getCode(), HttpJsonStatus.SUCCESS.getMessage());
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        this.send(res, HttpJsonStatus.SUCCESS.getCode(), HttpJsonStatus.SUCCESS.getMessage());
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        this.send(res, HttpJsonStatus.AUTH_REQUIRED.getCode(), e.getMessage());
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
        this.send(res, HttpJsonStatus.ACCESS_DENIED.getCode(), e.getMessage());
    }

    private void send (HttpServletResponse res, String code, String message) throws IOException {
        res.setHeader("Content-Type", "application/json;charset=utf-8");
        res.getWriter().print("{\"code\":\"" + code + "\",\"message\":\""+ message +"\"}");
        res.getWriter().flush();
    }

}
