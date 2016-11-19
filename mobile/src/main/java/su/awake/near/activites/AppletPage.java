package su.awake.near.activites;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import su.awake.near.R;

public class AppletPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applet_page);

        WebView webview  = (WebView) findViewById(R.id.applet_web_view);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webview.loadUrl(url);
        webview.getSettings().setJavaScriptEnabled(true);
    }
}
