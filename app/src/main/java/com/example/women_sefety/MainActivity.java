package com.example.women_sefety;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btn_siren;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    MyDbHelper myDbHelper;
    com.google.android.gms.location.LocationRequest locationRequest;
    private LocationCallback locationCallback;
    FusedLocationProviderClient client;
    private Location currentLocation;
    SettingContentObserver settingsContentObserver;
    String loc;
    PowerManager pm;
    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AppName:tag");
        wakeLock.acquire();
        myDbHelper = new MyDbHelper(this);
        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        settingsContentObserver = new SettingContentObserver(MainActivity.this, new Handler());
        LoadLanguage();
        getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, settingsContentObserver);

        locationRequest = new com.google.android.gms.location.LocationRequest();
        locationRequest.setPriority(LocationRequest.QUALITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        currentLocation = location;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "LocationResult is null", Toast.LENGTH_LONG).show();
                }
            }
        };
        //check location permission for request
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, settingsContentObserver);
        //check network connection
        if (!isconnected()) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_internet_connection);
            dialog.setCancelable(false);
            Button btn_reftesh = dialog.findViewById(R.id.btn_reftesh);
            btn_reftesh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            dialog.show();
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton bar = (ImageButton) findViewById(R.id.bar);
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomsheet();
            }
        });
        client = LocationServices.getFusedLocationProviderClient(this);
//        start add contact number btn--------------------------------------
        findViewById(R.id.btn_add_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Contatct_List.class));
            }
        });
//        end add contact number btn--------------------------------------
        findViewById(R.id.btn_panic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        sendMessageToAllContacts();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 44);
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
//        start siren btn -------------------------------------------
        btn_siren = findViewById(R.id.btn_siren);
        btn_siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog_siren);
                dialog.setCancelable(true);
                Button btn_PlaySiren = dialog.findViewById(R.id.btn_PlaySiren);
                btn_PlaySiren.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer == null) {
                            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);  //set Audio at max
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren);
                            mediaPlayer.start();
                        }
                    }
                });
                Button btn_stopSiren = dialog.findViewById(R.id.btn_stopSiren);
                btn_stopSiren.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer = null;
                        }
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });
//end siren btn-------------------------------
//        start emergency contact button activity---------------------
        findViewById(R.id.btn_Emergency_contect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, emergency_numberList.class));
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 44);
                }
            }
        });
