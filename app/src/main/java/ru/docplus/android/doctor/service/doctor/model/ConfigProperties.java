package ru.docplus.android.doctor.service.doctor.model;

/**
 * Сущность представляющая набор свойств с настройками приложения
 * в модели взаимодействия с API сервера.
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class ConfigProperties {

    private String DOC_UPDATING_GEOLOCATION;
    private String docUpdatingGeolocation;
    private int requestRate;

    public String getDOC_UPDATING_GEOLOCATION() {
        return DOC_UPDATING_GEOLOCATION;
    }

    public int getDocUpdatingGeolocation() {
        try {
            return Integer.parseInt(docUpdatingGeolocation) * 1000;
        } catch (Exception e) {
            return 60 * 1000;
        }
    }

    public int getRequestRate() {
        return requestRate;
    }

}