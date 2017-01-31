package ru.docplus.android.doctor.service.doctor.request;

import android.support.annotation.NonNull;

/**
 * Сущность представляющая запрос токена приложения
 * в модели взаимодействия с API сервера.
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class AuthRequest {

    private final String clientId;
    private final String password;

    public AuthRequest(@NonNull final String clientId, @NonNull final String password) {
        this.clientId = clientId;
        this.password = password;
    }

    /**
     * Возвращает идентификатор приложения.
     *
     * @return идентификатор приложения
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Возвращает пароль приложения.
     *
     * @return пароль приложения
     */
    public String getPassword() {
        return password;
    }

}
