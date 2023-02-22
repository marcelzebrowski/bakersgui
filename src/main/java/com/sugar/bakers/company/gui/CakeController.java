package com.sugar.bakers.company.gui;

import com.sugar.bakers.company.domain.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CakeController {

    public class Cake{
        public String name;
        public String beschreibung;

        public Cake(String name, String beschreibung){
            this.name = name;
            this.beschreibung = beschreibung;
        }
    }


    @RequestMapping(value = {"/cake","/"}, method = RequestMethod.GET)
    public String showAllCakes(Model model){

        List<Cake> cakeList = new ArrayList<>();
        cakeList.add(new Cake("Test Kuchen", "Ganz toller Kuchen"));
        cakeList.add(new Cake("Test Kuchen2", "Ganz toller Kuchen2"));

        model.addAttribute("cakes",cakeList);


        Order order;

        return "cake";
    }

}
