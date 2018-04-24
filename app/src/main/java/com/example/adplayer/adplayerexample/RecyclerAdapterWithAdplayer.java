package com.example.adplayer.adplayerexample;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RecyclerAdapterWithAdplayer extends RecyclerView.Adapter<RecyclerAdapterWithAdplayer.ViewHolder> {

    private String BASE_URL = "https://cdn.stat-rock.com";
    private String ENCODING = "UTF-8";
    private String MIME_TYPE = "text/html";
    private String HISTORY_URL = BASE_URL;

    int firstPosition;
    int interval;

    public RecyclerAdapterWithAdplayer(int firstPosition, int interval) {
        this.firstPosition = firstPosition;
        this.interval = interval;
    }

    @NonNull
    @Override
    public RecyclerAdapterWithAdplayer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_for_recycler, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterWithAdplayer.ViewHolder holder, int position) {
        if (Math.abs(position - firstPosition) % interval == 0) {

            try (InputStream inputStream = holder.itemView.getResources().getAssets().open("InPage.html");
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                String nextLine;
                final StringBuilder stringBuilder = new StringBuilder();
                while ((nextLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(nextLine + "\n");
                }
                Log.d("Loading", stringBuilder.toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true);
                }
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                final WebView webView = new WebView(holder.itemView.getContext()); //findViewById(R.id.web_view_player);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setAllowFileAccess(true);
//            webView.addJavascriptInterface(new AdLifecycleListener(), "lifecycleListener");
//                addContentView(webView, params);
                webView.loadDataWithBaseURL(BASE_URL, stringBuilder.toString(), MIME_TYPE, ENCODING, HISTORY_URL);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            holder.textView
        }
        holder.textView.setText("Item with position " + position);
    }

    @Override
    public int getItemCount() {
        return 40;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public ViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }
}
