package su.awake.near;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

public class MainActivity extends AppCompatActivity {
    
    private ProximityManager proximityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KontaktSDK.initialize(getString(R.string.kontakt_io_api_key));
        proximityManager = new ProximityManager(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
                Log.i("START", "started scanning");
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                Log.i("IBeacon", "IBeacon discovered: " + ibeacon.getUniqueId());

                Toast toast = Toast.makeText(getApplicationContext(),
                        "NEW " + ibeacon.getUniqueId(), Toast.LENGTH_LONG);

                toast.show();
            }

            @Override
            public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {
                Log.i("IBeacon", "IBeacon discovered: " + iBeacon.getUniqueId());

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Lost " + iBeacon.getUniqueId(), Toast.LENGTH_LONG);

                toast.show();
            }
        };
    }


}
