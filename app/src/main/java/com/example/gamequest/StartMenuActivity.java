package com.example.gamequest;

import static com.example.gamequest.gameCalsses.GameInstance.debug;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.example.gamequest.R;

public class StartMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//(hide softkey)
        setContentView(R.layout.activity_start_menu);

        SoundManager.getInstance(this);
        SoundManager.getInstance().startMusicPlayer();


    }


    public void playLevelsBtn(View view){
        SoundManager.getInstance().playSound(R.raw.button_click);
        startActivity(new Intent(this, LevelsSelectionActivity.class));
        finish();

    }
    public void optionsBtn(View view){
        SoundManager.getInstance().playSound(R.raw.button_click);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //debug("hi");

        SoundManager.getInstance().startMusicPlayer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //debug("goodbye");

        SoundManager.getInstance().stopMusicPlayer();
    }
}