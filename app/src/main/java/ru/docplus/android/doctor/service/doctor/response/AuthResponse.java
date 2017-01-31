package ru.docplus.android.doctor.service.doctor.response;

import ru.docplus.android.doctor.service.doctor.model.Token;

/**
 * TODO Доработать документацию
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class AuthResponse {

    private boolean success;
    private Token data;

    public AuthResponse(final boolean success, final Token data) {
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
     * Возвращает информацию о токене приложения.
     *
     * @return информация о токене
     */
    public Token getData() {
        return data;
    }

}
