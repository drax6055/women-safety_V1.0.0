package com.example.women_sefety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class search extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.google_map );

        client = LocationServices.getFusedLocationProviderClient( this );

        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44 );
        }


        //on button click open map
        findViewById( R.id.btn_serchPlace ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( "geo: 22.6916, 72.8634" ) );
                Intent chooser = Intent.createChooser( intent, "Launch map" );
                startActivity( chooser );
            }
        } );
        findViewById( R.id.btn_map_hospital ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener( new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            supportMapFragment.getMapAsync( new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    googleMap.clear();
                                    LatLng latlng = new LatLng( location.getLatitude(), location.getLongitude() );
                                    LatLng latlngP1 = new LatLng( 22.679649,72.873188);
                                    LatLng latlngP2 = new LatLng( 22.689081,72.871696);
                                    LatLng latlngP3 = new LatLng( 22.691495,72.862724);
                                    LatLng latlngP4 = new LatLng( 22.689227,72.872414 );
                                    LatLng latlngP5 = new LatLng( 22.692618,72.855290);

                                    MarkerOptions options = new MarkerOptions().position( latlng ).title( "You are here" );
                                    options.icon( BitmapDescriptorFactory.fromResource( R.drawable.loc ) );

                                    MarkerOptions optionsP1 = new MarkerOptions().position( latlngP1 ).title( "Civil Hospital Nadiad" );
                                    optionsP1.icon( BitmapDescriptorFactory.fromResource( R.drawable.telephone ) );

                                    MarkerOptions optionsP2 = new MarkerOptions().position( latlngP2 ).title( "Sheth H J Mahagujarat Hospital" );
                                    optionsP2.icon( BitmapDescriptorFactory.fromResource( R.drawable.telephone ) );

                                    MarkerOptions optionsP3 = new MarkerOptions().position( latlngP3 ).title( "Shreenath Hospital" );
                                    optionsP3.icon( BitmapDescriptorFactory.fromResource( R.drawable.telephone ) );

                                    MarkerOptions optionsP4 = new MarkerOptions().position( latlngP4 ).title( "DR. VIKRAM HOSPITAL" );
                                    optionsP4.icon( BitmapDescriptorFactory.fromResource( R.drawable.telephone ) );

                                    MarkerOptions optionsP5 = new MarkerOptions().position( latlngP5 ).title( "Keya Hospitals" );
                                    optionsP5.icon( BitmapDescriptorFactory.fromResource( R.drawable.telephone ) );

                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlng, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP1, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP2, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP3, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP4, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP5, 14 ) );

                                    googleMap.addMarker( options );
                                    googleMap.addMarker( optionsP1 );
                                    googleMap.addMarker( optionsP2 );
                                    googleMap.addMarker( optionsP3 );
                                    googleMap.addMarker( optionsP4 );
                                    googleMap.addMarker( optionsP5 );
                                    googleMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(@NonNull Marker marker) {
                                            Dialog dialog = new Dialog( search.this );
                                            dialog.setContentView( R.layout.dialog_hospital_info );
                                            dialog.setCancelable( true );
                                            ImageView hcall1 = dialog.findViewById( R.id.hcall1 );
                                            ImageView hcall2 = dialog.findViewById( R.id.hcall2 );
                                            ImageView hcall3 = dialog.findViewById( R.id.hcall3 );
                                            ImageView hcall4 = dialog.findViewById( R.id.hcall4 );
                                            ImageView hcall5 = dialog.findViewById( R.id.hcall5 );

                                            hcall1.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String eme_number = "02682529074";
                                                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                                                    phone_intent.setData( Uri.parse("tel:" + eme_number));
                                                    startActivity(phone_intent);
                                                }
                                            } );

                                            hcall2.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String eme_number = "02682526221";
                                                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                                                    phone_intent.setData( Uri.parse("tel:" + eme_number));
                                                    startActivity(phone_intent);
                                                }
                                            } );
                                            hcall3.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String eme_number = "02682567505";
                                                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                                                    phone_intent.setData( Uri.parse("tel:" + eme_number));
                                                    startActivity(phone_intent);
                                                }
                                            } );
                                            hcall4.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String eme_number = "07567828686";
                                                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                                                    phone_intent.setData( Uri.parse("tel:" + eme_number));
                                                    startActivity(phone_intent);
                                                }
                                            } );
                                            hcall5.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String eme_number = "09033031132";
                                                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                                                    phone_intent.setData( Uri.parse("tel:" + eme_number));
                                                    startActivity(phone_intent);
                                                }
                                            } );
                                            dialog.show();
                                            return true;
                                        }
                                    } );
                                }
                            } );
                        }
                    }
                } );
            }
        } );
        findViewById( R.id.btn_map_police ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener( new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            supportMapFragment.getMapAsync( new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    googleMap.clear();
                                    LatLng latlng = new LatLng( location.getLatitude(), location.getLongitude() );
                                    LatLng latlngP1 = new LatLng( 22.6941772, 72.8584392 );
                                    LatLng latlngP2 = new LatLng( 22.697118,72.8629673 );
                                    LatLng latlngP3 = new LatLng( 22.6985439, 72.8549872 );
                                    LatLng latlngP4 = new LatLng( 22.7091596, 72.8609389 );
                                    LatLng latlngP5 = new LatLng( 22.690597, 72.8552629 );

                                    MarkerOptions options = new MarkerOptions().position( latlng ).title( "You are here" );
                                    options.icon( BitmapDescriptorFactory.fromResource( R.drawable.loc ) );

                                    MarkerOptions optionsP1 = new MarkerOptions().position( latlngP1 ).title( "Nadiad Main Police Station" );
                                    optionsP1.icon( BitmapDescriptorFactory.fromResource( R.drawable.police ) );

                                    MarkerOptions optionsP2 = new MarkerOptions().position( latlngP2 ).title( "Amdavadi Bazzar Police Station" );
                                    optionsP2.icon( BitmapDescriptorFactory.fromResource( R.drawable.police ) );

                                    MarkerOptions optionsP3 = new MarkerOptions().position( latlngP3 ).title( "Subhash Nagar Police Station" );
                                    optionsP3.icon( BitmapDescriptorFactory.fromResource( R.drawable.police ) );

                                    MarkerOptions optionsP4 = new MarkerOptions().position( latlngP4 ).title( "Jawahar Police Choki" );
                                    optionsP4.icon( BitmapDescriptorFactory.fromResource( R.drawable.police ) );

                                    MarkerOptions optionsP5 = new MarkerOptions().position( latlngP5 ).title( "Vallabh nager Police Station" );
                                    optionsP5.icon( BitmapDescriptorFactory.fromResource( R.drawable.police ) );

                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlng, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP1, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP2, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP3, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP4, 14 ) );
                                    googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlngP5, 14 ) );

                                    googleMap.addMarker( options );
                                    googleMap.addMarker( optionsP1 );
                                    googleMap.addMarker( optionsP2 );
                                    googleMap.addMarker( optionsP3 );
                                    googleMap.addMarker( optionsP4 );
                                    googleMap.addMarker( optionsP5 );
                                    googleMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(@NonNull Marker marker) {
                                            Dialog dialog = new Dialog( search.this );
                                            dialog.setContentView( R.layout.dialog_policestation_info );
                                            dialog.setCancelable( true );
                                            ImageView pcall1 = dialog.findViewById( R.id.pcall1 );
                                            ImageView pcall3 = dialog.findViewById( R.id.pcall3 );
                                            ImageView pcall4 = dialog.findViewById( R.id.pcall4 );

                                            pcall1.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String eme_number = "2682550232";
                                                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                                                    phone_intent.setData( Uri.parse("tel:" + eme_number));
                                                    startActivity(phone_intent);
                                                }
                                            } );

                                            pcall3.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String eme_number = "2682561800";
                                                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                                                    phone_intent.setData( Uri.parse("tel:" + eme_number));
                                                    startActivity(phone_intent);
                                                }
                                            } );
                                            pcall4.setOnClickListener( new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String eme_number = "2682568087";
                                                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                                                    phone_intent.setData( Uri.parse("tel:" + eme_number));
                                                    startActivity(phone_intent);
                                                }
                                            } );
                                            dialog.show();
                                            return true;
                                        }
                                    } );
                                }
                            } );
                        }
                    }
                } );
            }
        } );
        //        bottom navigation
        BottomNavigationView bottomNavigationView = findViewById( R.id.navigation );
        bottomNavigationView.setSelectedItemId( R.id.bottomNavigationMap );
        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomNavigationhome:
                        startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.bottomNavigationBlog:
                        startActivity( new Intent( getApplicationContext(), blogger.class ) );
                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.bottomNavigationMap:
                        return true;
                }
                return false;
            }
        } );
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
                            googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latlng, 15 ) );
                            //add marker on map
                            googleMap.addMarker( options );
                        }
                    } );
                }
            }
        } );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit( 0 );
    }
}