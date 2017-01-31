package ru.docplus.android.doctor.service.doctor.request;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class LoginRequest {

    private final String phone;
    private final String password;
    private final String deviceId;
    private final String pushToken;

    public LoginRequest(final String phone, final String password, final String pushToken, final String deviceId) {
        this.phone = phone;
        this.password = password;
        this.pushToken = pushToken;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getPushToken() {
        return pushToken;
    }

}
