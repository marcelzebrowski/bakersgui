package com.sugar.bakers.company.gui;

import com.sugar.bakers.company.domain.Cake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class CakeController {

    @Value("${bakershexagon.service.url}")
    private String orderRestControllerUrl;

    @Value("${bakershexagon.service.port}")
    private String orderRestControllerPort;

    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = {"/cake","/"}, method = RequestMethod.GET)
    public String showAllCakes(Model model){

        String url = orderRestControllerUrl + ":" + orderRestControllerPort + "/cakes";

        ResponseEntity<Cake[]> response = restTemplate.getForEntity(url,Cake[].class);
        List<Cake> cakeList = Arrays.asList(Objects.requireNonNull(response.getBody()));

        generateUrlToImages(cakeList);

        model.addAttribute("cakes",cakeList);

        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());

        return "cake";
    }


    @PostMapping("/order/cake/{id}")
    public String dropOrder(Model model, @PathVariable Long id){
        return "success";
    }

    private void generateUrlToImages(List<Cake> cakeList) {
        for(Cake cake : cakeList){
            String imageUrl = orderRestControllerUrl + ":" + orderRestControllerPort + "/images/";
            cake.setPicture(imageUrl + cake.getPicture());
        }
    }

}
