package ru.docplus.android.doctor.service.doctor.response;

import ru.docplus.android.doctor.service.doctor.model.ConfigProperties;

/**
 * Сущность представляющая ответ сервера на запрос
 * получения списка настроек приложения в модели
 * взаимодействия с API сервера.
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class ConfigResponse {

    private boolean success;
    private ConfigProperties data;

    public ConfigResponse(boolean success, ConfigProperties data) {
        this.success = success;
        this.data = data;
    }

    /**
     * Возвращает результат выполнения операции.
     *
     * @return результат выполнения операции
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Возвращает список с настройками приложения.
     *
     * @return список с настройками
     */
    public ConfigProperties getData() {
        return data;
    }

}