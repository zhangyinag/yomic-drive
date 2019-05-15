package com.yomic.drive.config;

import com.yomic.drive.helper.RestAuthHandler;
import com.yomic.drive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
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
                .anyRequest().authenticated()
            .and().formLogin()
                .successHandler(handler)
                .failureHandler(handler)
            .and().logout()
                .logoutSuccessHandler(handler)
                .invalidateHttpSession(true)
            .and().exceptionHandling()
                .accessDeniedHandler(handler)
                .authenticationEntryPoint(handler)
            .and().csrf().disable();
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
            String[] roles = user.getRoleList().stream().map(role -> role.getRoleCode()).toArray(String[]::new);
            return User.withUsername(username).password(password).roles(roles).build();
        };
    }
}
