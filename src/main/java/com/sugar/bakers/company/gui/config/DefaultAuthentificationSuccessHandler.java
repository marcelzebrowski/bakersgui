package com.sugar.bakers.company.gui.config;

import com.sugar.bakers.company.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultAuthentificationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${bakershexagon.service.url}")
    private String orderRestControllerUrl;

    @Value("${bakershexagon.service.port}")
    private String orderRestControllerPort;

    private final RestTemplate restTemplate = new RestTemplate();

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
        request.getSession().setAttribute("user",findCustomer(authentication));
        redirectStrategy.sendRedirect(request, response, "cake");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        request.getSession().setAttribute("user",findCustomer(authentication));
        redirectStrategy.sendRedirect(request, response, "cake");
    }

    private Customer findCustomer(Authentication authentication){
        String url = orderRestControllerUrl + ":" + orderRestControllerPort + "/customer/name/";
        url += authentication.getName();
        ResponseEntity<Customer> loginResponse = restTemplate.getForEntity(url,Customer.class);
        return loginResponse.getBody();
    }
}
