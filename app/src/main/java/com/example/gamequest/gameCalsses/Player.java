package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.SoundManager;
import com.example.gamequest.engine3D_V1.AnimationFrame;
import com.example.gamequest.engine3D_V1.BaseAnimation;
import com.example.gamequest.engine3D_V1.BoxCollider;
import com.example.gamequest.engine3D_V1.EngineObjectModel;
import com.example.gamequest.engine3D_V1.Point3D;
import com.example.gamequest.engine3D_V1.Vector3D;

import java.util.Vector;

public class Player extends GameObject{

    public static final float ANIM_FRAME_DURATION = 350f;
    public static final float NORMAL_GROUND_SPEED = 5.3f, NORMAL_AIR_SPEED = 5.3f;// multiply with CELL_SIZE, scaled with frameRate
    public static final float NORMAL_PUSHING_SPEED = NORMAL_GROUND_SPEED*80/100f;// multiply with CELL_SIZE, scaled with frameRate
    public static final float NORMAL_JUMP_FORCE = 11.8f;// multiply with CELL_SIZE, scaled with frameRate
    public static final float NORMAL_JUMP_DECELERATION = GRAVITY_FORCE*60/100;// multiply with CELL_SIZE, scaled with frameRate
    public static final float NORMAL_GRAPPLE_SPEED = 10f;
    public static final int FRAMES_FOR_PUSH_START = 8;
    protected Vector<Power> powers;
    protected ImageView spriteViewDown;

    public boolean alive;
    public PowerBullet.BulletGrapple grappleBullet;
    protected int thisFrameMoveIntent;
    protected boolean inputMoveUp, inputMoveDown, inputMoveLeft, inputMoveRight;
    protected boolean inputJump;
    protected Power inputPower;
    protected BaseAnimation animMove, animIdle, animAirIdle, animAirIdleDown, animAirMove, animAirMoveDown;
    protected int thisFramePushDir, pushingFrames;
    protected Point3D pushEndPos;


    public PowerBullet bullet;


    //constructors
    public Player(Point3D pos, GameInstance game, ImageView view, ImageView view2) {
        super(pos, ID_BLOCK_PLAYER, GameInstance.TAG_PLAYER, game, view, R.drawable.player_0, R.drawable.player_0);
        powers = new Vector<>();
        thisFMovementDir = DIR_STOP;
        thisFrameMoveIntent = DIR_STOP;

        alive = true;
        usePhysic = true;
        useGravity = true;
        canCollectCoin = true;
        pushEndPos = null;
        grappleBullet = null;
        bullet = null;

        spriteViewDown = view2;
        if(spriteViewDown == null){
            spriteViewDown = new ImageView(game.context);
            game.layout.addView(spriteViewDown);
        }
        setDefaultViewValues(spriteViewDown);
        spriteViewDown.setVisibility(ImageView.INVISIBLE);
        colliders.add(new BoxCollider(this, new Point3D(0,game.CELL_SIZE/32f,0),new Vector3D(game.CELL_SIZE*6/8f, game.CELL_SIZE*15/16f,0)));


        Vector<AnimationFrame> frames = new Vector<>();
        frames.add(new AnimationFrame<Integer>(R.drawable.player_0, ANIM_FRAME_DURATION));
        frames.add(new AnimationFrame<Integer>(R.drawable.player_1, ANIM_FRAME_DURATION));
        animIdle = new BaseAnimation(frames);

        frames = new Vector<>();
        frames.add(new AnimationFrame<Integer>(R.drawable.player_move_0, ANIM_FRAME_DURATION));
        frames.add(new AnimationFrame<Integer>(R.drawable.player_move_1, ANIM_FRAME_DURATION));
        animMove = new BaseAnimation(frames);

        frames = new Vector<>();
        frames.add(new AnimationFrame<Integer>(R.drawable.player_jump_0, ANIM_FRAME_DURATION));
        animAirIdle = new BaseAnimation(frames);

        frames = new Vector<>();
        frames.add(new AnimationFrame<Integer>(R.drawable.player_jump_down_0, ANIM_FRAME_DURATION));
        animAirIdleDown = new BaseAnimation(frames);

        frames = new Vector<>();
        frames.add(new AnimationFrame<Integer>(R.drawable.player_jump_move_0, ANIM_FRAME_DURATION));
        animAirMove = new BaseAnimation(frames);

        frames = new Vector<>();
        frames.add(new AnimationFrame<Integer>(R.drawable.player_jump_move_down_0, ANIM_FRAME_DURATION));
        animAirMoveDown = new BaseAnimation(frames);

        haveInitializedGraphic = true;

    }


