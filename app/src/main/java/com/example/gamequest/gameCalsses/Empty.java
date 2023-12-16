package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;

public class Empty extends GameObject{
    public Empty(Point3D pos, GameInstance game, ImageView view) {
        super(pos, ID_BLOCK_EMPTY, GameInstance.TAG_EMPTY, game, view, R.drawable.empty, R.drawable.empty);

    }

    @Override
    public String getDescr() {
        return null;
    }
}
