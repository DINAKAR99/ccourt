package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class Restcontrol {

    // Example GET method
    @GetMapping("/api/{name}")
    public ResponseEntity<String> getExample(@PathVariable String name) {
        String responseBody = "dinakar";
        if (name.equals("dinakar")) {

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    }
}
