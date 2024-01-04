package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.BoxCollider;
import com.example.gamequest.engine3D_V1.EngineObjectModel;
import com.example.gamequest.engine3D_V1.Point3D;
import com.example.gamequest.engine3D_V1.Vector3D;

import java.util.Vector;

public class PowerBullet extends GameObject{
    public static final float PROJECTILE_SPEED = 14f;

    public static class BulletBCube extends PowerBullet{
        public BulletBCube(Point3D pos, GameInstance game, ImageView view, int dir) {
            super(pos, ID_BLOCK_POWER_BULLET, game, view, R.drawable.black_cube_bullet,dir);

            //debug("BulletBCube constructor end at pos: "+pos.toString());
        }

        @Override
        protected void impact(GameObject impactObj) {
            //SoundManager.getInstance().playSound(R.raw.black_cube);
            //debug("execute impact with: "+impactObj.getTag()+" of pos: "+impactObj.getPosition().toString()+"player at pos: "+game.player.getPosition().toString());
            destroy();

            Point3D pos = getSpawnPos(impactObj);
            game.instantiateDynamicForegroundObj(ID_BLOCK_B_CUBE, pos);
        }
    }
    public static class BulletGrapple extends PowerBullet{
        GameObject impactObj;
        public BulletGrapple(Point3D pos, GameInstance game, ImageView view, int dir) {
            super(pos, ID_BLOCK_POWER_BULLET, game, view, R.drawable.grapple,dir);
            ((BoxCollider)(colliders.get(0))).size = new Vector3D(game.CELL_SIZE/2f, game.CELL_SIZE/2f,0);
            impactObj = null;

            if(dir == DIR_UP){
                spriteView.setRotation(-90);
            }
            if(dir == DIR_DOWN){
                spriteView.setRotation(90);
            }
            if(dir == DIR_LEFT){
                spriteView.setRotation(180);
            }
        }

        @Override
        protected void impact(GameObject impactObj) {
            //debug("execute impact with: "+impactObj.getTag()+" of pos: "+impactObj.getPosition().toString()+"player at pos: "+game.player.getPosition().toString());
            this.impactObj = impactObj;

            if(impactObj.canBeGrappled){
                if(impactObj.movable){
                    if(dir == DIR_UP){
                        impactObj.translate(new Vector3D(0, game.CELL_SIZE,0));
                        if(impactObj.getGreedY() == game.player.getGreedY()){
                            game.player.setAdjacentTo(dir, impactObj);
                        }
                    }
                    if(dir == DIR_DOWN){
                        impactObj.translate(new Vector3D(0, -game.CELL_SIZE,0));
                        if(impactObj.getGreedY() == game.player.getGreedY()){
                            game.player.setAdjacentTo(dir, impactObj);
                        }
                    }
                    if(dir == DIR_LEFT){
                        impactObj.translate(new Vector3D(game.CELL_SIZE,0,0));
                        if(impactObj.getGreedX() == game.player.getGreedX()){
                            game.player.setAdjacentTo(dir, impactObj);
                        }
                    }
                    if(dir == DIR_RIGHT){
                        impactObj.translate(new Vector3D(-game.CELL_SIZE,0,0));
                        if(impactObj.getGreedX() == game.player.getGreedX()){
                            game.player.setAdjacentTo(dir, impactObj);
                        }
                    }

                    destroy();
                }else {
                    game.player.grappleBullet = this;
                }
            }else {
                destroy();
            }


        }

        @Override
        protected void movement(float deltaT) {
            if (impactObj == null) {
                super.movement(deltaT);
            }

        }
    }
    public static class BulletPhasing extends PowerBullet{
        public BulletPhasing(Point3D pos, GameInstance game, ImageView view, int dir) {
            super(pos, ID_BLOCK_POWER_BULLET, game, view, R.drawable.phase_bullet,dir);
            ((BoxCollider)(colliders.get(0))).size = new Vector3D(game.CELL_SIZE/2f, game.CELL_SIZE/2f,0);
            canHitPhasingObjects = true;

            if(dir == DIR_UP){
                spriteView.setRotation(-90);
            }
            if(dir == DIR_DOWN){
                spriteView.setRotation(90);
            }
            if(dir == DIR_LEFT){
                spriteView.setRotation(180);
            }
        }

