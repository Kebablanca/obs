package com.obs.controller;

import com.obs.entities.GlobalSettingsEntity;
import com.obs.entities.UserEntity;
import com.obs.services.GlobalSettingsService;
import com.obs.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/global-settings")
public class GlobalSettingsController {

    private final GlobalSettingsService globalSettingsService;
    private final UserService userService;
    

    @Autowired
    public GlobalSettingsController(GlobalSettingsService globalSettingsService, UserService userService) {
        this.globalSettingsService = globalSettingsService;
        this.userService = userService;
    }

    @GetMapping
    public String showGlobalSettings(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        GlobalSettingsEntity globalSettings = globalSettingsService.getGlobalSettings();
        if (globalSettings == null) {
            globalSettings = new GlobalSettingsEntity();
        }
        model.addAttribute("globalSettings", globalSettings);
        return "globalSettings";
    }

    @PostMapping
    public String saveGlobalSettings(@ModelAttribute GlobalSettingsEntity globalSettings, Model model) {
        globalSettingsService.saveGlobalSettings(globalSettings);
        model.addAttribute("globalSettings", globalSettings);
        model.addAttribute("message", "Global settings updated successfully");
        return "globalSettings";
    }
}
