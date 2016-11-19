package su.awake.near;

import android.util.Log;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import su.awake.near.activites.MainActivity;
import su.awake.near.apiObjects.Applet;


public class BeaconListener extends SimpleIBeaconListener {

    private MainActivity activity;

    public BeaconListener(MainActivity mainActivity) {
        super();
        activity = mainActivity;
    }

    @Override
    public void onIBeaconDiscovered(IBeaconDevice iBeacon, IBeaconRegion region) {

        Log.i("IBeacon", "IBeacon discovered: " + iBeacon.getUniqueId());

        Applet discoveredApplet = new Applet();
        discoveredApplet.getInfo(iBeacon.getUniqueId());

        activity.addBeacon(discoveredApplet);
    }

    @Override
    public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {

        Log.i("IBeacon", "IBeacon lost: " + iBeacon.getUniqueId());
        activity.removeBeacon(iBeacon.getUniqueId());
    }
}
