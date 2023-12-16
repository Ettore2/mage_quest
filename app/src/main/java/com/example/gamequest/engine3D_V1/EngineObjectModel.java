package com.example.gamequest.engine3D_V1;

import java.util.Vector;

public abstract class EngineObjectModel implements HaveTag{
    public static final String DEFAULT_TAG = "gameObject";
    public static final int DIR_STOP = 0, DIR_UP = 1, DIR_DOWN = -DIR_UP, DIR_RIGHT = 2, DIR_LEFT = -DIR_RIGHT;

    public final boolean needGraphicUpdate;


    public String tag;
    public Vector<Collider3D> colliders;


    //constructors
    public EngineObjectModel(String tag){
        this.tag = tag;
        colliders = new Vector<>();

        needGraphicUpdate = true;

    }


    //states methods
    public boolean collidingWith(EngineObjectModel obj){
        for(int i = 0; i < colliders.size(); i++){
            for(int j = 0; j < obj.colliders.size(); j++){
                if(colliders.get(i).isColliding2D(obj.colliders.get(j))){
                    return true;
                }
            }
        }

        return false;
    }
    public void logicUpdate(float deltaT){}
    public void collision(EngineObjectModel obj, float deltaT) {}
    public void postCollisionUpdate(float deltaT){}
    public void graphicUpdate(float deltaT){}
    public void postGraphicUpdate(float deltaT){}


    //position and rotation methods
    public abstract void setPosition(Point3D p);
    public abstract void translate(Vector3D v);
    public abstract void translate(float x, float y, float z);
    public abstract Point3D getPosition();
    public abstract void setRotation(Rotation3D r);
    public abstract void rotate(Vector3D v);
    public abstract void rotate(float x, float y, float z);
    public abstract Rotation3D getRotation();


    //otherMethods
    public boolean canCollide(){
        boolean camCollide = false;

        for(int i = 0; i < colliders.size(); i++){
            if(colliders.get(i).isActive){
                return true;
            }
        }
        return false;
    }


    //HaveTag overrides
    @Override
    public String getTag() {
        return tag;

    }
    @Override
    public void setTag(String tag) {
        this.tag = tag;

    }
}
