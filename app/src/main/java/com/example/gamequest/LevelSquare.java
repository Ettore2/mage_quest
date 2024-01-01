package com.example.gamequest;

import static com.example.gamequest.gameCalsses.GameInstance.debug;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelSquare {
    public static final int IMG_LOCKED = R.drawable.level_blocked,
            IMG_UNLOCKED = R.drawable.level_unlocked,
            IMG_COMPLETED = R.drawable.level_completed;
    public ImageButton btn;
    public TextView text;
    public LevelManager.Level level;
    public LevelsSelectionActivity context;

    public LevelSquare(ImageButton btn, TextView text, LevelManager.Level level, LevelsSelectionActivity context){
        this.btn = btn;
        this.text = text;
        this.level = level;
        this.context = context;

    }

    public void graphicUpdate(boolean available){
        if(level != null && available){
            if(LevelManager.getInstance().isCompleted(level)){
                btn.setImageResource(IMG_COMPLETED);
            }else {
                btn.setImageResource(IMG_UNLOCKED);
            }
        }else {
            btn.setImageResource(IMG_LOCKED);
        }
        text.setVisibility(level == null ? View.INVISIBLE : View.VISIBLE);

        if(level != null){
            text.setText(""+ level.id);
        }
    }
}
