package ru.docplus.android.doctor.realm;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Peter Bukhal (peter.bukhal@gmail.com)
 */
public final class RealmProvider {

    public static void configure(final String login, final String password) {
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name(login + ".realm")
                .encryptionKey(password.getBytes())
                .build());
    }

    public static Realm getInstance() {
        return Realm.getDefaultInstance();
    }

    private RealmProvider() {
        //
    }

}
