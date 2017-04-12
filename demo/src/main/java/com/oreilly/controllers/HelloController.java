package com.oreilly.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by hyan on 4/12/17.
 */
@Controller
public class HelloController {
    @GetMapping("/sayHello")
    public String sayHello(@RequestParam(value = "name", required = false,
                           defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";

    }
}
