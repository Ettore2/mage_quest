package com.example.gamequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gamequest.R;

public class StartMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//(hide softkey)
        setContentView(R.layout.activity_start_menu);

        SoundManager.getInstance(this);


    }


    public void playLevelsBtn(View view){
        SoundManager.getInstance().playSound(R.raw.button_click);
        startActivity(new Intent(this, LevelsSelectionActivity.class));
        finish();

    }
    public void optionsBtn(View view){
        SoundManager.getInstance().playSound(R.raw.button_click);

    }
}