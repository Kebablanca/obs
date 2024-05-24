package com.obs.controller;

import com.obs.entities.GlobalSettingsEntity;
import com.obs.services.GlobalSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/global-settings")
public class GlobalSettingsController {

    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public GlobalSettingsController(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    @GetMapping
    public String showGlobalSettingsForm(Model model) {
        GlobalSettingsEntity globalSettings = globalSettingsService.getGlobalSettings();
        if (globalSettings == null) {
            globalSettings = new GlobalSettingsEntity();
        }
        model.addAttribute("globalSettings", globalSettings);
        return "globalSettings";
    }

    @PostMapping
    public String saveGlobalSettings(@ModelAttribute GlobalSettingsEntity globalSettings) {
        GlobalSettingsEntity existingSettings = globalSettingsService.getGlobalSettings();
        if (existingSettings != null) {
            globalSettings.setId(existingSettings.getId());
        }
        globalSettingsService.saveGlobalSettings(globalSettings);
        return "redirect:/admin/global-settings";
    }
}
