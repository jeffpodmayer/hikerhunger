package com.coderscampus.hikerhunger.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("")
    public String getLandingPage(){
        return "landing-page";
    }
}
