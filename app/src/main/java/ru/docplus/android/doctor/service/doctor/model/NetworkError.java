package ru.docplus.android.doctor.service.doctor.model;

import android.support.annotation.NonNull;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 31.01.2017.
 */
public final class NetworkError {

    private final String message;
    private final Throwable throwable;

    public NetworkError(@NonNull final Throwable throwable) {
        this.message = "";
        this.throwable = throwable;
    }

    public NetworkError(@NonNull final String message, @NonNull final Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
