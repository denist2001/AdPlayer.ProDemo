package com.example.adplayer.adplayerexample;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        try (InputStream inputStream = getResources().getAssets().open("InPage.html");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String nextLine;
            final StringBuilder stringBuilder = new StringBuilder();
            while ((nextLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(nextLine + "\n");
            }
            Log.d("Loading", stringBuilder.toString());
            final WebView webView = findViewById(R.id.web_view_player);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.loadData(stringBuilder.toString(), "text/html", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
