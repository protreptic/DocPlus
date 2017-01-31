package ru.docplus.android.doctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import ru.docplus.android.doctor.service.DoctorService;
import ru.docplus.android.doctor.service.event.DoctorApiErrorEvent;
import ru.docplus.android.doctor.service.event.AppTokenUpdatedEvent;
import ru.docplus.android.doctor.service.event.BusProvider;
import ru.docplus.android.doctor.service.event.ConfigurationUpdatedEvent;
import ru.docplus.android.doctor.service.event.NetworkErrorEvent;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class MainActivity extends AppCompatActivity {

    private Bus mBus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBus = BusProvider.getInstance();
        mBus.register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(this, DoctorService.class));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onApplicationTokenUpdated(AppTokenUpdatedEvent event) {
        Toast.makeText(this, "Application token updated", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onConfigurationUpdated(ConfigurationUpdatedEvent event) {
        Toast.makeText(this, "Configuration updated", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onDoctorApiErrorOccurred(DoctorApiErrorEvent event) {
        Toast.makeText(this, "API error occurred: " + event.getError(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onNetworkErrorOccurred(NetworkErrorEvent event) {
        Toast.makeText(this, "Network error occurred", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopService(new Intent(this, DoctorService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBus.unregister(this);
    }

}