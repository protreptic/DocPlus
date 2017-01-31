package ru.docplus.android.doctor.service.doctor;

import android.content.Context;
import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.docplus.android.doctor.R;
import ru.docplus.android.doctor.service.doctor.interceptor.LoggingInterceptor;
import ru.docplus.android.doctor.service.doctor.interceptor.RequestInterceptor;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class DoctorApiProvider {

    private static DoctorApi sDoctorApi;

    public static DoctorApi getInstance(@NonNull final Context context) {
        if (sDoctorApi == null) {
            sDoctorApi = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.doctor_api))
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(new RequestInterceptor(context))
                            .addInterceptor(new LoggingInterceptor())
                            .build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(DoctorApi.class);
        }

        return sDoctorApi;
    }

    private DoctorApiProvider() {
        //
    }

}
