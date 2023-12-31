package com.example.gamequest;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class LevelSquare {
    public static final int IMG_LOCKED = R.drawable.level_blocked,
            IMG_UNLOCKED = R.drawable.level_unlocked,
            IMG_COMPLETED = R.drawable.level_completed;
    public static final int IMG_LOCKED_SELECTED = R.drawable.level_blocked_selected,
            IMG_UNLOCKED_SELECTED = R.drawable.level_unlocked_selected,
            IMG_COMPLETED_SELECTED = R.drawable.level_completed_selected;
    public ImageButton btn;
    public TextView text;
    public LevelsManager.Level level;
    public LevelsSelectionActivity context;

    public LevelSquare(ImageButton btn, TextView text, LevelsManager.Level level, LevelsSelectionActivity context){
        this.btn = btn;
        this.text = text;
        this.level = level;
        this.context = context;

    }

    public void graphicUpdate(boolean available, boolean isSelected){
        if(level != null && available){
            if(LevelsManager.getInstance().isCompleted(level)){
                btn.setImageResource(isSelected ? IMG_COMPLETED_SELECTED : IMG_COMPLETED);
            }else {
                btn.setImageResource(isSelected ? IMG_UNLOCKED_SELECTED : IMG_UNLOCKED);
            }
        }else {
            btn.setImageResource(isSelected ? IMG_LOCKED_SELECTED : IMG_LOCKED);
        }
        text.setVisibility(level == null ? View.INVISIBLE : View.VISIBLE);

        if(level != null){
            text.setText(""+ level.id);
        }
    }
}
