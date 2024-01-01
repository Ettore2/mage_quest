package com.example.gamequest;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gamequest.gameCalsses.Power;

public class PowerImgButton {
    Power power;
    ImageButton button;
    TextView text;

    //constructor
    PowerImgButton(ImageButton btn, TextView txt, Power p){
        this.button = btn;
        this.text = txt;
        this.power = p;
    }


    //other methods
    public void graphicUpdate(){
        if(power != null){
            button.setImageResource(power.getImgRes());
            button.setVisibility(View.VISIBLE);

            button.setEnabled(power.isUsable());

            if(power.isInfinite()){
                text.setVisibility(View.INVISIBLE);
            }else {
                text.setVisibility(View.VISIBLE);
                text.setText(String.valueOf(power.getAmount()));
            }

        }else{
            button.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
        }



    }



}
