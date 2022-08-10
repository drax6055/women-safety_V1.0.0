package com.example.women_sefety;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.Transaction;


import java.util.List;
import java.util.Locale;

//AppCompatActivity

public class blogger extends YouTubeBaseActivity {

    TextView card_details, card_details1, card_details2;
    LinearLayout layout, layout1, layout2;
    YouTubePlayerView youTubePlayerView;
    ImageView downarrow1, downarrow2, downarrow3;
    Button btn_playYt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_blogger );
        setLocale(getSharedPreferences("Preferred_Language",MODE_PRIVATE).getString("lang","en"));
        downarrow1 = findViewById( R.id.downarrow1 );
        downarrow2 = findViewById( R.id.downarrow2 );
        downarrow3 = findViewById( R.id.downarrow3 );

        card_details = findViewById( R.id.card_details );
        card_details1 = findViewById( R.id.card_details1 );
        card_details2 = findViewById( R.id.card_details2 );

        layout = findViewById( R.id.layout );
        layout1 = findViewById( R.id.layout1 );
        layout2 = findViewById( R.id.layout2 );

        layout.getLayoutTransition().enableTransitionType( LayoutTransition.CHANGING );
        layout1.getLayoutTransition().enableTransitionType( LayoutTransition.CHANGING );
        layout2.getLayoutTransition().enableTransitionType( LayoutTransition.CHANGING );
        youTubePlayerView = findViewById( R.id.youtube_player_view );

        YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo( "EGEuFGs2kdQ" );
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText( getApplicationContext(), "Initialization Failed", Toast.LENGTH_SHORT ).show();
            }
        };

        btn_playYt = findViewById( R.id.btn_playYt );
        btn_playYt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize( "AIzaSyBF3X73IM-xG03G2WSdRlVTW39uM1Z2xn8", listener );
                btn_playYt.setVisibility( View.GONE );
            }
        } );
        //        bottom navigation
        BottomNavigationView bottomNavigationView = findViewById( R.id.navigation );
        bottomNavigationView.setSelectedItemId( R.id.bottomNavigationBlog );
        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomNavigationhome:
                        startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.bottomNavigationBlog:
                        return true;
                    case R.id.bottomNavigationMap:
                        startActivity( new Intent( getApplicationContext(), search.class ) );
                        overridePendingTransition( 0, 0 );
                        return true;
                }
                return false;
            }
        } );

    }

    public void expandCard(View view) {
        int v = (card_details.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int w = (downarrow1.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        TransitionManager.beginDelayedTransition( layout, new AutoTransition() );
        card_details.setVisibility( v );
        downarrow1.setVisibility( w );
    }

    public void expandCard1(View view) {
        int v = (card_details1.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int w = (downarrow2.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        TransitionManager.beginDelayedTransition( layout1, new AutoTransition() );
        card_details1.setVisibility( v );
        downarrow2.setVisibility( w );
    }
    public void expandCard2(View view) {
        int v = (card_details2.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int w = (downarrow3.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        TransitionManager.beginDelayedTransition( layout2, new AutoTransition() );
        card_details2.setVisibility( v );
        downarrow3.setVisibility( w );
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged( newConfig );
        getBaseContext().getResources().updateConfiguration( newConfig, getBaseContext().getResources().getDisplayMetrics() );
        setContentView( R.layout.activity_blogger);
    }
    private void setLocale(String s) {
        Locale locale=new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getSharedPreferences("Preferred_Language",MODE_PRIVATE).edit().putString("lang",s).apply();
        onConfigurationChanged(configuration);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit( 0 );
    }
}