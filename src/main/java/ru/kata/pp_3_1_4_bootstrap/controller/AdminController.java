package ru.kata.pp_3_1_4_bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.pp_3_1_4_bootstrap.model.User;
import ru.kata.pp_3_1_4_bootstrap.service.UserService;


import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String allUsers(@AuthenticationPrincipal User user,
                           @ModelAttribute("newUser") User user1,
                           Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user);
        return "users/admin";
    }

    @GetMapping("/newUser")
    public String addNewUser(@ModelAttribute("user") User user) {
        return "users/new-user";
    }

    @PostMapping("/new")
    public String saveNewUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/new-user";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String getUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "users/edit-user";
    }

    @PatchMapping("/updateUser/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            return "users/edit-user";
        }
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
