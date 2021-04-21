package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.model.Role;
import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.model.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    static final Logger logger = LoggerFactory.getLogger("UserController");

    @Autowired
    UserRepo repo;

    @RequestMapping("/all")
    public String all(@AuthenticationPrincipal User user, Map<String, Object> model){
        model.put("users", repo.findAll());
        model.put("usrname", user.getName());

        return "/listUser";
    }

    @GetMapping("/edit/{user_id}")
    public String editGet(@PathVariable("user_id") Long id, Map<String, Object> model){
        User user = repo.findById(id).get();
        model.put("user", user);
        return "/editUser";
    }

    @PostMapping("/edit")
    public String editPost(@RequestParam Long id,
                           @RequestParam String username,
                           @RequestParam boolean active,
                           @RequestParam Map<String, String> form){
        User user = repo.findById(id).get();
        user.setName(username);
        user.setActive(active);
        Set<String> roles = Stream.of(Role.values()).map(Role::name).collect(Collectors.toSet());
        user.getRole().clear();
        form.entrySet().stream().forEach(x->{
            logger.info(x.getKey());
            if(roles.contains(x.getKey())){
                user.getRole().add(Role.valueOf(x.getKey()));
            }
        });
        repo.save(user);

        return "redirect:/user/all";
    }




}
