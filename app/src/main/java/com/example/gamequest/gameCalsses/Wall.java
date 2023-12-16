package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;

public class Wall extends GameObject{
    public Wall(Point3D pos, int id, String tag,GameInstance game, ImageView view, int imgRes, int imgResPh) {
        super(pos, id, tag, game, view, imgRes, imgResPh);
        createDefaultCollider();
        isObstacle = true;
    }
    public Wall(Point3D pos, GameInstance game, ImageView view) {
        this(pos, ID_BLOCK_WALL, GameInstance.TAG_WALL, game, view, R.drawable.wall, R.drawable.wall_ph);

    }

    @Override
    public String getDescr() {
        return null;
    }
}
