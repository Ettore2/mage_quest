package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;
import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;

public class NonGrabbableWall extends Wall{
    public NonGrabbableWall(Point3D pos, GameInstance game, ImageView view) {
        super(pos,ID_BLOCK_NON_GRABBABLE_WALL, TAG_NON_GRABBABLE_WALL,game, view,R.drawable.non_grabbable_wall,R.drawable.non_grabbable_wall_ph);
        canBeGrappled = false;
    }
}
