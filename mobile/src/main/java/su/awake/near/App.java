package su.awake.near;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.kontakt.sdk.android.common.KontaktSDK;


public class App extends Application {

    static String IMEI;

    @Override
    public void onCreate() {
        super.onCreate();
        KontaktSDK.initialize(this);
        MultiDex.install(this);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
    }
}