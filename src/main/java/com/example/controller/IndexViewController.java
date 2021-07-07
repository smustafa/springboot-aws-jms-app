package com.example.controller;

import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/listUsers")
    public String listAllUsers(Model model) {
        model.addAttribute("listOfUsers", userService.findAll());
        return "list-users";
    }
}
