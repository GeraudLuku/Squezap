package com.geraud.quizzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.plugins.RxJavaPlugins;

public class MainActivity extends AppCompatActivity {

    private static final String ADMOB_APP_ID = "ca-app-pub-4709428567137080~4107467797";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxJavaPlugins.setErrorHandler(e -> {
            Log.d("Main Activity",e.getLocalizedMessage());
        });

    }
}
