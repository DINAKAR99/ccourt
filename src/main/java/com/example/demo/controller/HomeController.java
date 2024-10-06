package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    Logger l1 = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/test")
    public String getDetails() {

        l1.warn("this is a working message");
        return "this is tesing url";

    }
}
