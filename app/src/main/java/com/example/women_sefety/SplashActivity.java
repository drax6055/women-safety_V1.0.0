package com.example.women_sefety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    private static int SPLAST_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.activity_splash );
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep( 1000 );
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    String id = getSharedPreferences( "Login", Context.MODE_PRIVATE ).getString( "number", null );
                    if (id == null) {
                        startActivity( new Intent( SplashActivity.this, sendOTPActivity.class ) );
                    } else {
                        startActivity( new Intent( SplashActivity.this, MainActivity.class ) );
                    }
                    finish();
                }
            }
        };
        thread.start();
    }

}