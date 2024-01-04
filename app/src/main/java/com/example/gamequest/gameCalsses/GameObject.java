package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import android.widget.ImageView;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.BoxCollider;
import com.example.gamequest.engine3D_V1.Collider3D;
import com.example.gamequest.engine3D_V1.EngineObjectModel;
import com.example.gamequest.engine3D_V1.Point3D;
import com.example.gamequest.engine3D_V1.Rotation3D;
import com.example.gamequest.engine3D_V1.Vector3D;

import java.util.Vector;

public abstract class GameObject extends EngineObjectModel {
    public static final float GRAVITY_FORCE = 3f, MAX_FALL_FORCE = 9f;
    public static final float PUDDING = 1/500f;
    public static final float OVERLAPPING_DISTANCE = 1/5f;
    char id;
    public ImageView spriteView;
    int priorityLevel;
    public int imgRes, imgResPh;
    public GameInstance game;
    public Vector<Integer> lineConnections;
    public boolean grounded;
    public boolean usePhysic, useGravity, movable, canCollectCoin, isObstacle, ignoreHorizontalClipping, canBeGrappled;
    protected GameObject pushedObj;
    protected int thisFMovementDir;
    protected GameObject thisFObstacleUp, thisFObstacleDown, thisFObstacleRight, thisFObstacleLeft;
    protected Vector<GameObject> thisFNonObstacleColls;
    protected boolean phasing;
    protected float currScaledYForce;
    boolean haveInitializedGraphic, destroyed;


    //constructors
    public GameObject(Point3D pos, char id, String tag,GameInstance game, ImageView view, int imgRes, int imgResPh) {
        super(tag);
        //debug("GameObject constructor flag 0");
        destroyed = false;
        currScaledYForce = 0;
        grounded = false;
        usePhysic = false;
        useGravity = false;
        isObstacle = false;
        canCollectCoin = false;
        movable = false;
        canBeGrappled = true;
        ignoreHorizontalClipping = false;
        pushedObj = null;
        thisFNonObstacleColls = new Vector<>();
        this.id = id;
        //set priority level
        priorityLevel = 0;
        for(int i = 0; i < OBJECTS_PRIORITY_LEVELS.length; i++){
            for(int j = 0; j < OBJECTS_PRIORITY_LEVELS[i].length; j++){
                if(OBJECTS_PRIORITY_LEVELS[i][j] == id){
                    priorityLevel = i;
                }
            }
        }
        //debug("GameObject constructor flag 1");

        this.game = game;
        this.spriteView = view;
        if(this.spriteView == null){
            if(game.availableImgs.size() > 0){
                //debug("have available img");
                this.spriteView = game.availableImgs.remove(0);
            }else {
                //debug("do not have available img");
                this.spriteView = new ImageView(game.context);
                game.layout.addView(this.spriteView);
            }
        }
        //debug("GameObject constructor flag 2");

        this.imgRes = imgRes;
        this.imgResPh = imgResPh;
        spriteView.setImageResource(imgRes);
        setDefaultViewValues(spriteView);
        //debug("GameObject constructor flag 3");

        lineConnections = new Vector<>();
        grounded = false;
        setPosition(pos);
        setPhasing(false);
        //debug("GameObject constructor flag 4");
        graphicUpdate(0);
        //debug("GameObject constructor flag 5");

    }
    public GameObject(Point3D pos, String tag,GameInstance game) {
        this(pos, '!', tag, game, null, R.drawable.default_img, R.drawable.default_img);

    }


