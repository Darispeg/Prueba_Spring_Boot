package com.example.ApplicationFull.Controller;

import javax.validation.Valid;

import com.example.ApplicationFull.Entity.User;
import com.example.ApplicationFull.Repository.RoleRepository;
import com.example.ApplicationFull.Service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    
	@Autowired
    @Qualifier("UserService")
	UserService uService;

    @Autowired
    RoleRepository repository;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/userForm")
    public String userForm(Model model){
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", uService.getAllUsers());
        model.addAttribute("roles", repository.findAll());
        model.addAttribute("listTab", "active");
        return "user-form/user-view";
    }

    @PostMapping("/userForm")
    public String createUser(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model){
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
        }else{
            try {
                uService.createUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", uService.getAllUsers());
                model.addAttribute("roles", repository.findAll());
            }
        }
        model.addAttribute("userList", uService.getAllUsers());
        model.addAttribute("roles", repository.findAll());
        return "user-form/user-view";
    }
}
