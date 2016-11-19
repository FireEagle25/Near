package su.awake.near.Listeners;

import android.os.AsyncTask;
import android.util.Log;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import su.awake.near.activites.MainActivity;
import su.awake.near.api.Applet;


public class BeaconListener extends SimpleIBeaconListener {

    private MainActivity activity;

    public BeaconListener(MainActivity mainActivity) {
        super();
        activity = mainActivity;
    }

    @Override
    public void onIBeaconDiscovered(IBeaconDevice iBeacon, IBeaconRegion region) {

        Log.i("IBeacon", "IBeacon discovered: " + iBeacon.getUniqueId());

        new GetBeaconInfo().execute(iBeacon);
    }

    @Override
    public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {

        Log.i("IBeacon", "IBeacon lost: " + iBeacon.getUniqueId());
        activity.removeBeacon(iBeacon.getUniqueId());
    }

    private class GetBeaconInfo extends AsyncTask<IBeaconDevice, Applet, Applet> {

        @Override
        protected Applet doInBackground(IBeaconDevice... devices) {

            Applet discoveredApplet = new Applet();
            discoveredApplet.getInfo(devices[0].getUniqueId());
            return discoveredApplet;
        }

        @Override
        protected void onPostExecute(Applet result)
        {
            super.onPostExecute(result);
            activity.addBeacon(result);
        }
    }
}
