package com.example.gamequest.gameCalsses;

import android.widget.ImageView;

import com.example.gamequest.engine3D_V1.Point3D;

public class PowerBullet extends GameObject{
    public PowerBullet(Point3D pos, char id, String tag, GameInstance game, ImageView view, int imgRes, int imgResPh) {
        super(pos, id, tag, game, view, imgRes, imgResPh);
    }

    @Override
    public String getDescr() {
        return null;
    }
}
