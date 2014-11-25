package ru.lizaalert.common;

import android.app.Application;
import com.yandex.metrica.YandexMetrica;

/**
 * Created by tsypa on 20/11/14.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetrica.initialize(getApplicationContext(), getString(R.string.yandex_api_key));
    }
}
