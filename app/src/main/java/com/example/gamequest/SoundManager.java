package com.example.gamequest;

import static com.example.gamequest.gameCalsses.GameInstance.debug;

import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SoundManager{
    private static final String PLAYER_VOLUME_FILE_NAME = "volume.txt";
    private static final int DEFAULT_VOLUME = 50;
    private static SoundManager instance;

    private MediaPlayer musicPlayer;
    private AppCompatActivity context;
    private int musicVol, effectsVol;


    //get instance
    public static SoundManager getInstance(AppCompatActivity context){
        if(instance == null){
            //debug("about to create soundManager");
            instance = new SoundManager(context);
            //debug("created soundManager");
        }

        if(context != null){
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

        File f = new File(context.getFilesDir()+"/"+PLAYER_VOLUME_FILE_NAME);
        //debug("get file");
        if (!f.exists()){
            //debug("file do not exist");
            try {
                //debug("about to create file");
                f.createNewFile();
                //debug("file created");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            musicVol = DEFAULT_VOLUME;
            effectsVol = DEFAULT_VOLUME;

            saveValuesOnFile();
        }
        //debug("about to load values");
        getValuesFromFile();

        //debug("about to startMusicPlayer");
        startMusicPlayer();

        //debug("constructor end");
    }


    //other methods
    public void playSound(int soundRes){
        if(context != null){
            MediaPlayer mp = MediaPlayer.create(context, soundRes);
            mp.setVolume(effectsVol/100f, effectsVol/100f);
            mp.start();
        }

    }
    public void setMusicVol(int musicVol) {
        this.musicVol = musicVol;

        if(musicPlayer != null){
            musicPlayer.setVolume(musicVol/100f, musicVol/100f);
        }
    }
    public void setEffectsVol(int effectsVol) {
        this.effectsVol = effectsVol;

    }
    public int getMusicVol(){
        return musicVol;

    }
    public int getEffectsVol(){
        return effectsVol;

    }
    synchronized public void saveValuesOnFile(){
        //debug("about to save things on file");
        //debug(context.getFilesDir()+"/"+PLAYER_VOLUME_FILE_NAME);
        File f = new File(context.getFilesDir()+"/"+PLAYER_VOLUME_FILE_NAME);
        //debug("got file");
        //debug("the file does not exist");
        try {
            //debug("created stream");
            FileOutputStream stream = new FileOutputStream(f);
            //debug("create stream");
            stream.write(new byte[]{(byte) musicVol, (byte) effectsVol});
            //debug("write stream");
            //debug("write  on volume files");
            stream.close();
            //debug("close stream");
            //debug("file created");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    //function-methods
    public void startMusicPlayer(){
        if(musicPlayer == null){
            if(context != null){
                musicPlayer = MediaPlayer.create(context,R.raw.music);
                musicPlayer.setVolume(musicVol/100f,musicVol/100f);
                musicPlayer.start();
                musicPlayer.setLooping(true);
            }
        }else {
            musicPlayer.start();
        }
    }
    public void stopMusicPlayer(){
        if(musicPlayer != null){
            musicPlayer.pause();
        }

    }
    private void getValuesFromFile(){
        File f = new File(context.getFilesDir()+"/"+PLAYER_VOLUME_FILE_NAME);
        int length = (int) f.length();
        byte[] bytes = new byte[length];
        FileInputStream in = null;

        //debug("about to get last completed level");
        try {
            in = new FileInputStream(f);
            in.read(bytes);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //debug("last completed level: "+(int)new String(bytes).charAt(0));
        String vals = new String(bytes);

        musicVol = vals.charAt(0);
        effectsVol = vals.charAt(1);
    }
}
