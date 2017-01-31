package ru.docplus.android.doctor.service.event;

import android.support.annotation.NonNull;

import ru.docplus.android.doctor.service.doctor.model.ApiError;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class DoctorApiErrorEvent {

    private final ApiError error;

    public DoctorApiErrorEvent() {
        this.error = new ApiError(0, "Unknown API error");
    }

    public DoctorApiErrorEvent(@NonNull final ApiError error) {
        this.error = error;
    }

    @NonNull
    public ApiError getError() {
        return error;
    }

}
