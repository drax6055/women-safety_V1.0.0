package com.example.women_sefety;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class sendOTPActivity extends AppCompatActivity {
    private static final int CREDENTIAL_PICKER_REQUEST = 120;
    public static String PREFS_NUMBER;
    EditText edt_inputMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_send_otpactivity );
        if (!isconnected()) {
            Dialog dialog = new Dialog( sendOTPActivity.this );
            dialog.setContentView( R.layout.dialog_internet_connection );
            dialog.setCancelable( false );
            Button btn_reftesh = dialog.findViewById( R.id.btn_reftesh );
            btn_reftesh.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity( intent );
                }
            } );
            dialog.show();
        } else {
            edt_inputMobileNumber = findViewById( R.id.edt_inputMobileNumber );
            checkPermission();
//            phoneNumSuggesion();
        }
        findViewById( R.id.text_skip ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( sendOTPActivity.this, MainActivity.class );
                finishAffinity();
                startActivity( intent );

            }
        } );
    }

    // suggestions phone number
    public void phoneNumSuggesion() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported( true )
                .build();
        PendingIntent intent = Credentials.getClient( this ).getHintPickerIntent( hintRequest );
        try {
            startIntentSenderForResult( intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle() );
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
            Credential credentials = data.getParcelableExtra( Credential.EXTRA_KEY );
            edt_inputMobileNumber.setText( credentials.getId().substring( 3 ) );
        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            Toast.makeText( this, "No phone numbers found", Toast.LENGTH_LONG ).show();
        }


    }

    //runtime permition dialogbox
    public void grantedPermission() {
        final EditText inputMobileNumber = findViewById( R.id.edt_inputMobileNumber );
        Button GetOtp = findViewById( R.id.btn_GetOtp );
        PREFS_NUMBER = inputMobileNumber.getText().toString();
        final ProgressBar progressBar = findViewById( R.id.prograssbar );

        GetOtp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              checkLogin();
                if (inputMobileNumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText( sendOTPActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT ).show();
                    return;
                }
                progressBar.setVisibility( View.VISIBLE );
                GetOtp.setVisibility( View.INVISIBLE );

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + inputMobileNumber.getText().toString(),
                        10,
                        TimeUnit.SECONDS, sendOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility( View.GONE );
                                GetOtp.setVisibility( View.VISIBLE );
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility( View.GONE );
                                GetOtp.setVisibility( View.VISIBLE );
                                Toast.makeText( sendOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility( View.GONE );
                                GetOtp.setVisibility( View.VISIBLE );
                                Intent intent = new Intent( getApplicationContext(), verifyOTPActivity.class );
                                intent.putExtra( "Mobile", inputMobileNumber.getText().toString() );
                                intent.putExtra( "verificationId", verificationId );
                                startActivity( intent );
                            }
                        }
                );
            }
        } );
    }

    public void checkPermission() {
        Dexter.withContext( this )
                .withPermissions(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener( new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                grantedPermission();
                phoneNumSuggesion();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }

        } ).check();

    }

    private boolean isconnected() {
        //check network connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService( Context.CONNECTIVITY_SERVICE );
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }
}