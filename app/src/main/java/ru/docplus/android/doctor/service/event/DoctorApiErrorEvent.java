package ru.docplus.android.doctor.service.event;

import android.support.annotation.NonNull;

import ru.docplus.android.doctor.service.doctor.model.Error;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class DoctorApiErrorEvent {

    private final Error error;

    public DoctorApiErrorEvent() {
        this.error = new Error(0, "Unknown API error");
    }

    public DoctorApiErrorEvent(@NonNull final Error error) {
        this.error = error;
    }

    @NonNull
    public Error getError() {
        return error;
    }

}
