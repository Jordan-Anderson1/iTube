package com.example.itube;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WatchActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_watch);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        WebView webView = findViewById(R.id.webView);

        intent = getIntent();
        String videoId = intent.getStringExtra("url");

        String video = String.format("<iframe width=\"100%%\" height=\"100%%\" src=\"https://www.youtube.com/embed/%s\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer;\" \"autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture;\" +\"web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>", videoId);

        webView.loadData(video, "text/html", "utf-8");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());


    }
}