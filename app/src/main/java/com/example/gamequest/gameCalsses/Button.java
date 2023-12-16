package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;

public class Button extends GameObject{
    public Button(Point3D pos, GameInstance game, ImageView view) {
        super(pos, ID_BLOCK_BUTTON, GameInstance.TAG_BUTTON, game, view, R.drawable.default_img, R.drawable.default_img);

    }

    @Override
    public String getDescr() {
        return null;
    }
}
