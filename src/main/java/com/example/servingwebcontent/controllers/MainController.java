package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.model.Message;
import com.example.servingwebcontent.model.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String add(@RequestParam String text, @RequestParam String tag){
        Message m = new Message();
        m.setText(text);
        m.setTag(tag);
        repository.save(m);

        return "redirect:/all";
    }

    @PostMapping("/filtered")
    public String filter(@RequestParam String tag, Map<String, Object> model){
        model.put("messages", repository.findByTagContains(tag));
        return "/listMessages";
    }

    @GetMapping("/all")
    public String all(Map<String, Object> model){
        model.put("messages", repository.findAll());
        return "/listMessages";
    }

}
