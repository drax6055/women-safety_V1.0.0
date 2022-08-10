package com.example.women_sefety;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class law extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    TextView link;
    TextView card_details;
    TextView click;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_law );
        toolbar = findViewById( R.id.toolbar );
        toolbar.setNavigationIcon( R.drawable.backbtn_toolbar ); // your drawable
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        click = findViewById( R.id.click );
        card_details = findViewById( R.id.card_details );
        layout = findViewById( R.id.layout );
        layout.getLayoutTransition().enableTransitionType( LayoutTransition.CHANGING );
        link = findViewById( R.id.link );
        link.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://ncw.nic.in/important-links/List-of-Laws-Related-to-Women" ) );
                startActivity( intent );
            }
        } );
    }

    public void expandCard(View view) {
        int v = (card_details.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int w = (click.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        TransitionManager.beginDelayedTransition( layout, new AutoTransition() );
        card_details.setVisibility( v );
        click.setVisibility( w );
    }
}