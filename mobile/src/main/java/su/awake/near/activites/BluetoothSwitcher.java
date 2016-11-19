package su.awake.near.activites;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import su.awake.near.R;

/**
 * Created by fireeagle on 11/20/16.
 */

public class BluetoothSwitcher extends AppCompatActivity implements View.OnClickListener{

    final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_switcher);

        final Button turnON = (Button) findViewById(R.id.bluetooth_turn_on_button);

        if (bluetooth.isEnabled())
            goToMainIfBluetoothEnabled();

        turnON.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        goToMainIfBluetoothEnabled();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        goToMainIfBluetoothEnabled();
    }

    protected void goToMainIfBluetoothEnabled() {
        if (bluetooth.isEnabled()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onClick(View v) {

        if (!bluetooth.isEnabled()) {

            Toast.makeText(getApplicationContext(),
                    "Turning ON Bluetooth", Toast.LENGTH_LONG);

            startActivityForResult(new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            goToMainIfBluetoothEnabled();
        }
    }

}
