package org.discord.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/hello")
    public  String  getHello(){
        return "Hello";
    }

    @GetMapping("/loginSuccess")
    public  String  loginSucess(){
        return "Hello from oath2";
    }

    @GetMapping("/loginFailure")
    public  String  loginFailure(){
        return  "Login FAiled";
    }

}
