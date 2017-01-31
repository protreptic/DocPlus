package ru.docplus.android.doctor.service.doctor.model;

import android.support.annotation.NonNull;

/**
 * Сущность представляющая ошибку запроса сервера
 * в модели взаимодействия с API сервера.
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class Error {

    private final int code;
    private final String message;

    public Error(final int code, @NonNull final String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Возвращает код ошибки.
     *
     * @return код ошибки
     */
    public int getCode() {
        return code;
    }

    /**
     * Возвращает описание ошибки.
     *
     * @return описание ошибки
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
