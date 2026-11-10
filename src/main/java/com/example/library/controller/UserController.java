package com.example.library.controller;

import com.example.library.model.AdminUser;
import com.example.library.model.RegularUser;
import com.example.library.model.User;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("users", userService.searchUsers(search));
        model.addAttribute("search", search);
        return "users";
    }

    @PostMapping("/add")
    public String addUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam(value = "membershipType", required = false) String membershipType,
            @RequestParam(value = "adminLevel", required = false) String adminLevel) {
        
        User user;
        if ("ADMIN".equalsIgnoreCase(role)) {
            user = new AdminUser(null, username, password, email, adminLevel);
        } else {
            user = new RegularUser(null, username, password, email, membershipType);
        }
        userService.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/edit")
    public String editUser(
            @RequestParam("id") String id,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam(value = "membershipType", required = false) String membershipType,
            @RequestParam(value = "adminLevel", required = false) String adminLevel) {
        
        User user;
        if ("ADMIN".equalsIgnoreCase(role)) {
            user = new AdminUser(id, username, password, email, adminLevel);
        } else {
            user = new RegularUser(id, username, password, email, membershipType);
        }
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
