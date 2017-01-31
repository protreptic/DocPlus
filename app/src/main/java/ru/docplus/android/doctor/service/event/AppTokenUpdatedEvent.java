package ru.docplus.android.doctor.service.event;

import android.support.annotation.NonNull;

/**
 * Событие наступает при обновлении токена приложения.
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class AppTokenUpdatedEvent {

    private final String token;

    public AppTokenUpdatedEvent(@NonNull final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
