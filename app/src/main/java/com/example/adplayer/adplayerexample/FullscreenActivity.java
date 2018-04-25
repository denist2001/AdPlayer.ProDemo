package com.example.adplayer.adplayerexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        ListView listView = findViewById(R.id.list_view);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 40; ++i) {
            list.add("Item number " + i);
        }

        ListArrayAdapter adapter = new ListArrayAdapter(this, list);

        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    private class AdLifecycleListener {

        @JavascriptInterface
        void mobPlayerProStart() {
            Log.d("TEST", "start");

        }

        @JavascriptInterface
        void mobPlayerProStop() {
            Log.d("TEST", "stop");
        }
    }

    private class ListArrayAdapter extends ArrayAdapter<String> {

        private String BASE_URL = "file:///android_asset/"; //"https://cdn.stat-rock.com"
        private String ENCODING = "UTF-8";
        private String MIME_TYPE = "text/html";
        private String HISTORY_URL = BASE_URL;

        HashMap<Integer, String> mIdMap = new HashMap<Integer, String>();

        public ListArrayAdapter(Context context,
                                List<String> objects) {
            super(context, -1, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(i, objects.get(i));
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            if (!item.isEmpty()) {
                return position;
            }
            return -1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (position % 2 == 0  ) {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.height = 400;
                final WebView webView = new WebView(parent.getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true);
                }
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setAllowFileAccess(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                    webView.getSettings().setAllowFileAccessFromFileURLs(true);
                }
                webView.getSettings().setAllowContentAccess(true);
//                    webView.addJavascriptInterface(new AdLifecycleListener(), "lifecycleListener");
//                    addContentView(webView, params);
                webView.setLayoutParams(params);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    String playerJs = loadLocalFile(parent, "demo.js");
//                    playerJs = playerJs + loadLocalFile(parent, "InPageLocalVideo.js");
//                    webView.loadUrl("javascript:(" + playerJs + ")()");
//                }
                String bannerHtml = loadLocalFile(parent, "InPageLocalVideo.html");
                Log.d("Loading", bannerHtml);
                webView.loadDataWithBaseURL(BASE_URL, bannerHtml, MIME_TYPE, ENCODING, HISTORY_URL);
                return webView;
            }
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_for_recycler, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.textView);
            textView.setText(mIdMap.get(position));
            return rowView;
        }

        private String loadLocalFile(@NonNull ViewGroup parent, @NonNull String filename) {
            final StringBuilder stringBuilder = new StringBuilder();
            try (InputStream inputStream = parent.getResources().getAssets().open(filename);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                String nextLine;
                while ((nextLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(nextLine + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
    }
}
