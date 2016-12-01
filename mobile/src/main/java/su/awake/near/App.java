package su.awake.near;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.kontakt.sdk.android.common.KontaktSDK;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KontaktSDK.initialize(this);
        MultiDex.install(this);
    }
}