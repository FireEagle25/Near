package su.awake.near;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.kontakt.sdk.android.common.KontaktSDK;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KontaktSDK.initialize(this);
        MultiDex.install(this);
    }
}