package com.example.gamequest;

import android.media.MediaPlayer;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

public class SoundManager{
    private static SoundManager instance;

    private MediaPlayer musicPlayer;
    private AppCompatActivity context;
    private int musicVol, effectsVol;


    //get instance
    public static SoundManager getInstance(AppCompatActivity context){
        if(instance == null){
            instance = new SoundManager(context);
        }else if(context != null){
            instance.context = context;
            instance.startMusicPlayer();

        }

        return instance;
    }
    public static SoundManager getInstance() {
        return getInstance(null);

    }


    //constructors
    private SoundManager(AppCompatActivity context){
        if(context != null){
            this.context = context;
        }


        //TODO: read volume from file
        musicVol = 50;
        effectsVol = 50;

        startMusicPlayer();

    }


    //other methods
    public void playSound(int soundRes){
        if(context != null){
            MediaPlayer mp = MediaPlayer.create(context, soundRes);
            mp.setVolume(effectsVol/100f, effectsVol/100f);
            mp.start();
        }

    }

    //function-methods
    private void startMusicPlayer(){
        if(musicPlayer == null && context != null){
            musicPlayer = MediaPlayer.create(context,R.raw.music);
            musicPlayer.setVolume(musicVol/100f,musicVol/100f);
            musicPlayer.start();
            musicPlayer.setLooping(true);
        }
    }
}
