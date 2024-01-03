package com.example.gamequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import java.util.Vector;

public class StartMenuActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private Vector<View> musicOptionsViews;
    private boolean optionsVisible;
    private SeekBar musicBar, effectsBar;
    private SoundManager soundManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//(hide softkey)
        setContentView(R.layout.activity_start_menu);

        soundManager = SoundManager.getInstance(this);
        soundManager.startMusicPlayer();

        musicBar = findViewById(R.id.seekBar_music_vol);
        musicBar.setProgress(soundManager.getMusicVol());
        musicBar.setOnSeekBarChangeListener(this);
        effectsBar = findViewById(R.id.seekBar_effects_vol);
        effectsBar.setProgress(soundManager.getEffectsVol());
        effectsBar.setOnSeekBarChangeListener(this);

        musicOptionsViews = new Vector<>();
        musicOptionsViews.add(findViewById(R.id.img_vol_options));
        musicOptionsViews.add(findViewById(R.id.text_music_vol));
        musicOptionsViews.add(findViewById(R.id.text_effects_vol));
        musicOptionsViews.add(musicBar);
        musicOptionsViews.add(effectsBar);

        optionsVisible = false;

        graphicUpdate();
    }


    public void playLevelsBtn(View view){
        soundManager.playSound(R.raw.button_click);
        startActivity(new Intent(this, LevelsSelectionActivity.class));
        finish();

    }
    public void optionsBtn(View view){
        soundManager.playSound(R.raw.button_click);

        optionsVisible = !optionsVisible;

        graphicUpdate();
    }

    public void graphicUpdate(){
        for(int i = 0; i < musicOptionsViews.size(); i++){
            musicOptionsViews.get(i).setVisibility(optionsVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //debug("hi");

        soundManager.startMusicPlayer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //debug("goodbye");

        soundManager.stopMusicPlayer();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar == musicBar){
            soundManager.setMusicVol(progress);
        }else {
            soundManager.setEffectsVol(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        soundManager.saveValuesOnFile();

    }
}