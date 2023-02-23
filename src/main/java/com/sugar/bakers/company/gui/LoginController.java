package com.sugar.bakers.company.gui;

import com.sugar.bakers.company.domain.Cake;
import com.sugar.bakers.company.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}