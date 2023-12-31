package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;
import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;

public class YCube extends Box{
    public YCube(Point3D pos, GameInstance game, ImageView view) {
        super(pos, ID_BLOCK_Y_CUBE, TAG_Y_CUBE, game, view, R.drawable.yellow_cube, R.drawable.yellow_cube_ph);
        canCollectCoin = true;
        useGravity = false;
    }
}