    //other methods
    public boolean havePower(int powerId){
        for(int i = 0; i < powers.size(); i++){
            if(powers.get(i).getId() == powerId){
                return true;
            }
        }
        return false;

    }
    public void addPower(Power p){
        if(p.getId() != GameInstance.ID_POWER_NOTHING){
            for(int i = 0; i < powers.size(); i++){
                if(powers.get(i).getId() == p.getId()){
                    powers.get(i).addAmount(p.getAmount());
                    //debug("not new power");
                    return;
                }
            }
            powers.add(p);
            //debug("new power");
        }

    }
    public Power getPower(int powerId){
        for(int i = 0; i < powers.size(); i++){
            if(powers.get(i).getId() == powerId){
                return powers.get(i);
            }
        }
        return null;

    }
    public Vector<Power> getPowers(){
        Vector<Power> v =new Vector();
        for(int i = 0; i < powers.size(); i++){
            v.add(powers.get(i));
        }

        return v;

    }
    public void removePowers(){
        powers = new Vector<>();

    }


    //input methods
    public void inputMovement(int direction){
        switch (direction){
            case DIR_UP:
                inputMoveUp = true;
                break;
            case DIR_DOWN:
                inputMoveDown = true;
                break;
            case DIR_LEFT:
                inputMoveLeft = true;
                break;
            case DIR_RIGHT:
                inputMoveRight = true;
                break;
        }

    }
    public void inputJump(){
        if(grounded && !isPushing() && bullet == null){
            inputJump = true;
        }

    }
    public void inputPower(Power p){
        this.inputPower = p;

    }


    //function-methods
    protected boolean isPushing(){
        return pushingFrames >= FRAMES_FOR_PUSH_START;

    }
    protected void die(){
        /*

         */
        if(alive && game.currState == STATE_PLAYING){
            SoundManager.getInstance().playSound(R.raw.player_death);
            //debug("player die");
            alive = false;
            game.currState = STATE_LOST;
            game.context.graphicUpdate();


            destroy();
        }
    }


    //GameObject overrides

    @Override
    public void destroy() {
        if(!destroyed){
            spriteViewDown.setVisibility(ImageView.INVISIBLE);
        }
        super.destroy();
    }
    @Override
    public void reset(){
        alive = true;
        bullet = null;
        grappleBullet = null;
        removePowers();
        setDefaultViewValues(spriteViewDown);

        super.reset();
    }


    @Override
    protected void horizontalMovement(float deltaT){
        if(grappleBullet == null){
            float movementAmount;
            if(bullet == null){
                if(grounded){
                    if(!isPushing()){
                        movementAmount = NORMAL_GROUND_SPEED;
                    }else {
                        movementAmount = NORMAL_PUSHING_SPEED;
                    }

                }else {
                    movementAmount = NORMAL_AIR_SPEED;
                }
                movementAmount = game.CELL_SIZE*movementAmount*deltaT/1000;

                if(!isPushing()){
                    if(thisFMovementDir == DIR_RIGHT){
                        translate(new Vector3D(movementAmount ,0,0));
                    }
                    if(thisFMovementDir == DIR_LEFT){
                        translate(new Vector3D(-movementAmount ,0,0));
                    }
                }else {
                    //debug("push dir:" +thisFramePushDir+"   "+(thisFBlockRight==null)+" "+(thisFBlockLeft==null));
                    if(thisFramePushDir == DIR_RIGHT){
                        translate(new Vector3D(movementAmount ,0,0));
                        thisFObstacleRight.push(this, movementAmount);
                    }
                    if(thisFramePushDir == DIR_LEFT){
                        translate(new Vector3D(-movementAmount ,0,0));
                        thisFObstacleLeft.push(this, -movementAmount);
                    }
                }
            }
        }

    }
    @Override
    protected void verticalMovement(float deltaT){
        if(grappleBullet == null){
            //Y = 0 -> up (fall = augment the y)
            //jump
            if(grounded && inputJump && !isPushing()){
                //SoundManager.getInstance().playSound(R.raw.jump);
                grounded = false;
                currScaledYForce = -NORMAL_JUMP_FORCE*game.CELL_SIZE*deltaT/1000;
            }

            super.verticalMovement(deltaT);

        }

    }
    @Override
    public String getDescr() {
        return GameInstance.ID_BLOCK_PLAYER + " 0";

    }
    @Override
    public void setPosition(Point3D p) {
        super.setPosition(p);

        if(spriteViewDown != null){
            spriteViewDown.setX(p.x);
            spriteViewDown.setY(p.y+game.CELL_SIZE);
            spriteViewDown.setZ(p.z);
        }

    }