        @Override
        protected void impact(GameObject impactObj) {
            //prioritize the phasing objects
            Vector <GameObject> objs = game.getCellForeground(impactObj.getGreedX(), impactObj.getGreedY());
            objs.remove(impactObj);
            boolean found = false;
            for(int i = 0; i < objs.size() && !found; i++){
                if(objs.get(i).phasing){
                    found = true;
                    impactObj = objs.get(i);
                }
            }//prioritize the phasing objects


            if(!impactObj.phasing && (game.player.getPower(ID_POWER_PHASE).isInfinite() || game.player.getPower(ID_POWER_PHASE).amount > 0)){
                game.player.getPower(ID_POWER_PHASE).decreaseAmount();
                impactObj.setPhasing(!impactObj.phasing);
            }else if(impactObj.phasing) {
                game.player.getPower(ID_POWER_PHASE).addAmount(1);
                impactObj.setPhasing(!impactObj.phasing);
            }

            destroy();
        }
    }

    public final int dir;
    public boolean canHitPhasingObjects;
    public PowerBullet(Point3D pos, char id, GameInstance game, ImageView view, int imgRes, int dir) {
        super(pos, id, TAG_POWER_BULLET, game, view, imgRes, imgRes);
        colliders.add(new BoxCollider(this, new Point3D(0,0,0),new Vector3D(game.CELL_SIZE/10f,game.CELL_SIZE/10f,0)));
        useGravity = false;
        isObstacle = false;
        canCollectCoin = true;
        canHitPhasingObjects = false;
        this.dir = dir;
        game.foreground.add(this);

        //debug("yay");
    }


    @Override
    public void destroy() {
        if(!destroyed){
            game.foreground.remove(this);
            game.player.bullet = null;
        }

        super.destroy();
    }

    @Override
    public String getDescr() {
        return "default power descr";

    }
    @Override
    public void collision(EngineObjectModel obj, float deltaT) {
        //debug("execute collision");
        GameObject gameObj = (GameObject) obj;
        if(gameObj.isObstacle && !gameObj.getTag().equals(TAG_PLAYER) && !gameObj.getTag().equals(TAG_POWER_BULLET)){
            if(!gameObj.phasing || canHitPhasingObjects){
                impact(gameObj);
            }
        }
    }
    @Override
    public void logicUpdate(float deltaT) {
        super.logicUpdate(deltaT);

        movement(deltaT);
    }
    protected void movement(float deltaT){
        if(dir == DIR_UP){
            translate(new Vector3D(0,-PROJECTILE_SPEED*game.CELL_SIZE/deltaT,0));
        }
        if(dir == DIR_DOWN){
            translate(new Vector3D(0,PROJECTILE_SPEED*game.CELL_SIZE/deltaT,0));
        }
        if(dir == DIR_LEFT){
            translate(new Vector3D(-PROJECTILE_SPEED*game.CELL_SIZE/deltaT,0,0));
        }
        if(dir == DIR_RIGHT){
            translate(new Vector3D(PROJECTILE_SPEED*game.CELL_SIZE/deltaT,0,0));
        }
    }

    //function-methods
    protected void impact(GameObject impactObj){}
    protected Point3D getSpawnPos(GameObject impactObj){
        if (dir == DIR_UP){
            return impactObj.getPosition().sum(new Point3D(0,game.CELL_SIZE,0));
        }
        if (dir == DIR_DOWN){
            return impactObj.getPosition().sum(new Point3D(0,-game.CELL_SIZE,0));
        }
        if (dir == DIR_LEFT){
            return impactObj.getPosition().sum(new Point3D(game.CELL_SIZE,0,0));
        }
        if (dir == DIR_RIGHT){
            return impactObj.getPosition().sum(new Point3D(-game.CELL_SIZE,0,0));
        }
        return impactObj.getPosition();
    }
    protected void freeSpriteView(){
        game.availableImgs.add(spriteView);
        this.spriteView = null;

    }
}
