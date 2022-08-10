package com.example.women_sefety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyOTPActivity extends AppCompatActivity {
    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private String vereficationId;
    String phoneData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_verify_otpactivity );
        TextView textPhoneNumber = findViewById( R.id.textPhoneNumber );
        textPhoneNumber.setText( String.format( "+91 - %s", getIntent().getStringExtra( "Mobile" ) ) );
        phoneData = textPhoneNumber.getText().toString().trim();
        findViewById( R.id.changeNum ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(verifyOTPActivity.this,sendOTPActivity.class);
                finishAffinity();
                startActivity( intent );
            }
        } );

        inputCode1 = findViewById( R.id.inputCode1 );
        inputCode2 = findViewById( R.id.inputCode2 );
        inputCode3 = findViewById( R.id.inputCode3 );
        inputCode4 = findViewById( R.id.inputCode4 );
        inputCode5 = findViewById( R.id.inputCode5 );
        inputCode6 = findViewById( R.id.inputCode6 );
        setOtpInputs();
        final ProgressBar progressBar = findViewById( R.id.prograssBar );
        final Button verifyBtn = findViewById( R.id.btn_Verify );
        vereficationId = getIntent().getStringExtra( "verificationId" );
        verifyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        || inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()) {
                    Toast.makeText( verifyOTPActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT ).show();
                    return;
                }
                String code = inputCode1.getText().toString() +
                        inputCode2.getText().toString() +
                        inputCode3.getText().toString() +
                        inputCode4.getText().toString() +
                        inputCode5.getText().toString() +
                        inputCode6.getText().toString();
                if (vereficationId != null) {
                    progressBar.setVisibility( View.VISIBLE );
                    verifyBtn.setVisibility( View.INVISIBLE );
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential( vereficationId, code );
                    FirebaseAuth.getInstance().signInWithCredential( phoneAuthCredential )
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility( View.GONE );
                                    verifyBtn.setVisibility( View.VISIBLE );
                                    if (task.isSuccessful()) {
                                        getSharedPreferences( "Login", Context.MODE_PRIVATE ).edit().putString( "number", phoneData ).apply();
                                        Intent intent = new Intent( getApplicationContext(), MainActivity.class );
                                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                        startActivity( intent );
                                    } else {
                                        Toast.makeText( verifyOTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT ).show();
                                    }
                                }
                            } );
                }
            }
        } );
        findViewById( R.id.txt_resendOtp ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( verifyOTPActivity.this, "Resending OTP please wait for while", Toast.LENGTH_SHORT ).show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra( "Mobile" ),
                        10,
                        TimeUnit.SECONDS, verifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText( verifyOTPActivity.this, "Generating new OTP", Toast.LENGTH_SHORT ).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText( verifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                vereficationId = newVerificationId;
                                Toast.makeText( verifyOTPActivity.this, "OTP SENT", Toast.LENGTH_SHORT ).show();
                            }
                        }
                );
            }
        } );
    }

    public void setOtpInputs() {
        inputCode1.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        inputCode2.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        inputCode3.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        inputCode4.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        inputCode5.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

    }
}