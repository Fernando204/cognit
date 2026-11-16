package com.example.cognit_invest.controllers.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/keep")
public class KeepController{

    @GetMapping()
    public String keep(){
        return "alive";
    }
}