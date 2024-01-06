package com.example.gamequest;

import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;

public class PressShower implements View.OnTouchListener{

    Integer filterPressed;

    public PressShower(int overlapColor){
        this.filterPressed = overlapColor;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                v.getBackground().setColorFilter(filterPressed, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                v.getBackground().clearColorFilter();
                v.invalidate();
                break;
            }
        }
        return false;
    }
}
