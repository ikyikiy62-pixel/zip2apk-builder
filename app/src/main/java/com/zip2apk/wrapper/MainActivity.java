package com.zip2apk.wrapper;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        Map<String, String> config = readConfig();
        String sourceType = config.getOrDefault("SOURCE_TYPE", "zip");
        String remoteUrl = config.getOrDefault("REMOTE_URL", "");

        if ("url".equals(sourceType) && remoteUrl != null && !remoteUrl.isEmpty()) {
            webView.loadUrl(remoteUrl);
        } else {
            webView.loadUrl("file:///android_asset/web/index.html");
        }
    }

    private Map<String, String> readConfig() {
        Map<String, String> map = new HashMap<>();
        try {
            InputStream is = getAssets().open("config.properties");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                int idx = line.indexOf('=');
                if (idx > 0) {
                    map.put(line.substring(0, idx).trim(), line.substring(idx + 1).trim());
                }
            }
            reader.close();
        } catch (Exception e) {
            // config tidak ditemukan, pakai default (mode zip)
        }
        return map;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
                                      }
