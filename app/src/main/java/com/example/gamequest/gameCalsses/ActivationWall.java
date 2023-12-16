package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;

public class ActivationWall extends Wall{
    public ActivationWall(Point3D pos, GameInstance game, ImageView view) {
        super(pos,ID_BLOCK_ACTIVATION_WALL, TAG_ACTIVATION_WALL, game, view, R.drawable.default_img, R.drawable.default_img);

    }
}
