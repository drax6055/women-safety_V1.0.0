package com.example.women_sefety;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.women_sefety.databinding.ActivityContatctListBinding;

public class Contatct_List extends AppCompatActivity {
    ListView listView;
    MyDbHelper myDbHelper;
    androidx.appcompat.widget.Toolbar toolbar;
    private int REQUEST_CODE = 1001;
    MainAdapter adapter;

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String cNumber = null;
                try {
                    Cursor c = managedQuery( uri, null, null, null, null );
                    if (c.moveToFirst()) {
                        String id = c.getString( c.getColumnIndexOrThrow( ContactsContract.Contacts._ID ) );
                        String hasPhone = c.getString( c.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER ) );
                        if (hasPhone.compareTo( "1" ) == 0) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null );
                            phones.moveToFirst();
                            cNumber = phones.getString( phones.getColumnIndex( "data1" ) );
                        }
                        String name = c.getString( c.getColumnIndex( ContactsContract.Contacts.DISPLAY_NAME ) );
                        if (!cNumber.equals( "" ))
                            myDbHelper.insertContact( name, cNumber );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    adapter.refreshadapter();
                }
            } else {
                Toast.makeText( this, "Please select contacts", Toast.LENGTH_SHORT ).show();
            }
        } else {
            Toast.makeText( this, "OK REQUEST CODE", Toast.LENGTH_SHORT ).show();
        }
        super.onActivityResult( requestCode, resultCode, data );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_contatct_list );
        toolbar = findViewById( R.id.toolbar );
        toolbar.setNavigationIcon( R.drawable.backbtn_toolbar );
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        myDbHelper = new MyDbHelper( this );
        listView = findViewById( R.id.lstv );
        adapter = new MainAdapter( Contatct_List.this );
        listView.setAdapter( adapter );
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE );
        findViewById( R.id.fab ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
                startActivityForResult( intent, REQUEST_CODE );
            }
        } );
    }

}