package com.example.women_sefety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class where_am_i extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.where_am_i );

        toolbar = findViewById( R.id.toolbar );
        toolbar.setNavigationIcon( R.drawable.backbtn_toolbar );
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.google_map );

        client = LocationServices.getFusedLocationProviderClient( this );

        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44 );
        }

    }

    public void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener( new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    supportMapFragment.getMapAsync( new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {

                            LatLng latlng = new LatLng( location.getLatitude(), location.getLongitude() );
                            MarkerOptions options = new MarkerOptions().position( latlng ).title( "You are here" );
                            options.icon( BitmapDescriptorFactory.fromResource( R.drawable.loc ) );
                            //zoom camera
                            googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlng, 20 ) );
                            //add marker on map
                            googleMap.addMarker( options );
                        }
                    } );
                }
            }
        } );
    }
}