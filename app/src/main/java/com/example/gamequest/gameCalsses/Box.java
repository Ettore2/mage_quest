package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.ID_BLOCK_BOX;
import static com.example.gamequest.gameCalsses.GameInstance.debug;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;

public class Box extends GameObject{
    public Box(Point3D pos, char id, String tag,GameInstance game, ImageView view, int imgRes, int imgResPh) {
        super(pos, id, tag, game, view, imgRes, imgResPh);
        createDefaultCollider();
        isObstacle = true;
        usePhysic = true;
        useGravity = true;
        movable = true;
    }
    public Box(Point3D pos, GameInstance game, ImageView view) {
        this(pos, ID_BLOCK_BOX, GameInstance.TAG_BOX, game, view, R.drawable.box, R.drawable.box_ph);
    }

    @Override
    public boolean canBePushed(GameObject pushingObj) {
        return super.canBePushed(pushingObj);

    }

    //GameObject overrides
    @Override
    public String getDescr() {
        return null;
    }
}
