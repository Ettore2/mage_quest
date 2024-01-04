package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.ID_BLOCK_COIN;
import static com.example.gamequest.gameCalsses.GameInstance.TAG_BOX;
import static com.example.gamequest.gameCalsses.GameInstance.TAG_COIN;
import static com.example.gamequest.gameCalsses.GameInstance.TAG_PLAYER;
import static com.example.gamequest.gameCalsses.GameInstance.TAG_SPIKE;
import static com.example.gamequest.gameCalsses.GameInstance.debug;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.SoundManager;
import com.example.gamequest.engine3D_V1.BaseAnimation;
import com.example.gamequest.engine3D_V1.BoxCollider;
import com.example.gamequest.engine3D_V1.EngineObjectModel;
import com.example.gamequest.engine3D_V1.Point3D;
import com.example.gamequest.engine3D_V1.Vector3D;

import java.util.Vector;

public class Coin extends Box{
    protected static final float ANIM_FRAME_DURATION = 200;
    protected BaseAnimation<Integer> animIdle;
    protected boolean collected;

    //the coin is pushed only because of the non clipping function, because it is not an obstacle it will not the put
    //in the thisFBlock* variables and will not thain the push functions
    //( this make it so that il will always get crushed to the right)
    public Coin(Point3D pos, GameInstance game, ImageView view) {
        super(pos, ID_BLOCK_COIN, TAG_COIN, game, view, R.drawable.coin_0, R.drawable.coin_0);
        colliders.add(new BoxCollider(this, new Point3D(0,0,0),new Vector3D(game.CELL_SIZE/2f, game.CELL_SIZE*5/8f,0)));
        isObstacle = false;
        collected = false;
        ignoreHorizontalClipping = true;

        Vector<Integer> imgsRes = new Vector<>();
        imgsRes.add(R.drawable.coin_0);
        imgsRes.add(R.drawable.coin_1);
        imgsRes.add(R.drawable.coin_2);
        imgsRes.add(R.drawable.coin_1);
        animIdle = new BaseAnimation(imgsRes, ANIM_FRAME_DURATION);
        haveInitializedGraphic = true;
    }


    //other methods
    public void collect(){
        if(!destroyed){
            //SoundManager.getInstance().playSound(R.raw.coin_collect);
            game.collectCoin();
            collected = true;

            destroy();
        }
    }
    @Override
    public void destroy(){
        if(!destroyed && ! collected){
            //SoundManager.getInstance().playSound(R.raw.coin_destroy);
        }

        super.destroy();
    }
    public BoxCollider getPickUpCollider(){
        if(colliders != null && colliders.size() >=2){
            return (BoxCollider) colliders.get(1);
        }else{

        }
        return null;

    }

    @Override
    public void collision(EngineObjectModel obj, float deltaT) {
        super.collision(obj, deltaT);
        GameObject otherObj = (GameObject) obj;

        if(!otherObj.phasing){
            if(otherObj.getTag().equals(TAG_PLAYER) && getPickUpCollider().isColliding2D(otherObj.getFirstColl()) && otherObj.canCollectCoin){
                collect();
                //debug("collect by player");
            }
            if(!otherObj.getTag().equals(TAG_PLAYER) && !otherObj.destroyed && !otherObj.getTag().equals(TAG_SPIKE) && sameCell(otherObj)){
                //debug("collision detected with:"+otherObj.getTag());
                if(otherObj.canCollectCoin){
                    collect();
                }else{
                    //debug("collision");
                    destroy();
                }

            }
        }
    }
    @Override
    public void graphicUpdate(float deltaT) {
        super.graphicUpdate(deltaT);

        if(haveInitializedGraphic){
            animIdle.update(deltaT);
            spriteView.setImageResource((Integer)(animIdle.getCurrFrame().info));
        }
        //debug("coing"+getPosition().toString());

    }
    @Override
    public String getDescr() {
        return GameInstance.ID_BLOCK_COIN + " 1";

    }
}
