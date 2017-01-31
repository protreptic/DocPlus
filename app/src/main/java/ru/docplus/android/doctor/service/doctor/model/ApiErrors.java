package ru.docplus.android.doctor.service.doctor.model;

import android.support.annotation.NonNull;

public final class ApiErrors {

    private final ApiError errors;

    public ApiErrors(@NonNull final ApiError errors) {
        this.errors = errors;
    }

    public ApiError getErrors() {
        return errors;
    }

}
