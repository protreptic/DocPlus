package ru.docplus.android.doctor.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import ru.docplus.android.doctor.service.event.ConnectionQualityChangedEvent;
import ru.docplus.android.doctor.service.event.DoctorApiErrorEvent;
import ru.docplus.android.doctor.service.event.LoginEvent;
import ru.docplus.android.doctor.service.event.LogoutEvent;
import ru.docplus.android.doctor.service.event.NetworkErrorEvent;
import ru.docplus.android.doctor.service.event.ReferenceUpdatedEvent;
import ru.docplus.android.doctor.service.event.UpdateAvailableEvent;

import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class DoctorService extends Service {

    public static final String TAG = "DoctorService";

    private Bus mBus;
    private DoctorApi mDoctorApi;

    private volatile boolean mHasConnection;

    private BroadcastReceiver mConnectivityServiceReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mHasConnection = !intent.hasExtra(EXTRA_NO_CONNECTIVITY)
                    || !intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, true);

            updateNotification();
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();

        mExecutorService = Executors.newScheduledThreadPool(5);

        registerReceiver(mConnectivityServiceReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mHasConnection = hasNetworkConnection();

        mBus = BusProvider.getInstance();
        mBus.register(this);

        mDoctorApi = DoctorApiProvider.getInstance(this);
    }

    private ScheduledExecutorService mExecutorService;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateNotification();

        mExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    new UpdateAppToken().execute(
                            new AuthRequest(
                                    getString(R.string.app_login),
                                    getString(R.string.app_password))).get();

                    new UpdateConfiguration().execute().get();
                } catch (Exception e) {
                    //
                }
            }
        }, 0, 5, TimeUnit.SECONDS);

        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onConnectionQualityChanged(ConnectionQualityChangedEvent event) {
        updateNotification();
    }

    private void updateNotification() {
        startForeground(54699, new NotificationCompat.Builder(this)
                .setSmallIcon(mHasConnection ? R.drawable.online_mode : R.drawable.offline_mode)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(mHasConnection ? "РАБОТАЕТ" : "АВТОНОМНЫЙ РЕЖИМ")
                .setAutoCancel(false)
                .setOngoing(true)
                .build());
    }

    public static String sAppToken = "";

    private class UpdateAppToken extends AsyncTask<AuthRequest, Void, Response<AuthResponse>> {

        @Override
        protected Response<AuthResponse> doInBackground(AuthRequest... requests) {
            final AuthRequest request = requests[0];

            try {
                return mDoctorApi.auth(request).execute();
            } catch (Exception e) {
                //
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response<AuthResponse> response) {
            if (response == null) {
                handleNetworkError(new RuntimeException());

                return;
            }

            if (response.isSuccessful()) {
                final String appToken =
                        response.body().getData().getToken();

                sAppToken = appToken;

                mBus.post(new AppTokenUpdatedEvent(appToken));
            } else {
                handleApiError(response);
            }
        }
    }

    private class UpdateConfiguration extends AsyncTask<Void, Void, Response<ConfigResponse>> {

        @Override
        protected Response<ConfigResponse> doInBackground(Void... voids) {
            try {
                return mDoctorApi.config().execute();
            } catch (Exception e) {
                //
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response<ConfigResponse> response) {
            if (response == null) {
                mBus.post(new NetworkErrorEvent(new NetworkError(new RuntimeException())));

                return;
            }

            if (response.isSuccessful()) {
                mBus.post(new ConfigurationUpdatedEvent(response.body().getData()));
            } else {
                handleApiError(response);
            }
        }

    }

    private void checkUpdate() {
        mBus.post(new UpdateAvailableEvent());
    }

    private void updateUserToken() {
        mDoctorApi.login(new LoginRequest("", "", "", ""))
                .enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
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

        unregisterReceiver(mConnectivityServiceReceiver);

        stopForeground(true);
    }

    private void handleApiError(Response response) {
        DoctorApiErrorEvent event = new DoctorApiErrorEvent();

        try {
            switch (response.code()) {
                case 400: {
                    event = new DoctorApiErrorEvent(
                            new GsonBuilder().create()
                                    .fromJson(response.errorBody().charStream(),
                                            ApiErrorResponse.class).getData().getErrors());
                } break;
                case 401: {
                    event = new DoctorApiErrorEvent(new ApiError(401, "401 Authorization Required"));
                } break;
                case 402: {
                    event = new DoctorApiErrorEvent(new ApiError(402, "402 Payment Required"));
                } break;
                case 403: {
                    event = new DoctorApiErrorEvent(new ApiError(403, "403 Forbidden"));
                } break;
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

    /**
     * Проверяет доступно ли в данный момент какое либо сетевое соединение.
     *
     * @return истина если есть соединение с сетью, иначе ложь
     */
    private boolean hasNetworkConnection() {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