    //engineObjectModel overrides

    @Override
    protected void gravity(float deltaT) {
        //fall (Y = 0 -> up) (fall = augment the y)
        if(!grounded && currScaledYForce < game.CELL_SIZE*MAX_FALL_FORCE*deltaT/1000){
            if(currScaledYForce < 0){
                currScaledYForce += game.CELL_SIZE*NORMAL_JUMP_DECELERATION*deltaT/1000;
            }else {
                currScaledYForce += game.CELL_SIZE*GRAVITY_FORCE*deltaT/1000;
            }

            if(currScaledYForce > game.CELL_SIZE*MAX_FALL_FORCE*deltaT/1000){
                currScaledYForce = game.CELL_SIZE*MAX_FALL_FORCE*deltaT/1000;
            }
        }
    }
    @Override
    public void logicUpdate(float deltaT) {
        super.logicUpdate(deltaT);

        //grapple movement
        if(grappleBullet != null){
            if(grappleBullet.dir == DIR_UP){
                translate(new Vector3D(0,-game.CELL_SIZE*NORMAL_GRAPPLE_SPEED*deltaT/1000));
            }
            if(grappleBullet.dir == DIR_DOWN){
                translate(new Vector3D(0,game.CELL_SIZE*NORMAL_GRAPPLE_SPEED*deltaT/1000));
            }
            if(grappleBullet.dir == DIR_LEFT){
                translate(new Vector3D(-game.CELL_SIZE*NORMAL_GRAPPLE_SPEED*deltaT/1000,0));
            }
            if(grappleBullet.dir == DIR_RIGHT){
                translate(new Vector3D(game.CELL_SIZE*NORMAL_GRAPPLE_SPEED*deltaT/1000,0));
            }
        }

        //reset status
        thisFrameMoveIntent = DIR_STOP;
        if(inputPower == null) {
            if (inputMoveRight && !inputMoveLeft) {
                thisFrameMoveIntent = DIR_RIGHT;
            }

            if (inputMoveLeft && !inputMoveRight) {
                thisFrameMoveIntent = DIR_LEFT;
            }
            thisFMovementDir = thisFrameMoveIntent;

        }else{
            int chosenDir = DIR_STOP;
            if(inputMoveLeft){
                chosenDir = DIR_LEFT;
            }
            if(inputMoveRight){
                chosenDir = DIR_RIGHT;
            }
            if(inputMoveDown){
                chosenDir = DIR_DOWN;
            }
            if(inputMoveUp){
                chosenDir = DIR_UP;
            }

            //TODO: use the power (check if the power allow that dir)
        }


        //powersActivation
    }

    @Override
    public void collision(EngineObjectModel obj, float deltaT) {
        super.collision(obj, deltaT);

        if(obj.getTag().equals(TAG_SPIKE)){
            die();

        }

        if(obj.equals(grappleBullet)){
            grappleBullet.destroy();
            grappleBullet = null;
            bullet = null;
        }// destroy the grapple once you reach it

    }

