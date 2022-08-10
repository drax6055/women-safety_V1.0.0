package com.example.women_sefety;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

import java.util.Objects;

public class SettingContentObserver extends ContentObserver {
    int previousVolume;
    AudioManager audio;
    MainActivity parentActivity;
    String currentPattern="";
    public SettingContentObserver(Context c,Handler handler) {
        super( handler );
        parentActivity=(MainActivity)c;
        audio = (AudioManager) parentActivity.getSystemService(Context.AUDIO_SERVICE);
        previousVolume =audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        AudioManager audio = (AudioManager) parentActivity.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = Objects.requireNonNull(audio).getStreamVolume(AudioManager.STREAM_MUSIC);
        int delta = previousVolume - currentVolume;
        if (delta > 0) {
            currentPattern=currentPattern+"-";
            previousVolume = currentVolume;
        } else if (delta < 0) {
            currentPattern=currentPattern+"+";
            previousVolume = currentVolume;
        }
        if(currentPattern.length()==4 && (currentPattern.equals("+-+-") || currentPattern.equals("-+-+"))){
            try
            {
                parentActivity.sendMessageToAllContacts();
                parentActivity.noti();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if(currentPattern.length()>=4){
            currentPattern="";
        }
        System.out.println(currentPattern);
    }

}
