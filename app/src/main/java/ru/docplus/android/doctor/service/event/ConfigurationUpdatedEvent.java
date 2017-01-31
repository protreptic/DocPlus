package ru.docplus.android.doctor.service.event;

import android.support.annotation.NonNull;

import ru.docplus.android.doctor.service.doctor.model.ConfigProperties;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class ConfigurationUpdatedEvent {

    private ConfigProperties configProperties;

    public ConfigurationUpdatedEvent(@NonNull final ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public ConfigProperties getConfigProperties() {
        return configProperties;
    }

}