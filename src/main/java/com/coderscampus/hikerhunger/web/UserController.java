package com.coderscampus.hikerhunger.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/landingPage")
    public String getLandingPage(){
        return "landingPage";
    }
}
