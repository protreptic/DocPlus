package ru.docplus.android.doctor.service.doctor.interceptor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import ru.docplus.android.doctor.service.DoctorService;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 31.01.2017.
 */
public final class RequestInterceptor implements Interceptor {

    private final Context context;
    private final String versionName;
    private final String versionCode;

    public RequestInterceptor(@NonNull final Context context) {
        this.context = context;
        this.versionName = getVersionName();
        this.versionCode = getVersionCode();
    }

    private String getVersionName() {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    private String getVersionCode() {
        try {
            return String.valueOf(
                    context.getPackageManager()
                            .getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(
                chain.request()
                        .newBuilder()
                        .addHeader("token", DoctorService.sAppToken)
                        .addHeader("X-DOC-VERSION-NAME", versionName)
                        .addHeader("X-DOC-VERSION-CODE", versionCode)
                        .build());
    }

}
