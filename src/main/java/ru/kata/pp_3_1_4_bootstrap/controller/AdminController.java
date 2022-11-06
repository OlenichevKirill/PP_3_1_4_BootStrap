package ru.kata.pp_3_1_4_bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.pp_3_1_4_bootstrap.model.User;
import ru.kata.pp_3_1_4_bootstrap.service.UserService;


import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping()
    public String allUsers(@AuthenticationPrincipal User user,
                           @ModelAttribute("newUser") User user1,
                           Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userInfo", user);
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
        return "redirect:/admin";
        //return "users/edit-user";
    }

    @PatchMapping("/updateUser/{id}")
    public String updateUser(@RequestParam("usernameEdit") String usernameEdit,
                             @RequestParam("passwordEdit") String passwordEdit,
                             @RequestParam("nameEdit") String nameEdit,
                             @RequestParam("lastNameEdit") String lastNameEdit,
                             @RequestParam("emailEdit") String emailEdit,
                             @PathVariable("id") long id) {
        User userEdit = userService.getUser(id);

        if (!userEdit.getPassword().equals(passwordEdit)) {
            userEdit.setPassword(bCryptPasswordEncoder.encode(passwordEdit));
        }

        userEdit.setUsername(usernameEdit);
        userEdit.setFirstName(nameEdit);
        userEdit.setLastName(lastNameEdit);
        userEdit.setEmail(emailEdit);
        userService.updateUser(userEdit);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
