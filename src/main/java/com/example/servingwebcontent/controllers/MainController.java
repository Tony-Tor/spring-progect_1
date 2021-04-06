package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.model.Message;
import com.example.servingwebcontent.model.MessageRepo;
import com.example.servingwebcontent.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    MessageRepo repository;

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/add")
    public String addForm() {
        return "messageForm";
    }

    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text, @RequestParam String tag){
        Message m = new Message();
        m.setText(text);
        m.setTag(tag);
        m.setAuthor(user);
        repository.save(m);

        return "redirect:/all";
    }

    @GetMapping("/all")
    public String all(@RequestParam(required = false, defaultValue = "") String filter, @AuthenticationPrincipal User user,Map<String, Object> model){
        model.put("messages", repository.findByTagContains(filter));
        model.put("usrname", user.getName());
        model.put("filter", filter);

        return "/listMessages";
    }

}
