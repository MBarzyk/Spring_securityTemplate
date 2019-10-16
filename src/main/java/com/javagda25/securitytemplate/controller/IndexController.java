package com.javagda25.securitytemplate.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    //    @Autowired // you can make the field final and @AllArgsConstructor above the class
//    private String name;
//
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String indexWithLogging() {
        return "login-form";
    }
}
