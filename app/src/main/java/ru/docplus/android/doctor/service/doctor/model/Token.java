package ru.docplus.android.doctor.service.doctor.model;

/**
 * Сущность представляющая токен приложения
 * в модели взаимодействия с API сервера.
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class Token {

    private String token;

    public Token(String token) {
        this.token = token;
    }

    /**
     * Возвращает строковой уникальный идентификатор
     * приложения для взаимодействия с API.
     *
     * @return токен
     */
    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }

}
