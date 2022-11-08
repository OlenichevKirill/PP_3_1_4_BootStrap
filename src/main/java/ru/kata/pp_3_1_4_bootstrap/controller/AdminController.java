package ru.kata.pp_3_1_4_bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.pp_3_1_4_bootstrap.model.Role;
import ru.kata.pp_3_1_4_bootstrap.model.User;
import ru.kata.pp_3_1_4_bootstrap.service.RoleService;
import ru.kata.pp_3_1_4_bootstrap.service.UserService;


import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }

    @GetMapping()
    public String allUsers(@AuthenticationPrincipal User user,
                           @ModelAttribute("newUser") User user1,
                           Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userInfo", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/admin";
    }

    @GetMapping("/newUser")
    public String addNewUser(@ModelAttribute("user") User user) {
        return "users/new-user";
    }

    @PostMapping("/new")
    public String saveNewUser(@AuthenticationPrincipal User user1,
                              @RequestParam("roles") Set<Role> roles,
                              @ModelAttribute("newUser") @Valid User user,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("userInfo", user1);
            return "users/admin";
        }
        user.setRoles(roles);
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
    public String updateUser(@RequestParam("passwordEdit") String passwordEdit,
                             @RequestParam("nameEdit") String nameEdit,
                             @RequestParam("lastNameEdit") String lastNameEdit,
                             @RequestParam("ageEdit") int age,
                             @RequestParam("emailEdit") String emailEdit,
                             @RequestParam(value = "rolesEdit", required = false) Set<Role> roles,
                             @PathVariable("id") long id) {
        User userEdit = userService.getUser(id);

        if (!userEdit.getPassword().equals(passwordEdit)) {
            userEdit.setPassword(bCryptPasswordEncoder.encode(passwordEdit));
        }
        if (roles != null) {
            userEdit.setRoles(roles);
        }

        userEdit.setAge(age);
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
