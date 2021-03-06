package com.course.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {

    @Value("${application.controllers.message}")
    private String mensage;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("mensaje",this.mensage);
        return "hola";
    }

}
