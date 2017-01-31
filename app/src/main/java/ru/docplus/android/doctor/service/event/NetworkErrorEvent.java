package ru.docplus.android.doctor.service.event;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class NetworkErrorEvent {

    private Throwable throwable;

    public NetworkErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
