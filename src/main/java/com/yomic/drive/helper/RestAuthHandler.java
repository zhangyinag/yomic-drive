package com.yomic.drive.helper;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler, AccessDeniedHandler, AuthenticationEntryPoint {
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        this.send(res, StatusDict.ERROR.getCode(), e.getMessage());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        this.send(res, StatusDict.SUCCESS.getCode(), StatusDict.SUCCESS.getMessage());
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        this.send(res, StatusDict.SUCCESS.getCode(), StatusDict.SUCCESS.getMessage());
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        this.send(res, StatusDict.ERROR.getCode(), e.getMessage());
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
        this.send(res, StatusDict.ERROR.getCode(), e.getMessage());
    }

    private void send (HttpServletResponse res, String code, String message) throws IOException {
        res.setHeader("Content-Type", "application/json;charset=utf-8");
        res.getWriter().print("{\"code\":\"" + code + "\",\"message\":\""+ message +"\"}");
        res.getWriter().flush();
    }

}
