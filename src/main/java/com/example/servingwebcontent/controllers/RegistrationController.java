package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.model.Role;
import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.model.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userFromDB = userRepo.findByName(user.getName());
        
        if(userFromDB != null){
            model.put("messege", "user is exists");
            return "registration";
        }

        user.setActive(true);
        user.setRole(Collections.singleton(Role.USER));
        userRepo.save(user);
        
        return "redirect:/login";
    }
}
