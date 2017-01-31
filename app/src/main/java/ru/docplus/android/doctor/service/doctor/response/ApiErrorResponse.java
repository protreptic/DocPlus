package ru.docplus.android.doctor.service.doctor.response;

import ru.docplus.android.doctor.service.doctor.model.ApiErrors;

/**
 * Сущность представляющая ответ сервера содержащий ошибки
 * в модели взаимодействия с API сервера.
 */
public final class ApiErrorResponse {

    private boolean success;
    private ApiErrors data;

    public ApiErrorResponse(final boolean success, final ApiErrors data) {
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
     * Возвращает список ошибок сервера в случае их возникновения.
     *
     * @return список ошибок
     */
    public ApiErrors getData() {
        return data;
    }

}
