package com.obs.controller;

import com.obs.entities.GlobalSettingsEntity;
import com.obs.services.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/global-settings")
public class GlobalSettingsController {

    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public GlobalSettingsController(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    @GetMapping
    public String showGlobalSettings(Model model) {
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
