package com.example.women_sefety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

public class emergency_numberList extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_emergency_number_list );
        toolbar = findViewById( R.id.toolbar );
        toolbar.setNavigationIcon( R.drawable.backbtn_toolbar ); // your drawable
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

//        button call event Women Helpline - 1091
        findViewById( R.id.btn_eme_num_1 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eme_number = "1091";
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData( Uri.parse("tel:" + eme_number));
                startActivity(phone_intent);
            }
        } );
//        button call event Women Helpline - 181 (Domestic abuse)
        findViewById( R.id.btn_eme_num_2 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eme_number = "181";
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData( Uri.parse("tel:" + eme_number));
                startActivity(phone_intent);
            }
        } );
//        button call event Police Station- 100
        findViewById( R.id.btn_eme_num_3 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eme_number = "100";
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData( Uri.parse("tel:" + eme_number));
                startActivity(phone_intent);
            }
        } );
//        button call event Ambulance - 108
        findViewById( R.id.btn_eme_num_4 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eme_number = "108";
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData( Uri.parse("tel:" + eme_number));
                startActivity(phone_intent);
            }
        } );
//        button call event Fire Station - 101
        findViewById( R.id.btn_eme_num_5 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eme_number = "101";
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData( Uri.parse("tel:" + eme_number));
                startActivity(phone_intent);
            }
        } );
//        button call event Blood Bank - 1910
        findViewById( R.id.btn_eme_num_6 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eme_number = "1910";
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                phone_intent.setData( Uri.parse("tel:" + eme_number));
                startActivity(phone_intent);
            }
        } );
    }
}