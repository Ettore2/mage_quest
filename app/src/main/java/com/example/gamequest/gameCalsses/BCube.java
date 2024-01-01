package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;

public class BCube extends Box{
    public BCube(Point3D pos, GameInstance game, ImageView view) {
        super(pos, ID_BLOCK_WALL, TAG_B_CUBE, game, view, R.drawable.black_cube, R.drawable.black_cube_ph);
        canCollectCoin = true;
        //debug("black cube constructor end |pos: "+getPosition().toString());

    }

    @Override
    public void setPosition(Point3D p) {
        super.setPosition(p);
    }
}
