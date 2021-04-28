package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.model.Message;
import com.example.servingwebcontent.model.MessageRepo;
import com.example.servingwebcontent.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class MainController {

    static final Logger logger = LoggerFactory.getLogger("MainController");

    @Autowired
    MessageRepo repository;

    @Value("${upload.path}")
    private String uploadPath;

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
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam("image") MultipartFile file) throws IOException {
        Message m = new Message();
        m.setText(text);
        m.setTag(tag);
        m.setAuthor(user);

        if(file!=null && !file.isEmpty()){
            File uploadFile = new File(uploadPath);
            if(!uploadFile.exists()){
                uploadFile.mkdirs();
            }

            logger.info(uploadPath);
            logger.info("" + uploadFile.exists());

            String filename = UUID.randomUUID().toString() + "." + file.getOriginalFilename();

            Path path = Paths.get(uploadPath+filename);

            file.transferTo(new File(path.toString()));

            m.setFilename(filename);
        }
        repository.save(m);

        return "redirect:/all";
    }

    @PostMapping("/filtered")
    public String filter(@RequestParam String tag, Map<String, Object> model){
        model.put("messages", repository.findByTagContains(tag));
        return "/listMessages";
    }

    @GetMapping("/all")
    public String all(@AuthenticationPrincipal User user,Map<String, Object> model){
        model.put("messages", repository.findAll());
        model.put("usrname", user.getName());

        return "/listMessages";
    }

}
