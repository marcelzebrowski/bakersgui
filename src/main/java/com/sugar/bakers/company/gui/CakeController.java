package com.sugar.bakers.company.gui;

import com.sugar.bakers.company.domain.Cake;
import com.sugar.bakers.company.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class CakeController {

    @Value("${bakershexagon.service.url}")
    private String orderRestControllerUrl;

    @Value("${bakershexagon.service.port}")
    private String orderRestControllerPort;

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = {"/cake","/"}, method = RequestMethod.GET)
    public String preparePage(HttpServletRequest request, Model model){

        String url = orderRestControllerUrl + ":" + orderRestControllerPort + "/cakes";

        ResponseEntity<Cake[]> response = restTemplate.getForEntity(url,Cake[].class);
        List<Cake> cakeList = Arrays.asList(Objects.requireNonNull(response.getBody()));

        generateUrlToImages(cakeList);

        model.addAttribute("cakes",cakeList);

        Customer customer = (Customer) request.getSession().getAttribute("user");
        model.addAttribute("username", customer.getName());

        return "cake";
    }


    @PostMapping("/order/cake/{id}")
    public String dropAnOrder(HttpServletRequest request, Model model, @PathVariable Long id){

        Customer customer = (Customer) request.getSession().getAttribute("user");
        String url = orderRestControllerUrl + ":" + orderRestControllerPort + "/order/new";

        url += "/" + customer.getCustomerId().getId();
        url += "/" + id;

        // place Order
        restTemplate.postForEntity( url, null , String.class );


        // next page
        url = orderRestControllerUrl + ":" + orderRestControllerPort + "/cake/" + id;
        ResponseEntity<Cake> response = restTemplate.getForEntity(url,Cake.class);
        Cake cake = response.getBody();
        assert cake != null;

        String imageUrl = orderRestControllerUrl + ":" + orderRestControllerPort + "/images/";
        cake.setPicture(imageUrl + cake.getPicture());

        model.addAttribute("cake", cake);

        return "success";
    }

    private void generateUrlToImages(List<Cake> cakeList) {
        for(Cake cake : cakeList){
            String imageUrl = orderRestControllerUrl + ":" + orderRestControllerPort + "/images/";
            cake.setPicture(imageUrl + cake.getPicture());
        }
    }

}
