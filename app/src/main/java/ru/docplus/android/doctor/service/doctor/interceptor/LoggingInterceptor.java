package ru.docplus.android.doctor.service.doctor.interceptor;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import ru.docplus.android.doctor.service.event.BusProvider;
import ru.docplus.android.doctor.service.event.ConnectionQualityChangedEvent;

public final class LoggingInterceptor implements Interceptor {

    private static final String TAG = "http";

    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = " in %.1fms (avg: %dms)";
    private static final String F_HEADERS = "\n%s";
    private static final String F_RESPONSE = F_BREAK + "Response -> %d";
    private static final String F_BODY = "\n%s";

    private static final String F_BREAKER = "-------------------------------------------" + F_BREAK;
    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;

    private volatile static long requestTotalTime = 0;
    private volatile static long requestCount = 0;
    private volatile static long requestAvgTime = 0;
    private volatile static String connectionQuality = "";

    public static String getConnectionQuality() {
        return connectionQuality;
    }

    private void updateConnectionQuality() {
        if (requestCount < 10) return;

        if (requestCount > 50) {
            requestTotalTime = 0;
            requestCount = 0;
        }

        String newConnectionQuality;

        if (requestAvgTime < 200) {
            newConnectionQuality = "Отличный уровень сигнала";
        } else if (requestAvgTime > 200 && requestAvgTime < 600) {
            newConnectionQuality = "Хороший уровень сигнала";
        } else if (requestAvgTime > 600 && requestAvgTime < 1200) {
            newConnectionQuality = "Низкий уровень сигнала";
        } else if (requestAvgTime > 1200) {
            newConnectionQuality = "Плохой уровень сигнала";
        } else  {
            newConnectionQuality = "";
        }

        if (!connectionQuality.equals(newConnectionQuality)) {
            connectionQuality = newConnectionQuality;

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    BusProvider.getInstance().post(new ConnectionQualityChangedEvent());
                }

            });

            requestTotalTime = 0;
            requestCount = 0;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = SystemClock.elapsedRealtimeNanos();
        Response response = chain.proceed(request);
        long t2 = SystemClock.elapsedRealtimeNanos();

        MediaType contentType = null;
        String bodyString = null;

        if (response.body() != null) {
            contentType = response.body().contentType();
            bodyString = response.body().string();
        }

        double time = (t2 - t1) / 1e6d;

        requestTotalTime += time;
        requestCount++;
        requestAvgTime = requestTotalTime / requestCount;

        updateConnectionQuality();

        if (request.method().equals("GET")) {
            Log.d(TAG, String.format("GET" + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url(), time, requestAvgTime, request.headers(), response.code(), response.headers(), prepareBodyString(bodyString)));
        } else if (request.method().equals("POST")) {
            Log.d(TAG, String.format("POST" + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, requestAvgTime, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), prepareBodyString(bodyString)));
        }

        if (response.body() != null) {
            return response.newBuilder().body(ResponseBody.create(contentType, bodyString == null ? "" : bodyString)).build();
        } else {
            return response;
        }
    }

    private String prepareBodyString(final String bodyString) {
        return (bodyString != null && bodyString.length() > 1024) ? "" : StringEscapeUtils.unescapeJson(bodyString);
    }

    private static String stringifyRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
