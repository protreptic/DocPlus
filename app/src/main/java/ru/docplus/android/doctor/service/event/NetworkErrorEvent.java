package ru.docplus.android.doctor.service.event;

import ru.docplus.android.doctor.service.doctor.model.NetworkError;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class NetworkErrorEvent {

    private NetworkError networkError;

    public NetworkErrorEvent(NetworkError throwable) {
        this.networkError = throwable;
    }

    public NetworkError getNetworkError() {
        return networkError;
    }

}
