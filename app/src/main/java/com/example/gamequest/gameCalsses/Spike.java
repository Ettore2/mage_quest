package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.BoxCollider;
import com.example.gamequest.engine3D_V1.EngineObjectModel;
import com.example.gamequest.engine3D_V1.Point3D;
import com.example.gamequest.engine3D_V1.Vector3D;

public class Spike extends GameObject{
    public Spike(Point3D pos, GameInstance game, ImageView view, int id) {
        super(pos, ID_BLOCK_SPIKE_UP ,GameInstance.TAG_SPIKE, game, view, R.drawable.spike, R.drawable.spike);
        switch (id){
            case ID_BLOCK_SPIKE_UP:
                spriteView.setRotation(0);
                this.id = ID_BLOCK_SPIKE_UP;
                colliders.add(new BoxCollider(this, new Point3D(0,game.CELL_SIZE/4f,0),new Vector3D(game.CELL_SIZE*2f/6,game.CELL_SIZE/6f,0)));
                break;
            case GameInstance.ID_BLOCK_SPIKE_DOWN:
                spriteView.setRotation(180);
                this.id = GameInstance.ID_BLOCK_SPIKE_DOWN;
                colliders.add(new BoxCollider(this, new Point3D(0,-game.CELL_SIZE/4f,0),new Vector3D(game.CELL_SIZE*2f/6,game.CELL_SIZE/6f,0)));
                break;
            case GameInstance.ID_BLOCK_SPIKE_LEFT:
                spriteView.setRotation(270);
                this.id = GameInstance.ID_BLOCK_SPIKE_LEFT;
                colliders.add(new BoxCollider(this, new Point3D(game.CELL_SIZE/4f,0,0),new Vector3D(game.CELL_SIZE/6f,game.CELL_SIZE*2f/6,0)));
                break;
            case GameInstance.ID_BLOCK_SPIKE_RIGHT:
                spriteView.setRotation(90);
                this.id = GameInstance.ID_BLOCK_SPIKE_RIGHT;
                colliders.add(new BoxCollider(this, new Point3D(-game.CELL_SIZE/4f,0,0),new Vector3D(game.CELL_SIZE/6f,game.CELL_SIZE*2f/6,0)));
                break;
        }
        //debug("create spike at: "+this.getPosition().toString());

        //setPosition(getPosition());//update the level
        usePhysic = false;
        useGravity = false;
    }

    @Override
    public String getDescr() {
        return null;
    }

}
