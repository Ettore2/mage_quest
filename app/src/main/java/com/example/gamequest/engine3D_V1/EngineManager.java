package com.example.gamequest.engine3D_V1;

import static com.example.gamequest.gameCalsses.GameInstance.debug;

import com.example.gamequest.gameCalsses.GameObject;

import java.util.Date;
import java.util.Vector;

public class EngineManager {
    private Vector<EngineObjectModel> managedObjects;
    protected int fps;
    protected float framesDelay;
    protected Date lastExecution;
    protected float frameTimer;
    protected boolean collisions3D, deleteTimeExceed;
    protected float timerToll;


    //constructors

    /**
     *
     * @param fps frames executed every second
     * @param collisions3D use collisions 3D o 2D
     * @param timerExceedTollPercent the max extra mount of time that will be tolerated in order to not delete the frame (represented as percentage of the delay between frames). -1 for accepting all frames
     */
    public EngineManager(int fps, boolean collisions3D,float timerExceedTollPercent, boolean deleteTimeExceed){
        this.collisions3D = collisions3D;
        this.managedObjects = new Vector<>();
        this.setFps(fps);
        this.lastExecution = new Date();
        this.frameTimer = 0;
        this.timerToll = timerExceedTollPercent;
        this.deleteTimeExceed = deleteTimeExceed;

    }
    /**
     *
     * @param fps frames executed every second
     * @param collisions3D use collisions 3D o 2D
     */
    public EngineManager(int fps, boolean collisions3D){
        this(fps,collisions3D,-1,false);

    }


    //setters
    synchronized public void setFps(int fps){
        this.fps = fps;
        framesDelay = 6000f / fps;

    }
    synchronized public void setCollisions3D(boolean collisions3D){
        this.collisions3D = collisions3D;

    }


    //getters
    synchronized public int getFps(){
        return fps;

    }
    synchronized public float getFramesDelay(){
        return framesDelay;

    }
    synchronized public boolean getCollisions3D(){
        return collisions3D;

    }


    //other methods
    /**
     * needs to be called enough for the frameRate to be constant but not enough to saturate teh cpu
     */
    synchronized public void doCycle(){
        updateTimer();

        //debug("hi");
        if(frameTimer >= framesDelay){
            if((timerToll < 0 || frameTimer <= framesDelay*timerToll/100)){
                //debug("do updates");
                doLogicUpdates(framesDelay);
                //debug("doLogicUpdates");
                doCollisions(framesDelay);
                //debug("doCollisions");
                doPostCollisionUpdates(framesDelay);
                //debug("doPostCollisionUpdates");
                doGraphicUpdates(framesDelay);
                //debug("doGraphicUpdates");
                doPostGraphicUpdates(framesDelay);
                //debug("doPostGraphicUpdates");

            }

            if(deleteTimeExceed){
                frameTimer = 0;
            }else {
                frameTimer -= framesDelay;
            }
        }
    }
    synchronized public boolean shouldCycle(){
        updateTimer();

        return frameTimer >= framesDelay;

    }
    /**
     * returns true if the object was not already been added
     */
    synchronized public boolean addObject(EngineObjectModel obj){
        if(managedObjects.contains(obj)){
            return false;
        }
        managedObjects.add(obj);
        return true;
    }
    /**
     * returns true if the object was managed by this manager
     */
    synchronized public boolean removeObject(EngineObjectModel obj){
        if(managedObjects.contains(obj)){
            managedObjects.remove(obj);
            return true;
        }
        return false;
    }
    /**
     * returns true if the object is managed by this manager
     */
    synchronized public boolean manageObject(EngineObjectModel obj){
        if(managedObjects.contains(obj)){
            return true;
        }
        return false;
    }
    synchronized public void removeAllObjects(){
        managedObjects.clear();

    }


    //function-methods
    private void updateTimer(){
        Date currDate = new Date();
        frameTimer += currDate.getTime() - lastExecution.getTime();
        lastExecution = currDate;
    }
    protected void doLogicUpdates(float deltaT){
        for (EngineObjectModel object : managedObjects){
            object.logicUpdate(deltaT);
        }
    }

    /**
     * will both check and resolve the collisions
     */
    protected void doCollisions(float deltaT){
        EngineObjectModel obj1,obj2;
        for(int i = 0; i < managedObjects.size()-1; i++){
            obj1 = managedObjects.get(i);
            if(obj1.canCollide()){
                for(int j = i+1; j < managedObjects.size(); j++){
                    //debug("  "+i +" "+j);
                    obj2 = managedObjects.get(j);
                    if(obj1.collidingWith(managedObjects.get(j))){
                        obj1.collision(obj2, deltaT);
                        obj2.collision(obj1, deltaT);
                    }
                }

            }
        }
    }
    protected void doPostCollisionUpdates(float deltaT){
        for (EngineObjectModel object : managedObjects){
            object.postCollisionUpdate(deltaT);
        }
    }
    protected void doGraphicUpdates(float deltaT){
        for (EngineObjectModel object : managedObjects){
            if(object.needGraphicUpdate){
                object.graphicUpdate(deltaT);
            }

        }
    }
    protected void doPostGraphicUpdates(float deltaT){
        for (EngineObjectModel object : managedObjects){
            object.postGraphicUpdate(deltaT);
        }
    }

}
