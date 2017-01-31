package ru.docplus.android.doctor.service.doctor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.docplus.android.doctor.service.doctor.request.AuthRequest;
import ru.docplus.android.doctor.service.doctor.request.LoginRequest;
import ru.docplus.android.doctor.service.doctor.request.LogoutRequest;
import ru.docplus.android.doctor.service.doctor.response.AuthResponse;
import ru.docplus.android.doctor.service.doctor.response.ConfigResponse;
import ru.docplus.android.doctor.service.doctor.response.LoginResponse;
import ru.docplus.android.doctor.service.doctor.response.LogoutResponse;

/**
 * Интерфейс API сервера.
 *
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public interface DoctorApi {

    /**
     * Возращает токен приложения, который необходимо
     * испозовать для взаимодействия с API.
     *
     * @return токен приложения
     */
    @POST("/api/auth")
    Call<AuthResponse> auth(@Body AuthRequest request);

    /**
     * Возвращает настройки приложения.
     *
     * @return список настроек приложения
     */
    @GET("/api/config")
    Call<ConfigResponse> config();

    /**
     *
     * @param request
     * @return
     */
    @POST("/api/doctor/auth")
    Call<LoginResponse> login(@Body LoginRequest request);

    /**
     *
     * @param request
     * @return
     */
    @POST("/api/doctor/logout")
    Call<LogoutResponse> logout(@Body LogoutRequest request);

}
