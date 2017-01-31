package ru.docplus.android.doctor.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.docplus.android.doctor.R;
import ru.docplus.android.doctor.service.doctor.DoctorApi;
import ru.docplus.android.doctor.service.doctor.DoctorApiProvider;
import ru.docplus.android.doctor.service.doctor.model.ApiError;
import ru.docplus.android.doctor.service.doctor.model.NetworkError;
import ru.docplus.android.doctor.service.doctor.request.AuthRequest;
import ru.docplus.android.doctor.service.doctor.request.LoginRequest;
import ru.docplus.android.doctor.service.doctor.request.LogoutRequest;
import ru.docplus.android.doctor.service.doctor.response.ApiErrorResponse;
import ru.docplus.android.doctor.service.doctor.response.AuthResponse;
import ru.docplus.android.doctor.service.doctor.response.ConfigResponse;
import ru.docplus.android.doctor.service.doctor.response.LoginResponse;
import ru.docplus.android.doctor.service.doctor.response.LogoutResponse;
import ru.docplus.android.doctor.service.event.AppTokenUpdatedEvent;
import ru.docplus.android.doctor.service.event.BusProvider;
import ru.docplus.android.doctor.service.event.ConfigurationUpdatedEvent;
import ru.docplus.android.doctor.service.event.DoctorApiErrorEvent;
import ru.docplus.android.doctor.service.event.LoginEvent;
import ru.docplus.android.doctor.service.event.LogoutEvent;
import ru.docplus.android.doctor.service.event.NetworkErrorEvent;
import ru.docplus.android.doctor.service.event.ReferenceUpdatedEvent;
import ru.docplus.android.doctor.service.event.UpdateAvailableEvent;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class DoctorService extends Service {

    public static final String TAG = "DoctorService";

    private Bus mBus;
    private DoctorApi mDoctorApi;

    @Override
    public void onCreate() {
        super.onCreate();

        mBus = BusProvider.getInstance();
        mBus.register(this);

        mDoctorApi = DoctorApiProvider.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateNotification();
        updateAppToken();
        updateConfiguration();
        updateUserToken();

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateNotification() {
        startForeground(54699, new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("РАБОТАЕТ")
                .setAutoCancel(false)
                .setOngoing(true)
                .build());
    }

    private void updateAppToken() {
        mDoctorApi.auth(new AuthRequest(
                getString(R.string.app_login),
                getString(R.string.app_password)))
                    .enqueue(new Callback<AuthResponse>() {

            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    final String appToken =
                            response.body().getData().getToken();

                    mBus.post(new AppTokenUpdatedEvent(appToken));
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Auth failed: ", t);

                handleNetworkError(t);
            }

        });
    }

    private void updateConfiguration() {
        mDoctorApi.config().enqueue(new Callback<ConfigResponse>() {

            @Override
            public void onResponse(Call<ConfigResponse> call, Response<ConfigResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new ConfigurationUpdatedEvent(response.body().getData()));
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<ConfigResponse> call, Throwable t) {
                Log.e(TAG, "Update configuration failed: ", t);

                handleNetworkError(t);
            }

        });
    }

    private void checkUpdate() {
        mBus.post(new UpdateAvailableEvent());
    }

    public static String appToken = "";

    private void updateUserToken() {
        mDoctorApi.login(new LoginRequest("", "", "", ""))
                .enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    appToken = response.body().getData().getToken();

                    //RealmProvider.configure("", "");

                    mBus.post(new LoginEvent());
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Login failed: ", t);

                handleNetworkError(t);
            }

        });
    }

    private void logout() {
        mDoctorApi.logout(new LogoutRequest()).enqueue(new Callback<LogoutResponse>() {

            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new LogoutEvent());
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Log.e(TAG, "Logout failed: ", t);

                handleNetworkError(t);
            }

        });
    }

    private void updateReference() {
        mBus.post(new ReferenceUpdatedEvent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBus.unregister(this);

        stopForeground(true);
    }

    private void handleApiError(Response response) {
        DoctorApiErrorEvent event = new DoctorApiErrorEvent();

        try {
            switch (response.code()) {
                case 404: {
                    event = new DoctorApiErrorEvent(new ApiError(404, "404 Not Found"));
                } break;
                default: {
                    event = new DoctorApiErrorEvent(
                            new GsonBuilder().create()
                                    .fromJson(response.errorBody().charStream(),
                                            ApiErrorResponse.class).getData().getErrors());
                } break;
            }
        } catch (Exception e) {
            //
        } finally {
            mBus.post(event);
        }
    }

    private void handleNetworkError(Throwable t) {
        mBus.post(new NetworkErrorEvent(new NetworkError(t)));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