//        end emergency contact button activity   -------------------------------
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, camera.class));
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 44);
                }
            }
        });
        findViewById(R.id.btn_law).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, law.class));
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomNavigationhome:
                        return true;
                    case R.id.bottomNavigationBlog:
                        Intent intent = new Intent(getApplicationContext(), blogger.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bottomNavigationMap:
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent1 = new Intent(getApplicationContext(), search.class);
                            startActivity(intent1);
                            overridePendingTransition(0, 0);
                            return true;
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        }
                }
                return false;
            }
        });
        findViewById(R.id.preEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.pre_event_dialog);
                    dialog.setCancelable(false);
                    TextView txt_time = (TextView) dialog.findViewById(R.id.txt_time);
                    Button btn_cancle = (Button) dialog.findViewById(R.id.btn_cancle);
                    dialog.show();

                    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long l) {
                            txt_time.setText("" + l / 1000);
                        }

                        @Override
                        public void onFinish() {
                            sendMessageToAllContacts();
                            dialog.dismiss();
                        }
                    };
                    countDownTimer.start();
                    btn_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            countDownTimer.cancel();
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 44);
                }
            }
        });
        findViewById(R.id.where_am_i).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, where_am_i.class));
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
        findViewById(R.id.selfDefence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, selfDefence.class));
            }
        });

        findViewById(R.id.shareLocationbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    void sendMessageToAllContacts() {
        Cursor cursor = myDbHelper.MobileNumber();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                try {
                    String number = cursor.getString(2);
                    SmsSender(number, "I am in trouble, Please help me. hear is my location\n" + "http://maps.google.com/maps?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "\n Sent by Women_safety Application");
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_sms_success);
            dialog.setCancelable(true);
            ImageView sms_cancle = dialog.findViewById(R.id.sms_cancle);
            sms_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            notificationSound();
        } else {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_no_contact_found);
            dialog.setCancelable(true);
            ImageView sms_cancle = dialog.findViewById(R.id.sms_cancle);
            sms_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void notificationSound() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);  //set Audio at max
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.notification);
        mediaPlayer.start();
    }

    void noti() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Message Sent", "Message sent", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Message Sent");
            builder.setContentTitle("Message Sent Successfully");
            builder.setContentText("Your Alert message Successfully sent to your contacts.");
            builder.setSmallIcon(R.drawable.pre_alert);
            builder.setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
            managerCompat.notify(1, builder.build());
        }
    }

    private void showBottomsheet() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(MainActivity.this);
        bottomSheet.setContentView(R.layout.bottom_sheet_dialog_layout);
        ImageView img_closeBottom = bottomSheet.findViewById(R.id.img_closeBottom);
        img_closeBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismiss();
            }
        });
        Button btn_userGuide = bottomSheet.findViewById(R.id.btn_userGuide);
        btn_userGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, nav_userguide.class));
                bottomSheet.dismiss();
            }
        });

        Button btn_settings = bottomSheet.findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String languages[] = {"English", "हिन्दी", "ગુજરાતી"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Choose your Language");
                dialog.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                setLocale("en");
                                break;
                            case 1:
                                setLocale("hi");
                                break;
                            case 2:
                                setLocale("gu");
                                break;
                        }

                        recreate();

                    }
                });
                dialog.setCancelable(true);
                dialog.create().show();
            }
        });
        Button btn_aboutus = bottomSheet.findViewById(R.id.btn_aboutus);
        btn_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, nav_aboutus.class));
                bottomSheet.dismiss();
            }
        });
        Button btn_rate = bottomSheet.findViewById(R.id.btn_rate);
        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_rate_us);
                dialog.setCancelable(true);
                float userRate = 0;
                ImageView emoji_image = dialog.findViewById(R.id.emoji_image);
                RatingBar rateBar = dialog.findViewById(R.id.rateBar);
                rateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if (rating <= 1) {
                            emoji_image.setImageResource(R.drawable.onestar);
                        } else if (rating <= 2) {
                            emoji_image.setImageResource(R.drawable.two_stare);
                        } else if (rating <= 3) {
                            emoji_image.setImageResource(R.drawable.three_emoji);
                        } else if (rating <= 4) {
                            emoji_image.setImageResource(R.drawable.four_stare);
                        } else if (rating <= 5) {
                            emoji_image.setImageResource(R.drawable.five_emoji);
                        }
                        animateImage(emoji_image);
                        float userRate = rating;
                    }
                });
                Button btn_Rate_now = dialog.findViewById(R.id.btn_Rate_now);
                btn_Rate_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        bottomSheet.dismiss();
                        Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
                    }
                });
                Button btn_Rate_Later = dialog.findViewById(R.id.btn_Rate_Later);
                btn_Rate_Later.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        bottomSheet.dismiss();
                    }
                });
                dialog.show();
            }
        });
        Button btn_logout = bottomSheet.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                finishAffinity();//clear all previous  activity
                Toast.makeText(MainActivity.this, "Successfully Logout", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, sendOTPActivity.class));
            }
        });
        Button btn_shareApplication = bottomSheet.findViewById(R.id.btn_shareApplication);
        btn_shareApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share via");
                    String shareMessage = "Women_Safety Application : \n https://play.google.com/store/apps/details?=" + BuildConfig.APPLICATION_ID + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(intent, "Share via"));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bottomSheet.show();
    }

    private boolean isconnected() {
        //check network connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }

    public void SmsSender(String phoneno, String textdata) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneno, null, textdata, null, null);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            loc = "http://maps.google.com/maps?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
            intent.putExtra(Intent.EXTRA_TEXT, loc);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share Location");
            startActivity(Intent.createChooser(intent, "Share.."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void animateImage(ImageView emoji_image) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        emoji_image.startAnimation(scaleAnimation);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
    }

    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getSharedPreferences("Preferred_Language", MODE_PRIVATE).edit().putString("lang", s).apply();
        onConfigurationChanged(configuration);
    }

    private void LoadLanguage() {
        String lang = getSharedPreferences("Preferred_Language", MODE_PRIVATE).getString("lang", "en");
        if (!lang.isEmpty()) {
            setLocale(lang);
        }
    }

    @Override
    protected void onDestroy() {
        client.removeLocationUpdates(locationCallback);
        getApplicationContext().getContentResolver().unregisterContentObserver(settingsContentObserver);
        wakeLock.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
}

