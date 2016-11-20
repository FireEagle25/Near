package su.awake.near.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import su.awake.near.Listeners.BeaconListener;
import su.awake.near.R;
import su.awake.near.api.Applet;
import su.awake.near.api.HttpRequest;
import su.awake.near.api.Like;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static String IMEI;
    private ProximityManager proximityManager;
    static public ArrayList<Applet> applets = new ArrayList<>();
    public HashMap<View, Applet> viewAppletHashMap = new HashMap<>();
    protected LinearLayout slider;
    protected View selectedIcon;
    protected Applet selectedApplet;

    Animation liftUp;
    Animation liftDown;

    Button openAppletPage;

    ImageButton like;
    ImageButton share;
    ImageButton reviews;




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

        liftUp = AnimationUtils.loadAnimation(this, R.anim.lift_up_icon);
        liftDown = AnimationUtils.loadAnimation(this, R.anim.lift_down_icon);

        openAppletPage = (Button) findViewById(R.id.open_applet_button);

        openAppletPage.setOnClickListener(this);

        like = (ImageButton) findViewById(R.id.like);
        like.setOnClickListener(this);

        share = (ImageButton) findViewById(R.id.share);
        share.setOnClickListener(this);

        reviews = (ImageButton) findViewById(R.id.reviews);
        reviews.setOnClickListener(this);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI=tm.getDeviceId();
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


    private void fadeInElements() {
        LinearLayout userReactions = (LinearLayout) findViewById(R.id.user_reactions);
        ImageView leftArrow = (ImageView) findViewById(R.id.arrow_left);
        ImageView rightArrow = (ImageView) findViewById(R.id.arrow_right);
        userReactions.setVisibility(View.VISIBLE);
        leftArrow.setVisibility(View.VISIBLE);
        rightArrow.setVisibility(View.VISIBLE);
    }

    protected void showAppletIcons() {
        viewAppletHashMap.clear();
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

                if (applets.size() == 1)
                    appletIcon.callOnClick();

                viewAppletHashMap.put(icon, applets.get(i));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    protected synchronized void setSelectedApplet(Applet applet) {
        selectedApplet = applet;
    }

    protected void setTextFieldsTexts(String title, String description, String actions) {
        TextView name = (TextView) findViewById(R.id.title);
        name.setText(title);

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(description);

        TextView actionsView = (TextView) findViewById(R.id.actions);
        actionsView.setText(actions);

        if (selectedApplet.isLiked()) {
            ImageButton likeButton = (ImageButton) findViewById(R.id.like);
            likeButton.setImageResource(R.drawable.star_checked);
        }

        TextView likeCount = (TextView) findViewById(R.id.likeCount);
        likeCount.setText(String.valueOf(selectedApplet.getLikeCount()));
    }




    //BEACONS

    public void addBeacon(Applet applet) {
        fadeInElements();
        for (int i = 0; i < applets.size(); i++)
            if (applets.get(i).getToken().equals(applet.getToken()))
                return;

        applets.add(applet);
        this.showAppletIcons();

        if (findViewById(R.id.loadingPanel) != null) {
            LinearLayout loading = (LinearLayout) findViewById(R.id.loadingPanel);
            ((LinearLayout) loading.getParent()).removeView(loading);
        }

        if (selectedApplet == null) {
            viewAppletHashMap.entrySet().iterator().next().getKey().callOnClick();
        }
    }

    public void removeBeacon(String token) {
        for (int i = 0; i < applets.size(); i++) {
            if (applets.get(i).getToken().equals(token)) {
                applets.remove(i);
                break;
            }
        }
/*
        for (Map.Entry<View, Applet> entry : viewAppletHashMap.entrySet())
            if (entry.getValue().getToken().equals(token))
                viewAppletHashMap.remove(entry.getKey());*/

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

        if (v == openAppletPage) {
            Intent intent = new Intent(this, AppletPage.class);
            intent.putExtra("url", selectedApplet.getSourceLink());
            startActivity(intent);
        }

        if (v == like) {

            (new LikeTask()).execute(selectedApplet);

            if (selectedApplet.isLiked()) {
                selectedApplet.setIsLiked(false);
                like.setImageResource(R.drawable.star_unchecked);

            }
            else{
                like.setImageResource(R.drawable.star_checked);
                selectedApplet.setIsLiked(true);
            }
            return;
        }

        if (v == share) {
            if(selectedApplet != null) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, selectedApplet.getDescription() + "\n" + selectedApplet.getSourceLink());
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, selectedApplet.getName());
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
            return;
        }
        else {
            LinearLayout parent = (LinearLayout) v.getParent();
            parent.startAnimation(liftUp);

            if (selectedIcon != null)
                selectedIcon.startAnimation(liftDown);

            selectedIcon = parent;
            setSelectedApplet(viewAppletHashMap.get(v));
            if (selectedApplet != null)
                setTextFieldsTexts(selectedApplet.getName(), selectedApplet.getDescription(), selectedApplet.getAppletActions());
        }
    }

    private class LikeTask extends AsyncTask<Applet, Applet, Like> {

        @Override
        protected Like doInBackground(Applet... applets) {

            Like like = new Like();
            like.getInfo(applets[0].getApplet_id());
            return like;
        }
    }

}
