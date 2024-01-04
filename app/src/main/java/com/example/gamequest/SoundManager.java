package com.example.gamequest;

import static com.example.gamequest.gameCalsses.GameInstance.debug;

import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.function.BiConsumer;

public class SoundManager{
    private static final String PLAYER_VOLUME_FILE_NAME = "volume.txt";
    private static final int DEFAULT_VOLUME = 50;
    private static SoundManager instance;

    private MediaPlayer musicPlayer;
    private AppCompatActivity context;
    private int musicVol, effectsVol;
    private Hashtable<Integer, MediaPlayer> sounds;


    //get instance
    public static SoundManager getInstance(AppCompatActivity context){
        if(instance == null){
            //debug("about to create soundManager");
            instance = new SoundManager(context);
            //debug("created soundManager");
        }

        if(context != null){
            if(instance.context != context){
                instance.context = context;
                instance.initializeSounds();
            }
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
        sounds = new Hashtable<>();


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
        if(context != null && effectsVol != 0){
            if(!sounds.containsKey(soundRes)){
                sounds.put(soundRes, MediaPlayer.create(context, soundRes));
                sounds.get(soundRes).setVolume(effectsVol, effectsVol);
            }

            debug(soundRes+"");
            if(sounds.get(soundRes).isPlaying()){
                sounds.get(soundRes).seekTo(0);
            }else {
                sounds.get(soundRes).start();
            }


        }

    }
    public void setMusicVol(int musicVol) {
        this.musicVol = musicVol;

        if(musicPlayer != null){
            musicPlayer.setVolume(musicVol/100f, musicVol/100f);
        }

        if(musicVol == 0){
            if(musicPlayer.isPlaying()){
                stopMusicPlayer();
            }
        }else {
            if(!musicPlayer.isPlaying()){
                startMusicPlayer();
            }
        }
    }
    public void setEffectsVol(int effectsVol) {
        this.effectsVol = effectsVol;
        sounds.forEach(new BiConsumer<Integer, MediaPlayer>() {
            @Override
            public void accept(Integer integer, MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(effectsVol/100f, effectsVol/100f);
            }
        });

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
                musicPlayer.setLooping(true);
            }
        }

        if(musicVol != 0){
            musicPlayer.start();
            //debug("start music");

        }

    }
    public void stopMusicPlayer(){
        if(musicPlayer != null){
            musicPlayer.pause();
            //debug("stop music");
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
    private void initializeSounds(){
        if(context != null){
            //debug("initialize sounds");
            sounds.clear();

            sounds.put(R.raw.black_cube, MediaPlayer.create(context, R.raw.black_cube));
            sounds.put(R.raw.black_bullet, MediaPlayer.create(context, R.raw.black_bullet));
            sounds.put(R.raw.jump, MediaPlayer.create(context, R.raw.jump));
            sounds.put(R.raw.win, MediaPlayer.create(context, R.raw.win));
            sounds.put(R.raw.player_death, MediaPlayer.create(context, R.raw.player_death));
            sounds.put(R.raw.coin_collect, MediaPlayer.create(context, R.raw.coin_collect));
            sounds.put(R.raw.coin_destroy, MediaPlayer.create(context, R.raw.coin_destroy));
            sounds.put(R.raw.yellow_cube, MediaPlayer.create(context, R.raw.yellow_cube));
            sounds.put(R.raw.phasing_bullet, MediaPlayer.create(context, R.raw.phasing_bullet));
            sounds.put(R.raw.phasing, MediaPlayer.create(context, R.raw.phasing));
            sounds.put(R.raw.grapple_bullet, MediaPlayer.create(context, R.raw.grapple_bullet));
            sounds.put(R.raw.button_click, MediaPlayer.create(context, R.raw.button_click));
            sounds.put(R.raw.teleport, MediaPlayer.create(context, R.raw.teleport));
            sounds.put(R.raw.power_select, MediaPlayer.create(context, R.raw.power_select));
            sounds.put(R.raw.power_deselect, MediaPlayer.create(context, R.raw.power_deselect));

            sounds.forEach(new BiConsumer<Integer, MediaPlayer>() {
                @Override
                public void accept(Integer integer, MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(effectsVol/100f, effectsVol/100f);
                }
            });
        }

    }
}
