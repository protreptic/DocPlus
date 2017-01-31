package ru.docplus.android.doctor.service.doctor.response;

import java.util.List;
import ru.docplus.android.doctor.service.doctor.model.Error;
/**
 * Сущность представляющая ответ сервера содержащий ошибки
 * в модели взаимодействия с API сервера.
 */
public final class ApiErrorResponse {

    private boolean success;
    private List<Error> data;

    public ApiErrorResponse(boolean success, List<Error> data) {
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
    public List<Error> getData() {
        return data;
    }

}
