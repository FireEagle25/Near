package su.awake.near.activites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.common.KontaktSDK;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import su.awake.near.BeaconListener;
import su.awake.near.R;
import su.awake.near.apiObjects.Applet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ProximityManager proximityManager;
    static public ArrayList<Applet> applets = new ArrayList<>();
    public HashMap<View, Applet> viewApplet = new HashMap<>();
    protected LinearLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KontaktSDK.initialize(getString(R.string.kontakt_io_api_key));
        proximityManager = new ProximityManager(this);
        proximityManager.setIBeaconListener(new BeaconListener(this));

        slider = (LinearLayout) findViewById(R.id.slider);
        showAppletIcons();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

    protected void showAppletIcons() {

        slider.removeAllViews();

        LayoutInflater ltInflater = getLayoutInflater();

        for (int i = 0; i < applets.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            LinearLayout appletIcon = (LinearLayout) inflater.inflate(R.layout.applet_icon, null, false);

            ImageView icon = (ImageView) appletIcon.findViewById(R.id.icon);
            URL url = null;
            try {
                url = new URL(applets.get(i).getIcon());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                icon.setImageBitmap(bmp);
                icon.setOnClickListener(this);
                slider.addView(appletIcon);
                viewApplet.put(icon, applets.get(i));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    //BEACONS

    public void addBeacon(Applet applet) {

        for (int i = 0; i < applets.size(); i++)
            if (applets.get(i).getToken().equals(applet.getToken()))
                return;

        applets.add(applet);
        this.showAppletIcons();
    }

    public void removeBeacon(String token) {
        for (int i = 0; i < applets.size(); i++) {
            if (applets.get(i).getToken().equals(token)) {
                applets.remove(token);
                break;
            }
        }

        this.showAppletIcons();
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


    @Override
    public void onClick(View v) {
        Log.i("applet", viewApplet.get(v).toString());

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.expand_icon);
        v.startAnimation(anim);


    }
}