    @Override
    public void postCollisionUpdate(float deltaT) {
        super.postCollisionUpdate(deltaT);

        //debug((thisFBlockRight == null) +"   "+(thisFBlockLeft == null)+"    "+pushingFrames);
        if(grounded) {
            //debug("gr");
            if(!isPushing()){
                //debug("!ps");
                if(thisFrameMoveIntent == DIR_STOP){
                    thisFramePushDir = DIR_STOP;
                    pushingFrames = 0;
                }
                if(thisFrameMoveIntent == DIR_RIGHT){
                    //debug("DIR_RIGHT "+(thisFBlockRight == null)+ "  "+pushingFrames);
                    if(thisFObstacleRight != null && thisFObstacleRight.canBePushed(this) ){
                        if(thisFramePushDir != DIR_RIGHT){
                            pushingFrames = 0;
                        }
                        thisFramePushDir = DIR_RIGHT;
                        pushingFrames++;
                    }else {
                        thisFramePushDir = DIR_STOP;
                        pushingFrames = 0;
                    }
                }
                if(thisFrameMoveIntent == DIR_LEFT){
                    //debug("DIR_LEFT");
                    if(thisFObstacleLeft != null && thisFObstacleLeft.canBePushed(this)){
                        if(thisFramePushDir != DIR_LEFT){
                            pushingFrames = 0;
                        }
                        thisFramePushDir = DIR_LEFT;
                        pushingFrames++;
                    }else {
                        thisFramePushDir = DIR_STOP;
                        pushingFrames = 0;
                    }
                }

                if(isPushing()){//push start
                    //debug("push start");
                    if(thisFramePushDir == DIR_RIGHT){
                        pushEndPos = getPosition().sum(new Point3D(game.CELL_SIZE,0,0));
                    }
                    if(thisFramePushDir == DIR_LEFT){
                        pushEndPos = getPosition().sum(new Point3D(-game.CELL_SIZE,0,0));
                    }

                    //debug("push start end");
                }
            }else {
                float x = getPosition().x;
                if((thisFramePushDir == DIR_RIGHT && (x >= pushEndPos.x || !thisFObstacleRight.canBePushed(this))) || (thisFramePushDir == DIR_LEFT && (x <= pushEndPos.x || !thisFObstacleLeft.canBePushed(this)))){//finish to push
                    if(thisFramePushDir == DIR_RIGHT){
                        thisFObstacleRight.endPush(this);
                        this.setAdjacentTo(DIR_RIGHT, thisFObstacleRight);
                    }
                    if(thisFramePushDir == DIR_LEFT){
                        thisFObstacleLeft.endPush(this);
                        this.setAdjacentTo(DIR_LEFT, thisFObstacleLeft);
                    }
                    thisFramePushDir = DIR_STOP;
                    pushingFrames = 0;

                }
            }
        }else {
            if(isPushing()){
                if(thisFramePushDir == DIR_RIGHT){
                    thisFObstacleRight.endPush(this);
                    this.setAdjacentTo(DIR_RIGHT, thisFObstacleRight);
                }
                if(thisFramePushDir == DIR_LEFT){
                    thisFObstacleLeft.endPush(this);
                    this.setAdjacentTo(DIR_LEFT, thisFObstacleLeft);
                }
            }
            thisFramePushDir = DIR_STOP;
            pushingFrames = 0;
        }
        //debug("end postCollisionUpdate player");
    }

    @Override
    protected void collisionResolve(float deltaT) {
        if(thisFObstacleUp != null && thisFObstacleUp.useGravity){
            die();
        }
        super.collisionResolve(deltaT);
    }

    @Override
    public void graphicUpdate(float deltaT) {
        //debug("player g update");
        super.graphicUpdate(deltaT);

        if(haveInitializedGraphic){
            if(grounded){
                spriteViewDown.setVisibility(ImageView.INVISIBLE);
            }else {
                spriteViewDown.setVisibility(ImageView.VISIBLE);
            }
            if(thisFMovementDir == DIR_STOP){
                if(grounded){
                    animIdle.update(deltaT);
                    spriteView.setImageResource((Integer)(animIdle.getCurrFrame().info));
                }else {
                    animAirIdle.update(deltaT);
                    animAirIdleDown.update(deltaT);
                    spriteView.setImageResource((Integer)(animAirIdle.getCurrFrame().info));
                    spriteViewDown.setImageResource((Integer)(animAirIdleDown.getCurrFrame().info));
                }
            }else {
                if(thisFMovementDir == DIR_RIGHT){
                    spriteView.setRotationY(0);
                    spriteViewDown.setRotationY(0);
                }else {
                    spriteView.setRotationY(180);
                    spriteViewDown.setRotationY(180);
                }

                if(grounded){
                    animMove.update(deltaT);
                    spriteView.setImageResource((Integer)(animMove.getCurrFrame().info));
                }else {
                    animAirMove.update(deltaT);
                    animAirMoveDown.update(deltaT);
                    spriteView.setImageResource((Integer)(animAirMove.getCurrFrame().info));
                    spriteViewDown.setImageResource((Integer)(animAirMoveDown.getCurrFrame().info));
                }
            }
        }
    }
    @Override
    public void postGraphicUpdate(float deltaT) {
        super.postGraphicUpdate(deltaT);

        //delete all inputs
        inputMoveUp = false;
        inputMoveDown = false;
        inputMoveLeft = false;
        inputMoveRight = false;
        inputJump = false;

    }
    @Override
    protected void resolveOverlapped(GameObject obj) {
        super.resolveOverlapped(obj);

        die();
    }
}











