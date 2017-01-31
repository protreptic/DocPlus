package ru.docplus.android.doctor.service.doctor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.docplus.android.doctor.R;
import ru.docplus.android.doctor.service.DoctorService;
import ru.docplus.android.doctor.service.doctor.model.Error;
import ru.docplus.android.doctor.service.doctor.response.ApiErrorResponse;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class DoctorApiProvider {

    private static DoctorApi sDoctorApi;

    public static DoctorApi getInstance(@NonNull final Context context) {
        if (sDoctorApi == null) {
            final OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new Interceptor() {

                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(
                            chain.request()
                                    .newBuilder()
                                    .addHeader("token", DoctorService.appToken)
                                    .build());
                }

            });

            sDoctorApi = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.doctor_api))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(DoctorApi.class);
        }

        return sDoctorApi;
    }

    public static Error parseApiError(final String responseBody) {
        return new GsonBuilder().create()
                .fromJson(responseBody, ApiErrorResponse.class).getData().get(0);
    }

    private DoctorApiProvider() {
        //
    }

}
