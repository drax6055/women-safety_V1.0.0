package com.example.women_sefety;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class camera extends AppCompatActivity {
    ImageView imageView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_camera );

        imageView = findViewById( R.id.imageView );
        Intent intent = new Intent( "android.media.action.IMAGE_CAPTURE" );
        startActivityForResult( intent, 7 );

        toolbar = findViewById( R.id.toolbar );
        toolbar.setNavigationIcon( R.drawable.backbtn_toolbar );
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        findViewById( R.id.btn_shareImg ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageandText( bitmap );
            }
        } );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
      if(requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get( "data" );
            imageView.setImageBitmap( bitmap );
        }
      else{
          camera.this.finish();
      }
    }

    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getmageToShare( bitmap );
        Intent intent = new Intent( Intent.ACTION_SEND );
        intent.putExtra( Intent.EXTRA_STREAM, uri );
        intent.putExtra( Intent.EXTRA_TEXT, "Shared by:\n Women_Sefety Application" );
        intent.putExtra( Intent.EXTRA_SUBJECT, "Subject Here" );
        intent.setType( "image/png" );
        startActivity( Intent.createChooser( intent, "Share Via" ) );
    }

    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File( getCacheDir(), "images" );
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File( imagefolder, "shared_image.png" );
            FileOutputStream outputStream = new FileOutputStream( file );
            bitmap.compress( Bitmap.CompressFormat.JPEG, 100, outputStream );
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile( this, "com.example.women_sefety", file );
        } catch (Exception e) {
            Toast.makeText( this, e.getMessage(), Toast.LENGTH_LONG ).show();
        }
        return uri;
    }
}