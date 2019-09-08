package com.ApiAccountManagement.ApiAccountManagement.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    @GetMapping
    public String status() {
        return "Working..";
    }
}
