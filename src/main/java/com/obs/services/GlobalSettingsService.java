package com.obs.services;

import com.obs.entities.GlobalSettingsEntity;
import com.obs.repositories.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalSettingsService {

    private final GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    public GlobalSettingsService(GlobalSettingsRepository globalSettingsRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
    }

    public GlobalSettingsEntity getGlobalSettings() {
        return globalSettingsRepository.findAll().stream().findFirst().orElse(null);
    }

    public void saveGlobalSettings(GlobalSettingsEntity globalSettings) {
        globalSettingsRepository.save(globalSettings);
    }
}
