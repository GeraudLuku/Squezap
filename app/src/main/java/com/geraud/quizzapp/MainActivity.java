package com.geraud.quizzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private static final String ADMOB_APP_ID = "ca-app-pub-4709428567137080~4107467797";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, ADMOB_APP_ID);
    }
}