    //other methods
    public ImageView getImageView(){
        return spriteView;

    }
    public void setPhasing(boolean ph){

        if(phasing && !ph){// if i stop to be phasing
            Vector<GameObject> objs = game.getCellForeground(this.getGreedX(), this.getGreedY());
            for(int i = 0; i < objs.size(); i++){
                if(!objs.get(i).phasing){
                    objs.get(i).destroy();
                }
            }
        }

        this.phasing = ph;
        setPosition(this.getPosition());//update the phasing of the things
    }
    public boolean getPhasing(){
        return phasing;

    }
    public void snapToGreed(){
        setPosition(new Point3D(getGreedX() * game.CELL_SIZE, getGreedY() * game.CELL_SIZE, getPosition().z));

    }
    public void centerVertically(){
        setPosition(new Point3D(this.getPosition().x, getGreedY() * game.CELL_SIZE, getPosition().z));

    }
    public void centerHorizontally(){
        setPosition(new Point3D(getGreedX() * game.CELL_SIZE, this.getPosition().y, getPosition().z));

    }
    public void setPositionCell(int x, int y){
        setPosition(new Point3D(x * game.CELL_SIZE, y * game.CELL_SIZE, getPosition().z));

    }
    public BoxCollider getFirstColl(){
        if(colliders != null && colliders.size() > 0){
            return (BoxCollider) colliders.get(0);
        }
        return null;

    }
    public void setAdjacentTo(int myLateDir, GameObject obj, float pudding){
        BoxCollider objColl = obj.getFirstColl();
        BoxCollider myColl = getFirstColl();
        switch (myLateDir){
            case DIR_UP:
                translate(new Vector3D(0,objColl.getDownLate() - myColl.getUpLate() + pudding,0));
                break;
            case DIR_DOWN:
                translate(new Vector3D(0,objColl.getUpLate() - myColl.getDownLate() + pudding,0));
                break;
            case DIR_LEFT:
                translate(new Vector3D(objColl.getRightLate() - myColl.getLeftLate() + pudding,0,0));
                break;
            case DIR_RIGHT:
                translate(new Vector3D(objColl.getLeftLate() - myColl.getRightLate() + pudding,0,0));
                break;
        }

    }
    public void setAdjacentTo(int myLateDir, GameObject obj){
        setAdjacentTo(myLateDir, obj, 0f);

    }
    public boolean canBePushed(GameObject pushingObj){
        int dir = pushingObj.getPosition().x < this.getPosition().x ? DIR_RIGHT : DIR_LEFT;

        if(movable && (!useGravity || grounded)){
            if(this.isObstacle){
                if(pushingObj.isObstacle || pushingObj.getTag().equals(TAG_PLAYER)){
                    //debug("obstacle X obstacle");
                    if(dir == DIR_RIGHT){
                        if (thisFObstacleRight == null){
                            return true;
                        }else {
                            return thisFObstacleRight.canBePushed(this);
                        }
                    }else {
                        if (thisFObstacleLeft == null){
                            return true;
                        }else {
                            return thisFObstacleLeft.canBePushed(this);
                        }

                    }
                }else {
                    //debug("non obstacle X obstacle");
                    return false;
                }
            }else {
                if(pushingObj.isObstacle){
                    return true;
                }else {
                    //debug("non obstacle X non obstacle");
                    //if the direction is blocked by an isObstacle return false
                    if((dir == DIR_RIGHT && thisFObstacleRight != null) || (dir == DIR_LEFT && thisFObstacleLeft != null)){
                        return false;
                    }

                    //else check fot non obstacle objects that are blocked and tho are blocking you
                    for(int i = 0; i < thisFNonObstacleColls.size(); i++){
                        GameObject currObj = thisFNonObstacleColls.get(i);
                        if(sameRow(currObj) && (dir == DIR_RIGHT) == (this.getPosition().x < currObj.getPosition().x)){
                            if(!currObj.canBePushed(this)) {
                                return false;
                            }

                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public void push(GameObject pushingObj, float amount){
        if(amount != 0){
            int dir = amount > 0 ? DIR_RIGHT : DIR_LEFT;
            if(canBePushed(pushingObj) && !this.phasing){
                if(isObstacle){
                    if(dir == DIR_RIGHT){
                        translate(new Vector3D(amount,0,0));
                        if(thisFObstacleRight != null && isObstacle){
                            thisFObstacleRight.push(this, amount);
                        }

                    }
                    if(dir == DIR_LEFT){
                        translate(new Vector3D(amount,0,0));
                        if(thisFObstacleLeft != null && isObstacle){
                            thisFObstacleLeft.push(this, amount);
                        }

                    }
                }else {
                    //if the direction is blocked set canMove to false
                    boolean canMove = true;
                    if((dir == DIR_RIGHT && thisFObstacleRight != null) || (dir == DIR_LEFT && thisFObstacleLeft != null)){
                        canMove = false;
                    }
                    for(int i = 0; i < thisFNonObstacleColls.size(); i++){
                        GameObject currObj = thisFNonObstacleColls.get(i);
                        if(sameRow(currObj) && (dir == DIR_RIGHT) == (this.getPosition().x < currObj.getPosition().x)){
                            if(!currObj.canBePushed(this)) {
                                canMove = false;
                            }

                        }
                    }

                    if(canMove){
                        translate(new Vector3D(amount,0,0));
                    }else {
                        for(int i = 0; i < thisFNonObstacleColls.size(); i++){
                            GameObject currObj = thisFNonObstacleColls.get(i);
                            if(sameRow(currObj) && (dir == DIR_RIGHT) == (this.getPosition().x < currObj.getPosition().x)){
                                if(!currObj.canBePushed(this)) {
                                    currObj.snapToGreed();
                                }

                            }
                        }//ensure that the other things stay centered
                        return;//important
                    }

                }
                for(int i = 0; i < thisFNonObstacleColls.size(); i++){
                    GameObject currObj = thisFNonObstacleColls.get(i);
                    if(currObj.canBePushed(this) && sameRow(currObj)){
                        if(dir == DIR_RIGHT && this.getPosition().x < currObj.getPosition().x){
                            currObj.push(this, amount);
                        }
                        if(dir == DIR_LEFT && this.getPosition().x > currObj.getPosition().x){
                            currObj.push(this, amount);
                        }
                    }
                }
            }
        }

    }
    public void endPush(GameObject pushingObj){
        int dir = pushingObj.getPosition().x < this.getPosition().x ? DIR_RIGHT : DIR_LEFT;
        //debug("end push");
        if(movable && !this.phasing){
            snapToGreed();
            if(this.isObstacle){
                if(dir == DIR_RIGHT && thisFObstacleRight != null){
                    thisFObstacleRight.endPush(this);
                }
                if(dir == DIR_LEFT && thisFObstacleLeft != null){
                    thisFObstacleLeft.endPush(this);
                }
            }
            for(int i = 0; i < thisFNonObstacleColls.size(); i++){
                GameObject currObj = thisFNonObstacleColls.get(i);
                if(sameRow(currObj) && currObj.movable){
                    if(dir == DIR_RIGHT && this.getPosition().x < currObj.getPosition().x){
                        currObj.endPush(this);
                    }
                    if(dir == DIR_LEFT && this.getPosition().x > currObj.getPosition().x){
                        currObj.endPush(this);
                    }
                }
            }

        }


    }
    public void destroy(){
        if(!destroyed){
            game.engineManager.removeObject(this);

            spriteView.setVisibility(ImageView.INVISIBLE);

            destroyed = true;
            game.context.graphicUpdate();
        }
    }
    public void reset(){
        destroyed = false;
        grounded = false;
        phasing = false;
        currScaledYForce = 0;
        setDefaultViewValues(spriteView);

        game.engineManager.addObject(this);
    }


    //abstractsMethods
    public abstract String getDescr();


    //function-methods
    public void setDefaultViewValues(ImageView view){
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.getLayoutParams().width = game.CELL_SIZE;
        view.getLayoutParams().height = game.CELL_SIZE;
        view.setRotation(0);
        view.setVisibility(ImageView.VISIBLE);
    }
    protected void createDefaultCollider(){
        colliders.add(new BoxCollider(this, new Point3D(0,0,0), new Vector3D(game.CELL_SIZE,game.CELL_SIZE,0)));


    }
    protected void gravity(float deltaT){
        if(usePhysic && useGravity){
            if(!grounded && currScaledYForce < game.CELL_SIZE*MAX_FALL_FORCE*deltaT/1000){
                if(currScaledYForce >= 0){
                    currScaledYForce += game.CELL_SIZE*GRAVITY_FORCE*deltaT/1000;
                }

                if(currScaledYForce > game.CELL_SIZE*MAX_FALL_FORCE*deltaT/1000){
                    currScaledYForce = game.CELL_SIZE*MAX_FALL_FORCE*deltaT/1000;
                }

            }
        }

    }
    protected int getGreedX(){
        return Math.round(getPosition().x/ game.CELL_SIZE);

    }
    protected int getGreedY(){
        return Math.round(getPosition().y/ game.CELL_SIZE);

    }
    protected void collisionResolve(float deltaT){
        if(thisFObstacleDown != null){
            grounded = true;
            currScaledYForce = 0;
            //preventing phasing objects from teleporting up on a falling object
            if(!phasing || this.getPosition().y + game.CELL_SIZE-PUDDING < thisFObstacleDown.getPosition().y){
                setAdjacentTo(DIR_DOWN, thisFObstacleDown,game.CELL_SIZE* PUDDING);
            }

            //centerVertically();
        }
        if(thisFObstacleUp != null){
            if(currScaledYForce < 0){
                currScaledYForce = 0;
                setAdjacentTo(DIR_UP, thisFObstacleUp,-game.CELL_SIZE* PUDDING);
                //centerVertically();
            }
        }
        if(thisFObstacleRight != null && thisFMovementDir != DIR_LEFT && !ignoreHorizontalClipping && !this.phasing){
            thisFMovementDir = DIR_STOP;
            setAdjacentTo(DIR_RIGHT, thisFObstacleRight,game.CELL_SIZE* PUDDING);
            if(getTag().equals(TAG_PLAYER)){
                //debug("adjacent right");
            }
        }
        if(thisFObstacleLeft != null && thisFMovementDir != DIR_RIGHT && !ignoreHorizontalClipping && !this.phasing){
            thisFMovementDir = DIR_STOP;
            setAdjacentTo(DIR_LEFT, thisFObstacleLeft,-game.CELL_SIZE* PUDDING);
            if(getTag().equals(TAG_PLAYER)){
                //debug("adjacent left");
            }
        }
    }
    protected void horizontalMovement(float deltaT){}
    protected void verticalMovement(float deltaT){
        //Y = 0 -> up (fall = augment the y)
        translate(new Vector3D(0, currScaledYForce *game.context.engineManager.getFramesDelay()/deltaT,0));

    }
    public boolean sameCell(GameObject obj){
        return sameRow(obj) && sameColum(obj);

    }
    public boolean sameRow(GameObject obj){
        return Math.abs(this.getPosition().y - obj.getPosition().y) <= game.CELL_SIZE/2f+PUDDING*game.CELL_SIZE;

    }
    public boolean sameColum(GameObject obj){
        return Math.abs(this.getPosition().x - obj.getPosition().x) <= game.CELL_SIZE/2f+PUDDING*game.CELL_SIZE;

    }
    protected void resolveOverlapped(GameObject obj){}


    //EngineObjectModel overrides
    @Override
    public void setPosition(Point3D p) {
        spriteView.setX(p.x);
        spriteView.setY(p.y);
        if(!phasing){
            spriteView.setZ(priorityLevel*LEVEL_JUMP);
        }else {
            spriteView.setZ(priorityLevel*LEVEL_JUMP);
        }

    }
    @Override
    public void translate(Vector3D v) {
        Point3D pos = getPosition();
        setPosition(new Point3D(pos.x + v.x, pos.y + v.y, pos.z + v.z));

    }
    @Override
    public void translate(float x, float y, float z) {
        translate(new Vector3D(x, y, z));
    }
    @Override
    public Point3D getPosition() {
        return new Point3D(spriteView.getX(), spriteView.getY(), spriteView.getZ());

    }
    @Override
    public void setRotation(Rotation3D r) {
        spriteView.setRotationX(r.x);
        spriteView.setRotationY(r.y);
        spriteView.setRotation(r.z);
    }
    @Override
    public void rotate(Vector3D v) {
        Rotation3D rot = getRotation();
        setRotation(new Rotation3D(rot.x + v.x, rot.y + v.y, rot.z + v.z));
    }
    @Override
    public void rotate(float x, float y, float z) {
        rotate(new Vector3D(x, y, z));

    }
    @Override
    public Rotation3D getRotation() {
        return new Rotation3D(spriteView.getRotationX(), spriteView.getRotationY(), spriteView.getRotation());

    }


    @Override
    public void logicUpdate(float deltaT) {
        super.logicUpdate(deltaT);

        grounded = false;
        thisFObstacleDown = null;
        thisFObstacleUp = null;
        thisFObstacleRight = null;
        thisFObstacleLeft = null;
        thisFNonObstacleColls.clear();
    }
    @Override
    public void collision(EngineObjectModel obj, float deltaT) {
        super.collision(obj, deltaT);
        String ogjTag = obj.getTag();
        GameObject otherObj = (GameObject) obj;

        if(this.usePhysic){
            if(otherObj.isObstacle && !otherObj.phasing){
                BoxCollider myCol = getFirstColl();

                boolean objectRight = this.getPosition().x < otherObj.getPosition().x;
                boolean objectUp = this.getPosition().y > otherObj.getPosition().y;
                if(sameCell(otherObj)){//same cell
                    Vector3D distances = getPosition().aziDistances(otherObj.getPosition());
                    //debug(game.CELL_SIZE +"| "+distances.x +"   "+ distances.y +"   "+OVERLAPPING_DISTANCE*game.CELL_SIZE);
                    if(distances.x >= OVERLAPPING_DISTANCE*game.CELL_SIZE || distances.y >= OVERLAPPING_DISTANCE*game.CELL_SIZE){//clipping from the angle
                        if(Math.abs(getPosition().x - otherObj.getPosition().x)+(myCol.size.x-game.CELL_SIZE)/2 < Math.abs(getPosition().y - otherObj.getPosition().y) && Math.abs(getPosition().x - otherObj.getPosition().x)+(myCol.size.x-game.CELL_SIZE)/2 < game.CELL_SIZE*PUDDING){
                            if(!objectUp){
                                thisFObstacleDown = otherObj;
                            }else {
                                thisFObstacleUp = otherObj;
                            }
                        }else {
                            if(objectRight){
                                thisFObstacleRight = otherObj;
                            }else {
                                thisFObstacleLeft = otherObj;
                            }
                        }
                    }else{//actually overlapped
                        if(!phasing){
                            resolveOverlapped(otherObj);
                        }

                    }
                }else{//normal collision
                    if(Math.abs(this.getPosition().x - otherObj.getPosition().x)+(myCol.size.x-game.CELL_SIZE)/2 < Math.abs(this.getPosition().y - otherObj.getPosition().y)){
                        //collide vertically
                        if(sameColum(otherObj)){
                            if(!objectUp){//may above the block (grounded)
                                if(thisFObstacleDown == null){
                                    thisFObstacleDown = otherObj;
                                }

                            }else {//may under the block
                                if(thisFObstacleUp == null){
                                    thisFObstacleUp = otherObj;
                                }

                            }
                        }
                    }else {
                        //if(getTag().equals(TAG_PLAYER)){
                            //debug("collide horizontally!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! with tag:"+otherObj.getTag());
                            //debug(getPosition().toString()+"   "+otherObj.getPosition().toString());
                            //debug(getGreedX()+"   "+getGreedY()+"   "+otherObj.getGreedX()+"   "+ otherObj.getGreedY());
                        //}
                        //collide horizontally
                        if(sameRow(otherObj)){
                            if(objectRight){//(can't go right
                                if(thisFObstacleRight == null){
                                    thisFObstacleRight = otherObj;
                                }
                            }
                            if(!objectRight){//(can't go left
                                if(thisFObstacleLeft == null){
                                    thisFObstacleLeft = otherObj;
                                }
                            }
                        }

                    }

                }


            }else {
                thisFNonObstacleColls.add(otherObj);
            }
        }

        collisionResolve(deltaT);//need to be there
    }

    @Override
    public void postCollisionUpdate(float deltaT) {
        super.postCollisionUpdate(deltaT);

        //after setting grounded
        gravity(deltaT);

        horizontalMovement(deltaT);
        verticalMovement(deltaT);

    }
    @Override
    public void graphicUpdate(float deltaT) {
        super.graphicUpdate(deltaT);
        spriteView.setImageResource(phasing ? imgResPh : imgRes);
    }

    @Override
    public void postGraphicUpdate(float deltaT) {
        super.postGraphicUpdate(deltaT);
        thisFMovementDir = DIR_STOP;
    }
}
